import org.apache.commons.cli.*;


public class Main {


    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(  "help", false, "print this message");
        options.addOption(  "client", false, "run as client");
        options.addOption(  "server", false, "run as server");

        options.addOption("address", true, "server address to connect (client) / to bind (server)");
        options.addOption("port", true, "server port to connect (client) / to bind (server)");
        options.addOption("key", true, "secret key (if not specified it will generate a new one)");

        options.addOption("classfile", true, "path of bytecode .class file to load remotely");
        options.addOption("classname", true, "name of class");
        options.addOption("classmethod", true, "name of method to invoke");


        // Parse command line
        CommandLine cmd = null;
        try{
            cmd = parser.parse(options, args);
        } catch (ParseException ex){
            System.err.println(ex.getMessage());
        }

        if(cmd == null){
            System.err.println("Error parsing arguments");
            return;
        }

        // Display -help
        if(cmd.hasOption("help")){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Main", options);
            return;
        }

        // Load into settings
        Settings.loadSettings(cmd);

        // Run as client OR server
        if(cmd.hasOption("client")){
            System.out.println("Running as client");
            Client client = Client.getInstance();
            client.run();

        }else if(cmd.hasOption("server")){
            System.out.println("Running as server");
            Server server = Server.getInstance();
            server.run();

        }else {
            System.err.println("You must select to either run as -client or -server");
        }

        return;
    }



}
