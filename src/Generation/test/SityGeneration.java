package Generation.test;

import java.util.*;

public class SityGeneration {
    public static void main(String[] args) {
        PriorityQueue priorityQueue = new PriorityQueue();
        priorityQueue.addAll(new ArrayList<Integer>(Arrays.asList(33,41,7,15,55,86,28,22,94,9,11,10,8,99,16,27,51,83,2)));
        System.out.println(priorityQueue);
        priorityQueue.pop();
        System.out.println(priorityQueue + "    \n\n\n\n");



    }
}
