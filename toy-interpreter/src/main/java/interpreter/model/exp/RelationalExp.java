package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record RelationalExp(RelOp op, IExp left, IExp right) implements IExp
{
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException
    {
        IValue v1 = this.left.eval(symTable, heap);
        if (!v1.getType().equals(new IntType()))
        {
            throw new ADTException("First operand is not an integer");
        }

        IValue v2 = this.right.eval(symTable, heap);
        if (!v2.getType().equals(new IntType()))
        {
            throw new ADTException("Second operand is not an integer");
        }

        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();

        return switch (this.op)
        {
            case LT -> new BoolValue(n1 < n2);
            case LE -> new BoolValue(n1 <= n2);
            case EQ -> new BoolValue(n1 == n2);
            case NE -> new BoolValue(n1 != n2);
            case GT -> new BoolValue(n1 > n2);
            case GE -> new BoolValue(n1 >= n2);
        };
    }
}