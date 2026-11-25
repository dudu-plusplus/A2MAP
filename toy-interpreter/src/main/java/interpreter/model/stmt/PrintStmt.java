package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IList;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;

public class PrintStmt implements IStmt
{
    private final IExp exp;

    public PrintStmt(IExp exp)
    {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IList<IValue> out = state.getOut();
        IValue val;
        try
        {
            val = this.exp.eval(state.getSymTable(), state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }
        out.add(val);
        return state;
    }

    @Override
    public String toString()
    {
        return "print(" + this.exp + ")";
    }
}