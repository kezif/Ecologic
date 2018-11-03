package Generation.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PriorityQueue<T extends Comparable<T>> extends ArrayList {
    //private int size;
//    Arr[(i-1)/2]	Returns the parent node
//    Arr[(2*i)+1]	Returns the left child node
//    Arr[(2*i)+2]	Returns the right child node
    private final ArrayList<T> array;

    public PriorityQueue() {
        array = new ArrayList<>();
    }
    private int parentIndex(int i){return (i-1)/2;}
    private int leftIndex(int i){return (2*i)+1;}
    private int rightIndex(int i){return (2*i)+2;}


    public void add(T value) {
        array.add(value);
        int i = array.size() - 1;
        shiftUp(i);
    }

    public void addAll(ArrayList<T> ar) {
        for (T integ : ar) {
            add(integ);
        }
    }


    private void swap(int i1, int i2){
        T temp = array.get(i1);
        array.set(i1, array.get(i2));
        array.set(i2, temp);
    }

    private void shiftUp(int i) {
        if (array.get(i).compareTo(array.get(parentIndex(i))) < 0 ) {
            swap(i, parentIndex(i));
            shiftUp(parentIndex(i));
        }
    }



    private void shiftDown(int i) {
        int l = leftIndex(i);
        int r = rightIndex(i);
        int small = i;
        if( l < array.size() && array.get(i).compareTo(array.get(l)) > 0) small = l;
        if( r < array.size() && array.get(small).compareTo(array.get(r))>0) small = r;
        if(small!= i){
            swap(i, small);
            shiftDown(small);
        }

    }

    public T peek() {
        if (array.size() < 0) {
            throw new NoSuchElementException();
        }
        return array.get(0);
    }

    public T pop() {
        T popN = peek();
        int lastIndex = array.size() - 1;
        array.set(0, array.get(lastIndex)); //move last value to top
        array.remove(array.size() - 1);
        shiftDown(0);
        return popN;

    }

    public int size() {
        return array.size();
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public int treeDepth() {
        int v = 0;
        int temp = array.size();
        while (temp > 0) {
            temp -= Math.pow(2, v++);
        }
        return v;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        int depth = treeDepth();
        for (int i = 0; i < array.size(); i++) {
            string.append(array.get(i) + " ");

        }
        return string.toString();
    }
}
