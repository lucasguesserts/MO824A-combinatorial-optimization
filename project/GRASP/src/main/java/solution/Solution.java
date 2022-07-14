package solution;

import java.util.Set;

public interface Solution {

    public Set<Integer> getElements();

    public Integer getCost();

    public Weight getCapacity();

    public Weight getWeight();

    public void addElement(final Integer element, final Weight elementWeight);

    public void removeElement(final Integer element, final Weight elementWeight);

    public void substituteElement(
        final Integer toRemove,
        final Weight toRemoveWeight,
        final Integer toAdd,
        final Weight toAddWeight
    );

}

