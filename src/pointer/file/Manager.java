package pointer.file;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Manager implements Serializable {
    private Map<Person, List<Pet>> zooClub = new HashMap<>();

    public Map<Person, List<Pet>> getZooClub() {
        return zooClub;
    }

    public void putAllToZooClub(Map<Person, List<Pet>> map) {
        this.zooClub.putAll(map);
    }

    public void addPerson(Person person) {
        if (zooClub.containsKey(person)) {
            System.out.println(person + " already exists in ZooClub.");
            return;
        }

        zooClub.put(person, new LinkedList<>());

        System.out.println(person + " is added.");
    }

    public void addPetToPerson(Person person, Pet pet) {
        if (hasNoPerson(person)) return;

        zooClub.get(person).add(pet);
        System.out.println(pet + " is added to " + person);
    }

    public void removePetFromPerson(Person person, Pet pet) {
        if (hasNoPerson(person)) return;

        zooClub.get(person).remove(pet);
        System.out.println(pet + " is removed from " + person);
    }

    public void removePerson(Person person) {
        if (hasNoPerson(person)) return;

        zooClub.remove(person);
        System.out.println(person + " is removed.");
    }

    public void removePetFromAllPersons(Pet pet) {
        zooClub.values().forEach(list -> list.remove(pet));
        System.out.println(pet + " is removed from all persons.");
    }

    private boolean hasNoPerson(Person person) {
        if (!zooClub.containsKey(person)) {
            System.out.println(person + " doesn't exist in Zoo");
            return true;
        }
        return false;
    }
}