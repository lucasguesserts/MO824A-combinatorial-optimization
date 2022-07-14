package solution;

import java.util.Arrays;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestElementSolution {

    @Test
    public void testConstructor() {
        final var solution = new ElementsSolution();
        Assert.assertNotNull(solution.getCost());
        Assert.assertNotNull(solution.getElements());
        Assert.assertTrue(solution.getElements().isEmpty());
    }

    @Test
    public void testAddElement() {
        final var solution = new ElementsSolution();
        solution.addElement(1);
        solution.addElement(2);
        final var expected = Arrays.asList(1, 2);
        Assert.assertFalse(solution.getElements().isEmpty());
        assertCollectionsAreEqual(solution.getElements(), expected);
    }

    @Test
    public void testCost() {
        final var solution = new ElementsSolution();
        solution.addElement(1);
        solution.addElement(2);
        Assert.assertEquals(solution.getCost(), (Integer) 2);
    }

    @Test
    public void testNoRepetition() {
        final var solution = new ElementsSolution();
        solution.addElement(1);
        Assert.expectThrows(
            AssertionError.class,
            () -> {
                solution.addElement(1);
            }
        );
    }

    @Test
    public void testNoNegativeValue() {
        final var solution = new ElementsSolution();
        Assert.expectThrows(
            AssertionError.class,
            () -> {
                solution.addElement(-1);
            }
        );
    }

    @Test
    public void testEquals() {
        final var first = new ElementsSolution();
        first.addElement(1);
        first.addElement(2);
        final var second = new ElementsSolution();
        second.addElement(1);
        second.addElement(2);
        Assert.assertEquals(first, second);
    }

    private void assertCollectionsAreEqual(final Collection<Integer> lhs, final Collection<Integer> rhs) {
        Assert.assertTrue(lhs.containsAll(rhs) && rhs.containsAll(lhs));
    }

}
