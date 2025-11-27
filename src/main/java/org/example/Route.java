package org.example;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "Route")
public class Route {

    @NotNull
    private String routeName;
    private LocalDate dateOfSetting;


    @ManyToOne
    private Author author;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Restriction> restrictions = new ArrayList<>();
    private int routeGrade; // 1-10

    //for hibernate
    public Route(){}


    public Route(String name, int grade, LocalDate dateOfSetting) {

        this.setRouteName(name);
        this.setRouteGrade(grade);
        this.dateOfSetting = dateOfSetting;
    }

    //for composition
    public Restriction createRestriction(String desctription){
        Restriction restriction = new Restriction(desctription);
        restrictions.add(restriction);
        return restriction;
    }

    public List<Restriction> getRestrictions() {
        return Collections.unmodifiableList(restrictions);
    }

    public LocalDate getDateOfSetting() {
        return dateOfSetting;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {

        // Check if we have the information already
        if(this.author != author) {
            this.author = author;
            // Add the reverse connection
            author.addRoute(this);
        }

    }

    public void removeAuthor(Author author){
        if(this.author == null){
            return;
        }
        this.author = null;
        author.removeRoute(this);

    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String name) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException("Name can't be empty");
        }
        this.routeName = name;
    }

    public int getRouteGrade() {
        return routeGrade;
    }
    public void setRouteGrade(int grade) {

        if(grade < 1 || grade > 10){
            throw new IllegalArgumentException("Route grade must be between 1-10");
        }
        this.routeGrade = grade;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    //Kompozycja jako klasa wewnętrzna
    //Referencja do klasy Route nie jest potrzebna

    @Embeddable
    public static class Restriction {

        private String description;
        private LocalDate dateAdded;

        public Restriction(){}
        public Restriction(String description) {
            this.setDescription(description);
            this.dateAdded = LocalDate.now();
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            if(description == null){
                throw new IllegalArgumentException("Description can't be empty");
            }
            this.description = description;
        }

        public LocalDate getDateAdded() {
            return dateAdded;
        }

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
