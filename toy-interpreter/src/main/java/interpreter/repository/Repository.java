package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgStates;
    private String logFilePath;

    public Repository(PrgState state) {
        this(state, "log.txt");
    }

    public Repository(PrgState state, String logFilePath) {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(state);
        this.logFilePath = logFilePath;
    }

    @Override
    public void addState(PrgState state) {
        prgStates.add(state);
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgStates;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.prgStates = prgList;
    }

    @Override
    public void logPrgStateExec(PrgState state) throws RepositoryException {
        try (var logFile = new BufferedWriter(new FileWriter(logFilePath, true))) {
            logFile.write(state.toLogString());
            logFile.flush();
        } catch (IOException e) {
            throw new RepositoryException("Error writing to log file: " + e.getMessage());
        }
    }

    @Override
    public String getLogFilePath() {
        return logFilePath;
    }

    @Override
    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
}