package backtracking_solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import backtracking_solver.*;
 
public class CSPSolver {

    public CSPSolver() {
        // Empty constructor is fine
    }

    public StringBuilder solve(BinaryCSP csp,
                               HashMap<Arc, ArrayList<BinaryTuple>> constraints,
                               ArrayList<HashSet<Integer>> domains) {

        StringBuilder sb = new StringBuilder();

        sb.append("     ==> Backtracking solver launched...\n");
        int nvar = domains.size();
        int[] assignment = new int[nvar]; // Array para almacenar la asignación de valores a las variables

        sb.append("     ==> Searching for solution...\n");
        backtrack(0, assignment, domains, constraints, sb);


        /*
        // Temporary output (you’ll replace it later with your backtracking algorithm)
        sb.append("Number of variables: ").append(domains.size()).append("\n");
        sb.append("Number of constraints: ").append(constraints.size() / 2).append("\n");

        // Here you will implement your backtracking search logic
        // For now, just a placeholder
        sb.append("Solver not implemented yet.\n");
        */

        sb.append("     ==> Backtracking finished.\n");
        return sb;
    }

    private void backtrack(int depth, int[] assignment,
                              ArrayList<HashSet<Integer>> domains,
                              HashMap<Arc, ArrayList<BinaryTuple>> constraints,
                              StringBuilder sb) {

        int n = domains.size(); 
        
        // CASO BASE: Si llegas al final, imprimir solución
        if (depth == n) {
            // All variables assigned, print solution
            System.out.println("Solution found:");
            sb.append("     ==> Solution found:\n");
            for (int i = 0; i < n; i++) {
                sb.append("          Variable ").append(i).append(" = ").append(assignment[i]).append("\n");
            }
            return; // Solution found
        }

        // CASO RECURSIVO: Asignar valores a la variable actual
        for (int d: domains.get(depth)){
            assignment[depth] = d; // assign value
            boolean consistent = true;

            // Check consistency with previous assignments
             for (int past = 0; past < depth && consistent; past++){
                int valPast = assignment[past];
                if (!isConsistent(past, valPast, depth, d, constraints)) {
                    consistent = false;
                }
            }
            
            // If consistent, go deeper
            if (consistent) {
                backtrack(depth + 1, assignment, domains, constraints, sb);
            }
            // Undo assignment before next value
            assignment[depth] = -1;
        }
    }

    private boolean isConsistent(int var1, int val1,
                                 int var2, int val2,
                                 HashMap<Arc, ArrayList<BinaryTuple>> constraints) {
    
        Arc arc = new Arc(var1, var2);
        ArrayList<BinaryTuple> allowed = constraints.get(arc);

        // If no constraint between these vars --> always consistent
        if (allowed == null){
           return true; 
        } 

        // Check if the pair <val1, val2> is in the allowed list
        for (BinaryTuple tuple : allowed) {
            if (tuple.firstVal() == val1 && tuple.secondVal() == val2)
                return true; // allowed
        }
        return false; // not allowed
    }                           
        
    
}