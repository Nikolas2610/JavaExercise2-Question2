import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EdgeServer extends Thread {

    private final Socket clientSocket;
    private final int clientId;

    private EdgeServer(Socket client, int id) {
        clientSocket = client;
        clientId = id;
        start();
    }

    public static void main(String[] args) throws IOException {
//      Initialize socket server with port
        int count = 0;
        ServerSocket serverSocket = null;
        final int PORT = 10007;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening at port " + PORT);
            try {
                while (true) {
                    new EdgeServer(serverSocket.accept(), count);
                    System.out.println("\033[34mClient " + count + " connected to server and waiting for connection...\033[0m");
                    count++;
                }
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(1);
            }
        } catch (IOException e) {
//      Catch the errors and exit program
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Cound not close port: " + PORT);
                System.exit(1);
            }
        }
////      Server connect and waiting for client
//        Socket clientSocket = null;
//        System.out.println("Server listening at port " + PORT);
//        System.out.println("Waiting for connection.....");
////      Server accepting the client
//        try {
//            clientSocket = serverSocket.accept();
//        } catch (IOException e) {
////            Catch the errors
//            System.err.println("Accept failed.");
//            System.exit(1);
//        }
////      Print success message from the connection
//        System.out.println("Connection successful");
//        System.out.println("Waiting for input.....");
//      Initialize the In/0ut response from the client

    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

//      Get the random Graph
            EdgeList edgeList = new EdgeList();

            while (true) {
//          Server send message to client for the details tha wants
                out.println("Write to keyboard the edge. Nodes are " + edgeList.getNODES() +
                        " and the accept format is X,X with max value " + (edgeList.getNODES() - 1)
                        + " (example: 4,2)\nFor exit write -1,-1:");
//          Server waiting for response from client
                inputLine = in.readLine();
//          Checking the empty message
                if (inputLine == null || inputLine.length() == 0) {
                    out.println("Empty input");
                    continue;
                }
//          Split the message from client
                String[] edges = inputLine.split(",");
//          Checking if the array is the correct
                if (edges.length == 2) {
                    try {
//                  Transform the string data to numbers
                        int[] edgesInt = new int[2];
                        for (int i = 0; i < edges.length; i++) {
                            edgesInt[i] = Integer.parseInt(edges[i]);
                        }
//                  Check if the user want to close the connection
                        if (edges[0].equals("-1") && edges[1].equals("-1")) {
                            System.out.println("\033[31mClient " + this.clientId +  " has been disconnected from server\033[0m");
                            out.println("Close connection with server!");
                            break;
                        }
//                  Check if the user gives available numbers with the min and max nodes
                        if (edgesInt[0] >= edgeList.getNODES() || edgesInt[1] >= edgeList.getNODES() ||
                                edgesInt[0] < 0 || edgesInt[1] < 0) {
                            out.println("Wrong input");
                            continue;
                        }
//                  Check if the edge exists to the graph and create message for the client
                        String message = edgeList.edgeExists(edgesInt[0], edgesInt[1]);
                        System.out.println("\033[33mClient " + this.clientId +  ": Row: " + edges[0] + " Col: " + edges[1] + "\033[0m");
//                  Send the server response to the client
                        out.println(message);
                    } catch (NumberFormatException e) {
                        // Handle the exception if the user input are numbers
                        out.println("Wrong input");
                        continue;
                    }
                } else {
//              Capture the error for bad input
                    out.println("Wrong input");
                    continue;
                }
            }
//      Close client connection
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Problem with Communication Server");
            System.exit(1);
        }

    }
}
