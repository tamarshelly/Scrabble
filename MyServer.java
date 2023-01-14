package test;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {

    int port;
    ClientHandler ch;
    Boolean close;
    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.ch = clientHandler;
    }

    public void start() {
        close= false;
        new Thread(() -> startServer()).start();
    }
    public void startServer(){
        try {
            ServerSocket server= new ServerSocket(port);
            server.setSoTimeout(1000);
            while (!close) {
                try{
                    Socket client = server.accept();//block state until next client
                    ch.handleClient(client.getInputStream(), client.getOutputStream());
                    ch.close();
                    client.close();
                } catch (SocketTimeoutException e){}
            }
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public void close() {
        close = true;
    }
}
