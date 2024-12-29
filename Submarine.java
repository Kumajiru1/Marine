public class Submarine {
    int x, y, health;

    public Submarine(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
    }
    // x座標のゲッター
    public int getX() {
        return x;
    }

    // y座標のゲッター
    public int getY() {
        return y;
    }

    // 健康状態のゲッター
    public int getHealth() {
        return health;
    }

    // 潜水艦の位置を更新するためのメソッド
    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
}
    