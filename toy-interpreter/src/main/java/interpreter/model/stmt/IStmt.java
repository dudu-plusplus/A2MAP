package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws StatementException;
    String toString();
}