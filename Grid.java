public class Grid {
    private static final int GRID_SIZE = 5;
    private static final String EMPTY_CELL = ".";
    private static final String SUBMARINE_CELL = "S";
    private static final String DESTROYED_CELL = "D";

    private String[][] grid;
    private int[][] expectationGrid; // 期待値グリッド
    private int[][] dangerGrid; // 危険度グリッド

    public Grid() {
        grid = new String[GRID_SIZE][GRID_SIZE];
        expectationGrid = new int[GRID_SIZE][GRID_SIZE];
        dangerGrid = new int[GRID_SIZE][GRID_SIZE];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = EMPTY_CELL;
                expectationGrid[i][j] = 0;
                dangerGrid[i][j] = 0;
            }
        }
    }

    public boolean isEmptyCell(int x, int y) {
        return grid[x][y].equals(EMPTY_CELL);
    }

    public void placeSubmarine(int x, int y) {
        if (isValidCoordinate(x, y)) {
            grid[x][y] = SUBMARINE_CELL;
        }
    }

    public void moveSubmarine(int oldX, int oldY, int newX, int newY) {
        if (isValidCoordinate(oldX, oldY) && isValidCoordinate(newX, newY)) {
            grid[oldX][oldY] = EMPTY_CELL;
            grid[newX][newY] = SUBMARINE_CELL;
        }
    }

    public void destroySubmarine(int x, int y) {
        if (isValidCoordinate(x, y)) {
            grid[x][y] = DESTROYED_CELL;
        }
    }

    public void updateDangerGrid(int x, int y, boolean isNearMiss) {
        if (isNearMiss) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = x + dx;
                    int ny = y + dy;
                    if (isValidCoordinate(nx, ny)) {
                        dangerGrid[nx][ny] += 1;
                    }
                }
            }
        } else {
            dangerGrid[x][y] = 0;
        }
    }

    public void updateExpectationGrid(int x, int y, boolean isHit, boolean isNearMiss) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (isValidCoordinate(nx, ny)) {
                    if (isHit) {
                        continue;
                    } else if (isNearMiss) {
                        expectationGrid[nx][ny] += 1;
                    } else {
                        expectationGrid[nx][ny] = 0;
                    }
                }
            }
        }
    }

    public int getExpectationValue(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return expectationGrid[x][y];
        }
        return -1; // Invalid coordinate
    }

    public void moveEnemy(int oldX, int oldY, int newX, int newY) {
        if (isValidCoordinate(oldX, oldY) && isValidCoordinate(newX, newY)) {
            expectationGrid[newX][newY] += expectationGrid[oldX][oldY];
            expectationGrid[oldX][oldY] = 0;
        }
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    public void displayGrid() {
        System.out.println("Initial Submarine Positions:");
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}