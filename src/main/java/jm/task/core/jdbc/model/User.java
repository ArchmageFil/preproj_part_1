package jm.task.core.jdbc.model;

import lombok.Data;

import javax.persistence.*;

@SuppressWarnings({"SpellCheckingInspection", "JpaDataSourceORMInspection"})
@Entity
@Table(name = "lesson_114", schema = "mydbtest")
@Data
public class User {
    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "age")
    private Byte age;

    public User() {

    }

    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public User(Long id, String name, String lastName, Byte age) {
        this(name, lastName, age);
        this.id = id;
    }
}

