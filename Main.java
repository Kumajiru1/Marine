import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SubmarineGame game = new SubmarineGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("先行 (1) か 後攻 (2) を選択してください:");
        int order = scanner.nextInt();

        if (order == 1) {
            game.performInitialAttack();
        } else {
            game.performInitialMove();
        }

        while (true) {
            System.out.println("敵の行動を入力してください (攻撃: A, 移動: M):");
            char action = scanner.next().charAt(0);

            if (action == 'A') {
                System.out.println("敵の攻撃位置を入力してください (例: A1):");
                String position = scanner.next();
                System.out.println("攻撃が波高しだったかどうか (true/false):");
                boolean isNearMiss = scanner.nextBoolean();
                game.recordEnemyAttack(position, isNearMiss);
            } else if (action == 'M') {
                System.out.println("敵の移動位置を入力してください (例: A1 -> B2):");
                String move = scanner.next();
                game.recordEnemyMove(move);
            }

            System.out.println("自分の行動を選択してください (攻撃: A, 移動: M):");
            action = scanner.next().charAt(0);

            if (action == 'A') {
                System.out.println("攻撃位置を入力してください (例: A1):");
                String position = scanner.next();
                int x = position.charAt(1) - '1';
                int y = position.charAt(0) - 'A';

                System.out.println("攻撃結果を入力してください (命中: H, 波高し: N, 外れ: M):");
                char result = scanner.next().charAt(0);

                boolean isHit = result == 'H';
                boolean isNearMiss = result == 'N';

                game.fireTorpedo(x, y, isHit, isNearMiss);
            } else if (action == 'M') {
                game.performMove();
            }

            game.displayGrid();
        }
    }
}