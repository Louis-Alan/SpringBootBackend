package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePersonDataAccess implements PersonDao {

    private static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonbyId(UUID id) {
        return DB.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    @Override
    public int deletePersonbyId(UUID id) {
        Optional<Person> maybePerson = selectPersonbyId(id);
        if (maybePerson.isEmpty()) {
            return 0;
        }
        DB.remove(maybePerson.get());
        return 1;
    }

    @Override
    public int updatePersonbyId(UUID id, Person person) {
        return selectPersonbyId(id)
                .map(person1 -> {
                    int index = DB.indexOf(person1);
                    if (index >= 0) {
                        DB.set(index, new Person(id, person.getName()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
