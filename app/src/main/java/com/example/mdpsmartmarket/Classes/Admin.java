package com.example.mdpsmartmarket.Classes;

import java.util.ArrayList;

public class Admin extends User {

    public void addStaff(Staff s){
        Staff.add(s);
    }

    public void editStaff(Staff s){
        Staff.update(s);
    }

    public void deleteStaff(String staffUsername){
        Staff.delete(staffUsername);
    }

    public ArrayList<User> viewStaff(String staffUsername){
        return ((ArrayList<User>) Staff.view(staffUsername));
    }

}
