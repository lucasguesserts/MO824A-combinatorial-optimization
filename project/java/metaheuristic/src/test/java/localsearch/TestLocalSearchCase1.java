package localsearch;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import metaheuristic.greedy_criteria.GreedyCriteria;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.Problem;
import problem.ProblemData;
import solution.Solution;
import solution.Weight;
import solution.WeightedSolution;

public class TestLocalSearchCase1 {

    static final String CASES_DIR = "localsearch/";
    static final String INSTANCE_NAME = "case_1.json";

    private Problem problem;
    private Solution initialSolution;
    private GreedyCriteria greedyCriteria;
    private Integer maximumNumberOfIterationsWithoutImprovement;
    private Double tenureRatio;
    private Double capacityExpansionRatio;

    @BeforeMethod
    public void init() throws JSONException, IOException, InvalidInputException {
        this.problem = new ProblemData(CASES_DIR + INSTANCE_NAME);
        this.initialSolution = new WeightedSolution(this.problem.getCapacity());
        for (final var element : new Integer[]{0, 1}) {
            this.initialSolution.addElement(element, this.problem.getWeightMap().get(element));
        }
        this.greedyCriteria = new GreedyCriteriaMax();
        this.maximumNumberOfIterationsWithoutImprovement = 4;
        this.tenureRatio = 0.4;
        this.capacityExpansionRatio = 0.2;
    }

    @Test
    public void testLocalOptimal() throws JSONException, IOException, InvalidInputException {
        final LocalSearch localOptimalRunner = new LocalOptimal(this.problem, this.greedyCriteria);
        final Solution localOptimalSolution = localOptimalRunner.search(initialSolution);
        assertSolution(localOptimalSolution);
    }

    @Test
    public void testTabuSearch() throws JSONException, IOException, InvalidInputException {
        final LocalSearch localOptimalRunner = new TabuSearch(
            this.problem,
            this.greedyCriteria,
            this.tenureRatio,
            this.capacityExpansionRatio,
            this.maximumNumberOfIterationsWithoutImprovement
        );
        final Solution localOptimalSolution = localOptimalRunner.search(initialSolution);
        assertSolution(localOptimalSolution);
    }

    private void assertSolution(final Solution solution) {
        Assert.assertTrue(problem.getGraph().nodes().containsAll(solution.getElements()));
        Assert.assertTrue(solution.getElements().size() > 0);
        Assert.assertEquals(solution.getCost(), (Integer) 3);
        Assert.assertFalse(solution.getWeight().exceeds(problem.getCapacity()));
        Assert.assertEquals(solution.getWeight(), new Weight(Arrays.asList(160, 160)));
    }

}
