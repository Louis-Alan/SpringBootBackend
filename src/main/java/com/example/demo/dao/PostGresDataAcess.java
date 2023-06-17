package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PostGresDataAcess implements PersonDao{

    @Autowired
    public PostGresDataAcess(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;
    @Override
    public int insertPerson(UUID id, Person person) {
        String sql = "INSERT INTO person (id, name) VALUES (?, ?)";
        return jdbcTemplate.update(sql, id, person.getName());
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id, name FROM person";
        return jdbcTemplate.query(sql, (resultSet, i)->{
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id, name);
        });
    }

    @Override
    public Optional<Person> selectPersonbyId(UUID id) {
        final String sql = "SELECT id, name FROM PERSON where id = ?";
        Person p = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i)->{
            UUID pId = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(pId, name);
        });

        return Optional.ofNullable(p);
    }

    @Override
    public int deletePersonbyId(UUID id) {
        final String sql = "DELETE FROM person WHERE id ='"+id+"'";
        return jdbcTemplate.update(sql);
    }

    @Override
    public int updatePersonbyId(UUID id, Person person) {
        final String sql = "UPDATE person set name='"+person.getName()+"' where id='"+id+"'";
        return jdbcTemplate.update(sql);
    }
}
