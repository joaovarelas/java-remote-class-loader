import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

    public static byte[] file2ByteArray(String filePath) {

        try {
            File file = new File(filePath);
            DataInputStream reader = new DataInputStream(new FileInputStream(file));
            int bytesToRead = reader.available();

            if (bytesToRead > 0) {
                byte[] bytes = new byte[bytesToRead];
                reader.read(bytes);
                return bytes;
            }

        } catch (IOException e) {
            System.out.println("Error reading .class file for loading");
            throw new RuntimeException(e);
        }

        return new byte[]{};
    }

}
