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

public class NewStmt implements IStmt
{
    private final String varName;
    private final IExp exp;

    public NewStmt(String varName, IExp exp)
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

        if (!(varVal.getType() instanceof RefType refType))
        {
            throw new StatementException("Variable " + this.varName + " is not of RefType");
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

        if (!evaluated.getType().equals(refType.inner()))
        {
            throw new StatementException("Type mismatch in new(" + this.varName + ", " + this.exp + ")");
        }

        int address = heap.allocate(evaluated);
        try
        {
            symTable.update(this.varName, new RefValue(address, refType.inner()));
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
        return "new(" + this.varName + ", " + this.exp + ")";
    }
}