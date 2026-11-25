package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IfStmtTests
{
    @Test
    void nonBooleanConditionThrows()
    {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                new NopStmt()
        );
        IStmt ifStmt = new IfStmt(new ValueExp(new IntValue(1)), new NopStmt(), new NopStmt());
        assertThrows(StatementException.class, () -> ifStmt.execute(state));
    }
}