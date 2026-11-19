package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;
    private Path filePath;

    @BeforeEach
    void setUp() throws Exception {
        filePath = dir.resolve("numbers.in");
        Files.writeString(filePath, "7\n");
        file = new ValueExp(new StringValue(filePath.toString()));
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void openRFileAddsToFileTableAndErrorsThrow() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(new StringValue(filePath.toString())));

        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(file).execute(state));

        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(new IntValue(123))).execute(state));

        ValueExp missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));

        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void readFileReadsInOrderThenZeroAndErrorsThrow() throws Exception {
        Path multi = dir.resolve("multi.in");
        Files.writeString(multi, "10\n25\n");
        ValueExp multiExp = new ValueExp(new StringValue(multi.toString()));

        new OpenRFileStmt(multiExp).execute(state);
        new ReadFileStmt(multiExp, "x").execute(state);
        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
        new ReadFileStmt(multiExp, "x").execute(state);
        assertEquals(new IntValue(25), state.getSymTable().lookup("x"));
        new ReadFileStmt(multiExp, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));

        // Undeclared variable
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(multiExp, "undeclared").execute(state));

        // Non-int variable
        new VarDeclStmt("s", new StringType()).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(multiExp, "s").execute(state));

        // File not open
        ValueExp unopened = new ValueExp(new StringValue(dir.resolve("unopened.in").toString()));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(unopened, "x").execute(state));

        // Non-integer line
        Path badLine = dir.resolve("bad.in");
        Files.writeString(badLine, "abc\n");
        ValueExp badExp = new ValueExp(new StringValue(badLine.toString()));
        new OpenRFileStmt(badExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badExp, "x").execute(state));

        new CloseRFileStmt(multiExp).execute(state);
        new CloseRFileStmt(badExp).execute(state);
    }

    @Test
    void closeRFileRemovesFromFileTableAndUnopenedThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().isDefined(new StringValue(filePath.toString())));
        assertThrows(RuntimeException.class,
                () -> new CloseRFileStmt(file).execute(state));
    }
}