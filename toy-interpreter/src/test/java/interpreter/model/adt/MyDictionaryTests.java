package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTests {
    @Test
    void testPutAndLookup() throws ADTException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("a", 10);
        assertTrue(dict.isDefined("a"));
        assertEquals(10, dict.lookup("a"));
    }

    @Test
    void testLookupUndefinedThrows() {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        assertThrows(ADTException.class, () -> dict.lookup("missing"));
    }

    @Test
    void testUpdate() throws ADTException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("a", 10);
        dict.update("a", 20);
        assertEquals(20, dict.lookup("a"));
        assertThrows(ADTException.class, () -> dict.update("missing", 30));
    }

    @Test
    void testRemoveAndToMap() throws ADTException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("a", 10);
        assertEquals(Map.of("a", 10), dict.toMap());

        dict.remove("a");
        assertFalse(dict.isDefined("a"));
        assertThrows(ADTException.class, () -> dict.remove("a"));
    }
}