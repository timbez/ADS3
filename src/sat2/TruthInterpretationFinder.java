package sat2;

import clause.Atom;
import clause.Clause;
import clause.Literal;
import clause.OrClause;
import container.CNF;
import graph.Graph;
import graph.Pair;

import java.util.*;

public class TruthInterpretationFinder implements InterpretationFinder {

    private CNF clauses;

    private CNF clausesBackup;

    private Graph<Literal> implicationGraph;

    private Map<Atom, Boolean> interpretation = new HashMap<>();

    public TruthInterpretationFinder(Graph<Literal> implicationGraph, CNF clauses) {
        this.implicationGraph = implicationGraph;
        this.clauses = clauses;
    }

    public Interpretation find() {
        Set<Atom> atoms =  collectAtoms();

        while (!clauses.getClauses().isEmpty()) {
            resolveKnownAtoms();

            if (!clauses.getClauses().isEmpty()) {
                Atom next = clauses.getClauses().get(0).getFirst().getAtom();
                assignAndTestAtom(next);
            }
        }

        // unassigned values might be found out through implications
        if (interpretation.size() != atoms.size())
            findMissingByImplicationGraph(implicationGraph, atoms.size());

        // by this moment there still can be unassigned values, set them to false
        atoms.forEach(atom -> {
            if (!interpretation.containsKey(atom)) {
                interpretation.put(atom, false);
            }
        });

        return new Interpretation(interpretation);
    }

    private Set<Atom> collectAtoms() {
        Set<Atom> atoms = new HashSet<>();

        clauses.getClauses().forEach(clause -> {
            atoms.add(clause.getFirst().getAtom());
            atoms.add(clause.getSecond().getAtom());
        });

        return atoms;
    }

    private void resolveKnownAtoms() {
        List<Clause> toRemove = new ArrayList<>();
        Set<Atom> assignedAtoms = new HashSet<>();

        if (clauses.stream().noneMatch(this::isClauseRedundant))
            return;

        clauses.forEach(clause -> {
            if (isClauseRedundant(clause)) {
                interpretation.put(clause.getFirst().getAtom(), !clause.getFirst().isNegated());
                assignedAtoms.add(clause.getFirst().getAtom());
                toRemove.add(clause);
            }
        });
        clauses.getClauses().removeAll(toRemove);
        toRemove.clear();

        clauses.forEach(clause -> {
            assignedAtoms.forEach(atom -> {
                if (!isAtomFirstInClause(atom, clause) && !isAtomSecondInClause(atom, clause))
                    return;

                boolean shouldDelete = resolveClauseByAtom(atom, clause);
                if (shouldDelete)
                    toRemove.add(clause);

            });
        });

        clauses.getClauses().removeAll(toRemove);

        resolveKnownAtoms();
    }

    private void assignAndTestAtom(Atom atom) {
        backupClauses();

        List<Clause> toRemove = new ArrayList<>();

        // atom is assumed to be False
        interpretation.put(atom, false);

        clauses.forEach(clause -> {
            if (resolveClauseByAtom(atom, clause))
                toRemove.add(clause);
        });
        clauses.getClauses().removeAll(toRemove);

        // check if assumption was correct - CNF is still satisfiable
        Algorithm2Sat algorithm2Sat = new Algorithm2Sat(clauses);
        boolean isSatisfiable = algorithm2Sat.isSatisfiable();

        if (!isSatisfiable) {
            // assumption that atom could be False was incorrect, rollback and assign to True
            interpretation.put(atom, true);
            rollback();
            toRemove.clear();

            clauses.forEach(clause -> {
                if (resolveClauseByAtom(atom, clause))
                    toRemove.add(clause);
            });

            clauses.getClauses().removeAll(toRemove);
        }

    }

    /** side-effect: alters clause !!! */
    private boolean resolveClauseByAtom(Atom atom, Clause clause) {
        boolean atomValue = interpretation.get(atom);
        boolean shouldDeleteClause = false;

        if (isAtomFirstInClause(atom, clause)) {
            Literal first = clause.getFirst();

            if (first.evaluate(atomValue)) {
                shouldDeleteClause = true;

            } else {
                clause.setFirst(clause.getSecond());
            }

        } else if (isAtomSecondInClause(atom, clause)) {
            Literal second = clause.getSecond();

            if (second.evaluate(atomValue)) {
                shouldDeleteClause = true;

            } else {
                clause.setSecond(clause.getFirst());
            }

        }

        return shouldDeleteClause;
    }

    private void findMissingByImplicationGraph(Graph<Literal> implicationGraph, Integer maxDepth) {
        findMissingByImplicationGraph(implicationGraph, 0, maxDepth);
    }

    private void findMissingByImplicationGraph(Graph<Literal> implicationGraph, Integer currentDepth, Integer maxDepth) {
        if (currentDepth.equals(maxDepth))
            return;

        currentDepth++;

        for (Pair<Literal> pair : implicationGraph.getPairs()) {
            Literal first = pair.getFirst();
            Atom firstAtom = first.getAtom();

            Literal second = pair.getSecond();
            Atom secondAtom = second.getAtom();

            if (interpretation.containsKey(firstAtom) && !interpretation.containsKey(secondAtom)) {
                if (first.evaluate(interpretation.get(firstAtom))) {
                    interpretation.put(secondAtom, !second.isNegated());
                    findMissingByImplicationGraph(implicationGraph, currentDepth, maxDepth);
                }
            }
        }
    }

    private void backupClauses() {
        clausesBackup = clauses.copy();
    }

    private void rollback() {
        clauses = clausesBackup;
    }

    private boolean isClauseRedundant(Clause clause) {
        return clause.getFirst().equals(clause.getSecond());
    }

    private boolean isAtomFirstInClause(Atom atom, Clause clause) {
        return clause.getFirst().getAtom().equals(atom);
    }

    private boolean isAtomSecondInClause(Atom atom, Clause clause) {
        return clause.getSecond().getAtom().equals(atom);
    }

}
