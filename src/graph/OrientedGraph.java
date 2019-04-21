package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrientedGraph<T> implements Graph<T> {

    private List<T> nodes = new ArrayList<>();
    private List<Pair<T>> pairs = new ArrayList<>();

    public OrientedGraph() {
    }

    public OrientedGraph(List<T> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void add(T node) {
        nodes.add(node);
    }

    @Override
    public void addPair(T first, T second) {
        pairs.add(new Pair<>(first, second));
    }

    @Override
    public List<T> getAdjacentToNode(T node) {
        return pairs.stream()
                .filter(pair -> pair.getFirst().equals(node))
                .map(Pair::getSecond)
                .collect(Collectors.toList());
    }

    @Override
    public OrientedGraph<T> getTransposed() {
        OrientedGraph<T> transposed = new OrientedGraph<>();

        List<Pair<T>> transPairs = pairs.stream()
                .map(pair -> new Pair<>(pair.getSecond(), pair.getFirst()))
                .collect(Collectors.toList());

        transposed.setNodes(nodes);
        transposed.setPairs(transPairs);

        return transposed;
    }

    @Override
    public OrientedGraph<T> subGraph(List<T> members) {
        OrientedGraph<T> subGraph = new OrientedGraph<>(members);

        List<Pair<T>> pairs = this.pairs.stream()
                .filter(pair ->
                        members.stream().anyMatch(mem -> pair.getFirst().equals(mem)) && members.stream().anyMatch(mem -> pair.getSecond().equals(mem)) )
                .map(pair -> new Pair<>(pair.getFirst(), pair.getSecond()))
                .collect(Collectors.toList());
        subGraph.setPairs(pairs);

        return subGraph;
    }

    @Override
    public List<T> getNodes() {
        return nodes;
    }

    public void setNodes(List<T> nodes) {
        this.nodes = nodes;
    }

    @Override
    public List<Pair<T>> getPairs() {
        return pairs;
    }

    public void setPairs(List<Pair<T>> pairs) {
        this.pairs = pairs;
    }

}
