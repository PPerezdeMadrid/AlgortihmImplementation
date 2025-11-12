package solvers;
import java.util.*;
public final class BinaryCSP {
    private int[][] domainBounds;
    private ArrayList<BinaryConstraint> constraints;
    
    public BinaryCSP(int[][] db, ArrayList<BinaryConstraint> c) { 
        domainBounds = db; constraints = c; 
    }
    
    public int getNoVariables() {
        return domainBounds.length; 
    }
    
    public int[][] getDomainBounds() {
        return domainBounds; 
    }
    
    public int getLB(int varIndex) {
        return domainBounds[varIndex][0]; 
    } 
    
    public int getUB(int varIndex) { 
        return domainBounds[varIndex][1]; 
    }
    
    public ArrayList<BinaryConstraint> getConstraints() { 
        return constraints; 
    }
}