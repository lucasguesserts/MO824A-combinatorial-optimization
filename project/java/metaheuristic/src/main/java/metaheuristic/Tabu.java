package metaheuristic;

import localsearch.LocalSearch;
import localsearch.TabuSearch;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;
import solution.WeightedSolution;

public class Tabu extends ConstructiveGrasp {

    private final LocalSearch localSearch;
    private final Integer maximumNumberOfIterationsWithoutImprovement;
    private final Double tenureRatio;
    private final Double capacityExpansionRatio;
    private final Integer numberOfRestarts;

    private Solution bestSolution;

    public Tabu(
        final Problem problem,
        final GreedyCriteria greedyCriteria,
        final Double greedyParameter,
        final Double tenureRatio,
        final Double capacityExpansionRatio,
        final Integer maximumNumberOfIterationsWithoutImprovement
    ) {
        super(problem, greedyCriteria, greedyParameter);
        this.numberOfRestarts = this.getNumberOfRestarts();
        this.tenureRatio = tenureRatio;
        this.capacityExpansionRatio = capacityExpansionRatio;
        this.maximumNumberOfIterationsWithoutImprovement = maximumNumberOfIterationsWithoutImprovement;
        this.localSearch = new TabuSearch(
            this.problem,
            this.greedyCriteria,
            this.tenureRatio,
            this.capacityExpansionRatio,
            this.maximumNumberOfIterationsWithoutImprovement
        );
        this.bestSolution = new WeightedSolution(this.problem.getCapacity());
        return;
    }

    public Solution solve() {
        for (long count = 0; count < this.numberOfRestarts; ++count) {
            final var initialSolution = this.constructiveHeuristic();
            final var localOptimalSolution = this.localSearch.search(initialSolution);
            this.updateBestSolution(localOptimalSolution);
        }
        return this.bestSolution;
    }

    private void updateBestSolution(final Solution solution) {
        if (solution.getCost() > this.bestSolution.getCost()) {
            this.bestSolution = solution;
        }
        return;
    }

    private Integer getNumberOfRestarts() {
        return ((Long) Math.round(Math.sqrt(this.problem.getGraph().nodes().size()))).intValue();
    }

}
