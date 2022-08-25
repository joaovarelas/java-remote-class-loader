import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private static Server inst;
    private final Settings settings = Settings.getInstance();
    Encryption cipher;
    private Thread thread;
    private ServerSocket serverSocket;
    private ClientHandler clientHandler;

    public Server() {
    }

    public static Server getInstance() {
        if (inst == null)
            inst = new Server();

        return inst;
    }


    public void run() {

        try {
            serverSocket = new ServerSocket(settings.getPort());
            System.out.println(String.format("Server running on %s:%d ",
                    settings.getAddress(), settings.getPort()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cipher = new Encryption(settings.getKey());

        // Handle incoming connections and spawn new thread for each client
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
        private final Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            System.out.println(String.format("New client! [%s]",
                    clientSocket.getLocalAddress().getHostAddress()));

            try {
                BufferedInputStream in = null;
                BufferedOutputStream out = null;

                while (clientSocket.isConnected() && !clientSocket.isClosed()) {

                    in = new BufferedInputStream(clientSocket.getInputStream());
                    out = new BufferedOutputStream(clientSocket.getOutputStream());

                    // Send classfile to client
                    byte[] classBytes = Utils.file2ByteArray(Settings.getInstance().getClassFile());
                    out.write(cipher.encrypt(classBytes));
                    out.flush();
                    System.out.println(String.format("Sent %d bytes to client", classBytes.length));

                    // Receive execution output from client
                    byte[] buffer = new byte[4096];
                    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                    
                    try {
                        byteArrayStream.write(buffer, 0, in.read(buffer));
                    } catch (RuntimeException e) {
                        System.out.println("Error receiving data from client");
                        continue;
                    }

                    System.out.println(String.format("Received %d bytes from client: %s", byteArrayStream.size(),
                            new String(cipher.decrypt(byteArrayStream.toByteArray()))));
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (SocketException e) {
                System.out.println("Lost connection to client");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
