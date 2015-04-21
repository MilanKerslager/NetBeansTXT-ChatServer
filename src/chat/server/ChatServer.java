package chat.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer implements Runnable {
    private final int id;
    private final Socket socket;
    private DataInputStream streamIn;

    public ChatServer(Socket s, int i) {
        this.socket = s;
        this.id = i;
    }

    public static void main(String[] args) {
        int count=0, port=3333;
        try {
            System.out.println("Připojuji se na port: " + port);
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server běží: " + server);
            while (true) {
                Socket socket = server.accept();
                Thread thread = new Thread(new ChatServer(socket, ++count));
                thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void run() {
        try {
            String TimeStamp = new java.util.Date().toString();
            System.out.println(TimeStamp+" Thread "+id+" přijal klienta: "+socket);
            streamIn = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            while (true) {
                String line = streamIn.readUTF();
                System.out.println(line);
                if (line.equals("QUIT")) {
                    break;
                }
            }
            socket.close();
            streamIn.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
