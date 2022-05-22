package objectiveFunction;

public interface ObjectiveFunction<E, V> extends Cloneable {

    public Integer getDomainSize();
    public Boolean isValidCandidate(final E element);
    public V evaluateInsertionCost(final E element);
    public V evaluateRemovalCost(final E element);
    public V evaluateExchangeCost(final E elementToInsert, final E elementToRemove);
    public V evaluateTwoAdditionOneRemovalCost(
        final E firstElementToInsert,
        final E secondElementToInsert,
        final E elementToRemove
    );
    public V evaluateOneAdditionTwoRemovalCost(
        final E elementToInsert,
        final E firstElementToRemove,
        final E secondElementToRemove
    );

}
