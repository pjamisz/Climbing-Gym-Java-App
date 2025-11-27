package org.example;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "Membership")
public class Membership {

    @ManyToOne
    private Climber climber;

    @ManyToOne
    private Gym gym;

    private LocalDate validityDate;
    private int cost;
    private boolean isPaid;

    public Membership(){}
    public Membership(Climber climber, Gym gym, LocalDate validityDate){

        if(climber == null){
            throw new IllegalArgumentException("Climber can't be null");
        }
        if (gym == null){
            throw new IllegalArgumentException("Gym can't be null");
        }
        if(validityDate == null){
            throw new IllegalArgumentException("Date can't be null");
        }
        this.climber = climber;
        this.gym = gym;
        this.validityDate = validityDate;

        gym.addMembership(this);
        climber.addMembership(this);

    }

    private Climber getClimber() {
        return climber;
    }

    private void setClimber(Climber climber) {
        this.climber = climber;
    }

    private Gym getGym() {
        return gym;
    }

    private void setGym(Gym gym) {
        this.gym = gym;
    }

    public LocalDate getValidityDate(){
        return validityDate;
    }
    public String getGymName(){
        return gym.getName();
    }
    public String getClimberName(){
        return climber.getName();
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isValid(){
        return isPaid && !validityDate.isBefore(LocalDate.now());
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
