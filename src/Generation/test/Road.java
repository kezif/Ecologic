package Generation.test;

//import com.sun.javafx.geom.Vec2f;

public class Road implements Comparable<Road> {
    int x, y;
    int length = 20;
    //Vec2f start;
    //Vec2f end;
    float sX;
    float sY;
    float eX;
    float eY;
    double angle;
    int priority;


    public Road(int t, float sX, float sY, float eX, float eY) {
        this.priority = t;
        this.eX = eX;
        this.eY = eY;
        this.sX = sX;
        this.eY = sY;
    }

    public Road(int t, float sX, float sY, double angle){
        this.priority = t;
        this.sX = sX;
        this.sY = sY;
        this.eX = (float)Math.cos(angle) * sX;
        this.eY = (float)Math.sin(angle) * sY;

    }

    public float getEx() {
        return eX;
    }
    public float getEy() {
        return eY;
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
