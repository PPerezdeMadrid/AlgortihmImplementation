package fc_solver;
/**
 * Assumes tuple values are integers
 */
public final class BinaryTuple {
  private int val1, val2;

  public BinaryTuple(int v1, int v2) {
    val1 = v1;
    val2 = v2;
  }

  public String toString() {
    return "<" + val1 + ", " + val2 + ">";
  }

  public int firstVal() {
    return val1;
  }

  public int secondVal() {
    return val2;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Arc) {
      BinaryTuple comp = (BinaryTuple) o;
      return comp.firstVal() == val1 && comp.secondVal() == val2;
    }
    return false;
  }
}
