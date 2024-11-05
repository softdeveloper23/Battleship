package battleship;

import java.util.*;

/**
 * Main class for the Battleship game.
 */
public class Main {
    public static final int GRID_SIZE = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        // List of ships to place
        List<ShipInfo> shipsToPlace = Arrays.asList(
                new ShipInfo("Aircraft Carrier", 5),
                new ShipInfo("Battleship", 4),
                new ShipInfo("Submarine", 3),
                new ShipInfo("Cruiser", 3),
                new ShipInfo("Destroyer", 2)
        );

        // Place ships for Player 1
        placeShips(player1, scanner, shipsToPlace);

        // Place ships for Player 2
        placeShips(player2, scanner, shipsToPlace);

        // Main game loop
        boolean gameOver = false;
        Player currentPlayer = player1;
        Player opponent = player2;

        while (!gameOver) {
            gameOver = takeTurn(currentPlayer, opponent, scanner);

            // Swap players if the game is not over
            if (!gameOver) {
                Player temp = currentPlayer;
                currentPlayer = opponent;
                opponent = temp;
            }
        }

        scanner.close();
    }

    /**
     * Handles ship placement for a player.
     *
     * @param player        The player placing ships.
     * @param scanner       Scanner for input.
     * @param shipsToPlace  List of ships to place.
     */
    private static void placeShips(Player player, Scanner scanner, List<ShipInfo> shipsToPlace) {
        System.out.println(player.getName() + ", place your ships on the game field");
        player.getGameField().print(false);

        for (ShipInfo shipInfo : shipsToPlace) {
            boolean placed = false;
            while (!placed) {
                // Prompt the user to enter coordinates for the current ship.
                Ship ship = getShipFromInput(scanner, shipInfo);

                if (ship == null) {
                    // Invalid input; prompt the user again.
                    continue;
                }

                // Attempt to place the ship on the player's game field.
                if (player.getGameField().placeShip(ship)) {
                    // Successfully placed the ship; print the updated game field.
                    System.out.println(shipInfo.getName() + " placed successfully.");
                    player.getGameField().print(false);
                    placed = true;
                } else {
                    System.out.println("Error: Cannot place " + shipInfo.getName() + " at the specified coordinates.");
                }
            }
        }
        promptEnterKey();
        clearScreen();
    }

    /**
     * Handles a player's turn.
     *
     * @param currentPlayer The player taking the turn.
     * @param opponent      The opponent player.
     * @param scanner       Scanner for input.
     * @return True if the game is over; false otherwise.
     */
    private static boolean takeTurn(Player currentPlayer, Player opponent, Scanner scanner) {
        // Display the opponent's field with fog of war
        opponent.getGameField().print(true);
        System.out.println("---------------------");
        // Display the current player's own field
        currentPlayer.getGameField().print(false);
        System.out.println();

        System.out.println(currentPlayer.getName() + ", it's your turn:");
        String shotInput;
        int[] shotCoordinate = null;

        do {
            System.out.print("> ");
            shotInput = scanner.nextLine();
            shotCoordinate = parseCoordinate(shotInput);
            if (shotCoordinate == null || isOutOfBounds(shotCoordinate[0], shotCoordinate[1])) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            } else {
                break;
            }
        } while (true);

        // Process the shot on the opponent's game field
        GameField.ShotResult result = opponent.getGameField().processShot(shotCoordinate[0], shotCoordinate[1]);

        // Update messages based on the result
        switch (result) {
            case MISS:
                System.out.println("You missed!");
                break;
            case HIT:
                System.out.println("You hit a ship!");
                break;
            case SUNK:
                if (opponent.getGameField().areAllShipsSunk()) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    return true; // Game over
                } else {
                    System.out.println("You sank a ship!");
                }
                break;
        }

        promptEnterKey();
        clearScreen();
        return false; // Continue game
    }

    /**
     * Prompts the user to press Enter to continue.
     */
    private static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the console screen.
     */
    private static void clearScreen() {
        // Clear the console (works in most terminals)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Prompts the user to enter ship coordinates and creates a Ship object.
     *
     * @param scanner  The Scanner object for user input.
     * @param shipInfo Information about the ship to place.
     * @return A Ship object if the input is valid; null otherwise.
     */
    private static Ship getShipFromInput(Scanner scanner, ShipInfo shipInfo) {
        System.out.println("Enter the coordinates of the " + shipInfo.getName() + " (" + shipInfo.getLength() + " cells):");
        System.out.print("> ");
        String coordinates = scanner.nextLine();

        // Split the input into two tokens based on whitespace.
        String[] tokens = coordinates.trim().split("\\s+");
        if (tokens.length != 2) {
            System.out.println("Error: You must enter exactly two coordinates separated by a space (e.g., A1 A5).");
            return null;
        }

        // Parse the coordinates.
        int[] coordinate1 = parseCoordinate(tokens[0]);
        int[] coordinate2 = parseCoordinate(tokens[1]);

        // If parsing failed, exit the method.
        if (coordinate1 == null || coordinate2 == null) {
            System.out.println("Error: Invalid coordinate format. Please use the format LetterNumber (e.g., A5).");
            return null;
        }

        int row1 = coordinate1[0];
        int col1 = coordinate1[1];
        int row2 = coordinate2[0];
        int col2 = coordinate2[1];

        // Check that coordinates are within bounds.
        if (isOutOfBounds(row1, col1) || isOutOfBounds(row2, col2)) {
            System.out.println("Error: Coordinates are out of bounds. Please enter values between A-J and 1-10.");
            return null;
        }

        // Check that the ship is placed either horizontally or vertically.
        if (row1 != row2 && col1 != col2) {
            System.out.println("Error: Ship must be placed horizontally or vertically (not diagonally).");
            return null;
        }

        // Calculate the ship's length based on input coordinates.
        int calculatedLength = calculateShipLength(row1, col1, row2, col2);

        // Validate the ship's length.
        if (calculatedLength != shipInfo.getLength()) {
            System.out.println("Error: The length of the " + shipInfo.getName() + " must be " + shipInfo.getLength() + " cells.");
            return null;
        }

        // Create the Ship object with the specified coordinates.
        Ship ship = new Ship(row1, col1, row2, col2, calculatedLength);

        return ship;
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

            // Validate row character.
            if (rowChar < 'A' || rowChar > 'J') {
                return null;
            }

            // Convert row letter to row index (0-based).
            int row = rowChar - 'A';

            // Convert column string to column index (0-based).
            int col = Integer.parseInt(colStr) - 1;

            // Validate column number.
            if (col < 0 || col >= GRID_SIZE) {
                return null;
            }

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
 * Represents a player in the game.
 */
class Player {
    private final String name;
    private final GameField gameField;

    public Player(String name) {
        this.name = name;
        this.gameField = new GameField(Main.GRID_SIZE);
    }

    public String getName() {
        return name;
    }

    public GameField getGameField() {
        return gameField;
    }
}

/**
 * Represents the game field (grid) for the Battleship game.
 */
class GameField {
    private final int size;
    private final char[][] grid;
    private final List<Ship> ships = new ArrayList<>();

    /**
     * Enum to represent the result of a shot.
     */
    public enum ShotResult {
        MISS,
        HIT,
        SUNK
    }

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
            Arrays.fill(grid[i], '~');
        }
    }

    /**
     * Prints the current status of the game field.
     *
     * @param fogOfWar If true, hides the ships; otherwise, shows the full field.
     */
    public void print(boolean fogOfWar) {
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
                char cell = grid[i][j];
                if (fogOfWar) {
                    if (cell == 'O') {
                        System.out.print("~ "); // Hide the ship under fog.
                    } else {
                        System.out.print(cell + " "); // Display hits ('X') and misses ('M') as is.
                    }
                } else {
                    System.out.print(cell + " "); // Display the full field.
                }
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

        // Check if any part of the ship overlaps with existing ships or is adjacent.
        for (int[] coord : shipCoordinates) {
            int row = coord[0];
            int col = coord[1];
            if (grid[row][col] == 'O') {
                System.out.println("Error: Ship overlaps with another ship.");
                return false;
            }

            // Check adjacent cells to enforce no adjacency rule.
            if (isAdjacent(row, col)) {
                System.out.println("Error: Ships cannot be adjacent to each other.");
                return false;
            }
        }

        // Place the ship on the grid.
        for (int[] coord : shipCoordinates) {
            int row = coord[0];
            int col = coord[1];
            grid[row][col] = 'O'; // 'O' represents a ship part.
        }

        // Add the ship to the list of ships.
        ships.add(ship);

        return true;
    }

    /**
     * Checks if the given cell is adjacent to any existing ship.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return True if adjacent to an existing ship; false otherwise.
     */
    private boolean isAdjacent(int row, int col) {
        // Define the relative positions to check for adjacency.
        int[] deltaRows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] deltaCols = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < deltaRows.length; i++) {
            int newRow = row + deltaRows[i];
            int newCol = col + deltaCols[i];

            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                if (grid[newRow][newCol] == 'O') {
                    return true; // Adjacent to another ship.
                }
            }
        }
        return false; // No adjacency.
    }

    /**
     * Processes a shot at the given coordinates.
     *
     * @param row The row index of the shot.
     * @param col The column index of the shot.
     * @return The result of the shot.
     */
    public ShotResult processShot(int row, int col) {
        if (grid[row][col] == 'O' || grid[row][col] == 'X') {
            grid[row][col] = 'X'; // Mark hit
            // Find the ship that was hit
            for (Ship ship : ships) {
                if (ship.containsCoordinate(row, col)) {
                    ship.hit(row, col);
                    if (ship.isSunk()) {
                        return ShotResult.SUNK;
                    } else {
                        return ShotResult.HIT;
                    }
                }
            }
            return ShotResult.HIT; // Default to HIT if ship not found
        } else {
            grid[row][col] = 'M'; // Mark miss
            return ShotResult.MISS;
        }
    }

    /**
     * Checks if all ships have been sunk.
     *
     * @return True if all ships are sunk; false otherwise.
     */
    public boolean areAllShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
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
    private final Set<String> hitCoordinates = new HashSet<>();

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
     * Calculates the coordinates occupied by the ship in the order specified by the user.
     *
     * @return A list of int arrays representing the ship's coordinates.
     */
    private List<int[]> calculateCoordinates() {
        List<int[]> coords = new ArrayList<>();

        if (rowStart == rowEnd) {
            // Horizontal ship.
            int step = (colStart <= colEnd) ? 1 : -1;
            for (int col = colStart; col != colEnd + step; col += step) {
                coords.add(new int[]{rowStart, col});
            }
        } else {
            // Vertical ship.
            int step = (rowStart <= rowEnd) ? 1 : -1;
            for (int row = rowStart; row != rowEnd + step; row += step) {
                coords.add(new int[]{row, colStart});
            }
        }
        return coords;
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
     * Checks if the ship occupies the given coordinate.
     *
     * @param row The row index.
     * @param col The column index.
     * @return True if the ship occupies the coordinate; false otherwise.
     */
    public boolean containsCoordinate(int row, int col) {
        for (int[] coord : coordinates) {
            if (coord[0] == row && coord[1] == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * Records a hit on the ship.
     *
     * @param row The row index of the hit.
     * @param col The column index of the hit.
     */
    public void hit(int row, int col) {
        hitCoordinates.add(row + "," + col);
    }

    /**
     * Checks if the ship has been sunk.
     *
     * @return True if all parts of the ship have been hit; false otherwise.
     */
    public boolean isSunk() {
        return hitCoordinates.size() == coordinates.size();
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

/**
 * Represents information about a ship to be placed on the game field.
 */
class ShipInfo {
    private final String name;
    private final int length;

    /**
     * Constructs a ShipInfo object with the specified name and length.
     *
     * @param name   The name of the ship.
     * @param length The length of the ship in cells.
     */
    public ShipInfo(String name, int length) {
        this.name = name;
        this.length = length;
    }

    /**
     * Gets the name of the ship.
     *
     * @return The ship's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the length of the ship.
     *
     * @return The ship's length.
     */
    public int getLength() {
        return length;
    }
}
