package fc_solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.*;

public class ProblemPrinter {
    protected int nvar;             // number of variables in the problem

    // A list of the variable domains, indexed by variable ID (its number)
    // Each domain is treated as a set of integers.
    protected ArrayList<HashSet<Integer>> domains = new ArrayList<>(); 
    
    // The problem constraints. Those are a map from an arc to a list of 
    // valid tuples. That is, a table constraint for two variables. Note
    // that both Arc and BinaryTuple are custom implementations.
    protected HashMap<Arc, ArrayList<BinaryTuple>> constraints = new HashMap<>(); // the problem constraints

    // Printing mode
    protected String mode;
    protected Boolean run_solver;
    protected StringBuilder sb_solver;
    protected String solver_type;

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
    protected ProblemPrinter(BinaryCSP csp, String mode, Boolean run_solver, String solver_type) {
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

        this.run_solver = run_solver;
        this.solver_type = solver_type;

        if(run_solver){
            if("A".equalsIgnoreCase(solver_type)){
                FCSolver solver = new FCSolver();
                sb_solver = solver.solve(domains, constraints);
            }
            if("B".equalsIgnoreCase(solver_type)){
                FCSolver2way solver = new FCSolver2way();
                sb_solver = solver.solve(domains, constraints);  
            }

        }
    }

    /**
     * @return A string representing the current state.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // TODO: implement different printing modes
        if ("verbose".equalsIgnoreCase(mode)) {
            sb.append("Verbose mode chosen: \n");
            sb.append("Number of variables: ").append(nvar).append("\n");

            sb.append("==== Domains: ==== \n");
            for (int i = 0; i<domains.size(); i++){
                sb.append(domains.get(i)).append("\n");
            }

            sb.append("==== Constraints: ==== \n");
            HashSet<String> printed = new HashSet<>();

            for(Map.Entry<Arc, ArrayList<BinaryTuple>> entry: constraints.entrySet()){
                // sb.append(entry);
                Arc arc = entry.getKey();
                ArrayList<BinaryTuple> tuples = entry.getValue();

                // Evitar las reversas
                String key = arc.getFirstVar() + "," + arc.getSecondVar() ;
                printed.add(key);

                sb.append("[").append(arc.getFirstVar()).append(",").append(arc.getSecondVar()).append("] : [");

                for(int i=0; i<tuples.size(); i++){
                    sb.append(tuples.get(i));
                    if(i < tuples.size() -1) sb.append(", ");
                }

                sb.append(" ] \n");
                
            }

        } else if("compact".equalsIgnoreCase(mode)){
            sb.append("Compact mode chosen: \n");
            sb.append("Number of variables: ").append(nvar).append("\n");
            sb.append("Domains: \n");

        } else {
            sb.append("Unknown mode. Available modes are 'verbose' and 'compact'.\n");
        }

        if(run_solver){
            sb.append(this.sb_solver);
        }
        return sb.toString();
    }
}