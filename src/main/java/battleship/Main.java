package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Main class for the Battleship game.
 */
public class Main {
    private static final int GRID_SIZE = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize the game field.
        GameField gameField = new GameField(GRID_SIZE);

        // Print the initial empty game field.
        gameField.print();

        // Get ship coordinates from the player.
        Ship ship = getShipFromInput(scanner);

        // Place the ship on the game field if valid.
        if (ship != null) {
            if (gameField.placeShip(ship)) {
                // Print the game field with the ship placed
                gameField.print();
            } else {
                System.out.println("Error: Cannot place ship at the specified coordinates.");
            }
        }

        scanner.close();
    }

    /**
     * Prompts the user to enter ship coordinates and creates a Ship object.
     *
     * @param scanner The Scanner object for user input.
     * @return A Ship object if the input is valid; null otherwise.
     */
    private static Ship getShipFromInput(Scanner scanner) {
        System.out.println("Enter the coordinates of the ship (e.g., A1 A5):");
        String coordinates = scanner.nextLine();

        // Validation check begins.
        String[] tokens = coordinates.trim().split("\\s+");
        if (tokens.length != 2) {
            System.out.println("Error: You must enter exactly two coordinates.");
            return null;
        }

        // Parse the coordinates.
        int[] coordinate1 = parseCoordinate(tokens[0]);
        int[] coordinate2 = parseCoordinate(tokens[1]);

        // If parsing failed, exit the method.
        if (coordinate1 == null || coordinate2 == null) {
            System.out.println("Error: Invalid coordinate format.");
            return null;
        }

        int row1 = coordinate1[0];
        int col1 = coordinate1[1];
        int row2 = coordinate2[0];
        int col2 = coordinate2[1];

        // Check that coordinates are within bounds.
        if (isOutOfBounds(row1, col1) || isOutOfBounds(row2, col2)) {
            System.out.println("Error: Coordinates are out of bounds.");
            return null;
        }

        // Check that the ship is placed either horizontally or vertically.
        if (row1 != row2 && col1 != col2) {
            System.out.println("Error: Ship must be placed horizontally or vertically.");
            return null;
        }

        // Calculates the ship's length.
        int length = calculateShipLength(row1, col1, row2, col2);
        System.out.println("Coordinates are valid.");
        System.out.println("Length: " + length);

        // Create the Ship object.
        Ship ship = new Ship(row1, col1, row2, col2, length);

        // Print the ship parts.
        printShipParts(ship);

        // Return the Ship object.
        return ship;
    }

    /**
     * Prints the coordinates (parts) of the ship.
     *
     * @param ship The ship whose parts are to be printed.
     */
    private static void printShipParts(Ship ship) {
        System.out.print("Parts: ");
        List<int[]> shipCoords = ship.getCoordinates();

        for (int[] coord : shipCoords) {
            char rowChar = (char) ('A' + coord[0]);
            int colNumber = coord[1] + 1; // Convert to 1-based index
            System.out.print(rowChar + "" + colNumber + " ");
        }
        System.out.println(); // For a new line after listing parts
    }

    /**
     * Parses a coordinate string into row and column indices.
     *
     * @param coordinate The coordinate string (e.g., "A5").
     * @return An array with row and column indices; null if parsing fails.
     */
    private static int[] parseCoordinate(String coordinate) {
        try {
            coordinate = coordinate.trim().toUpperCase();
            // Extract the row letter and the column number.
            char rowChar = coordinate.charAt(0);
            String colStr = coordinate.substring(1);

            // Convert row letter to row index (0-based).
            int row = rowChar - 'A';
            // Convert column string to column index (0-based).
            int col = Integer.parseInt(colStr) - 1;

            return new int[]{row, col};
        } catch (Exception e) {
            // If parsing fails, return null.
            return null;
        }
    }

    /**
     * Checks if the given row and column are out of bounds.
     *
     * @param row The row index.
     * @param col The column index.
     * @return True if out of bounds; false otherwise.
     */
    private static boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE;
    }

    /**
     * Calculates the length of the ship based on its coordinates.
     *
     * @param row1 The starting row index.
     * @param col1 The starting column index.
     * @param row2 The ending row index.
     * @param col2 The ending column index.
     * @return The length of the ship.
     */
    private static int calculateShipLength(int row1, int col1, int row2, int col2) {
        if (row1 == row2) {
            // Horizontal ship.
            return Math.abs(col2 - col1) + 1;
        } else {
            // Vertical ship.
            return Math.abs(row2 - row1) + 1;
        }
    }
}

