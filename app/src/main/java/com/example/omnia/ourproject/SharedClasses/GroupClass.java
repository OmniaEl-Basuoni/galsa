package com.example.omnia.ourproject.SharedClasses;

import java.util.ArrayList;
import java.util.List;

public class GroupClass {
    private List<String> membersList=new ArrayList<>();
    private String GroupID;
    private String GroupName;
    private String GroupDescription;
    private String GroupDateDay;
    private String GroupDateTime;
    private int GroupCapicety;

    public GroupClass( String groupID, String groupName,
                      String groupDescription, String groupDateDay, String groupDateTime, int groupCapicety) {
        GroupID = groupID;
        GroupName = groupName;
        GroupDescription = groupDescription;
        GroupDateDay = groupDateDay;
        GroupDateTime = groupDateTime;
        GroupCapicety = groupCapicety;
    }

    public List<String> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<String> membersList) {
        this.membersList = membersList;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupDescription() {
        return GroupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        GroupDescription = groupDescription;
    }

    public String getGroupDateDay() {
        return GroupDateDay;
    }

    public void setGroupDateDay(String groupDateDay) {
        GroupDateDay = groupDateDay;
    }

    public String getGroupDateTime() {
        return GroupDateTime;
    }

    public void setGroupDateTime(String groupDateTime) {
        GroupDateTime = groupDateTime;
    }

    public int getGroupCapicety() {
        return GroupCapicety;
    }

    public void setGroupCapicety(int groupCapicety) {
        GroupCapicety = groupCapicety;
    }
}
