package Generation.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RoadGeneration {


    public static void main(String[] args) {

        int seed = new Random().nextInt();

        PriorityQueue<Road> q = new PriorityQueue<Road>();     //Create a priority queue of things to process, add a single seed
        q.add(new Road(0));
        LinkedList<Road> s = new LinkedList<>();               //Create a list of segments (the result we're building)

        while(q.isEmpty()) {
            Road cur = q.pop();                                //Remove the highest priority item from the priority queue
            Road modified = checkLocalConstraints(cur, s);     //Check that it is valid, skip to the next segment if it is not
            if (modified == null)
                continue;
            s.add(cur);                                        //It's valid, so add it to S. It is now part of the final result
            for(Road r : GlobalGoals(cur, s)) {                //Now produce potential segments leading off this road according to some global goal
                q.add(r);
            }
        }
    }

    private static Road checkLocalConstraints(Road cur, LinkedList<Road> allRoads) {
        return null;
    }

    private static ArrayList<Road> GlobalGoals(Road cur, LinkedList<Road> allRoads) {
        return new ArrayList<>();
    }
}
