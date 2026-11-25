package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.StringType;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt
{
    private final IExp exp;

    public CloseRFileStmt(IExp exp)
    {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        IValue val;
        try
        {
            val = this.exp.eval(state.getSymTable(), state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!val.getType().equals(new StringType()))
        {
            throw new StatementException("CloseRFile expression must evaluate to a StringType");
        }

        StringValue fileName = (StringValue) val;
        if (!fileTable.isDefined(fileName))
        {
            throw new StatementException("File is not open: " + fileName.val());
        }

        BufferedReader reader;
        try
        {
            reader = fileTable.lookup(fileName);
        }
        catch (ADTException e)
        {
            throw new StatementException(e.getMessage());
        }

        try
        {
            reader.close();
            try
            {
                fileTable.remove(fileName);
            }
            catch (ADTException e)
            {
                throw new StatementException(e.getMessage());
            }
        }
        catch (IOException e)
        {
            throw new StatementException("Error closing file: " + fileName.val());
        }

        return state;
    }

    @Override
    public String toString()
    {
        return "closeRFile(" + this.exp + ")";
    }
}