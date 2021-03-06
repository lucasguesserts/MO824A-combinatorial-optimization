package graph;

import java.util.HashSet;
import java.util.Set;

import com.google.common.graph.Graph;

public interface GraphTools {

    public static Set<Integer> findRootNodes(final Graph<Integer> graph) {
        final Set<Integer> rootNodes = new HashSet<>(graph
            .nodes()
            .stream()
            .map(node -> new ElementValuePair<Integer>(node, graph.inDegree(node)))
            .filter(pair -> pair.value.equals(0)) // no predecessor
            .map(pair -> pair.element) // get node
            .toList()
        );
        return rootNodes;
    }

    public static Set<Integer> findLeafNodes(final Graph<Integer> graph) {
        final Set<Integer> leafNodes = new HashSet<>(graph
            .nodes()
            .stream()
            .map(node -> new ElementValuePair<Integer>(node, graph.outDegree(node)))
            .filter(pair -> pair.value.equals(0)) // no successor
            .map(pair -> pair.element) // get node
            .toList()
        );
        return leafNodes;
    }

}
