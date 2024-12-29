import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubmarineGame {
    private static final int GRID_SIZE = 5;
    private static final int NUM_SUBMARINES = 4;
    private static final int SUBMARINE_HEALTH = 3;

    private Grid grid;
    private List<Submarine> submarines;
    private Random random;

    public SubmarineGame() {
        grid = new Grid();
        submarines = new ArrayList<>();
        random = new Random();
        placeSubmarines();
        grid.displayGrid(); // 潜水艦を配置した後にグリッドを表示
    }

    private void placeSubmarines() {
        int[][] regions = {
            {0, 0, 1, 1},
            {0, 3, 1, 4},
            {3, 0, 4, 1},
            {3, 3, 4, 4}
        };

        for (int[] region : regions) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(region[2] - region[0] + 1) + region[0];
                int y = random.nextInt(region[3] - region[1] + 1) + region[1];

                if (grid.isEmptyCell(x, y)) {
                    Submarine sub = new Submarine(x, y, SUBMARINE_HEALTH);
                    submarines.add(sub);
                    grid.placeSubmarine(x, y);
                    placed = true;
                }
            }
        }
    }

    public void performInitialAttack() {
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);
        fireTorpedo(x, y, false, false);
        System.out.println("初回の攻撃を行いました: (" + x + ", " + y + ")");
    }

    public void performInitialMove() {
        moveSubmarine();
        System.out.println("初回の移動を行いました");
    }

    public void recordEnemyAttack(String position, boolean isNearMiss) {
        int x = position.charAt(1) - '1';
        int y = position.charAt(0) - 'A';
        grid.updateExpectationGrid(x, y, false, isNearMiss);
        grid.updateDangerGrid(x, y, isNearMiss);
        System.out.println("敵が攻撃しました: (" + x + ", " + y + ")");
    }

    public void recordEnemyMove(String move) {
        String[] positions = move.split(" -> ");
        int oldX = positions[0].charAt(1) - '1';
        int oldY = positions[0].charAt(0) - 'A';
        int newX = positions[1].charAt(1) - '1';
        int newY = positions[1].charAt(0) - 'A';
        grid.moveEnemy(oldX, oldY, newX, newY);
        System.out.println("敵が移動しました: (" + oldX + ", " + oldY + ") -> (" + newX + ", " + newY + ")");
    }

    public boolean shouldAttack() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid.getExpectationValue(i, j) > 3 && isInAttackRange(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void performAttack() {
        int maxExpectation = 0;
        int targetX = 0;
        int targetY = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid.getExpectationValue(i, j) > maxExpectation && isInAttackRange(i, j)) {
                    maxExpectation = grid.getExpectationValue(i, j);
                    targetX = i;
                    targetY = j;
                }
            }
        }

        fireTorpedo(targetX, targetY, false, false);
        System.out.println("攻撃を行いました: (" + targetX + ", " + targetY + ")");
    }

    public void performMove() {
        moveSubmarine();
        System.out.println("移動を行いました");
    }

    public void fireTorpedo(int x, int y, boolean isHit, boolean isNearMiss) {
        System.out.println("Fired torpedo at (" + x + ", " + y + ")");
        grid.updateDangerGrid(x, y, true);
        if (isHit) {
            grid.destroySubmarine(x, y);
        }
        grid.updateExpectationGrid(x, y, isHit, isNearMiss);
    }

    private void moveSubmarine() {
        for (Submarine sub : submarines) {
            if (sub.getHealth() > 0) {
                if (tryMoveSubmarine(sub, sub.getX(), sub.getY() - 1)) return;
                if (tryMoveSubmarine(sub, sub.getX(), sub.getY() - 2)) return;
                if (tryMoveSubmarine(sub, sub.getX(), sub.getY() + 1)) return;
                if (tryMoveSubmarine(sub, sub.getX(), sub.getY() + 2)) return;
                if (tryMoveSubmarine(sub, sub.getX() - 1, sub.getY())) return;
                if (tryMoveSubmarine(sub, sub.getX() - 2, sub.getY())) return;
                if (tryMoveSubmarine(sub, sub.getX() + 1, sub.getY())) return;
                if (tryMoveSubmarine(sub, sub.getX() + 2, sub.getY())) return;
            }
        }
    }

    private boolean tryMoveSubmarine(Submarine sub, int newX, int newY) {
        if (newX >= 0 && newX < GRID_SIZE && newY >= 0 && newY < GRID_SIZE && grid.isEmptyCell(newX, newY)) {
            grid.moveSubmarine(sub.getX(), sub.getY(), newX, newY);
            System.out.println("潜水艦を移動しました: (" + sub.getX() + ", " + sub.getY() + ") -> (" + newX + ", " + newY + ")");
            sub.move(newX, newY);
            return true; // 一度の行動で一つの潜水艦しか動かさない
        }
        return false;
    }

    private boolean isInAttackRange(int x, int y) {
        for (Submarine sub : submarines) {
            if (sub.getHealth() > 0) {
                if ((sub.getX() == x - 1 && sub.getY() == y) || (sub.getX() == x + 1 && sub.getY() == y) || 
                    (sub.getX() == x && sub.getY() == y - 1) || (sub.getX() == x && sub.getY() == y + 1) || 
                    (sub.getX() == x - 1 && sub.getY() == y - 1) || (sub.getX() == x + 1 && sub.getY() == y + 1) || 
                    (sub.getX() == x - 1 && sub.getY() == y + 1) || (sub.getX() == x + 1 && sub.getY() == y - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void displayGrid() {
        grid.displayGrid();
    }
}