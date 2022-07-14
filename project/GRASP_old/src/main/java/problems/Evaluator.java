package problems;

import solutions.Solution;

public interface Evaluator {

    public abstract Integer getDomainSize();

    public abstract Integer evaluate(Solution sol);

    public abstract Integer evaluateInsertionCost(Integer elem, Solution sol);

    public abstract Integer evaluateRemovalCost(Integer elem, Solution sol);

    public abstract Integer evaluateExchangeCost(Integer elemIn, Integer elemOut, Solution sol);

}
