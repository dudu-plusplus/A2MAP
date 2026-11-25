package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogicExpTests
{
    private final MyDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    @Test
    void andOrOperatorsWork() throws MyException
    {
        IExp t = new ValueExp(new BoolValue(true));
        IExp f = new ValueExp(new BoolValue(false));
        assertEquals(new BoolValue(false), new LogicExp(LogicOp.AND, t, f).eval(symTable, heap));
        assertEquals(new BoolValue(true), new LogicExp(LogicOp.OR, t, f).eval(symTable, heap));
    }

    @Test
    void nonBooleanOperandThrows()
    {
        IExp bad = new LogicExp(LogicOp.AND, new ValueExp(new BoolValue(true)), new ValueExp(new IntValue(1)));
        assertThrows(ADTException.class, () -> bad.eval(symTable, heap));
    }
}