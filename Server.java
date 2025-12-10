import java.net.*;
import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader Started....");

            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                } catch (Exception e) {
                    System.out.println("Connnection closed..");
                    break;
                }
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer is Starting ..");

            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                while (!socket.isClosed()) {
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println("Connnection closed..");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Server..going to start Server ");
        new Server();
    }
}
