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
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteHeapStmtTests
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
    void writeHeapUpdatesAllocatedValue() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(this.state);

        RefValue refVal = (RefValue) this.state.getSymTable().lookup("v");
        IStmt write = new WriteHeapStmt("v", new ValueExp(new IntValue(30)));
        write.execute(this.state);

        assertEquals(new IntValue(30), this.state.getHeap().get(refVal.address()));
    }

    @Test
    void writeHeapThrowsWhenVariableUndeclared()
    {
        IStmt write = new WriteHeapStmt("missing", new ValueExp(new IntValue(30)));
        assertThrows(StatementException.class, () -> write.execute(this.state));
    }

    @Test
    void writeHeapThrowsWhenVariableNotRefValue() throws MyException
    {
        new VarDeclStmt("x", new IntType()).execute(this.state);
        IStmt write = new WriteHeapStmt("x", new ValueExp(new IntValue(30)));
        assertThrows(StatementException.class, () -> write.execute(this.state));
    }

    @Test
    void writeHeapThrowsWhenAddressNotInHeap() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        // Default RefValue has address 0, which is not allocated in MyHeap
        IStmt write = new WriteHeapStmt("v", new ValueExp(new IntValue(30)));
        assertThrows(StatementException.class, () -> write.execute(this.state));
    }

    @Test
    void writeHeapThrowsWhenTypeMismatchesLocationType() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(this.state);

        IStmt write = new WriteHeapStmt("v", new ValueExp(new BoolValue(false)));
        assertThrows(StatementException.class, () -> write.execute(this.state));
    }
}