package problems;

import solutions.Solution;

public interface Evaluator<E> {

    public abstract Integer getDomainSize();

    public abstract Integer evaluate(Solution<E> sol);

    public abstract Integer evaluateInsertionCost(E elem, Solution<E> sol);

    public abstract Integer evaluateRemovalCost(E elem, Solution<E> sol);

    public abstract Integer evaluateExchangeCost(E elemIn, E elemOut, Solution<E> sol);

}
