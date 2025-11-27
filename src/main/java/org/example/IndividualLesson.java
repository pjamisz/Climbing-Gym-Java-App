package org.example;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity(name = "individual_lesson")
public class IndividualLesson extends ClimbingLesson{

    @ManyToOne
    private Climber climber;

    private final double individualHourlyRate = 100.0;

    public IndividualLesson(){}
    public IndividualLesson(Instructor instructor, int duration, LocalDateTime dateTime,Climber climber) {
        super(instructor, duration, dateTime);
        this.setClimber(climber);
    }

    public Climber getClimber() {
        return this.climber;
    }

    public void setClimber(Climber climber) {
        if(climber == null){
            throw new IllegalArgumentException("Climber is null");
        }
        this.climber = climber;
    }


    public void removeClimber(Climber climber){
        if(this.climber == climber){
            this.climber = null;
            climber.removeIndividualLesson(this);
        }
    }
    public double getHourlyRate() {
        return individualHourlyRate;
    }

    //method polimorphism
    @Override
    public double calculateCost() {
        return (getDuration() / 60.0) * individualHourlyRate;
    }
}
