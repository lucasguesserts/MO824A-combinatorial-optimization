package input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph.Builder;

import problem.Problem;
import solution.Weight;

public class ProblemData implements Problem {

    private final String fileName;
    private final JSONObject input;
    private final Integer numberOfNodes;
    private final Integer weightSize;
    private final Graph<Integer> graph;
    private final Weight capacity;
    private final Map<Integer, Weight> weightMap;

    public ProblemData(final String fileName) throws IOException, JSONException, InvalidInputException {
        this.fileName = fileName;
        this.input = readJson(this.fileName);
        this.numberOfNodes = this.input.getInt("number_of_nodes");
        this.weightSize = this.input.getJSONArray("capacity").length();
        this.graph = readGraph(this.numberOfNodes, this.input.getJSONArray("edges"));
        this.capacity = readCapacity(this.input.getJSONArray("capacity"));
        this.weightMap = readWeights(this.input.getJSONArray("weights"));
        this.checkAll();
    }

    public Integer getNumberOfNodes() {
        return this.numberOfNodes;
    }

    public Integer getWeightSize() {
        return this.weightSize;
    }

    public Graph<Integer> getGraph() {
        return this.graph;
    }

    public Weight getCapacity() {
        return this.capacity;
    }

    public Map<Integer, Weight> getWeightMap() {
        return this.weightMap;
    }

    private static JSONObject readJson(final String fileName) throws IOException {
        final var filePath = Path.of(fileName);
        final var fileContent = Files.readString(filePath);
        final var obj = new JSONObject(fileContent);
        return obj;
    }

    private Graph<Integer> readGraph(final Integer numberOfNodes, final JSONArray edges) {
        final var graphBuilder = GraphBuilder
            .directed()
            .allowsSelfLoops(false)
            .<Integer>immutable();
        addNodes(graphBuilder, numberOfNodes);
        addEdges(graphBuilder, edges); // If either or both endpoints are not already present in this graph, this method will silently add each missing endpoint to the graph.
        final var graph = graphBuilder.build();
        return graph;

    }

    private static void addNodes(final Builder<Integer> graph, final Integer numberOfNodes) {
        for(int i = 0; i < numberOfNodes; ++i) {
            graph.addNode(i);
        }
    }

    private static void addEdges(final Builder<Integer> graph, final JSONArray edges) {
        for(int e = 0; e < edges.length(); ++e) {
            final JSONArray edgeArray = edges.getJSONArray(e);
            graph.putEdge(
                edgeArray.getInt(0),
                edgeArray.getInt(1)
            );
        }
    }

    private static Weight readCapacity(final JSONArray capacityArray) {
        final var capacityList = toIntegerList(capacityArray);
        return new Weight(capacityList);
    }

    private static Map<Integer, Weight> readWeights(final JSONArray weight2DArray) {
        final var weightMap = new HashMap<Integer, Weight>(weight2DArray.length());
        for (int i = 0; i < weight2DArray.length(); ++i) {
            final var weightArray = weight2DArray.getJSONArray(i);
            final var weightList = toIntegerList(weightArray);
            final var weight = new Weight(weightList);
            weightMap.put(i, weight);
        }
        return weightMap;
    }

    private static List<Integer> toIntegerList(final JSONArray array) {
        List<Integer> list = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); ++i) {
            list.add(array.getInt(i));
        }
        return list;
    }

    private void checkAll() throws InvalidInputException {
        this.checkGraphProperties();
        this.checkGraphNumberOfNodes();
        this.checkCapacitySize();
        this.checkWeightMapSize();
    }

    private void checkGraphProperties() throws InvalidInputException {
        if(!this.graph.isDirected()) {
            throw new InvalidInputException(this.fileName, "graph is not directed");
        }
    }

    private void checkGraphNumberOfNodes() throws InvalidInputException {
        final var numberOfNodesOfGraph = this.graph.nodes().size();
        if(!this.numberOfNodes.equals(numberOfNodesOfGraph)) {
            throw new InvalidInputException(this.fileName, String.format(
                "number of nodes of graph %d does not match the number of nodes %d",
                numberOfNodesOfGraph,
                this.numberOfNodes
            ));
        }
    }

    private void checkCapacitySize() throws InvalidInputException {
        if (!this.weightSize.equals(this.capacity.size())) {
            throw new InvalidInputException(this.fileName, String.format(
                "'capacity' size %d does not match the number of nodes %d",
                this.capacity.size(),
                this.numberOfNodes
            ));
        }
    }

    private void checkWeightMapSize() throws InvalidInputException {
        if (!this.numberOfNodes.equals(this.weightMap.size())) {
            throw new InvalidInputException(this.fileName, String.format(
                "'weightMap' size %d does not match the number of nodes %d",
                this.weightMap.size(),
                this.numberOfNodes
            ));
        }
        for (Map.Entry<Integer, Weight> entry : this.weightMap.entrySet()) {
            final var node = entry.getKey();
            final var weight = entry.getValue();
            if (!this.weightSize.equals(weight.size())) {
                throw new InvalidInputException(this.fileName, String.format(
                    "weight of node %d is %d, which differs from the weight size %d",
                    node,
                    weight.size(),
                    this.weightSize
                ));
            }
        }
    }

}
