package pointer.file;

import com.sun.istack.internal.NotNull;

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

    public void putAllToZooClub(@NotNull Map<Person, List<Pet>> map) {
        this.zooClub.putAll(map);
    }

    public void addPerson(@NotNull Person person) {
        zooClub.putIfAbsent(person, new LinkedList<>());
        System.out.println(person + " is added.");
    }

    public void addPetToPerson(@NotNull Person person, @NotNull Pet pet) {
        acceptForPerson(person, pets -> {
            pets.add(pet);
            System.out.println(pet + " is added to " + person);
        });
    }

    public void removePetFromPerson(@NotNull Person person, @NotNull Pet pet) {
        acceptForPerson(person, pets -> {
            pets.remove(pet);
            System.out.println(pet + " is removed from " + person);
        });
    }

    public void removePerson(@NotNull Person person) {
        acceptForPerson(person, pets -> {
            zooClub.remove(person);
            System.out.println(person + " is removed.");
        });
    }

    public void removePetFromAllPersons(@NotNull Pet pet) {
        zooClub.values().forEach(list -> list.remove(pet));
        System.out.println(pet + " is removed from all persons.");
    }

    private void acceptForPerson(@NotNull Person person, @NotNull Consumer<List<Pet>> petConsumer) {
        if (!zooClub.containsKey(person)) {
            System.out.println(person + " doesn't exist in Zoo");
            return;
        }

        petConsumer.accept(zooClub.get(person));
    }
}