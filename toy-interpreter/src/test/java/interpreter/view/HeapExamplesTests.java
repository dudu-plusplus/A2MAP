package interpreter.view;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeapExamplesTests
{
    private PrgState runProgram(IStmt program) throws MyException
    {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                program
        );

        IStack<IStmt> exeStack = state.getExeStack();
        while (!exeStack.isEmpty())
        {
            IStmt currentStmt = exeStack.pop();
            currentStmt.execute(state);
        }

        return state;
    }

    @Test
    void ex6ProducesExpectedOutput() throws MyException
    {
        PrgState state = this.runProgram(Examples.EX6);
        List<IValue> out = state.getOut().toList();

        assertEquals(List.of(new IntValue(20), new IntValue(25)), out);
    }

    @Test
    void ex7ProducesExpectedOutput() throws MyException
    {
        PrgState state = this.runProgram(Examples.EX7);
        List<IValue> out = state.getOut().toList();

        assertEquals(List.of(new IntValue(20), new IntValue(35)), out);
    }
}