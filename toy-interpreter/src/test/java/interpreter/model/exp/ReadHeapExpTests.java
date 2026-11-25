package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.stmt.WriteHeapStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadHeapExpTests
{
    private PrgState state;
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp()
    {
        this.symTable = new MyDictionary<>();
        this.heap = new MyHeap();
        this.state = new PrgState(
                new MyStack<>(),
                this.symTable,
                new MyList<>(),
                new MyDictionary<>(),
                this.heap,
                new NopStmt()
        );
    }

    @Test
    void readHeapReturnsAllocatedAndUpdatedValue() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        IStmt alloc = new NewStmt("v", new ValueExp(new IntValue(20)));
        alloc.execute(this.state);

        IExp read = new ReadHeapExp(new VarExp("v"));
        assertEquals(new IntValue(20), read.eval(this.symTable, this.heap));

        IStmt write = new WriteHeapStmt("v", new ValueExp(new IntValue(30)));
        write.execute(this.state);
        assertEquals(new IntValue(30), read.eval(this.symTable, this.heap));
    }

    @Test
    void readHeapThrowsWhenExpressionIsNotRefValue()
    {
        IExp readInt = new ReadHeapExp(new ValueExp(new IntValue(5)));
        assertThrows(ExpressionException.class, () -> readInt.eval(this.symTable, this.heap));
    }

    @Test
    void readHeapThrowsWhenAddressIsUnallocated() throws MyException
    {
        new VarDeclStmt("v", new RefType(new IntType())).execute(this.state);
        // Default RefValue has address 0, which is not in heap
        IExp readDefaultRef = new ReadHeapExp(new VarExp("v"));
        assertThrows(ExpressionException.class, () -> readDefaultRef.eval(this.symTable, this.heap));

        // Explicit unallocated RefValue address
        IExp readMissingAddr = new ReadHeapExp(new ValueExp(new RefValue(999, new IntType())));
        assertThrows(ExpressionException.class, () -> readMissingAddr.eval(this.symTable, this.heap));
    }
}