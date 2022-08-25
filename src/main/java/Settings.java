import org.apache.commons.cli.*;

public class Settings {
    private static Settings inst = null;
    String key;
    String address;
    int port = 0;
    String classFile;
    String className;
    String classMethod;
    boolean keepalive = false;

    public Settings() {
    }

    public static CommandLine parseArgs(String[] args) {

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("help", false, "print this message");
        options.addOption("client", false, "run as client");
        options.addOption("server", false, "run as server");

        options.addOption("address", true, "address to connect (client) / to bind (server)");
        options.addOption("port", true, "port to connect (client) / to bind (server)");
        options.addOption("key", true, "secret key - 256 bits in base64 format (if not specified it will generate a new one)");

        options.addOption("classfile", true, "filename of bytecode .class file to load remotely (default: Payload.class)");
        options.addOption("classname", true, "name of class (default: Payload)");
        options.addOption("classmethod", true, "name of method to invoke (default: exec)");

        options.addOption("keepalive", false, "keeps the client getting classfile from server every X seconds (default: 3 seconds)");


        // Parse command line
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }

        // Display -help
        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Main", options);
            return null;
        }

        return cmd;
    }

    public static void loadSettings(CommandLine cmd) {

        // Parse values
        String address = cmd.hasOption("address") ? cmd.getOptionValue("address") : "0.0.0.0";
        int port = cmd.hasOption("port") ? Integer.parseInt(cmd.getOptionValue("port")) : 31337;
        String key = cmd.hasOption("key") ? cmd.getOptionValue("key") : null;
        String classFile = cmd.hasOption("classfile") ? cmd.getOptionValue("classfile") : "Payload.class";
        String className = cmd.hasOption("classname") ? cmd.getOptionValue("classname") : "Payload";
        String classMethod = cmd.hasOption("classmethod") ? cmd.getOptionValue("classmethod") : "exec";
        boolean keepalive = cmd.hasOption("keepalive");


        // Define settings
        Settings settings = Settings.getInstance();

        settings.setAddress(address);
        settings.setPort(port);
        settings.setKey(key);
        settings.setClassFile(classFile);
        settings.setClassName(className);
        settings.setClassMethod(classMethod);
        settings.setKeepalive(keepalive);
    }

    public static Settings getInstance() {
        if (inst == null)
            inst = new Settings();

        return inst;
    }

    public boolean getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
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
