package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.state.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;

import java.util.Scanner;

public class TextMenu {
    private IStmt selectedProgram = null;

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Toy Language Interpreter ---");
            System.out.println("1. Select a program");
            System.out.println("2. Run selected program");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> selectProgram(scanner);
                case "2" -> runSelectedProgram();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void selectProgram(Scanner scanner) {
        System.out.println("Available programs:");
        System.out.println("  1: " + Examples.EX1);
        System.out.println("  2: " + Examples.EX2);
        System.out.println("  3: " + Examples.EX3);
        System.out.print("Select program (1-3): ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                selectedProgram = Examples.EX1;
                System.out.println("Selected Ex1.");
            }
            case "2" -> {
                selectedProgram = Examples.EX2;
                System.out.println("Selected Ex2.");
            }
            case "3" -> {
                selectedProgram = Examples.EX3;
                System.out.println("Selected Ex3.");
            }
            default -> System.out.println("Invalid program number.");
        }
    }

    private void runSelectedProgram() {
        if (selectedProgram == null) {
            System.out.println("Please select a program first (option 1).");
            return;
        }
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                selectedProgram
        );
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo, true);

        try {
            controller.allStep();
            System.out.println("Final Out: " + state.getOut());
        } catch (MyException e) {
            System.out.println("Execution error: " + e.getMessage());
        }
    }
}