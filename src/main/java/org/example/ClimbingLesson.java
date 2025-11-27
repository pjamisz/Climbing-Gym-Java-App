package org.example;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

@Entity(name = "climbing_lesson")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ClimbingLesson {

    @ManyToOne
    private Instructor instructor;

    private int duration;
    private LocalDateTime dateTime;

    private boolean isConfirmed = false;
    private boolean isPaid = false;
    private boolean isCancelled = false;


    public ClimbingLesson(){}
    public ClimbingLesson(Instructor instructor,int duration, LocalDateTime dateTime) {
        this.setInstructor(instructor);
        this.setDuration(duration);
        this.setDateTime(dateTime);
    }

    public Instructor getInstructor() {
        return this.instructor;
    }

    public void setInstructor(Instructor instructor){

        // Check if we have the information already
        if(this.instructor != instructor) {
            this.instructor = instructor;
            // Add the reverse connection
            instructor.addLesson(this);
        }

    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if(duration < 0 || duration > 720){
            throw new IllegalArgumentException("Lesson must be between 0 and 720 minutes");
        }
        this.duration = duration;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // abstract method to overload
    public abstract double calculateCost();


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
