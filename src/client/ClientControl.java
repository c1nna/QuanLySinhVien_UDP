/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import model.Request;
import model.Response;
import model.Student;

/**
 *
 * @author Admin
 */
public class ClientControl {
    private int serverPort, clientPort;
    private String serverHost;
    private DatagramSocket mySocket;

    public ClientControl() {
        serverPort = 8888;
        serverHost = "localhost";
        clientPort = 8890;
    }
    
    public void openSocket(){
        try{
            mySocket = new DatagramSocket(clientPort);
            System.out.println(mySocket);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void sendReq(Request req){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(req);
            byte[] data = baos.toByteArray();
            DatagramPacket sendPkg = new DatagramPacket(data, data.length, 
                    InetAddress.getByName(serverHost), serverPort);
            mySocket.send(sendPkg);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public Response getRes(){
        Response res = null;
        try{
            byte[] data = new byte[1024];
            DatagramPacket receivePkg = new DatagramPacket(data, data.length);
            mySocket.receive(receivePkg);
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePkg.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Response) ois.readObject();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }
    
    public void closeSocket(){
        try{
            mySocket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
