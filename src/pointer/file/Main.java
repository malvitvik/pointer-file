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
                        }

                        if ("file".equalsIgnoreCase(next)) {
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
                        boolean rewrite = scanner.nextBoolean();

                        if (rewrite) {
                            fileManager.appendToFile(manager.getZooClub().toString());
                            System.out.println("Data are appended to file.");
                            break;
                        }

                        fileManager.rewriteFile(manager.getZooClub().toString());
                        System.out.println("File is rewritten.");
                        break;

                    case SAVE:
                        fileManager.serializeToFile(manager);
                        System.out.println("ZooClub is saved to file");
                        break;

                    case RESTORE:
                        Manager restoredManager = fileManager.deserializeFromFile();
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
            } catch (InputMismatchException imex) {
                System.out.println("Wrong parameter type. Try to use integer.");
                scanner.nextLine();
            } finally {
                command = Command.fromString(scanner);
            }
        }
    }

    private static void help() {
        System.out.println("- add person <userName:String> <userAge:int>");
        System.out.println("- add pet <userName:String> <userAge:int> <petName:String> <petAge: int>");
        System.out.println("- remove person <userName:String> <userAge:int>");
        System.out.println("- remove pet <userName:String> <userAge:int> <petName:String> <petAge: int>");
        System.out.println("- remove pets <petName:String> <petAge: int>");
        System.out.println("- show all|file - show current zoo club OR read from txt file to console");
        System.out.println("- write true|false - append|rewrite txt file.");
        System.out.println("- save - serialize zoo club to file.");
        System.out.println("- restore - read from serialized file and add to zoo club.");
        System.out.println("- help");
        System.out.println("- exit");
    }
}
