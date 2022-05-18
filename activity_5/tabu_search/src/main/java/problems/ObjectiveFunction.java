package problems;

import solutions.Solution;

public interface ObjectiveFunction<E, V> {

    public Integer getDomainSize();
    public V evaluate(final Solution<E, V> solution);
    public V evaluateInsertionCost(final E element, final Solution<E, V> solution);
    public V evaluateRemovalCost(final E element, final Solution<E, V> solution);
    public V evaluateExchangeCost(final E elementToInsert, final E elementToRemove, final Solution<E, V> solution);

}
