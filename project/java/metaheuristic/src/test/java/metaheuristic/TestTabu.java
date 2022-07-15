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

public class TestTabu {

    static final String INSTANCES_DIR = "metaheuristic/";

    private Problem problem;
    private GreedyCriteria greedyCriteria;
    private Double greedyParameter;
    private Integer maximumNumberOfIterationsWithoutImprovement;
    private Double tenureRatio;
    private Double capacityExpansionRatio;

    public void init(final String instanceName) throws JSONException, IOException, InvalidInputException {
        this.problem = new ProblemData(INSTANCES_DIR + instanceName);
        this.greedyCriteria = new GreedyCriteriaMax();
        this.greedyParameter = 0.2;
        this.maximumNumberOfIterationsWithoutImprovement = 4;
        this.tenureRatio = 0.4;
        this.capacityExpansionRatio = 0.2;
    }

    @Test
    public void testInstance1() throws JSONException, IOException, InvalidInputException {
        this.init("instance_1.json");
        final var tabu = new Tabu(
            problem,
            greedyCriteria,
            greedyParameter,
            tenureRatio,
            capacityExpansionRatio,
            maximumNumberOfIterationsWithoutImprovement
        );
        final var solution = tabu.solve();
        Assert.assertTrue(solution.getElements().size() > 0);
        Assert.assertTrue(problem.getGraph().nodes().containsAll(solution.getElements()));
        Assert.assertEquals(solution.getCost(), (Integer) 2);
        Assert.assertFalse(solution.getWeight().exceeds(problem.getCapacity()));
    }

    @Test
    public void testInstance3() throws JSONException, IOException, InvalidInputException {
        this.init("instance_3.json");
        final var tabu = new Tabu(
            problem,
            greedyCriteria,
            greedyParameter,
            tenureRatio,
            capacityExpansionRatio,
            maximumNumberOfIterationsWithoutImprovement
        );
        final var solution = tabu.solve();
        Assert.assertTrue(problem.getGraph().nodes().containsAll(solution.getElements()));
        Assert.assertTrue(solution.getElements().size() > 0);
        Assert.assertTrue(solution.getCost() <= 5);
        Assert.assertFalse(solution.getWeight().exceeds(problem.getCapacity()));
    }

}
