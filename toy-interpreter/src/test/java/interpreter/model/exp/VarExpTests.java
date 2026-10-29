package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VarExpTests {
    @Test
    void undefinedVariableThrows() {
        MyDictionary<String, IValue> symTable = new MyDictionary<>();
        IExp var = new VarExp("x");
        assertThrows(ExpressionException.class, () -> var.eval(symTable));
    }
}