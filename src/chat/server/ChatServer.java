// Mature Chat server with socket
// Server accepts many clients many times.
// For testing use PuTTY, connect to localhost, port 3333, raw connection.
// To end the session, write QUIT to the server alone on the line.
// https://github.com/MilanKerslager/NetBeansTXT-ChatServer

package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer implements Runnable {
    private final int id;
    private final Socket socket;

    public ChatServer(Socket s, int i) {
        this.socket = s;
        this.id = i;
    }

    public static void main(String[] args) {
        final int PORT = 3333;
        int count=0;
        try {
            System.out.println("Chat server started and binded to port: " + PORT);
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server is running and waiting: " + server);
            while (true) {
                Socket socket = server.accept();
                Thread thread = new Thread(new ChatServer(socket, ++count));
                thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        SimpleDateFormat TimeStamp = new SimpleDateFormat("dd.MM.yyyy H:mm:ss");
        BufferedReader streamIn;
        try {
            System.out.println(TimeStamp.format(new Date())+
                    ": thread "+id+": client accepted at: "+socket);
            streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = streamIn.readLine();
                System.out.println(TimeStamp.format(new Date())+": thread "+id+": "+line);
                if (line.equals("QUIT")) {
                    // client wants to close the connection now (ie end this thread)
                    break;
                }
            }
            System.out.println(TimeStamp.format(new Date())+
                    ": thread "+id+": This thread ends now. Bye.");
            socket.close();
            streamIn.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
