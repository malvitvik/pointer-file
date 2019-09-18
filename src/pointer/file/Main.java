package pointer.file;

import java.util.*;
import java.util.function.BiConsumer;

public class Main {

    private static Map<Creature, BiConsumer<Manager, Scanner>> removeMap = new HashMap<>();
    private static Map<Creature, BiConsumer<Manager, Scanner>> addMap = new HashMap<>();

    static {
        removeMap.put(Creature.PERSON, (manager, scanner) ->
                manager.removePerson(createPerson(scanner)));
        removeMap.put(Creature.PET, (manager, scanner) ->
                manager.removePetFromPerson(createPerson(scanner), createPet(scanner)));
        removeMap.put(Creature.PETS, (manager, scanner) -> manager.removePetFromAllPersons(createPet(scanner)));

        addMap.put(Creature.PERSON, (manager, scanner) -> manager.addPerson(createPerson(scanner)));
        addMap.put(Creature.PET, (manager, scanner) -> manager.addPetToPerson(createPerson(scanner), createPet(scanner)));
    }

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
                        addMap.getOrDefault(Creature.fromString(scanner.next()),
                                (m, s) -> System.out.println("Cannot add."))
                                .accept(manager, scanner);
                        break;

                    case REMOVE:
                        removeMap.getOrDefault(Creature.fromString(scanner.next()),
                                (m, s) -> System.out.println("Cannot remove.")).accept(manager, scanner);
                        break;

                    case WRITE:
                        fileManager.writeToFile(manager.getZooClub(), scanner.nextBoolean());
                        break;

                    case SAVE:
                        fileManager.serializeToFile(manager);
                        break;

                    case RESTORE:
                        Manager restoredManager = fileManager.deserializeFromFile();

                        if (Objects.isNull(restoredManager)) {
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
        System.out.println("- add person <userName:String> <userAge:int>\n" +
                "- add pet <userName:String> <userAge:int> <petName:String> <petAge: int>\n" +
                "- remove person <userName:String> <userAge:int>\n" +
                "- remove pet <userName:String> <userAge:int> <petName:String> <petAge: int>\n" +
                "- remove pets <petName:String> <petAge: int>\n" +
                "- show all|file - show current zoo club OR read from txt file to console\n" +
                "- write true|false - append|rewrite txt file.\n" +
                "- save - serialize zoo club to file.\n" +
                "- restore - read from serialized file and add to zoo club.\n" +
                "- help\n" +
                "- exit");
    }

    private static Person createPerson(Scanner scanner) {
        return new Person(scanner.next(), scanner.nextInt());
    }

    private static Pet createPet(Scanner scanner) {
        return new Pet(scanner.next(), scanner.nextInt());
    }
}