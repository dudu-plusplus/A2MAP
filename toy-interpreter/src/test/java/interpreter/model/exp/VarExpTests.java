package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VarExpTests
{
    @Test
    void undefinedVariableThrows()
    {
        MyDictionary<String, IValue> symTable = new MyDictionary<>();
        IHeap heap = new MyHeap();
        IExp var = new VarExp("x");
        assertThrows(ADTException.class, () -> var.eval(symTable, heap));
    }
}