package org.example;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "Worker")
public class Worker extends Person {

    private LocalDate dateOfEmployment;
    private int baseSalary = 3000;
    private String adress; //optional


    @ManyToMany
    private Set<Gym> gyms = new HashSet<>();

    public Worker(){}

    public Worker(String name){
        this.setName(name);
        this.setDateOfEmployment(LocalDate.now());
    }
    public Worker(String name,int baseSalary,LocalDate dateOfEmployment)
    {
        this.setName(name);
        this.setBaseSalary(baseSalary);
        this.setDateOfEmployment(dateOfEmployment);
    }


    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public int getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(int baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    //Adding and removing assciations with reverse links
    public void addGym(Gym gym) {
        this.gyms.add(gym);
        gym.getWorkers().add(this);
    }

    public void removeGym(Gym gym) {
        this.gyms.remove(gym);
        gym.getWorkers().remove(this);
    }
    public Set<Gym> getGyms(){
        return Collections.unmodifiableSet(this.gyms);
    }

    public int calculateSalary(){
        return baseSalary;
    }


}
