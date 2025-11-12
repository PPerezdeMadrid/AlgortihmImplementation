package solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

public class ProblemPrinter {
    public int nvar;             // number of variables in the problem

    // A list of the variable domains, indexed by variable ID (its number)
    // Each domain is treated as a set of integers.
    public ArrayList<HashSet<Integer>> domains = new ArrayList<>(); 
    
    // The problem constraints. Those are a map from an arc to a list of 
    // valid tuples. That is, a table constraint for two variables. Note
    // that both Arc and BinaryTuple are custom implementations.
    public HashMap<Arc, ArrayList<BinaryTuple>> constraints = new HashMap<>(); // the problem constraints

    // Printing mode
    public String mode;
    public boolean run_solver;
    public StringBuilder sb_solver;
    public String solver_option;

    /**
     * 
     * @param csp The BinaryCSP instance containing the variables, domains, and constraints.
     * @param mode The mode for printing the problem (e.g., "verbose" or "compact").
     * 
     * This constructor initializes the following:
     * - The number of variables (`nvar`) from the given CSP.
     * - The `domains` list with the domain of each variable as specified in the CSP file.
     * - The `constraints` map with all binary constraints in both directions for easy querying.
     * - The printing mode for later use in the `toString` method.
     */
    public ProblemPrinter(BinaryCSP csp, String mode, boolean run_solver, String solver_option) {
        this.run_solver = run_solver;
        this.solver_option = solver_option;

        this.nvar = csp.getNoVariables(); 
        int[][] domainBounds = csp.getDomainBounds();
        for (int i = 0; i < domainBounds.length; i++) {
            // initialize its domain to the range specified in the .csp file
            // i.e., a 0 3 will create 0, 1, 2, 3 as its domain
            HashSet<Integer> newDomain = new HashSet<>();
            for (int j = domainBounds[i][0]; j <= domainBounds[i][1]; j++)
                newDomain.add(j);
            domains.add(newDomain); // This assumes domainBounds iteration is done in order
        }

        // We now store constraints, with tuples on both directions
        ArrayList<BinaryConstraint> cspConstraints = csp.getConstraints();
        for (BinaryConstraint con : cspConstraints) { // iterate over all constraints
            // add both directions, making it then easy to query and ask for the set of tuples.
            constraints.put(new Arc(con.getFirstVar(), con.getSecondVar()), con.getTuples());
            constraints.put(new Arc(con.getSecondVar(), con.getFirstVar()), con.getReversedTuples());
        }

        // finally we store the printing mode
        this.mode = mode;
        
        System.out.println("You chose: "+(run_solver));
        if (run_solver){
            BinaryCSPSolver solver = new BinaryCSPSolver();
            if(solver_option.equals("A")) sb_solver = solver.solve(csp, domains, constraints);
            if (solver_option.equals("B")) sb_solver = solver.solve_bnb(csp, domains, constraints);
            if(solver_option.equals("C")) sb_solver = solver.solve_FC_dway(csp, domains, constraints);
        }

    }

    /**
     * @return A string representing the current state.
     
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // TODO: implement different printing modes
        return sb.toString();
    }*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if ("verbose".equalsIgnoreCase(mode)) {
            sb.append("-- BinaryCSP: --\n");
            sb.append("Number of variables: ").append(nvar).append("\n");

            // --- Domains ---
            sb.append("Domains:\n");
            for (int i = 0; i < domains.size(); i++) {
                sb.append("  Variable ").append(i).append(": ");
                sb.append(domains.get(i));
                sb.append("\n");
            }

            sb.append("\nConstraints:\n");
            // We use a HashSet to avoid printing both (Xi,Xj) and (Xj,Xi)
            HashSet<String> printed = new HashSet<>();

            for (Map.Entry<Arc, ArrayList<BinaryTuple>> entry : constraints.entrySet()) {
                Arc arc = entry.getKey();
                ArrayList<BinaryTuple> tuples = entry.getValue();

                // Avoid duplicate printing of reversed arcs
                String key1 = arc.getFirstVar() + "," + arc.getSecondVar();
                printed.add(key1);

                sb.append("  ")
                .append(arc.getFirstVar())
                .append(" ")
                .append(arc.getSecondVar())
                .append(": [");

                for (int i = 0; i < tuples.size(); i++) {
                    sb.append(tuples.get(i).toString());
                    if (i < tuples.size() - 1)
                        sb.append(", "); // separador entre tuplas
                }
                sb.append("]");
                sb.append("\n");
            }

        } else if ("compact".equalsIgnoreCase(mode)) {
            sb.append("-- BinaryCSP: --\n");
            sb.append("Number of variables: ").append(nvar).append("\n");
            sb.append("Domains:\n");

            for (int i = 0; i < domains.size(); i++) {
                sb.append("  Variable ").append(i).append(": ");
                sb.append(domains.get(i).toString());
                sb.append("\n");
            }
        } else {
            sb.append("[Unknown printing mode: ").append(mode).append("]\n");
            sb.append("Use 'verbose' or 'compact'.\n");
        }

        if(run_solver){
            sb.append(sb_solver);
        }

        return sb.toString();
    }

}