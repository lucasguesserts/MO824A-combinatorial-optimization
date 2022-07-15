package metaheuristic;

import java.io.IOException;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import metaheuristic.greedy_criteria.GreedyCriteria;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.Problem;
import problem.ProblemData;

public class TestGrasp {

    static final String INSTANCES_DIR = "metaheuristic/";

    Problem problem;
    GreedyCriteria greedyCriteria;
    Double greedyParameter;

    public void init(final String instanceName) throws JSONException, IOException, InvalidInputException {
        this.problem = new ProblemData(INSTANCES_DIR + instanceName);
        this.greedyCriteria = new GreedyCriteriaMax();
        this.greedyParameter = 0.2;
    }

    @Test
    public void testGraspInstance1() throws JSONException, IOException, InvalidInputException {
        this.init("instance_1.json");
        final var grasp = new Grasp(problem, greedyCriteria, greedyParameter);
        final var solution = grasp.solve();
        Assert.assertTrue(solution.getElements().size() > 0);
        Assert.assertTrue(problem.getGraph().nodes().containsAll(solution.getElements()));
        Assert.assertEquals(solution.getCost(), (Integer) 2);
        Assert.assertFalse(solution.getWeight().exceeds(problem.getCapacity()));
    }

    @Test
    public void testGraspInstance3() throws JSONException, IOException, InvalidInputException {
        this.init("instance_3.json");
        final var grasp = new Grasp(problem, greedyCriteria, greedyParameter);
        final var solution = grasp.solve();
        Assert.assertTrue(problem.getGraph().nodes().containsAll(solution.getElements()));
        Assert.assertTrue(solution.getElements().size() > 0);
        Assert.assertTrue(solution.getCost() <= 5);
        Assert.assertFalse(solution.getWeight().exceeds(problem.getCapacity()));
    }

}
