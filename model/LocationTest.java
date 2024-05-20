package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class LocationTest {
    
    @Test
    public void testHashCode() {
        Location a = new Location(0, 0);
        Location b = new Location(0, 1);
        assertFalse(a.hashCode() == b.hashCode());

        Location c = new Location(0, 0);
        assertTrue(a.hashCode() == c.hashCode());
    }

    @Test
    public void testEquals() {
        Location a = new Location(0, 0);
        Location b = new Location(0, 1);
        Location c = new Location(0, 0);

        assertNotEquals(a, b);
        assertEquals(a, c);
    }
}