/**
 * Represents the game field (grid) for the Battleship game.
 */
class GameField {
    private final int size;
    private final char[][] grid;

    /**
     * Initializes the game field with the specified size.
     *
     * @param size The size of the grid (e.g., 10 for a 10x10 grid).
     */
    public GameField(int size) {
        this.size = size;
        this.grid = new char[size][size];
        // Initialize the grid with '~' to represent water.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '~';
            }
        }
    }
    /**
     * Prints the current status of the game field.
     */
    public void print() {
        // Print column numbers.
        System.out.print("  ");
        for (int i = 1; i <= size; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Print each row with its letter label.
        for (int i = 0; i < size; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Places a ship on the game field.
     *
     * @param ship The ship to place.
     * @return True if the ship was placed successfully; false otherwise.
     */
    public boolean placeShip(Ship ship) {
        // Get the coordinates of the ship's parts.
        List<int[]> shipCoordinates = ship.getCoordinates();

        // Check if any part of the ship overlaps with existing ships.
        for (int[] coord : shipCoordinates) {
            int row = coord[0];
            int col = coord[1];
            if (grid[row][col] == 'O') {
                System.out.println("Error: Ship is already occupied.");
                return false;
            }
        }

        // Place the ship on the grid.
        for (int[] coord : shipCoordinates) {
            int row = coord[0];
            int col = coord[1];
            grid[row][col] = 'O'; // 'O' represents a ship part.
        }
        return true;
    }
}

/**
 * Represents a ship in the Battleship game.
 */
class Ship {
    private final int rowStart;
    private final int colStart;
    private final int rowEnd;
    private final int colEnd;
    private final int length;
    private final List<int[]> coordinates;

    /**
     * Creates a ship with the specified coordinates and length.
     *
     * @param rowStart Starting row index.
     * @param colStart Starting column index.
     * @param rowEnd   Ending row index.
     * @param colEnd   Ending column index.
     * @param length   Length of the ship.
     */

    public Ship(int rowStart, int colStart, int rowEnd, int colEnd, int length) {
        this.rowStart = rowStart;
        this.colStart = colStart;
        this.rowEnd = rowEnd;
        this.colEnd = colEnd;
        this.length = length;
        this.coordinates = calculateCoordinates();
    }

    /**
     * Calculates the coordinates occupied by the ship.
     *
     * @return A list of int arrays representing the ship's coordinates.
     */
    private List<int[]> calculateCoordinates() {
        List<int[]> coordinates = new ArrayList<>();

        if (rowStart == rowEnd) {
            // Horizontal ship.
            int step = (colStart <= colEnd) ? 1 : -1;
            for (int col = colStart; col != colEnd + step; col += step) {
                coordinates.add(new int[]{rowStart, col});
            }
        } else {
            // Vertical ship.
            int step = (rowStart <= rowEnd) ? 1 : -1;
            for (int row = rowStart; row != rowEnd + step; row += step) {
                coordinates.add(new int[]{row, colStart});
            }
        }
        return coordinates;
    }

    /**
     * Gets the coordinates occupied by the ship.
     *
     * @return A list of int arrays representing the ship's coordinates.
     */
    public List<int[]> getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the length of the ship.
     *
     * @return The length of the ship.
     */
    public int getLength() {
        return length;
    }
}