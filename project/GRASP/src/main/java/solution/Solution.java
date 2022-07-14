package solution;

import java.util.Set;

public interface Solution {

    public Set<Integer> getElements();

    public Integer getCost();

    public Weight getCapacity();

    public Weight getWeight();

    public void addElement(final Integer element, final Weight elementWeight);

}

