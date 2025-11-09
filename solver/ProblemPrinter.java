package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

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
    protected ProblemPrinter(BinaryCSP csp, String mode) {
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
    }

    /**
     * @return A string representing the current state.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // TODO: implement different printing modes
        return sb.toString();
    }
}