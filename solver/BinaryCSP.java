package solver;

import java.util.ArrayList;

/**
 * Represents a Binary Constraint Satisfaction Problem (CSP).
 * It contains:
 *  - the number of variables,
 *  - the domain bounds for each variable,
 *  - and a list of all binary constraints.
 */
public class BinaryCSP {

    private int nvar; // number of variables
    private int[][] domainBounds; // domains of each variable [i][0]=lower bound, [i][1]=upper bound
    private ArrayList<BinaryConstraint> constraints; // list of constraints

    /**
     * Constructor for BinaryCSP.
     *
     * @param domainBounds the lower and upper bounds for each variable
     * @param constraints  the list of binary constraints
     */
    public BinaryCSP(int[][] domainBounds, ArrayList<BinaryConstraint> constraints) {
        this.domainBounds = domainBounds;
        this.constraints = constraints;
        this.nvar = domainBounds.length;
    }

    /**
     * @return the number of variables in the CSP.
     */
    public int getNoVariables() {
        return nvar;
    }

    /**
     * @return the domain bounds array.
     */
    public int[][] getDomainBounds() {
        return domainBounds;
    }

    /**
     * @return the list of constraints.
     */
    public ArrayList<BinaryConstraint> getConstraints() {
        return constraints;
    }

    /**
     * @return a readable string version of the CSP.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-- BinaryCSP: --\n");
        sb.append("Number of variables: ").append(nvar).append("\n");
        sb.append("Domains:\n");
        for (int i = 0; i < domainBounds.length; i++) {
            sb.append("  Variable ").append(i).append(": [")
              .append(domainBounds[i][0]).append(", ")
              .append(domainBounds[i][1]).append("]\n");
        }
        sb.append("Constraints: ").append(constraints.size()).append("\n");
        return sb.toString();
    }
}
