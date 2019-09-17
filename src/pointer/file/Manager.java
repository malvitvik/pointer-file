package pointer.file;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Manager implements Serializable {
    private Map<Person, List<Pet>> zooClub = new HashMap<>();

    public Map<Person, List<Pet>> getZooClub() {
        return zooClub;
    }

    public void putAllToZooClub(Map<Person, List<Pet>> map) {
        this.zooClub.putAll(map);
    }

    public void addPerson(Person person) {
        zooClub.putIfAbsent(person, new LinkedList<>());
        System.out.println(person + " is added.");
    }

    public void addPetToPerson(Person person, Pet pet) {
        acceptForPerson(person, pets -> {
            pets.add(pet);
            System.out.println(pet + " is added to " + person);
        });
    }

    public void removePetFromPerson(Person person, Pet pet) {
        acceptForPerson(person, pets -> {
            pets.remove(pet);
            System.out.println(pet + " is removed from " + person);
        });
    }

    public void removePerson(Person person) {
        acceptForPerson(person, pets -> {
            zooClub.remove(person);
            System.out.println(person + " is removed.");
        });
    }

    public void removePetFromAllPersons(Pet pet) {
        zooClub.values().forEach(list -> list.remove(pet));
        System.out.println(pet + " is removed from all persons.");
    }

    private void acceptForPerson(Person person, Consumer<List<Pet>> petConsumer) {
        if (!zooClub.containsKey(person)) {
            System.out.println(person + " doesn't exist in Zoo");
            return;
        }

        petConsumer.accept(zooClub.get(person));
    }
}