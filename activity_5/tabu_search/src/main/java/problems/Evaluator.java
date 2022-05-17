package problems;

import solutions.Solution;

public interface Evaluator<E, V extends Number> {

	public abstract Integer getDomainSize();

	public abstract V evaluate(Solution<E, V> sol);

	public abstract V evaluateInsertionCost(E elem, Solution<E, V> sol);

	public abstract V evaluateRemovalCost(E elem, Solution<E, V> sol);

	public abstract V evaluateExchangeCost(E elemIn, E elemOut, Solution<E, V> sol);

}
