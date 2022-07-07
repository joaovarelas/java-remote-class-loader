import org.apache.commons.cli.CommandLine;

public class Settings {
    String key = null;
    String address = null;
    int port = 0;

    String classFile = null;

    String className = null;

    String classMethod = null;

    int refresh_ms = 5000; // 5 seconds
    private static Settings inst = null;


    public static void loadSettings(CommandLine cmd) {
        // TODO: refactor
        String address = cmd.getOptionValue("address") == null ? "0.0.0.0" : cmd.getOptionValue("address");
        int port = cmd.getOptionValue("port") == null ? 31337 : Integer.parseInt(cmd.getOptionValue("port"));
        String key = cmd.getOptionValue("key"); //== null ? null : cmd.getOptionValue("key");

        String classFile = cmd.getOptionValue("classfile") == null ? "Payload.class" : cmd.getOptionValue("classfile");
        String className = cmd.getOptionValue("classname") == null ? "Payload" : cmd.getOptionValue("classname");
        String classMethod = cmd.getOptionValue("classmethod") == null ? "exec" : cmd.getOptionValue("classmethod");


        Settings settings = Settings.getInstance();
        settings.setAddress(address);
        settings.setPort(port);
        settings.setKey(key);
        settings.setClassFile(classFile);
        settings.setClassName(className);
        settings.setClassMethod(classMethod);
    }


    public String getClassFile() {
        return classFile;
    }

    public void setClassFile(String classFile) {
        this.classFile = classFile;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }



    public Settings() {
    }

    public static Settings getInstance()
    {
        if (inst == null)
            inst = new Settings();

        return inst;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
