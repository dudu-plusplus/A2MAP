package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public class ArithExp implements IExp
{
    private final ArithOp op;
    private final IExp left;
    private final IExp right;

    public ArithExp(ArithOp op, IExp left, IExp right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }

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
            case ADD -> new IntValue(n1 + n2);
            case SUB -> new IntValue(n1 - n2);
            case MUL -> new IntValue(n1 * n2);
            case DIV ->
            {
                if (n2 == 0)
                {
                    throw new ADTException("Division by zero");
                }
                yield new IntValue(n1 / n2);
            }
        };
    }

    @Override
    public String toString()
    {
        return "(" + this.left + " " + this.op + " " + this.right + ")";
    }
}