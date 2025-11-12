package solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BinaryCSPSolver {
    private boolean consistent;
    private double bestObjective;       // Alpha (mejor valor encontrado)
    private int[] bestAssignment;
    private boolean foundSolution = false;


    public StringBuilder solve(BinaryCSP csp,
                               ArrayList<HashSet<Integer>> domains,
                               HashMap<Arc, ArrayList<BinaryTuple>> constraints) {
        StringBuilder sb = new StringBuilder();

        sb.append("=========== Starting the solver... =========== \n");
        int nvar = domains.size();
        int[] assignment = new int[nvar]; // asignaciones (inicialmente 0s)

        backtrack(0, assignment, domains, constraints, sb);

        sb.append("=========== Backtracking finished ===========");
        return sb;
    }

    public StringBuilder solve_bnb(BinaryCSP csp,
                                   ArrayList<HashSet<Integer>> domains,
                                   HashMap<Arc, ArrayList<BinaryTuple>> constraints) {
        StringBuilder sb = new StringBuilder();

        sb.append("=========== Starting the solver BnB... =========== \n");
        int nvar = domains.size();
        int[] assignment = new int[nvar];
        bestObjective = Double.POSITIVE_INFINITY;  // α = +∞
        bestAssignment = null;
        foundSolution = false;

        branchAndBound(0, assignment, domains, constraints, sb);

        sb.append("========= Branch and Bound finished =========");
        return sb;
    }

    public StringBuilder solve_FC_dway(BinaryCSP csp,
                                       ArrayList<HashSet<Integer>> domains,
                                       HashMap<Arc, ArrayList<BinaryTuple>> constraints) {
        StringBuilder sb = new StringBuilder();

        int nvar = domains.size();
        int[] assignment = new int[nvar];
        foundSolution = false;

        forwardChecking(0, assignment, domains, constraints, sb);
        if (!foundSolution) {
            sb.append("No solution found!");
        }

        sb.append("========= Forward Checking finished =========");
        return sb;
    }



    private void backtrack(int depth,
                           int[] assignment,
                           ArrayList<HashSet<Integer>> domains,
                           HashMap<Arc, ArrayList<BinaryTuple>> constraints,
                           StringBuilder sb) {
        int n = domains.size();

        // Caso base
        if (n == depth) {
            sb.append("Solution Found! \n");
            consistent = true;
            for (int i = 0; i < n; i++) {
                sb.append("==> Variable ").append(i).append(" = ").append(assignment[i]).append("\n");
            }
            return;
        }

        // Intentar valores del dominio de la variable 'depth'
        for (int d : domains.get(depth)) {
            assignment[depth] = d;
            boolean consistent = true;

            // Revisar consistencia con variables anteriores
            for (int past = 0; past < depth && consistent; past++) {
                int valPast = assignment[past];
                if (!isConsistent(past, valPast, depth, d, constraints)) {
                    consistent = false;
                }
            }

            if (consistent) {
                backtrack(depth + 1, assignment, domains, constraints, sb);
            }

            assignment[depth] = -1;
        }
    }

    // --------- HELPERS: BRANCH & BOUND ---------

    private void branchAndBound(int depth,
                                int[] assignment,
                                ArrayList<HashSet<Integer>> domains,
                                HashMap<Arc, ArrayList<BinaryTuple>> constraints,
                                StringBuilder sb) {
        int n = domains.size();

        // Caso base
        if (n == depth) {
            double objVal = calculateObjective(assignment);

            if (objVal < bestObjective) {
                bestObjective = objVal;
                bestAssignment = assignment.clone();
                foundSolution = true;

                sb.append("New best solution: (").append(objVal).append(" ): \n");
                for (int i = 0; i < n; i++) {
                    sb.append("==> Variable ").append(i).append(" = ").append(assignment[i]).append("\n");
                }
            }
            return;
        }

        // Exploración
        for (int d : domains.get(depth)) {
            assignment[depth] = d;

            // Bound (estimación sencilla basada en suma parcial)
            double lowerBound = calculateObjective(assignment);
            if (lowerBound >= bestObjective) {
                assignment[depth] = -1;
                continue;
            }

            // Consistencia con el pasado
            boolean consistent = true;
            for (int past = 0; past < depth && consistent; past++) {
                int valPast = assignment[past];
                if (!isConsistent(past, valPast, depth, d, constraints)) {
                    consistent = false;
                }
            }

            if (consistent) {
                branchAndBound(depth + 1, assignment, domains, constraints, sb);
            }

            assignment[depth] = -1;
        }
    }

    private double calculateObjective(int[] assignment) {
        // Ejemplo: suma de valores asignados (minimizar). Ignora posiciones -1.
        double sum = 0;
        for (int val : assignment) {
            if (val != -1) {
                sum += val;
            }
        }
        return sum;
    }

    // --------- HELPERS: FORWARD CHECKING (d-way) ---------

    private void forwardChecking(int depth,
                                 int[] assignment,
                                 ArrayList<HashSet<Integer>> domains,
                                 HashMap<Arc, ArrayList<BinaryTuple>> constraints,
                                 StringBuilder sb) {
        int n = domains.size();

        // Caso base
        if (n == depth) {
            foundSolution = true;
            sb.append("Solution found! \n");
            for (int i = 0; i < n; i++) {
                sb.append("==> Variable ").append(i).append(": ").append(assignment[i]).append("\n");
            }
            return;
        }

        // Probar todos los valores del dominio de la variable actual
        for (int d : domains.get(depth)) {
            assignment[depth] = d;
            boolean consistent = true;

            // Copia temporal de dominios para undo pruning
            ArrayList<HashSet<Integer>> savedDomains = deepCopyDomains(domains);

            // Revisar futuras variables (podar dominios)
            for (int future = depth + 1; future < n && consistent; future++) {
                consistent = revise(future, depth, assignment, domains, constraints);
            }

            if (consistent) {
                forwardChecking(depth + 1, assignment, domains, constraints, sb);
            }

            // Undo pruning
            for (int i = 0; i < n; i++) {
                domains.set(i, new HashSet<>(savedDomains.get(i)));
            }
            assignment[depth] = -1;
        }
    }

    private boolean revise(int futureVar,
                           int currentVar,
                           int[] assignment,
                           ArrayList<HashSet<Integer>> domains,
                           HashMap<Arc, ArrayList<BinaryTuple>> constraints) {
        int valCurrent = assignment[currentVar];
        Arc arc = new Arc(futureVar, currentVar);
        ArrayList<BinaryTuple> allowed = constraints.get(arc);

        // No hay restricción entre ambas variables
        if (allowed == null) {
            return true;
        }

        // Filtrar dominio de 'futureVar' con base en el valor actual
        HashSet<Integer> newDomain = new HashSet<>();
        for (int valFuture : domains.get(futureVar)) {
            boolean supported = false;
            for (BinaryTuple tuple : allowed) {
                if (tuple.firstVal() == valFuture && tuple.secondVal() == valCurrent) {
                    supported = true;
                    break;
                }
            }
            if (supported) newDomain.add(valFuture);
        }

        // Actualiza el dominio podado
        domains.set(futureVar, newDomain);

        // Si queda vacío, inconsistente
        return !newDomain.isEmpty();
    }

    // --------- UTILIDADES ---------

    private ArrayList<HashSet<Integer>> deepCopyDomains(ArrayList<HashSet<Integer>> originalDomains) {
        ArrayList<HashSet<Integer>> copy = new ArrayList<>();
        for (HashSet<Integer> domain : originalDomains) {
            copy.add(new HashSet<>(domain));
        }
        return copy;
    }

    private boolean isConsistent(int var1, int val1, int var2, int val2,
                                 HashMap<Arc, ArrayList<BinaryTuple>> constraints) {
        Arc arc = new Arc(var1, var2);
        ArrayList<BinaryTuple> allowed = constraints.get(arc);

        // Si no hay restricción entre var1 y var2, es consistente
        if (allowed == null) return true;

        // Comprueba si el par (val1, val2) está permitido
        for (BinaryTuple tuple : allowed) {
            if (tuple.firstVal() == val1 && tuple.secondVal() == val2) {
                return true;
            }
        }
        return false;
    }
}
