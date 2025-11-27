package org.example;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    //Hibernate necessity
    Person(){}


    String name;
    LocalDate dateOfBirth;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Name can't be empty");
        }
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
