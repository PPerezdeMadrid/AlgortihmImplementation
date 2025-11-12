package fc_solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FCSolver2way {

    private boolean foundSolution = false;

    public StringBuilder solve(ArrayList<HashSet<Integer>> domains,
                               HashMap<Arc, ArrayList<BinaryTuple>> constraints) {

        int nvar = domains.size();
        int[] assignments = new int[nvar];
        for (int i = 0; i < nvar; i++) assignments[i] = -1;

        StringBuilder sb = new StringBuilder();
        sb.append("=========== Starting the 2-Way Forward Checking solver ===========\n");

        fc2(0, assignments, domains, constraints, sb);

        if (!foundSolution)
            sb.append("No solution found :(\n");

        return sb;
    }

    // ------------------------------------------------------------------------

    private void fc2(int depth,
                     int[] A,
                     ArrayList<HashSet<Integer>> D,
                     HashMap<Arc, ArrayList<BinaryTuple>> C,
                     StringBuilder sb) {

        int n = D.size();

        // Caso base: todas las variables asignadas
        if (depth == n) {
            foundSolution = true;
            sb.append("Solution found!\n");
            for (int i = 0; i < n; i++) {
                sb.append("==> Variable ").append(i).append(" : ").append(A[i]).append("\n");
            }
            return;
        }

        // Selecciona la primera variable no asignada
        int var = selectVar(A);
        if (var == -1) return; // no hay más variables por asignar

        // Si el dominio está vacío → inconsistente
        if (D.get(var).isEmpty()) return;

        // Selecciona el primer valor del dominio
        int val = D.get(var).iterator().next();

        // -------- Rama izquierda: var = val --------
        ArrayList<HashSet<Integer>> savedLeft = deepCopyDomains(D);
        A[var] = val;
        D.get(var).clear();
        D.get(var).add(val);

        boolean consistentLeft = propagateFromAssigned(var, A, D, C);
        if (consistentLeft) {
            fc2(depth + 1, A, D, C, sb);
            if (foundSolution) return; // detener búsqueda tras la primera solución
        }

        restoreDomains(D, savedLeft);
        A[var] = -1;

        // -------- Rama derecha: var ≠ val --------
        ArrayList<HashSet<Integer>> savedRight = deepCopyDomains(D);
        D.get(var).remove(val);

        if (!D.get(var).isEmpty()) {
            // En la rama derecha NO propagamos con var (no tiene valor asignado)
            fc2(depth, A, D, C, sb);
            if (foundSolution) return;
        }

        restoreDomains(D, savedRight);
    }

    // ------------------------------------------------------------------------

    private int selectVar(int[] assignments) {
        for (int i = 0; i < assignments.length; i++) {
            if (assignments[i] == -1) return i;
        }
        return -1;
    }

    // Propagación del dominio cuando una variable ya está asignada
    private boolean propagateFromAssigned(int currentVar,
                                          int[] A,
                                          ArrayList<HashSet<Integer>> D,
                                          HashMap<Arc, ArrayList<BinaryTuple>> C) {
        int n = D.size();

        for (int future = 0; future < n; future++) {
            if (future == currentVar) continue;
            // Solo revisar variables no asignadas
            if (A[future] == -1) {
                boolean ok = revise(future, currentVar, A, D, C);
                if (!ok) return false;
            }
        }
        return true;
    }

    // Revisión de un arco (filtro de dominio)
    private boolean revise(int futureVar,
                           int currentVar,
                           int[] A,
                           ArrayList<HashSet<Integer>> D,
                           HashMap<Arc, ArrayList<BinaryTuple>> C) {

        int valCurrent = A[currentVar];
        if (valCurrent == -1) return true; // no hay valor asignado

        Arc arc = new Arc(futureVar, currentVar);
        ArrayList<BinaryTuple> allowed = C.get(arc);

        if (allowed == null) return true;

        HashSet<Integer> newDomain = new HashSet<>();

        for (int valFuture : D.get(futureVar)) {
            boolean supported = false;
            for (BinaryTuple tuple : allowed) {
                if (tuple.firstVal() == valFuture && tuple.secondVal() == valCurrent) {
                    supported = true;
                    break;
                }
            }
            if (supported) newDomain.add(valFuture);
        }

        D.set(futureVar, newDomain);
        return !newDomain.isEmpty();
    }

    // ------------------------------------------------------------------------

    // Copia profunda de dominios
    private ArrayList<HashSet<Integer>> deepCopyDomains(ArrayList<HashSet<Integer>> original) {
        ArrayList<HashSet<Integer>> copy = new ArrayList<>();
        for (HashSet<Integer> domain : original) {
            copy.add(new HashSet<>(domain));
        }
        return copy;
    }

    // Restauración segura de dominios
    private void restoreDomains(ArrayList<HashSet<Integer>> D, ArrayList<HashSet<Integer>> saved) {
        for (int i = 0; i < D.size(); i++) {
            D.set(i, new HashSet<>(saved.get(i)));
        }
    }
}
