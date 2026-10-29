package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NopStmsTests {
    @Test
    void nopStmtRuleS6() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
        IStmt nop = new NopStmt();
        assertEquals(state, nop.execute(state));
    }
}