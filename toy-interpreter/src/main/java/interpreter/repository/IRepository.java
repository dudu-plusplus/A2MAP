package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;
import java.util.List;

public interface IRepository {
    void addState(PrgState state);
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
    void logPrgStateExec(PrgState state) throws RepositoryException;
    String getLogFilePath();
    void setLogFilePath(String logFilePath);
}