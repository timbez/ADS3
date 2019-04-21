package clause;

import java.io.Serializable;

public class OrClause implements Clause {

    private Literal first;
    private Literal second;

    public OrClause() {
    }

    public OrClause(Literal first, Literal second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Literal getFirst() {
        return first;
    }

    @Override
    public void setFirst(Literal first) {
        this.first = first;
    }

    @Override
    public Literal getSecond() {
        return second;
    }

    @Override
    public void setSecond(Literal second) {
        this.second = second;
    }

    @Override
    public Clause clone() {
        return new OrClause(first, second);
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " ∨ " + second.toString() + ")";
    }
}
