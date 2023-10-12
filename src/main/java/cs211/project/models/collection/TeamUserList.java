package cs211.project.models.collection;

import cs211.project.models.TeamUser;

import java.util.ArrayList;

public class TeamUserList {
    ArrayList<TeamUser> teamUserList;

    public TeamUserList() { teamUserList = new ArrayList<>();}
    public void addNewTeamUser(String eventName, String teamName, String username){
        eventName = eventName.trim();
        teamName = teamName.trim();
        username = username.trim();
        if (!eventName.equals("") && !teamName.equals("") && !username.equals("")){
            TeamUser exist = findTeamUser(eventName, teamName, username);
            if (exist == null){
                teamUserList.add(new TeamUser(eventName, teamName, username));
            }
        }

    }
    public TeamUser findTeamUser(String eventName, String teamName, String username){
        for (TeamUser teamUser: teamUserList){
            if (teamUser.isEvent(eventName) && teamUser.isTeam(teamName) && teamUser.isUsername(username)){
                return teamUser;
            }
        }
        return null;
    }
    public  ArrayList<TeamUser> getTeamUser(){
        return teamUserList;
    }
}
