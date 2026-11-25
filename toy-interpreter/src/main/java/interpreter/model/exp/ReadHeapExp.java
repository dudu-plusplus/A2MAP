package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public class ReadHeapExp implements IExp
{
    private final IExp exp;

    public ReadHeapExp(IExp exp)
    {
        this.exp = exp;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException
    {
        IValue evaluated = this.exp.eval(symTable, heap);
        if (!(evaluated instanceof RefValue refVal))
        {
            throw new ExpressionException("Expression " + this.exp + " does not evaluate to a RefValue");
        }

        int address = refVal.address();
        if (!heap.isDefined(address))
        {
            throw new ExpressionException("Heap address " + address + " is not allocated");
        }

        try
        {
            return heap.get(address);
        }
        catch (ADTException e)
        {
            throw new ExpressionException(e.getMessage());
        }
    }

    @Override
    public String toString()
    {
        return "rH(" + this.exp + ")";
    }
}