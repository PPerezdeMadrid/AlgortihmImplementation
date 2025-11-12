package fc_solver;

import fc_solver.BinaryTuple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class FCSolver{

    private boolean foundSolution;

    public StringBuilder solve(ArrayList<HashSet<Integer>> domains, HashMap<Arc,ArrayList<BinaryTuple>> constraints){
        int nvar = domains.size();
        int[] assignments = new int[nvar];

        StringBuilder sb = new StringBuilder();

        forwardChecking(0, assignments, domains, constraints, sb);

        if(!foundSolution) sb.append("No solution found :(! \n");
        return sb;
    }


    private void forwardChecking(int depth, int[] assignments, ArrayList<HashSet<Integer>> domains, HashMap<Arc,ArrayList<BinaryTuple>> constraints, StringBuilder sb){
        // TODO
        int n = domains.size(); 
        if(depth == n){
            foundSolution = true;
            sb.append("Solution found!\n");
            for(int i = 0; i<n;i++){
                sb.append("==> Variable ").append(i).append(" : ").append(assignments[i]).append("\n");
            }
            return;
        }

        for(int d: domains.get(depth)){
            assignments[depth]=d;
            boolean consistent = true;

            ArrayList<HashSet<Integer>> savedDomains = deepCopyDomains(domains);

            for(int future=depth+1; future<n && consistent; future++){
                // Falta el savedDomains
                consistent = revise(future, depth, assignments, domains, constraints);
            }
            if(consistent) forwardChecking(depth+1, assignments, domains, constraints, sb);
           
           /**
            * NOTA
            * El cosistent despues del for de future, no dentro!
            */
           
            // Undo pruning
            // Falta el savedDomains
            for(int i = 0; i<domains.size(); i++){
                domains.set(i, new HashSet<>(savedDomains.get(i)));
            }
            assignments[depth] = -1;
            
        }
    }

    private boolean revise(int futureVar, int currentVar, int[] assignments,ArrayList<HashSet<Integer>> domains, HashMap<Arc,ArrayList<BinaryTuple>> constraints ){
        // TODO
        int valCurrent = assignments[currentVar];
        Arc arc = new Arc(futureVar, currentVar);
        ArrayList<BinaryTuple> allowed = constraints.get(arc);

        if(allowed==null) return true;

        HashSet<Integer> newDomains = new HashSet<>(); // NO ArrayList!!!!

        for (int valFuture: domains.get(futureVar)){
            boolean supported = false;
            for(BinaryTuple tuple: allowed){
                if(tuple.firstVal()==valFuture && tuple.secondVal()==valCurrent){
                    supported = true;
                    break; // QUE SE TE OLVIDA SIEMPRE
                }
            }
            if(supported) newDomains.add(valFuture);
        }

        domains.set(futureVar, newDomains); // AÑDIRLO AL DOMINIO (se te olvida)
        return !newDomains.isEmpty();
    }

    private ArrayList<HashSet<Integer>> deepCopyDomains( ArrayList<HashSet<Integer>> originalDomains){
        //TODO
        int n = originalDomains.size();
        ArrayList<HashSet<Integer>> copy = new ArrayList<>();

        for(HashSet<Integer> domain: originalDomains){
            copy.add(new HashSet<>(domain));
        }

        return copy;

    }
}