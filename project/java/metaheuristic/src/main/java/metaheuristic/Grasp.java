package metaheuristic;

import localsearch.LocalOptimal;
import localsearch.LocalSearch;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;
import solution.WeightedSolution;

public class Grasp extends ConstructiveGrasp {

    private final Integer numberOfIterations;
    private final LocalSearch localSearch;
    private Solution bestSolution;

    public Grasp(
        final Problem problem,
        final GreedyCriteria greedyCriteria,
        final Double greedyParameter
    ) {
        super(problem, greedyCriteria, greedyParameter);
        this.numberOfIterations = this.getNumberOfIterations();
        this.localSearch = new LocalOptimal(this.problem, this.greedyCriteria);
        this.bestSolution = new WeightedSolution(this.problem.getCapacity());
        return;
    }

    public Solution solve() {
        for (long count = 0; count < this.numberOfIterations; ++count) {
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

    private Integer getNumberOfIterations() {
        return ((Long) Math.round(Math.sqrt(this.problem.getGraph().nodes().size()))).intValue();
    }

}
