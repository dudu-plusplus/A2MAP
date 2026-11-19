package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.StringType;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt {
    private final IExp exp;

    public CloseRFileStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) {
        try {
            IValue val = exp.eval(state.getSymTable());
            if (!(val.getType() instanceof StringType)) {
                throw new StatementException("closeRFile expression must evaluate to a string");
            }
            StringValue fileName = (StringValue) val;
            if (!state.getFileTable().isDefined(fileName)) {
                throw new StatementException("File is not open: " + fileName.val());
            }
            BufferedReader reader = state.getFileTable().lookup(fileName);
            try {
                reader.close();
            } catch (IOException e) {
                throw new StatementException("Error closing file: " + fileName.val());
            }
            state.getFileTable().remove(fileName);
            return state;
        } catch (MyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}