package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
