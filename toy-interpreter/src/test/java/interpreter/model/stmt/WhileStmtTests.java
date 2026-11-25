package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTests
{
    private PrgState state;

    @BeforeEach
    void setUp()
    {
        this.state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                new NopStmt()
        );
    }

    private void runUntilEmpty(PrgState prgState) throws MyException
    {
        IStack<IStmt> exeStack = prgState.getExeStack();
        while (!exeStack.isEmpty())
        {
            IStmt crtStmt = exeStack.pop();
            crtStmt.execute(prgState);
        }
    }

    @Test
    void whileExecutesBodyWhileConditionIsTrue() throws MyException
    {
        // int v; v = 4; (while v > 0 do (print(v); v = v - 1)); print(v);
        IStmt loopBody = new CompStmt(
                new PrintStmt(new VarExp("v")),
                new AssignStmt("v", new ArithExp(
                        ArithOp.SUB,
                        new VarExp("v"),
                        new ValueExp(new IntValue(1))
                ))
        );

        IStmt program = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt(
                                        new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                        loopBody
                                ),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );

        PrgState prgState = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                program
        );

        this.runUntilEmpty(prgState);

        assertEquals(
                List.of(
                        new IntValue(4),
                        new IntValue(3),
                        new IntValue(2),
                        new IntValue(1),
                        new IntValue(0)
                ),
                prgState.getOut().toList()
        );
    }

    @Test
    void whileSkipsBodyWhenConditionIsFalse() throws MyException
    {
        PrgState freshState = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
                new NopStmt()
        );
        freshState.getExeStack().pop();

        IStmt stmt = new WhileStmt(
                new ValueExp(new BoolValue(false)),
                new PrintStmt(new ValueExp(new IntValue(99)))
        );

        stmt.execute(freshState);

        assertTrue(freshState.getExeStack().isEmpty());
        assertTrue(freshState.getOut().toList().isEmpty());
    }

    @Test
    void whileThrowsWhenConditionIsNotBool()
    {
        IStmt stmt = new WhileStmt(
                new ValueExp(new IntValue(1)),
                new NopStmt()
        );

        assertThrows(StatementException.class, () -> stmt.execute(this.state));
    }

    @Test
    void whileThrowsWhenConditionExpressionFails()
    {
        IStmt stmt = new WhileStmt(
                new VarExp("undeclaredVar"),
                new NopStmt()
        );

        assertThrows(StatementException.class, () -> stmt.execute(this.state));
    }
}