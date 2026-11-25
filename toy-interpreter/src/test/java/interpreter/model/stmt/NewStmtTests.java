package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewStmtTests
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

    @Test
    void newStmtAllocatesOnHeapAndUpdatesRefValue() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        IStmt alloc = new NewStmt("v", new ValueExp(new IntValue(20)));
        alloc.execute(this.state);

        IValue symVal = this.state.getSymTable().lookup("v");
        assertInstanceOf(RefValue.class, symVal);
        RefValue refVal = (RefValue) symVal;
        assertTrue(refVal.address() > 0);
        assertEquals(new IntType(), refVal.locationType());
        assertTrue(this.state.getHeap().isDefined(refVal.address()));
        assertEquals(new IntValue(20), this.state.getHeap().get(refVal.address()));
    }

    @Test
    void newStmtThrowsWhenVariableUndeclared()
    {
        IStmt alloc = new NewStmt("missing", new ValueExp(new IntValue(10)));
        assertThrows(StatementException.class, () -> alloc.execute(this.state));
    }

    @Test
    void newStmtThrowsWhenVariableNotRefType() throws MyException
    {
        new VarDeclStmt("x", new IntType()).execute(this.state);
        IStmt alloc = new NewStmt("x", new ValueExp(new IntValue(10)));
        assertThrows(StatementException.class, () -> alloc.execute(this.state));
    }

    @Test
    void newStmtThrowsWhenExpressionTypeMismatchesInnerType() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        IStmt alloc = new NewStmt("v", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> alloc.execute(this.state));
    }
}