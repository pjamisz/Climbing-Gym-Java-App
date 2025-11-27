package org.example;

import javax.persistence.*;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "Climber")
public class Climber extends Person {

    private LocalDate dateOfRegistration;

    @OneToMany(mappedBy = "climber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "climber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndividualLesson> individualLessons = new ArrayList<>();

    @ManyToMany
    private Set<GroupLesson> groupLessons = new HashSet<>();

    public Climber(){}
    public Climber(String name, LocalDate date){
        this.setName(name);
        this.setDateOfRegistration(date);
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public void addMembership(Membership membership){

        if(membership == null){
            throw new IllegalArgumentException("Membership can't be null");
        }
        memberships.add(membership);

    }

    public void addGroupLesson(GroupLesson groupLesson){

        if(groupLesson == null){
            throw new IllegalArgumentException("Group Lesson can't be null");
        }
        if(!groupLessons.contains(groupLesson)){
            this.groupLessons.add(groupLesson);
            groupLesson.addClimber(this);
        }


    }

    public void removeGroupLesson(GroupLesson lesson){
        if(groupLessons.contains(lesson)){
            this.groupLessons.remove(lesson);
            lesson.removeClimber(this);}
    }

    public void addIndividualLesson(IndividualLesson individualLesson){

        if(individualLesson == null){
            throw new IllegalArgumentException("Individual lesson can't be null");
        }
        if(individualLessons.contains(individualLesson)){
            individualLessons.add(individualLesson);
            individualLesson.setClimber(this);
        }

    }
    public void removeIndividualLesson(IndividualLesson lesson){
        if(individualLessons.contains(lesson))
        {   this.individualLessons.remove(lesson);
            lesson.removeClimber(this);
        }

    }

    public Set<GroupLesson> getGroupLessons() {
        return Collections.unmodifiableSet(groupLessons);
    }


    public List<IndividualLesson> getIndividualLessons() {
        return Collections.unmodifiableList(individualLessons);
    }


    public List<Membership> getMemberships(){
            return Collections.unmodifiableList( this.memberships);
    }

    public boolean isUnderage(){
        return this.dateOfBirth.isAfter(LocalDate.now().minusYears(18));
    }

}
