package testing_framework;

import org.testng.Assert;
import org.testng.annotations.Test;

public final class TestngEqualityTestSuite
{

    @Test
    public void test_equals()
    {
        final Integer actual = 1;
        final Integer expected = 1;
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual, expected);
        Assert.assertTrue(actual.equals(expected));
    }

    @Test
    public void test_not_equals()
    {
        final Integer actual = 1;
        final Integer not_expected = 2;
        Assert.assertNotNull(actual);
        Assert.assertNotSame(actual, not_expected);
        Assert.assertNotEquals(actual, not_expected);
        Assert.assertFalse(actual.equals(not_expected));
    }

}
