package graph;

import java.util.List;

public interface Graph<T> {

    void add(T node);

    void addPair(T first, T second);

    List<T> getAdjacentToNode(T node);

    Graph<T> getTransposed();

    Graph<T> subGraph(List<T> members);

    List<T> getNodes();

    List<Pair<T>> getPairs();
}
