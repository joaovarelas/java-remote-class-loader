import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

public class Payload {

    /*
    Sample class to load remotely.
    Class name, method and signature must match LoadClass.java definition.
     */


    /*
    public static String exec() {
        String output = "";
        try {
            output = "Hello world from client!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

     */


    /*
    public static String exec(){
        String output = "";
        try{
            Properties prop = System.getProperties();
            output = prop.toString().replaceAll(",", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    */


    public static String exec(){
        String output = "";
        try {
            String os = System.getProperty("os.name");
            String cmd[];
            if(os.toLowerCase().contains("win")){
                cmd = new String[]{"powershell.exe", "-c", "Get-Process | Out-File -FilePath C:\\Users\\Public\\ps.txt"};
            }else{
                cmd = new String[]{"/bin/bash", "-c", "ps auxef > /tmp/ps.txt"};
            }
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            BufferedReader err = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            String s = null;
            while ((s = in.readLine()) != null) {
                output += s;
            }
            while ((s = err.readLine()) != null) {
                output += s;
            }

            //System.exit(0);
        } catch (Exception e) {
            output = e.getMessage();
            e.printStackTrace();
        }
        return output;
    }


}
