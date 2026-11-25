package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueExpTests
{
    @Test
    void valueExpReturnsCorrectValue() throws MyException
    {
        MyDictionary<String, IValue> symTable = new MyDictionary<>();
        IHeap heap = new MyHeap();
        IExp exp = new ValueExp(new IntValue(42));
        assertEquals(new IntValue(42), exp.eval(symTable, heap));
    }
}