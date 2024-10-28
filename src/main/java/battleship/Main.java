package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        shipCoordinates(scanner);
        printGameField();
    }
    // Print game field.
    private static void printGameField() {
        System.out.print("  ");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();
        for (char letter = 'A'; letter <= 'J'; letter++) {
            System.out.print(letter + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print("~ ");
            }
            System.out.println();
        }
    }
    // Ship coordinates
    private static void shipCoordinates(Scanner scanner) {
        System.out.println("Enter the coordinates of the ship:");
        String coordinates = scanner.nextLine();
        // TODO: Add validation check to prevent errors.
        String[] tokens = coordinates.split(" ");
        // TODO: Add method to figure out length.
        // TODO: Add method to figure out parts.
    }

    private static void shipLength() {
        // TODO: Add code.
    }

    private static void shipParts() {
        // TODO: Add code.
    }
}

class Coordinate {
    private char row; // 'A' to 'J'.
    private int column; // 1 to 10,

    public Coordinate(char row, int column) {
        this.row = row;
        this.column = column;
    }

    public char getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    // Parse a string like "A1" into a coordinate object.
    public static Coordinate parse(String coord) throws IllegalArgumentException {
        if (coord.length() < 2 || coord.length() > 3) {
            throw new IllegalArgumentException("Invalid coordinate format: " + coord);
        }

        char row = Character.toUpperCase(coord.charAt(0));
        if (row < 'A' || row > 'J') {
            throw new IllegalArgumentException("Row must be between A and J: " + row);
        }

        String colStr = coord.substring(1);
        int column;
        try {
            column = Integer.parseInt(colStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Column must be a number between 1 and 10: " + colStr);
        }

        if (column < 1 || column > 10) {
            throw new IllegalArgumentException("Column must be between 1 and 10: " + column);
        }
        return new Coordinate(row, column);
    }

    @Override
    public String toString() {
        return "" + row + column;
    }
}
