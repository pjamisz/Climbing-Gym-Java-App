package org.example;

import javax.persistence.Entity;

@Entity(name = "receptionist_author")
//Multiinheritance using interfaces
public class ReceptionistAuthor extends Author implements ReceptionistRole{

    private int membershipsSold;

    public ReceptionistAuthor(){}
    public ReceptionistAuthor(String name, int membershipsSold){
        super(name);
        setMembershipsSold(membershipsSold);
    }

    @Override
    public int getMembershipsSold() {
        if(membershipsSold < 0) {
            throw new IllegalArgumentException("Number of memberships sold cant be negative");
        }
        return membershipsSold;
    }

    @Override
    public void setMembershipsSold(int membershipsSold) {
        if(membershipsSold < 0) {
            throw new IllegalArgumentException("Number of memberships sold cant be negative");
        }
        this.membershipsSold = membershipsSold;
    }


    @Override
    public int calculateSalary() {
        return getBaseSalary() + membershipsSold * 50 + getNumberOfRoutes() * 200;
    }


}
