package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringValueTests {
    @Test
    void stringDefaultValueAndEquality() {
        StringType type = new StringType();
        assertEquals(new StringValue(""), type.defaultValue());

        StringValue s1 = new StringValue("test.in");
        StringValue s2 = new StringValue("test.in");
        StringValue s3 = new StringValue("other.in");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
    }
}