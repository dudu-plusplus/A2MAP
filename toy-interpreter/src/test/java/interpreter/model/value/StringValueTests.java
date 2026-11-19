package interpreter.model.value;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {
    @Test
    void stringVarGetsDefaultEmptyString() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        new VarDeclStmt("s", new StringType()).execute(state);
        assertEquals(new StringValue(""), state.getSymTable().lookup("s"));
    }

    @Test
    void equalStringValuesHaveSameEqualsAndHashCode() {
        StringValue a = new StringValue("hello");
        StringValue b = new StringValue("hello");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}