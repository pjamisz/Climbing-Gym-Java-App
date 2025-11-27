package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Receptionist")
public class Receptionist extends Worker implements ReceptionistRole {

    private int membershipsSold;


    public Receptionist() {}

    public Receptionist(String name, int membershipsSold) {
        this.setName(name);
        this.setMembershipsSold(membershipsSold);
    }


    public int getMembershipsSold() {
        if(membershipsSold < 0) {
            throw new IllegalArgumentException("Number of memberships sold cant be negative");
        }
        return membershipsSold;
    }

    public void setMembershipsSold(int membershipsSold) {
        if(membershipsSold < 0) {
            throw new IllegalArgumentException("Number of memberships sold cant be negative");
        }
        this.membershipsSold = membershipsSold;
    }


    @Override
    public int calculateSalary() {
        return getBaseSalary() + membershipsSold * 50;
    }

}