package container;

import clause.Clause;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ClauseContainer {

    List<Clause> getClauses();

    void forEach(Consumer<Clause> consumer);

    Stream<Clause> stream();

    CNF copy();
}
