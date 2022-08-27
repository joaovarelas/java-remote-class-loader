import org.apache.commons.cli.CommandLine;


public class Main {


    public static void main(String[] args) {

        // Parse arguments
        CommandLine cmd = Settings.parseArgs(args);
        if (cmd == null) {
            System.out.println("Use '-help' to get a list of commands.");
            return;
        }

        // Load settings from specified args
        Settings.loadSettings(cmd);

        // Run as client OR server
        if (cmd.hasOption("client")) {
            System.out.println("Running as client");
            Client.getInstance().run();

        } else if (cmd.hasOption("server")) {
            System.out.println("Running as server");
            Server.getInstance().run();

        } else {
            System.out.println("You must select to either run as -client or -server");
        }

    }

}
