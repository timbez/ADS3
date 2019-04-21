package container;

import clause.Clause;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CNF implements ClauseContainer {

    private List<Clause> clauses;

    public CNF(List<Clause> clauses) {
        this.clauses = clauses;
    }

    @Override
    public List<Clause> getClauses() {
        return clauses;
    }

    @Override
    public void forEach(Consumer<Clause> consumer) {
        clauses.forEach(consumer);
    }

    @Override
    public Stream<Clause> stream() {
        return clauses.stream();
    }

    @Override
    public CNF copy() {
        List<Clause> copied = new ArrayList<>();

        clauses.forEach(clause -> {

            Clause cloned = clause.clone();
            if (cloned == null)
                throw new IllegalStateException("Clause cloned as null: This should not happen.");

            copied.add(cloned);
        });

        return new CNF(copied);
    }

    @Override
    public String toString() {
        return clauses.stream().map(Clause::toString).collect(Collectors.joining(" ∧ "));
    }
}
