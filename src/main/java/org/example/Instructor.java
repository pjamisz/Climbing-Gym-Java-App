package org.example;

import javax.persistence.*;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Instructor")
public class Instructor extends Worker {

    private boolean speaksEnglish;
    private String licenceId;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ClimbingLesson> lessons = new HashSet<>();

    public Instructor() {}

    public Instructor(String name){
        super(name);
    }
    public Instructor(String name, int baseSalary, LocalDate dateOfEmployment, boolean speaksEnglish,String licenceId) {
        super(name,baseSalary,dateOfEmployment);
        this.setSpeaksEnglish(speaksEnglish);
        this.setLicenceId(licenceId);
    }

    //Adding and removing assciations with reverse links
    public void addLesson(ClimbingLesson lesson) {
        if(!lessons.contains(lesson)){
            this.lessons.add(lesson);
            lesson.setInstructor(this);
        }
    }

    public void removeLesson(ClimbingLesson lesson) {

        if(lessons.contains(lesson)){
            this.lessons.remove(lesson);
            lesson.setInstructor(null);
        }
    }

    public Set<ClimbingLesson> getLessons(){
        return Collections.unmodifiableSet(this.lessons);
    }

    public String getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(String licenceId) {

        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Licence id cant be empty");
        }
        this.licenceId = licenceId;
    }
    public boolean isSpeaksEnglish() {
        return speaksEnglish;
    }

    public void setSpeaksEnglish(boolean speaksEnglish) {
        this.speaksEnglish = speaksEnglish;
    }


    //derivative attribute
    public int getNumberOfLessons() {
        return lessons.size();
    }

    public void confirmLesson(ClimbingLesson climbingLesson) {
        if(lessons.contains(climbingLesson)){
            climbingLesson.setConfirmed(true);
        }else{
            throw new IllegalArgumentException("That lesson is not taught by this instructor");
        }
    }

    public boolean isEnglishSpeaker() {
        return speaksEnglish;
    }


    @Override
    public int calculateSalary() {
        return getBaseSalary() + getNumberOfLessons() * 100;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}