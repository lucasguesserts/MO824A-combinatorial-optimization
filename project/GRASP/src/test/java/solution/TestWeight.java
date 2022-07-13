package solution;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestWeight {

    @Test
    public void testConstructor() {
        final var weight = new Weight(Arrays.asList(10, 20, 30));
        Assert.assertNotNull(weight);
        Assert.assertEquals(weight.size(), (Integer) 3);
    }

    @Test
    public void testEquality() {
        final var first = new Weight(Arrays.asList(10, 20, 30));
        final var second = new Weight(Arrays.asList(10, 20, 30));
        final var third = new Weight(Arrays.asList(10, 20, 999));
        Assert.assertEquals(first, second);
        Assert.assertNotEquals(first, third);
    }

    @Test
    public void testExceeds() {
        final var low = new Weight(Arrays.asList(1, 2, 3));
        final var high = new Weight(Arrays.asList(10, 20, 30));
        Assert.assertTrue(high.exceeds(low));
        Assert.assertFalse(low.exceeds(high));
    }

    @Test
    public void testAdd() {
        final var lhs = new Weight(Arrays.asList(10, 20, 30));
        final var rhs = new Weight(Arrays.asList(1, 2, 3));
        final var result = lhs.add(rhs);
        final var expected = new Weight(Arrays.asList(11, 22, 33));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testSubtract() {
        final var lhs = new Weight(Arrays.asList(10, 20, 30));
        final var rhs = new Weight(Arrays.asList(1, 2, 3));
        final var result = lhs.subtract(rhs);
        final var expected = new Weight(Arrays.asList(9, 18, 27));
        Assert.assertEquals(result, expected);
    }

}
