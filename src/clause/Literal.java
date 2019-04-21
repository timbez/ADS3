package clause;

import java.util.Objects;

public class Literal {

    private Atom atom;

    private boolean isNegated;

    public Literal(Atom atom, boolean isNegated) {
        this.atom = atom;
        this.isNegated = isNegated;
    }

    public Atom getAtom() {
        return atom;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public boolean evaluate(boolean atomValue) {
        return isNegated != atomValue;
    }

    public Literal getNegated() {
        return new Literal(atom, !isNegated);
    }

    /* TODO redundant? */
    public boolean isEqual(Literal other) {
        return this.atom.equals(other.atom) && this.isNegated == other.isNegated;
    }

    public boolean isNegated(Literal other) {
        return this.atom.equals(other.atom) && this.isNegated != other.isNegated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return isNegated == literal.isNegated &&
                Objects.equals(atom, literal.atom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atom, isNegated);
    }

    @Override
    public String toString() {
        return !isNegated ? atom.getLabel() : "-" + atom.getLabel();
    }
}
