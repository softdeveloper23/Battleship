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
    }

    private static Ship getShipFromInput(Scanner scanner) {
        // TODO: Code goes here.
        return null;
    }

    private static int[] parseCoordinate(String coordinate) {
        // TODO: Code goes here.
        return null;
    }

    private static boolean isOutOfBounds(int row, int col) {
        // TODO: Code goes here.
        return false;
    }

    private static int calculateShipLength(int row1, int col1, int row2, int col2) {
        return 0;
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
        System.out.print(" ");
        for (int i = 1; i <= size; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Print each row with its letter label.
        for (int i = 0; i < size; i++) {
            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(grid[j][i] + " ");
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
        // TODO: Code logic.
        return false;
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
        // TODO: Code goes here.
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