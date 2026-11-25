package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTests
{
    @Test
    void printStmtRuleS4() throws MyException
    {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                new NopStmt()
        );
        IStmt print = new PrintStmt(new ValueExp(new IntValue(10)));
        print.execute(state);
        assertEquals(new IntValue(10), state.getOut().get(0));
    }
}