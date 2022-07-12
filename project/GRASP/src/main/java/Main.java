import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

public class Main {
    public static void main(final String[] args) throws IOException {
        // System.out.println(System.getProperty("user.dir"));
        final var obj = Auxiliary.readJson("../instances/instance_1.json");
        final MutableGraph<Integer> graph = GraphBuilder.directed().build();
        Auxiliary.addNodes(graph, obj.getInt("number_of_nodes"));
        Auxiliary.addEdges(graph, obj.getJSONArray("edges"));
        for (final var n : graph.nodes()) {
            System.out.println(String.format("mutable node: %d", n));
        }
        for (final var e : graph.edges()) {
            System.out.println(String.format("mutable edge: %d %d", e.nodeU(), e.nodeV()));
        }
    }
}

class Auxiliary {
    static JSONObject readJson(final String fileName) throws IOException {
        final var filePath = Path.of(fileName);
        final var fileContent = Files.readString(filePath);
        final var obj = new JSONObject(fileContent);
        return obj;
    }

    static void addNodes(final MutableGraph<Integer> graph, final Integer numberOfNodes) {
        for(int i = 0; i < numberOfNodes; ++i) {
            graph.addNode(i);
        }
    }

    static void addEdges(final MutableGraph<Integer> graph, final JSONArray edges) {
        for(int e = 0; e < edges.length(); ++e) {
            final JSONArray edgeArray = edges.getJSONArray(e);
            graph.putEdge(
                edgeArray.getInt(0),
                edgeArray.getInt(1)
            );
        }
    }
}
