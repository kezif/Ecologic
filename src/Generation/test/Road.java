package Generation.test;

import com.sun.javafx.geom.Vec2f;

public class Road implements Comparable<Road> {
    int x, y;
    int length = 20;
    Vec2f start;
    Vec2f end;
    int priority;


    public Road(int t) {
        this.priority = t;

    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLength() {
        return length;
    }
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Road o) {
        return (this.getPriority() - o.getPriority());
    }



}
