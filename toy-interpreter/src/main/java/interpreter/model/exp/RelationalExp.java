package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record RelationalExp(RelOp op, IExp left, IExp right) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws ExpressionException {
        IValue v1 = left.eval(symTable);
        if (!(v1 instanceof IntValue i1))
            throw new ExpressionException("first operand is not an integer");

        IValue v2 = right.eval(symTable);
        if (!(v2 instanceof IntValue i2))
            throw new ExpressionException("second operand is not an integer");

        int n1 = i1.getVal();
        int n2 = i2.getVal();

        return new BoolValue(switch (op) {
            case LT -> n1 < n2;
            case LE -> n1 <= n2;
            case EQ -> n1 == n2;
            case NE -> n1 != n2;
            case GT -> n1 > n2;
            case GE -> n1 >= n2;
        });
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}