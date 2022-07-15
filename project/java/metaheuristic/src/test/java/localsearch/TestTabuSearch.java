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

public class TestTabuSearch {

    static final String CASES_DIR = "localsearch/";

    private Problem problem;
    private GreedyCriteria greedyCriteria;
    private Integer maximumNumberOfIterationsWithoutImprovement;
    private Double tenureRatio;
    private Double capacityExpansionRatio;

    public void init(final String instanceName) throws JSONException, IOException, InvalidInputException {
        this.problem = new ProblemData(CASES_DIR + instanceName);
        this.greedyCriteria = new GreedyCriteriaMax();
        this.maximumNumberOfIterationsWithoutImprovement = 4;
        this.tenureRatio = 0.4;
        this.capacityExpansionRatio = 0.2;
    }

    @Test
    public void testCase1() throws JSONException, IOException, InvalidInputException {
        this.init("case_1.json");
        final Solution initialSolution = new WeightedSolution(this.problem.getCapacity());
        for (final var element : new Integer[]{0, 1}) {
            initialSolution.addElement(element, this.problem.getWeightMap().get(element));
        }
        final LocalSearch localOptimalRunner = new TabuSearch(
            this.problem,
            this.greedyCriteria,
            this.tenureRatio,
            this.capacityExpansionRatio,
            this.maximumNumberOfIterationsWithoutImprovement
        );
        final Solution localOptimalSolution = localOptimalRunner.search(initialSolution);
        Assert.assertTrue(problem.getGraph().nodes().containsAll(localOptimalSolution.getElements()));
        Assert.assertTrue(localOptimalSolution.getElements().size() > 0);
        Assert.assertEquals(localOptimalSolution.getCost(), (Integer) 3);
        Assert.assertFalse(localOptimalSolution.getWeight().exceeds(problem.getCapacity()));
        Assert.assertEquals(localOptimalSolution.getWeight(), new Weight(Arrays.asList(160, 160)));
    }

}
