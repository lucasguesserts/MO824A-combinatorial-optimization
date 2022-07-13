package input;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import problem.InvalidInputException;
import problem.ProblemData;
import solution.Weight;

public class TestProblemLoader {

    @Test
    public void testConstructor() throws JSONException, IOException, InvalidInputException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        final var problemData = new ProblemData("problem_data/instance_3.json");
        Assert.assertNotNull(problemData);
        Assert.assertEquals(problemData.getNumberOfNodes(), (Integer) 10);
        Assert.assertEquals(problemData.getWeightSize(), (Integer) 3);
        final var weightMap = problemData.getWeightMap();
        Assert.assertNotNull(weightMap);
        Assert.assertEquals(weightMap.size(), 10);
        Assert.assertEquals(weightMap.get(0).size(), (Integer) 3);
        final var graph = problemData.getGraph();
        Assert.assertNotNull(graph);
        Assert.assertEquals(graph.nodes().size(), 10);
        Assert.assertEquals(graph.edges().size(), 11);
        final var capacity = problemData.getCapacity();
        Assert.assertNotNull(capacity);
        Assert.assertEquals(capacity, new Weight(Arrays.asList(2250, 3442, 1466)));
    }
}
