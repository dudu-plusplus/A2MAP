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

class ArithExpTests
{
    private final MyDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    @Test
    void allArithmeticOperatorsAndDivisionByZero() throws MyException
    {
        assertEquals(new IntValue(5), new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new IntValue(1), new ArithExp(ArithOp.SUB, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(2))).eval(symTable, heap));
        assertEquals(new IntValue(6), new ArithExp(ArithOp.MUL, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new IntValue(2), new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(6)), new ValueExp(new IntValue(3))).eval(symTable, heap));

        IExp divZero = new ArithExp(ArithOp.DIV, new ValueExp(new IntValue(1)), new ValueExp(new IntValue(0)));
        assertThrows(ADTException.class, () -> divZero.eval(symTable, heap));
    }

    @Test
    void nonIntegerOperandThrows()
    {
        IExp bad = new ArithExp(ArithOp.ADD, new ValueExp(new BoolValue(true)), new ValueExp(new IntValue(1)));
        assertThrows(ADTException.class, () -> bad.eval(symTable, heap));
    }
}