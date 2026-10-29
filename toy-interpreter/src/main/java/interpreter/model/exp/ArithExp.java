package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public class ArithExp implements IExp {
    private final ArithOp op;
    private final IExp left;
    private final IExp right;

    public ArithExp(ArithOp op, IExp left, IExp right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws ExpressionException {
        IValue v1 = left.eval(symTable);
        if (!v1.getType().equals(new IntType())) {
            throw new ExpressionException("first operand is not an integer");
        }
        IValue v2 = right.eval(symTable);
        if (!v2.getType().equals(new IntType())) {
            throw new ExpressionException("second operand is not an integer");
        }
        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();

        return switch (op) {
            case ADD -> new IntValue(n1 + n2);
            case SUB -> new IntValue(n1 - n2);
            case MUL -> new IntValue(n1 * n2);
            case DIV -> {
                if (n2 == 0) throw new ExpressionException("division by zero");
                yield new IntValue(n1 / n2);
            }
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}