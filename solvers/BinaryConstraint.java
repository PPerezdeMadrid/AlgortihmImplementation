package solvers;
import java.util.*;
public final class BinaryConstraint { 
  private int firstVar, secondVar; 
  private ArrayList<BinaryTuple> tuples; 
  private ArrayList<BinaryTuple> reversed_tuples; 
  
  public BinaryConstraint(int fv, int sv, ArrayList<BinaryTuple> t, ArrayList<BinaryTuple> r_t) {
    firstVar = fv; 
    secondVar = sv; 
    tuples = t; 
    reversed_tuples = r_t; 
  } 
  
  public String toString() { 
    StringBuffer result = new StringBuffer(); 
    result.append("c(" + firstVar + ", " + secondVar + ")\n"); 
    for (BinaryTuple bt : tuples) result.append(bt + "\n");
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
    return reversed_tuples; 
  } 
}