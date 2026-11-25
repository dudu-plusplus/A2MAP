package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;

public class AssignStmt implements IStmt
{
    private final String id;
    private final IExp exp;

    public AssignStmt(String id, IExp exp)
    {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IDictionary<String, IValue> symTable = state.getSymTable();

        if (!symTable.isDefined(this.id))
        {
            throw new StatementException("Variable is not defined: " + this.id);
        }

        IValue val;
        try
        {
            val = this.exp.eval(symTable, state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        IValue currentVal;
        try
        {
            currentVal = symTable.lookup(this.id);
        }
        catch (ADTException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!val.getType().equals(currentVal.getType()))
        {
            throw new StatementException("Type mismatch in assignment to " + this.id);
        }

        symTable.put(this.id, val);
        return state;
    }

    @Override
    public String toString()
    {
        return this.id + " = " + this.exp;
    }
}