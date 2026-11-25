package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest
{
    private final MyDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    @Test
    void allSixOperatorsTrueAndFalseCases() throws MyException
    {
        // "<"
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LT, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(2))).eval(symTable, heap));

        // "<="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LE, new ValueExp(new IntValue(6)), new ValueExp(new IntValue(5))).eval(symTable, heap));

        // "=="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(4))).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(3))).eval(symTable, heap));

        // "!="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.NE, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.NE, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(4))).eval(symTable, heap));

        // ">"
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GT, new ValueExp(new IntValue(7)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GT, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(7))).eval(symTable, heap));

        // ">="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GE, new ValueExp(new IntValue(7)), new ValueExp(new IntValue(7))).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GE, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(7))).eval(symTable, heap));
    }

    @Test
    void nonIntegerOperandThrowsExpressionException()
    {
        assertThrows(ADTException.class,
                () -> new RelationalExp(RelOp.LT, new ValueExp(new StringValue("a")), new ValueExp(new IntValue(1))).eval(symTable, heap));
        assertThrows(ADTException.class,
                () -> new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(1)), new ValueExp(new BoolValue(true))).eval(symTable, heap));
    }
}