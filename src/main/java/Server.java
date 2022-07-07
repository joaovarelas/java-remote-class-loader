import java.io.*;
import java.net.*;

public class Server {
    public Server() {
    }

    private Thread thread = null;
    private ServerSocket serverSocket = null;
    private ClientHandler clientHandler = null;
    private static Server inst = null;

    private Settings settings = Settings.getInstance();

    Encryption cipher = null;


    public static Server getInstance()
    {
        if (inst == null)
            inst = new Server();

        return inst;
    }


    public void run(){

        try {
            serverSocket = new ServerSocket(settings.getPort());
            System.out.println(String.format("Server running on %s:%d ",
                    settings.getAddress(), settings.getPort()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cipher = settings.getKey() == null ? new Encryption() : new Encryption(settings.getKey());

        while (true) {
            try {
                clientHandler = new ClientHandler(serverSocket.accept());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            thread = new Thread(clientHandler);
            thread.start();
        }
    }


    class ClientHandler implements Runnable {
        private Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            System.out.println(String.format("New client! [%s]",
                    clientSocket.getLocalAddress().getHostAddress()));
        }

        @Override
        public void run() {
            // create input buffer and output buffer
            // wait for input from client and send response back to client
            // close all streams and sockets
            try {
                InputStream inpStream = clientSocket.getInputStream();
                OutputStream outStream = clientSocket.getOutputStream();

                byte[] classBytes = Utils.file2ByteArray(Settings.getInstance().getClassFile());
                outStream.write(cipher.encrypt(classBytes));

                inpStream.close();
                outStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
