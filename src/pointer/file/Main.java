package pointer.file;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        FileManager fileManager = new FileManager("zooclub.txt", "zooclub.ser");

        help();
        Command command = Command.fromString(scanner);

        while (command != Command.EXIT) {
            try {
                switch (command) {
                    case SHOW:
                        String next = scanner.next();
                        if ("all".equalsIgnoreCase(next)) {
                            System.out.println(manager.getZooClub());
                            break;
                        } else if ("file".equalsIgnoreCase(next)) {
                            System.out.println(fileManager.readFromFile());
                            break;
                        }

                        System.out.println("Nothing to show.");
                        break;

                    case ADD:
                        Creature type = Creature.fromString(scanner.next());
                        switch (type) {
                            case PERSON:
                                manager.addPerson(new Person(scanner.next(), scanner.nextInt()));
                                break;
                            case PET:
                                manager.addPetToPerson(new Person(scanner.next(), scanner.nextInt()),
                                        new Pet(scanner.next(), scanner.nextInt()));
                                break;
                        }
                        break;

                    case REMOVE:
                        type = Creature.fromString(scanner.next());
                        switch (type) {
                            case PERSON:
                                manager.removePerson(new Person(scanner.next(), scanner.nextInt()));
                                break;
                            case PET:
                                manager.removePetFromPerson(new Person(scanner.next(), scanner.nextInt()),
                                        new Pet(scanner.next(), scanner.nextInt()));
                                break;
                            case PETS:
                                manager.removePetFromAllPersons(new Pet(scanner.next(), scanner.nextInt()));
                                break;
                        }
                        break;

                    case WRITE:
                        fileManager.writeToFile(manager.getZooClub().toString(), scanner.nextBoolean());
                        break;

                    case SAVE:
                        fileManager.serializeToFile(manager);
                        break;

                    case RESTORE:
                        Manager restoredManager = fileManager.deserializeFromFile();

                        if (restoredManager == null) {
                            System.out.println("ZooClub is not restored from file.");
                            break;
                        }

                        manager.putAllToZooClub(restoredManager.getZooClub());
                        System.out.println("ZooClub is restored from file.");
                        break;

                    case HELP:
                        help();
                        break;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Wrong command type");
                scanner.nextLine();
            } catch (InputMismatchException ex) {
                System.out.println("Wrong parameter type. Try to use integer.");
                scanner.nextLine();
            } finally {
                command = Command.fromString(scanner);
            }
        }
    }

    private static void help() {
        StringBuilder stringBuilder = new StringBuilder("- add person <userName:String> <userAge:int>\n")
                .append("- add pet <userName:String> <userAge:int> <petName:String> <petAge: int>\n")
                .append("- remove person <userName:String> <userAge:int>\n")
                .append("- remove pet <userName:String> <userAge:int> <petName:String> <petAge: int>\n")
                .append("- remove pets <petName:String> <petAge: int>\n")
                .append("- show all|file - show current zoo club OR read from txt file to console\n")
                .append("- write true|false - append|rewrite txt file.\n")
                .append("- save - serialize zoo club to file.\n")
                .append("- restore - read from serialized file and add to zoo club.\n")
                .append("- help\n")
                .append("- exit");
        System.out.println(stringBuilder);
    }
}
