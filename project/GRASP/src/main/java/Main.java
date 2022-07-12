import java.util.Set;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;

public class Main {
    public static void main(final String[] args) {
        ImmutableGraph<Integer> graph =
            GraphBuilder.directed()
                .<Integer>immutable()
                .addNode(1)
                .putEdge(2, 3) // also adds nodes 2 and 3 if not already present
                .putEdge(2, 3) // no effect; Graph does not support parallel edges
                .build();

        Set<Integer> successorsOfTwo = graph.successors(2); // returns {3}
        System.out.println(String.format(
            "successor of 2: %s",
            successorsOfTwo.toString()
        ));
    }
}
