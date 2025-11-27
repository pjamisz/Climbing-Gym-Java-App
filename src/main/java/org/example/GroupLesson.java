package org.example;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "group_lesson")
public class GroupLesson extends ClimbingLesson{
    private int numberOfClimbers;
    private final int maxNumberofCLimbers = 20;
    private final double hourlyCost = 50.0;

    @ManyToMany
    private Set<Climber> climbers = new HashSet<>();

    public GroupLesson(){}
    public GroupLesson(Instructor instructor, int duration, int numberOfClimbers, LocalDateTime dateTime) {
        super(instructor, duration, dateTime);
        this.setNumberOfClimbers(numberOfClimbers);
    }

    //Adding and removing assciations with reverse links
    public void addClimber(Climber climber) {
        if (!this.climbers.contains(climber)) {
            this.climbers.add(climber);
            climber.addGroupLesson(this);
        }

    }

    public void removeClimber(Climber climber){
        if (!this.climbers.contains(climber)) {
            this.climbers.remove(climber);
            climber.removeGroupLesson(this);
            }
    }
    public Set<Climber> getClimbers() {
        return Collections.unmodifiableSet(climbers);
    }


    public double getHourlyCost() {
        return hourlyCost;
    }

    public int getNumberOfClimbers() {
        return numberOfClimbers;
    }

    public void setNumberOfClimbers(int numberOfClimbers) {
        if(numberOfClimbers < 1 || numberOfClimbers > maxNumberofCLimbers){
            throw new IllegalArgumentException("Group lesson must have between 1 and " + maxNumberofCLimbers +" participants");
        }
        this.numberOfClimbers = numberOfClimbers;
    }

    @Override
    public double calculateCost() {
        return (this.getDuration() / 60.0) * hourlyCost * numberOfClimbers;
    }
}
