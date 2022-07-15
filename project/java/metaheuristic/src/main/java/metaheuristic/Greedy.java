package metaheuristic;

import localsearch.LocalOptimal;
import localsearch.LocalSearch;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;

public class Greedy extends ConstructiveGrasp implements Metaheuristic {

    private static final Double GREEDY_PARAMETER = 0.0;
    private final LocalSearch localSearch;

    public Greedy(
        final Problem problem,
        final GreedyCriteria greedyCriteria
    ) {
        super(problem, greedyCriteria, GREEDY_PARAMETER);
        this.localSearch = new LocalOptimal(this.problem, this.greedyCriteria);
        return;
    }

    public Solution solve() {
        final var initialSolution = this.constructiveHeuristic();
        final var localOptimalSolution = this.localSearch.search(initialSolution);
        return localOptimalSolution;
    }

}
