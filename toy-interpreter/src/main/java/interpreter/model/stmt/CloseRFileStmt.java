package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;
import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws StatementException {
        try {
            IValue val = exp.eval(state.getSymTable());
            if (!(val instanceof StringValue fileName))
                throw new StatementException("closeRFile: " + exp + " is not a string");

            var fileTable = state.getFileTable();
            if (!fileTable.isDefined(fileName))
                throw new StatementException("closeRFile: " + fileName + " is not open");

            BufferedReader reader = fileTable.lookup(fileName);
            reader.close();
            fileTable.remove(fileName);
        } catch (ExpressionException | ADTException e) {
            throw new StatementException(e.getMessage());
        } catch (IOException e) {
            throw new StatementException("closeRFile: I/O error: " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}