package localsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import metaheuristic.greedy_criteria.GreedyCriteria;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import metaheuristic.greedy_criteria.GreedyCriteriaNorm2;
import problem.InvalidInputException;
import problem.Problem;
import problem.ProblemData;
import solution.Solution;
import solution.WeightedSolution;

public class TestLocalOptimalCase2 {

    private static final String CASES_DIR = "localsearch/";
    static final String INSTANCE_NAME = "case_2.json";

    private Problem problem;
    private List<GreedyCriteria> greedyCriteriaList = Arrays.asList(
        new GreedyCriteriaMax(),
        new GreedyCriteriaNorm2()
    );

    private Integer maximumNumberOfIterationsWithoutImprovement;
    private Double tenureRatio;
    private Double capacityExpansionRatio;

    private static final List<List<Integer>> VALID_SOLUTIONS = Arrays.asList(
        Arrays.asList(0, 1, 2, 3, 4),
        Arrays.asList(0, 1, 2, 3, 5),
        Arrays.asList(0, 1, 2, 3, 7),
        Arrays.asList(0, 1, 2, 4, 5),
        Arrays.asList(0, 1, 2, 4, 7),
        Arrays.asList(0, 1, 2, 5, 7),
        Arrays.asList(0, 1, 3, 4, 5),
        Arrays.asList(0, 1, 3, 4, 7),
        Arrays.asList(0, 1, 3, 5, 7),
        Arrays.asList(0, 1, 4, 5, 6),
        Arrays.asList(0, 1, 4, 5, 7),
        Arrays.asList(0, 1, 2, 3),
        Arrays.asList(0, 1, 2, 4),
        Arrays.asList(0, 1, 2, 5),
        Arrays.asList(0, 1, 2, 7),
        Arrays.asList(0, 1, 3, 4),
        Arrays.asList(0, 1, 3, 5),
        Arrays.asList(0, 1, 3, 7),
        Arrays.asList(0, 1, 4, 5),
        Arrays.asList(0, 1, 4, 7),
        Arrays.asList(0, 1, 5, 7),
        Arrays.asList(0, 1, 2),
        Arrays.asList(0, 1, 3),
        Arrays.asList(0, 1, 4),
        Arrays.asList(0, 1, 5),
        Arrays.asList(0, 1, 7),
        Arrays.asList(0, 4, 5),
        Arrays.asList(0, 1),
        Arrays.asList(0, 4),
        Arrays.asList(0, 5),
        Arrays.asList(0)
    );

    private static final List<List<Integer>> INITIAL_SOLUTIONS_LIST = Arrays.asList(
        Arrays.asList(0),
        Arrays.asList(0, 1),
        Arrays.asList(0, 1, 2),
        Arrays.asList(0, 1, 3),
        Arrays.asList(0, 1, 4),
        Arrays.asList(0, 1, 7),
        Arrays.asList(0, 4, 5)
    );

    @BeforeMethod
    private void init() throws JSONException, IOException, InvalidInputException {
        this.problem = new ProblemData(CASES_DIR + INSTANCE_NAME);
        this.maximumNumberOfIterationsWithoutImprovement = 4;
        this.tenureRatio = 0.4;
        this.capacityExpansionRatio = 0.2;
    }

    @Test
    public void testLocalOptimal() throws JSONException, IOException, InvalidInputException {
        for (final var greedyCriteria : greedyCriteriaList)
        for (final var initialElements : INITIAL_SOLUTIONS_LIST){
            final var initialSolution = initializeSolution(initialElements);
            final LocalSearch localOptimalRunner = new LocalOptimal(this.problem, greedyCriteria);
            final Solution localOptimalSolution = localOptimalRunner.search(initialSolution);
            assertBasicStuff(localOptimalSolution);
            assertSolutionIsValid(localOptimalSolution);
            assertIsOptimal(localOptimalSolution);
        }
    }

    @Test
    public void testTabuSearch() throws JSONException, IOException, InvalidInputException {
        for (final var greedyCriteria : greedyCriteriaList)
        for (final var initialElements : INITIAL_SOLUTIONS_LIST){
            final var initialSolution = initializeSolution(initialElements);
            final LocalSearch localOptimalRunner = new TabuSearch(
                this.problem,
                greedyCriteria,
                this.tenureRatio,
                this.capacityExpansionRatio,
                this.maximumNumberOfIterationsWithoutImprovement
            );
            final Solution localOptimalSolution = localOptimalRunner.search(initialSolution);
            assertBasicStuff(localOptimalSolution);
            assertSolutionIsValid(localOptimalSolution);
            assertIsOptimal(localOptimalSolution);
        }
    }

    private Solution initializeSolution(final List<Integer> initialElements) {
        final Solution solution = new WeightedSolution(this.problem.getCapacity());
        for (final var element : initialElements) {
            solution.addElement(element, this.problem.getWeightMap().get(element));
        }
        return solution;
    }

    private void assertBasicStuff(final Solution solution) {
        Assert.assertTrue(solution.getElements().size() > 0);
        Assert.assertFalse(solution.getWeight().exceeds(problem.getCapacity()));

    }

    private void assertSolutionIsValid(final Solution solution) {
        Assert.assertTrue(
            VALID_SOLUTIONS.contains(
                new ArrayList<>(solution.getElements())
            )
        );
        return;
    }

    private void assertIsOptimal(final Solution solution) {
        Assert.assertEquals(solution.getCost(), (Integer) 5);
        return;
    }

}
