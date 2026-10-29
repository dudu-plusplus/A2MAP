package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTests {
    private static PrgState run(IStmt program) throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
        new Controller(new Repository(state), false).allStep();
        return state;
    }

    @Test
    void exampleProgramsGiveExpectedOutput() throws MyException {
        PrgState state1 = run(Examples.EX1);
        assertEquals(new IntValue(2), state1.getOut().get(0));

        PrgState state2 = run(Examples.EX2);
        assertEquals(new IntValue(18), state2.getOut().get(0));

        PrgState state3 = run(Examples.EX3);
        assertEquals(new IntValue(2), state3.getOut().get(0));
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        Controller ctrl = new Controller(new Repository(state), false);
        ctrl.oneStep(state);
        assertThrows(StatementException.class, () -> ctrl.oneStep(state));
    }
}