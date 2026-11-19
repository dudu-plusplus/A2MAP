package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {
    private final MyDictionary<String, interpreter.model.value.IValue> symTable = new MyDictionary<>();

    @Test
    void allSixOperatorsTrueAndFalseCases() throws MyException {
        // "<"
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LT, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(2))).eval(symTable));

        // "<="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LE, new ValueExp(new IntValue(6)), new ValueExp(new IntValue(5))).eval(symTable));

        // "=="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(4))).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(3))).eval(symTable));

        // "!="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.NE, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(3))).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.NE, new ValueExp(new IntValue(4)), new ValueExp(new IntValue(4))).eval(symTable));

        // ">"
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GT, new ValueExp(new IntValue(7)), new ValueExp(new IntValue(3))).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GT, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(7))).eval(symTable));

        // ">="
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GE, new ValueExp(new IntValue(7)), new ValueExp(new IntValue(7))).eval(symTable));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GE, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(7))).eval(symTable));
    }

    @Test
    void nonIntegerOperandThrowsExpressionException() {
        assertThrows(ExpressionException.class,
                () -> new RelationalExp(RelOp.LT, new ValueExp(new StringValue("a")), new ValueExp(new IntValue(1))).eval(symTable));
        assertThrows(ExpressionException.class,
                () -> new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(1)), new ValueExp(new BoolValue(true))).eval(symTable));
    }
}