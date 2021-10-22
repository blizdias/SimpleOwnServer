import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {


    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public void activateSocket(int portNumber) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        logger.log(Level.INFO, "server online!");
        while(true){
            Socket client = serverSocket.accept();
            handleClient(client);
        }
    }


    public void handleClient(Socket client) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String line = in.readLine();
        String[] lineArray = line.split(" ");


        DataOutputStream clientOutput = new DataOutputStream(client.getOutputStream());

        File html = new File("www" + lineArray[1]);

        if (!html.exists()) {
            html = new File("www/404.html");
            FileInputStream file = new FileInputStream(html);
            clientOutput.write(("HTTP/1.0 404 Not Found\r\n").getBytes());
            clientOutput.write(("Content-Type: text/html; charset=UTF-8\r\n").getBytes());
            clientOutput.write(("Content-Length: " + html.length() + "\r\n").getBytes());
            clientOutput.write("\r\n".getBytes());

            fileRead(file, clientOutput);

        } else {
            FileInputStream file = new FileInputStream(html);
            clientOutput.write("HTTP/1.0 200 Document Follows\r\n".getBytes());
            clientOutput.write(("Content-Type: "+ validQuery(lineArray[1]) + " charset=UTF-8\r\n").getBytes());
            clientOutput.write(("Content-Length: " + html.length() + "\r\n").getBytes());
            clientOutput.write("\r\n".getBytes());

            fileRead(file, clientOutput);
        }

        closeStreams(in, clientOutput, client);

    }


    public void fileRead(FileInputStream file, DataOutputStream clientOutput) {
        int fileRead;
        try {
            while ((fileRead = file.read()) != -1) {
                clientOutput.write(fileRead);
                clientOutput.flush();
            }
        } catch (IOException e) {
                e.printStackTrace();
            }


    }

    public String validQuery (String str) {
        String [] arrayStr = str.split("\\.");
        if (arrayStr.length != 2) {
            return "null";
        }

        switch (arrayStr[arrayStr.length-1]) {
            case "html" :
                return "text/html;";

            case "png" :
                return "image/png;";

            default:
                return "null";
        }

    }


    public void closeStreams(BufferedReader in, DataOutputStream out, Socket clientSocket) {
        try {
            in.close();
            clientSocket.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
