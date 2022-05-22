package objectiveFunction;

import java.util.Collection;

public interface ObjectiveFunction<E, V> extends Cloneable {

    public Integer getDomainSize();
    public Boolean isValidCandidate(final E element);
    public V evaluateInsertionCost(final E element);
    public V evaluateRemovalCost(final E element);
    public V evaluateExchangeCost(final E elementToInsert, final E elementToRemove);
    public V evaluate(
        final Collection<E> elementsToInsert,
        final Collection<E> elementsToRemove
    );

}
