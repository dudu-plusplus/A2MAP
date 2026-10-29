package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

public class ValueExp implements IExp {
    private final IValue val;

    public ValueExp(IValue v) {
        val = v;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> tbl) throws ExpressionException {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}