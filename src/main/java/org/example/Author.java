package org.example;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "Author")
public class Author extends Worker{


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Route> routes = new HashSet<>();

    //for hibernate
    public Author(){}

    //overloaded constructors
    public Author(String name) {
        super(name);
    }

    public Author(String name, int baseSalary, LocalDate dateOfEmployment)
    {
        super(name,baseSalary, dateOfEmployment);
    }


    //Adding and removing assciations with reverse links
    public void addRoute(Route route) {
        this.routes.add(route);
        route.setAuthor(this);
    }

    public void removeRoute(Route route) {
        this.routes.remove(route);
        route.setAuthor(null);
    }

    //Unmodifiable set for safety
    public Set<Route> getRoutes(){
        return Collections.unmodifiableSet(this.routes);
    }

    @Override
    public int calculateSalary(){
        return getBaseSalary() + getNumberOfRoutes() * 200;
    }
    //Derivative attribute
    int getNumberOfRoutes(){
        return routes.size();
    }


}
