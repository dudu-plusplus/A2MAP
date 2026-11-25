package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.StringType;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStmt implements IStmt
{
    private final IExp exp;

    public OpenRFileStmt(IExp exp)
    {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        IValue val;
        try
        {
            val = this.exp.eval(symTable, state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!val.getType().equals(new StringType()))
        {
            throw new StatementException("OpenRFile expression must evaluate to a StringType");
        }

        StringValue fileName = (StringValue) val;
        if (fileTable.isDefined(fileName))
        {
            throw new StatementException("File is already open: " + fileName.val());
        }

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileName.val()));
            fileTable.put(fileName, reader);
        }
        catch (IOException e)
        {
            throw new StatementException("Could not open file: " + fileName.val());
        }

        return state;
    }

    @Override
    public String toString()
    {
        return "openRFile(" + this.exp + ")";
    }
}