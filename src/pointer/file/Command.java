package pointer.file;

import java.util.Scanner;

public enum Command {
    ADD, REMOVE, HELP, SHOW, SAVE, RESTORE, WRITE, EXIT;

    public static Command fromString(Scanner scanner) {
        try {
            return valueOf(scanner.next().toUpperCase());
        } catch (IllegalArgumentException ex) {
            System.out.println("Wrong command");
            scanner.nextLine();
            return fromString(scanner);
        }
    }
}
