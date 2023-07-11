package lk.ijse.chatApp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Handler extends Thread{
    private ArrayList<Handler> clients;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Handler(Socket socket, ArrayList<Handler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                if(message.equalsIgnoreCase("bye")){
                    break;
                }
                for (Handler h1: clients){
                    h1.printWriter.println(message);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        finally {
            try {
                bufferedReader.close();
                printWriter.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
