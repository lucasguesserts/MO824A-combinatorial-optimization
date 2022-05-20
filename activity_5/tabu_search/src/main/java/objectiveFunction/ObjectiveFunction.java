package objectiveFunction;

public interface ObjectiveFunction<E, V> {

    public Integer getDomainSize();
    public V evaluateInsertionCost(final E element);
    public V evaluateRemovalCost(final E element);
    public V evaluateExchangeCost(final E elementToInsert, final E elementToRemove);

}
