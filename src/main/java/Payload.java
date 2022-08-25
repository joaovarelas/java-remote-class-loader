import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Payload {

    /*
    Sample class to load remotely.
    Class name, method and signature must match LoadClass.java definition.
     */


    public static String exec() {
        String output = "Hello world";
        return output;
    }

    /*
    public static String exec(){
        String output = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "uname -a");
            Process proc = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                output += line;
            }
            proc.waitFor();
            in.close();
            //System.exit(0);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return output;
    }
    */

}
