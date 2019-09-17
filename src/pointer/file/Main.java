package pointer.file;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {

    private static Scanner scanner;
    private static Map<String, Function<Manager, String>> showMap = new HashMap<>();
    private static Map<Creature, Consumer<Manager>> removeMap = new HashMap<>();
    private static Map<Creature, Consumer<Manager>> addMap = new HashMap<>();

    static {
        showMap.put("all", (manager) -> manager.getZooClub().toString());
        showMap.put("file", (manager) -> manager.getZooClub().toString());

        removeMap.put(Creature.PERSON, (manager) -> manager.removePerson(createPerson()));
        removeMap.put(Creature.PET, (manager) -> manager.removePetFromPerson(createPerson(), createPet()));
        removeMap.put(Creature.PETS, (manager) -> manager.removePetFromAllPersons(createPet()));

        addMap.put(Creature.PERSON, (manager) -> manager.addPerson(createPerson()));
        addMap.put(Creature.PET, (manager) -> manager.addPetToPerson(createPerson(), createPet()));
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        Manager manager = new Manager();
        FileManager fileManager = new FileManager("zooclub.txt", "zooclub.ser");

        help();
        Command command = Command.fromString(scanner);

        while (command != Command.EXIT) {
            try {
                switch (command) {
                    case SHOW:
                        String str = showMap.getOrDefault(scanner.next(), (m) -> "Nothing to show.")
                                .apply(manager);

                        System.out.println(str);
                        break;

                    case ADD:
                        addMap.getOrDefault(Creature.fromString(scanner.next()),
                                (m) -> System.out.println("Cannot add."))
                                .accept(manager);
                        break;

                    case REMOVE:
                        removeMap.getOrDefault(Creature.fromString(scanner.next()),
                                (m) -> System.out.println("Cannot remove.")).accept(manager);
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

    private static Person createPerson() {
        return new Person(scanner.next(), scanner.nextInt());
    }

    private static Pet createPet() {
        return new Pet(scanner.next(), scanner.nextInt());
    }
}