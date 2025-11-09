package backtracking_solver;

import java.util.*;
import backtracking_solver.BinaryTuple;

public class BinaryConstraint {
    private int firstVar;
    private int secondVar;
    private ArrayList<BinaryTuple> tuples;
    private ArrayList<BinaryTuple> reversedTuples;

    public BinaryConstraint(int first, int second, ArrayList<BinaryTuple> t, ArrayList<BinaryTuple> rt) {
        this.firstVar = first;
        this.secondVar = second;
        this.tuples = t;
        this.reversedTuples = rt;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("First var = ").append(firstVar).append(", second var = ").append(secondVar).append("\n");
        for (BinaryTuple t : tuples) {
            result.append(t.toString()).append("\n");
        }
        return result.toString();
    }

    public int getFirstVar() {
        return this.firstVar;
    }

    public int getSecondVar() {
        return this.secondVar;
    }

    public ArrayList<BinaryTuple> getTuples() {
        return tuples;
    }

    public ArrayList<BinaryTuple> getReversedTuples() {
        return reversedTuples;
    }
}
