/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import model.Request;
import model.Response;
import model.Student;

/**
 *
 * @author Admin
 */
public class ServerControl {
    private int port;
    private DatagramSocket myServer;
    private DatagramPacket receivePkg;
    private DAO dao;
   
    public ServerControl() {
        port = 8888; 
        dao = new DAO();
        openConnection();
        while(true){
            execute();
        }
    }
    
    public void openConnection(){
        try {
            myServer = new DatagramSocket(port);
            System.out.println(myServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendRes(Response res){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(res);
            byte[] data = baos.toByteArray();
            DatagramPacket pkg = new DatagramPacket(data, data.length, 
                    receivePkg.getAddress(), receivePkg.getPort());
            myServer.send(pkg);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Request receiveReq(){
        Request req = null;
        try{
            byte[] data = new byte[1024];
            receivePkg = new DatagramPacket(data, data.length);
            myServer.receive(receivePkg);
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePkg.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Request) ois.readObject();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return req;
    }
    
    public void execute(){
        try{
            Request req = receiveReq();
            Response res = null;
            switch (req.getCode()) {
                case 1:
                    if (dao.addStudent((Student) req.getO())) {
                        res = new Response(1, "ok");
                        sendRes(res);
                        System.out.println("Them thanh cong!!!");
                    }
                    else {
                        res = new Response(1, "failed");
                        sendRes(res);
                        System.out.println("That bai!!!");
                    }
                    break;
                case 2:
                    List<Student> list = dao.getAll();
                    res = new Response(2, list);
                    sendRes(res);
                    break;
                case 3:
                    if (dao.updateStudent((Student) req.getO())) {
                        res = new Response(3, "ok");
                        sendRes(res);
                        System.out.println("Sua thanh cong!!!");
                    }
                    else {
                        res = new Response(3, "failed");
                        sendRes(res);
                        System.out.println("That bai!!!");
                    }
                    break;
                case 4:
                    if (dao.delStudent((Student) req.getO())) {
                        res = new Response(4, "ok");
                        sendRes(res);
                        System.out.println("Xoa thanh cong!!!");
                    }
                    else {
                        res = new Response(4, "failed");
                        sendRes(res);
                        System.out.println("That bai!!!");
                    }
                    break;
                case 5:
                    List<Student> list1 = dao.getStudentByName((Student) req.getO());
                    res = new Response(5, list1);
                    sendRes(res);
                    break;
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
