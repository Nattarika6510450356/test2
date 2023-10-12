package cs211.project.models.collection;

import cs211.project.models.Team;

import java.util.ArrayList;

public class TeamList {
    private ArrayList<Team> teams;

    public TeamList() {
        teams = new ArrayList<>();
    }

    public void addNewTeam (String teamName, Integer teamMaxSeat, Integer availableSeat, String registrationOpenDate, String registrationCloseDate, String teamInEvent){
        teamName = teamName.trim();
        if (!teamName.equals("") && teamMaxSeat != null && availableSeat != null && !registrationOpenDate.equals("") && !registrationCloseDate.equals("")) {
            Team exist = findTeamByTeamName(teamName);
            if (exist == null) {
                teams.add(new Team(teamName, teamMaxSeat, availableSeat, registrationOpenDate, registrationCloseDate, teamInEvent));
            }
        }
    }

    public Team findTeamByTeamName (String teamName){
        for (Team team : teams) {
            if (team.isTeamName(teamName)) {
                return team;
            }
        }
        return null;
    }
    public boolean isEmpty () {
        return teams.isEmpty();
    }
    public ArrayList<Team> getEvents () {
        return teams;
    }

    public ArrayList<Team> getTeams () {
        return teams;
    }


}
