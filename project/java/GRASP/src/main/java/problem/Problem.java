package problem;

import java.util.Map;

import com.google.common.graph.Graph;

import solution.Weight;

public interface Problem {

    public Integer getNumberOfNodes();

    public Integer getWeightSize();

    public Graph<Integer> getGraph();

    public Weight getCapacity();

    public Map<Integer, Weight> getWeightMap();

}
