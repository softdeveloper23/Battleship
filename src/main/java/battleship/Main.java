package battleship;

import java.util.Scanner;

public class Main {
    private static final int GRID_SIZE = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printGameField();
        shipCoordinates(scanner);
    }
    // Print game field.
    private static void printGameField() {
        System.out.print("  ");
        for (int i = 1; i <= GRID_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (char letter = 'A'; letter <= 'J'; letter++) {
            System.out.print(letter + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print("~ ");
            }
            System.out.println();
        }
    }
    // Ship coordinates
    private static void shipCoordinates(Scanner scanner) {
        System.out.println("Enter the coordinates of the ship:");
        String coordinates = scanner.nextLine();

        // Validation check begins.
        String[] tokens = coordinates.split(" ");
        if (tokens.length != 2) {
            System.out.println("Error: You must enter exactly two coordinates.");
            return;
        }
        // Parse the coordinates.
        int[] coord1 = parseCoordinate(tokens[0]);
        int[] coord2 = parseCoordinate(tokens[1]);

        // If parsing failed, exit the method.
        if (coord1 == null || coord2 == null) {
            System.out.println("Error: Invalid coordinate format.");
            return;
        }

        int row1 = coord1[0];
        int col1 = coord1[1];
        int row2 = coord2[0];
        int col2 = coord2[1];

        // Check that coordinates are within bounds.
        if (isOutOfBounds(row1, col1) || isOutOfBounds(row2, col2)) {
            System.out.println("Error: Coordinates are out of bounds.");
            return;
        }

        // Check that the ship is placed either horizontally or vertically.
        if (row1 != row2 && col1 != col2) {
            System.out.println("Error: Ship must be placed horizontally or vertically.");
            return;
        }

        // At this point, the coordinates are valid.
        System.out.println("Coordinates are valid.");

        // TODO: Add method to figure out ship length.
        int length = shipLength(col1, col2);
        System.out.println("Length: " + length);
        // TODO: Add method to figure out parts.
    }

    private static int[] parseCoordinate(String coord) {
        try {
            // Extract the row letter and the column number.
            char rowChar = coord.charAt(0);
            String colStr = coord.substring(1);

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

    private static boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE;
    }

    private static int shipLength(int col1, int col2) {
        int shipLength;
        if (col1 > col2) {
            shipLength = (col1 - col2) + 1;
        } else {
            shipLength = (col2 - col1) + 1;
        }
        return shipLength;
    }

    private static void shipParts() {
        // TODO: Add code.
    }
}
