package parser;

import clause.Atom;
import clause.Clause;
import clause.Literal;
import clause.OrClause;
import container.CNF;
import container.ClauseContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClauseParser implements Parser<List<String>, ClauseContainer> {

    public static final String ARGUMENT_SEPARATOR = " ";
    public static final String NEGATION = "-";

    private List<Atom> atoms = new ArrayList<>();

    @Override
    public CNF parse(List<String> parsed) {
        List<Clause> clauses = parsed.stream()
                .map(this::buildClause)
                .collect(Collectors.toList());

        return new CNF(clauses);
    }

    private Clause buildClause(String stringClause) {
        String[] args = stringClause.split(ARGUMENT_SEPARATOR);

        Literal first = makeLiteral(args[0]);

        if (args.length <= 2) {
            return new OrClause(first, first);

        } else {
            Literal second = makeLiteral(args[1]);
            return new OrClause(first, second);
        }
    }

    private Literal makeLiteral(String arg) {
        String clean = arg.replace(NEGATION, "");
        Atom atom = findOrCreateAtom(clean);
        return new Literal(atom, arg.contains(NEGATION));
    }

    private Atom findOrCreateAtom(String label) {
        return atoms.stream()
                .filter(atom -> atom.getLabel().equals(label))
                .findFirst()
                .orElseGet(() -> createAtom(label));
    }

    private Atom createAtom(String label) {
        Atom atom = new Atom(label);
        atoms.add(atom);

        return atom;
    }
}
