import org.omg.CORBA.WCharSeqHelper;

import java.io.IOException;

public class Main {

    public static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {

        try {

            int portnumber = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

            Server server = new Server();
            server.activateSocket(portnumber);

        } catch (NumberFormatException | IOException e) {
            System.err.println("Usage: Server [portnumber]");
            System.exit(1);
        }


    }
}
