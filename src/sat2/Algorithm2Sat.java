package sat2;

import clause.Literal;
import container.CNF;
import graph.Graph;
import graph.OrientedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Algorithm2Sat {

    private CNF clauses;
    private Graph<Literal> graph;
    List<Graph<Literal>> stronglyConnectedComponents;
    private List<Literal> visited = new ArrayList<>();

    public Algorithm2Sat(CNF clauses) {
        this.clauses = clauses;
    }

    public boolean isSatisfiable() {
        return run();
    }

    private boolean run() {
        graph = getGraph();

        stronglyConnectedComponents = buildSCCs(graph);

        return checkSatisfiable(stronglyConnectedComponents);
    }

    public List<Graph<Literal>> getSCCs() {
        return stronglyConnectedComponents == null ? buildSCCs(getGraph()) : stronglyConnectedComponents;
    }

    public Graph<Literal> getGraph() {
        return graph == null ? buildGraph() : graph;
    }

    private Graph<Literal> buildGraph() {
        Graph<Literal> graph = new OrientedGraph<>();

        clauses.getClauses().forEach(clause -> {
            Literal notFirst = clause.getFirst().getNegated();
            Literal notSecond = clause.getSecond().getNegated();

            addPairIfNotExists(graph, notFirst, clause.getSecond());
            addPairIfNotExists(graph, notSecond, clause.getFirst());

            addNodeIfNotExists(graph, notFirst);
            addNodeIfNotExists(graph, notSecond);
            addNodeIfNotExists(graph, clause.getSecond());
            addNodeIfNotExists(graph, clause.getFirst());
        });

        return graph;
    }

    private void addNodeIfNotExists(Graph<Literal> graph, Literal node) {
        if (graph.getNodes().stream().noneMatch(it -> it.isEqual(node))) {
            graph.add(node);
        }
    }

    private void addPairIfNotExists(Graph<Literal> graph, Literal first, Literal second) {
        if (graph.getPairs().stream().noneMatch(pair -> pair.getFirst().isEqual(first) && pair.getSecond().isEqual(second))) {
            graph.addPair(first, second);
        }
    }

    private List<Graph<Literal>> buildSCCs(Graph<Literal> graph) {
        List<List<Literal>> sccsMembers = new ArrayList<>();
        Stack<Literal> stack = new Stack<>();

        visited = new ArrayList<>();

        graph.getNodes().forEach(node -> {
            if (!visited.contains(node)) {
                fillOrder(graph, node, stack);
            }
        });

        Graph<Literal> transposed = graph.getTransposed();
        visited = new ArrayList<>();

        while (!stack.empty()) {
            Literal literal = stack.pop();
            List<Literal> collected = new ArrayList<>();

            if (!visited.contains(literal)) {
                collectDepthFirstSearch(transposed, literal, collected);
            }

            if (!collected.isEmpty())
                sccsMembers.add(collected);
        }

        List<Graph<Literal>> sccs = new ArrayList<>();
        sccsMembers.forEach(members -> sccs.add(graph.subGraph(members)));

        return sccs;
    }

    private void fillOrder(Graph<Literal> graph, Literal node, Stack<Literal> stack) {
        visited.add(node);

        graph.getAdjacentToNode(node).forEach(adjacent -> {
            if (!visited.contains(adjacent)) {
                fillOrder(graph, adjacent, stack);
            }
        });

        stack.push(node);
    }

    private void collectDepthFirstSearch(Graph<Literal> transposed, Literal node, List<Literal> collected) {
        visited.add(node);
        collected.add(node);

        transposed.getAdjacentToNode(node).forEach(adjacent -> {
            if (!visited.contains(adjacent)) {
                collectDepthFirstSearch(transposed, adjacent, collected);
            }
        });

    }

    private boolean checkSatisfiable(List<Graph<Literal>> stronglyConnectedComponents) {
        for (Graph<Literal> graph : stronglyConnectedComponents) {
            List<Literal> nodes = graph.getNodes();
            for (Literal node : nodes) {
                for (Literal compared : nodes) {
                    if (node.isNegated(compared))
                        return false;
                    }
                }
            }
        return true;
    }

}
