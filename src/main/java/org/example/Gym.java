package org.example;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Gym")
public class Gym {

    private String name;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();


    @ManyToMany(mappedBy = "gyms")
    private Set<Worker> workers = new HashSet<Worker>();

    public Gym(){}
    public Gym(String name){

        this.setName(name);
    }

    public void setName(String name){
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException("Name can't be empty");
        }
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void addMembership(Membership membership){
        if(membership == null){
            throw new IllegalArgumentException("Memebership can't be null");
        }
        memberships.add(membership);


    }

    //public List<Membership> getMemberships(){
        //return Collections.unmodifiableList( this.memberships);
    //}
    private List<Membership> getMemberships() {
        return Collections.unmodifiableList(this.memberships);
    }


    public void addWorker(Worker worker) {
        this.workers.add(worker);
        worker.getGyms().add(this);
    }

    public void removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.getGyms().remove(this);
    }

    public Set<Worker> getWorkers(){
        return this.workers;
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
