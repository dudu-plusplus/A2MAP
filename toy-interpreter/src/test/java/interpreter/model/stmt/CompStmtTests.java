package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompStmtTests
{
    @Test
    void compStmtRuleS1() throws MyException
    {
        IStmt comp = new CompStmt(new VarDeclStmt("a", new IntType()), new NopStmt());
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                comp
        );
        state.getExeStack().pop();
        comp.execute(state);
        assertFalse(state.getExeStack().isEmpty());
    }
}