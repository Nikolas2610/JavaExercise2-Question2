import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EdgeServer extends Thread {

    private final Socket clientSocket;
    private final int clientId;
    private static Graph graph;
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    private EdgeServer(Socket client, int id) {
        clientSocket = client;
        clientId = id;
        start();
    }

    public static void main(String[] args) throws IOException {
        // Get the random Graph
        graph = new Graph(3000000, 3000000);
//      Initialize socket server with port
        int count = 0;
        ServerSocket serverSocket = null;
        final int PORT = 10007;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening at port " + PORT);
            try {
//                Create multiple connections with clients
                while (true) {
                    new EdgeServer(serverSocket.accept(), count);
                    System.out.println(ANSI_BLUE + "Client " + count + " connected to server and waiting for connection..." + ANSI_RESET);
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
    }


    public void run() {
        try {
//          Set variables to read and write to the client
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while (true) {
//          Server send message to client for the details tha wants
                out.println("Write to keyboard the edge. Nodes are " + graph.getNODES() +
                        " and the accept format is X,X with max value " + (graph.getNODES() - 1)
                        + " (example: 4,2)\nFor exit write -1,-1:");
//          Server waiting for response from client
                inputLine = in.readLine();
                if (inputLine.equals("save-file")) {
//          Save the graph to the graph.txt file
                    graph.saveGraphToFile();
                } else {
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
                                System.out.println(ANSI_RED + "Client " + this.clientId + " has been disconnected from server" + ANSI_RESET);
                                out.println("Close connection with server!");
                                break;
                            }
//                  Check if the user gives available numbers with the min and max nodes
                            if (edgesInt[0] >= graph.getNODES() || edgesInt[1] >= graph.getNODES() ||
                                    edgesInt[0] < 0 || edgesInt[1] < 0) {
                                out.println("Wrong input");
                                continue;
                            }
//                  Check if the edge exists to the graph and create message for the client
                            String message = graph.edgeExists(edgesInt[0], edgesInt[1]);
                            System.out.println(ANSI_YELLOW + "Client " + this.clientId + ": Row: " + edges[0] + " Col: " + edges[1] + ANSI_RESET);
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
