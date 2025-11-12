package solvers;

import java.util.Objects;

public class Arc {
    private int var1;
    private int var2;

    public Arc(int var1, int var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    public int getFirstVar() {
        return var1;
    }

    public int getSecondVar() {
        return var2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(var1, var2);
    }

    @Override
    public String toString() {
        return var1 + " " + var2;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Arc) {
            Arc comp = (Arc) o;
            return comp.var1 == var1 && comp.var2 == var2;
        }
        return false;
    }
}
