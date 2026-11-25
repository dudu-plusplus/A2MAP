package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt
{
    private final IExp exp;
    private final String varName;

    public ReadFileStmt(IExp exp, String varName)
    {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        if (!symTable.isDefined(this.varName))
        {
            throw new StatementException("Variable is not defined: " + this.varName);
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

        if (!varVal.getType().equals(new IntType()))
        {
            throw new StatementException("Variable is not of IntType: " + this.varName);
        }

        IValue evalVal;
        try
        {
            evalVal = this.exp.eval(symTable, state.getHeap());
        }
        catch (MyException e)
        {
            throw new StatementException(e.getMessage());
        }

        if (!evalVal.getType().equals(new StringType()))
        {
            throw new StatementException("ReadFile expression must evaluate to a StringType");
        }

        StringValue fileName = (StringValue) evalVal;
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
            String line = reader.readLine();
            int parsed = (line == null || line.isEmpty()) ? 0 : Integer.parseInt(line);
            symTable.put(this.varName, new IntValue(parsed));
        }
        catch (IOException e)
        {
            throw new StatementException("Error reading from file: " + fileName.val());
        }
        catch (NumberFormatException e)
        {
            throw new StatementException("Invalid integer in file: " + fileName.val());
        }

        return state;
    }

    @Override
    public String toString()
    {
        return "readFile(" + this.exp + ", " + this.varName + ")";
    }
}