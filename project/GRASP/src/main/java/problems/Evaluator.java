package problems;

import solutions.Solution;

public interface Evaluator<E> {

    public abstract Integer getDomainSize();

    public abstract Double evaluate(Solution<E> sol);

    public abstract Double evaluateInsertionCost(E elem, Solution<E> sol);

    public abstract Double evaluateRemovalCost(E elem, Solution<E> sol);

    public abstract Double evaluateExchangeCost(E elemIn, E elemOut, Solution<E> sol);

}
