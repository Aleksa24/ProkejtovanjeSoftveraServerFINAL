/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import model.Menadzer;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aleksa
 */
public class ServerThread extends Thread {

    private final ServerSocket serverSocket;
    private final List<ClientThread> clients;

    public ServerThread() throws IOException {
        serverSocket = new ServerSocket(getServerPort());
        clients = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            System.out.println("Waiting client");
            try {
                Socket socket = serverSocket.accept();
                ClientThread thread = new ClientThread(socket);
                clients.add(thread);
                thread.start();
                
                System.out.println("Client connected");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        stopAllThreads();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private void stopAllThreads() {
        for (ClientThread client : clients) {
            try {
                client.getSocket().close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public List<Menadzer> getAllUsers() {
        List<Menadzer> users=new ArrayList<>();
        
        for (ClientThread client : clients) {
            users.add(client.getLoginUser());
        }
        
        return users;
    }

    /**
     * reads port from properties file
     * @return 
     */
    private int getServerPort() {
        try {
            int port = -1;
            Properties properties = new Properties();
            properties.load(new FileInputStream("server.properties"));
            port = Integer.valueOf(properties.getProperty("port"));
            return port;
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}
