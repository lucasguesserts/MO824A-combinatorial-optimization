package graph;

import java.util.HashSet;
import java.util.Set;

import com.google.common.graph.Graph;

public interface GraphTools {

    public static Set<Integer> findRootNodes(final Graph<Integer> graph) {
        final Set<Integer> rootNodes = new HashSet<>(graph
            .nodes()
            .stream()
            .map(node -> new NodeValuePair<Integer>(node, graph.inDegree(node)))
            .filter(pair -> pair.value.equals(0)) // no predecessor
            .map(pair -> pair.node) // get node
            .toList()
        );
        return rootNodes;
    }

}
