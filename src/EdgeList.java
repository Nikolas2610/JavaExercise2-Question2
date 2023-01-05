import java.util.Random;

public class EdgeList {
    private final int NODES = 10;
    private final int[][] edgeArray = new int[NODES][NODES];

    public EdgeList() {
//      Add edges to table when class create
        addEdgesToTable();
    }

    public String edgeExists(int row, int col) {
        String message = "";
        if (edgeArray[row][col] == 1) {
            message += "++++++++++++++++++++++++++++++++++++++++++++\n" +
                    "The edge [" + row + "][" + col + "] exist to the graph.\n" +
                    "++++++++++++++++++++++++++++++++++++++++++++\n";
            message += printTable(edgeArray, row, col).toString();
        } else {
            message += "++++++++++++++++++++++++++++++++++++++++++++\n" +
                    "The edge [" + row + "][" + col + "] does not exist to the graph.\n" +
                    "++++++++++++++++++++++++++++++++++++++++++++\n";
            message += printTable(edgeArray, row, col).toString();
        }
        return message;
    }

//  Return nodes value
    public int getNODES() {
        return NODES;
    }

    private void addEdgesToTable() {
        setDefaultValuesToEdgeTable();  // Initialize the table with -1 values
        for (int i = 0; i < edgeArray.length; i++) {
            for (int j = 0; j < edgeArray.length; j++) {
                if (edgeArray[i][j] == -1) {    // if node didn't initialize with value
                    edgeArray[i][j] = randomNumber(0, 1);   // add random value
                    if (i != j) {   // if is not the same node
                        edgeArray[j][i] = edgeArray[i][j];  // add edge to the opposite side
                    }
                }
            }
        }
    }

//    Create Node table as string
    private StringBuilder printTable(int[][] array, int row, int col) {
        StringBuilder table = new StringBuilder();
        for (int i = 0; i < edgeArray.length; i++) {
            if (i == 0) {
                table.append("\033[34m   |");
                for (int j = 0; j < edgeArray.length; j++) {
                    table.append("\033[34m ").append(j).append(" |\033[0m");
                }
                // Change Line
                table.append("\n");
                for (int j = 0; j < edgeArray.length + 1; j++) {
                    table.append("---+");
                }
                table.append("\n");
            }
            table.append("\033[34m").append(i).append("  |\033[0m");
            for (int j = 0; j < edgeArray[i].length; j++) {
                if (row == i && col == j) {
                    table.append(" \033[31m").append(edgeArray[i][j]).append(" \033[0m|");
                } else {
                    table.append(" ").append(edgeArray[i][j]).append(" |");
                }
            }
//          Line Break
            table.append("\n");
            for (int j = 0; j < edgeArray.length + 1; j++) {
                table.append("---+");
            }
            table.append("\n");
        }
        return table;
    }

//    Set default value -1 to the Graph
    private void setDefaultValuesToEdgeTable() {
        for (int i = 0; i < edgeArray.length; i++) {
            for (int j = 0; j < edgeArray.length; j++) {
                edgeArray[i][j] = -1;
            }
        }
    }

//  Return random number
    private static int randomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max + 1) - min) + (min);
    }
}
