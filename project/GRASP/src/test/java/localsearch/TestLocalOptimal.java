package localsearch;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import metaheuristic.greedy_criteria.GreedyCriteria;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.Problem;
import problem.ProblemData;
import solution.Solution;
import solution.Weight;
import solution.WeightedSolution;

public class TestLocalOptimal {

    static final String RESOURCES_DIR = "./GRASP/src/test/resources/";
    static final String CASES_DIR = RESOURCES_DIR + "localsearch/";

    Problem problem;
    GreedyCriteria greedyCriteria;

    public void init(final String instanceName) throws JSONException, IOException, InvalidInputException {
        this.problem = new ProblemData(CASES_DIR + instanceName);
        this.greedyCriteria = new GreedyCriteriaMax();
    }

    @Test
    public void testCase1() throws JSONException, IOException, InvalidInputException {
        this.init("case_1.json");
        final Solution initialSolution = new WeightedSolution(this.problem.getCapacity());
        for (final var element : new Integer[]{0, 1}) {
            initialSolution.addElement(element, this.problem.getWeightMap().get(element));
        }
        final LocalSearch localOptimalRunner = new LocalOptimal(this.problem, this.greedyCriteria);
        final Solution localOptimalSolution = localOptimalRunner.search(initialSolution);
        Assert.assertTrue(problem.getGraph().nodes().containsAll(localOptimalSolution.getElements()));
        Assert.assertTrue(localOptimalSolution.getElements().size() > 0);
        Assert.assertEquals(localOptimalSolution.getCost(), (Integer) 3);
        Assert.assertFalse(localOptimalSolution.getWeight().exceeds(problem.getCapacity()));
        Assert.assertEquals(localOptimalSolution.getWeight(), new Weight(Arrays.asList(160, 160)));
    }

}
