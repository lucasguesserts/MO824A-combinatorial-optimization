package metaheuristic.greedy_criteria;

import solution.Weight;

public class GreedyCriteriaNorm2 extends GreedyCriteria {

    public GreedyCriteriaNorm2() {}

    @Override
    public Double evaluate(final Weight weight) {
        return weight.getNorm2();
    }

}
