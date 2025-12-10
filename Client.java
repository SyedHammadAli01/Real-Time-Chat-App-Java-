import java.net.*;
import java.io.*;

class Client {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server");

            socket = new Socket("192.168.100.99", 7777);
            System.out.println("Connection done..");

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
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
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
        System.out.println("This is Client...");
        new Client();
    }
}
