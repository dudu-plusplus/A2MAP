package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;

public class VarExp implements IExp
{
    private final String id;

    public VarExp(String id)
    {
        this.id = id;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException
    {
        if (!symTable.isDefined(this.id))
        {
            throw new ADTException("Variable " + this.id + " is not defined");
        }
        return symTable.lookup(this.id);
    }

    @Override
    public String toString()
    {
        return this.id;
    }
}