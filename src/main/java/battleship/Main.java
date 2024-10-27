package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
}
