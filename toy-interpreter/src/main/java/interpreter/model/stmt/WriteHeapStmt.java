package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.exp.IExp;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public class WriteHeapStmt implements IStmt
{
    private final String varName;
    private final IExp exp;

    public WriteHeapStmt(String varName, IExp exp)
    {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(this.varName))
        {
            throw new StatementException("Variable " + this.varName + " is not declared in symTable");
        }

        IValue varVal;
        try
        {
            varVal = symTable.lookup(this.varName);
        }
        catch (ADTException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!(varVal.getType() instanceof RefType) || !(varVal instanceof RefValue refVal))
        {
            throw new StatementException("Variable " + this.varName + " is not a RefValue");
        }

        int address = refVal.address();
        if (!heap.isDefined(address))
        {
            throw new StatementException("Heap address " + address + " is not allocated");
        }

        IValue evaluated;
        try
        {
            evaluated = this.exp.eval(symTable, heap);
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!evaluated.getType().equals(refVal.locationType()))
        {
            throw new StatementException("Type mismatch in wH(" + this.varName + ", " + this.exp + ")");
        }

        try
        {
            heap.update(address, evaluated);
        }
        catch (ADTException e)
        {
            throw new StatementException(e.getMessage());
        }

        return state;
    }

    @Override
    public String toString()
    {
        return "wH(" + this.varName + ", " + this.exp + ")";
    }
}