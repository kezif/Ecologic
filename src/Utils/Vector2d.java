package Utils;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(double x, double y) {
        this.x = (int)Math.round(x);
        this.y = (int)Math.round(y);
    }
}
