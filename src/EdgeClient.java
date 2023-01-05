import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EdgeClient {
    public static void main(String[] args) throws IOException {
//      Set localhost address
        String serverHostname = "127.0.0.1";
        if (args.length > 0) {
            serverHostname = args[0];
        }

        System.out.println("Attempting to connect to host " + serverHostname + " on port 10007.");
//      Initialize Client Socket, In/Out response with server
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            echoSocket = new Socket(serverHostname, 10007);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
//          Error with address or port
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
//          Error with server down
            System.err.println("Couldn't get I/O for the connection to: " + serverHostname);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        String serverMessage;


        while ((serverMessage = in.readLine()) != null) {
//      Print all server messages
            System.out.println(serverMessage);
//      Print specific message from server and server waiting response from client
            if (serverMessage.length() > 0 && serverMessage.charAt(serverMessage.length() - 1) == ':') {
                userInput = stdIn.readLine();
                out.println(userInput);
            }
        }

//      Close connection
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
