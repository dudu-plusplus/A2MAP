package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public class LogicExp implements IExp
{
    private final LogicOp op;
    private final IExp left;
    private final IExp right;

    public LogicExp(LogicOp op, IExp left, IExp right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException
    {
        IValue v1 = this.left.eval(symTable, heap);
        if (!v1.getType().equals(new BoolType()))
        {
            throw new ADTException("First operand is not a boolean");
        }

        IValue v2 = this.right.eval(symTable, heap);
        if (!v2.getType().equals(new BoolType()))
        {
            throw new ADTException("Second operand is not a boolean");
        }

        boolean b1 = ((BoolValue) v1).getVal();
        boolean b2 = ((BoolValue) v2).getVal();

        return switch (this.op)
        {
            case AND -> new BoolValue(b1 && b2);
            case OR -> new BoolValue(b1 || b2);
        };
    }

    @Override
    public String toString()
    {
        return "(" + this.left + " " + this.op + " " + this.right + ")";
    }
}