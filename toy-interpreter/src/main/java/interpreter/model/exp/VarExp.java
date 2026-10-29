package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

public class VarExp implements IExp {
    private final String key;

    public VarExp(String k) {
        key = k;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> tbl) throws ExpressionException {
        try {
            return tbl.lookup(key);
        } catch (ADTException e) {
            throw new ExpressionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return key;
    }
}