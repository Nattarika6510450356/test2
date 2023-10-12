package cs211.project.models;

public class TeamUser {
    String eventName;
    String teamName;
    String username;

    public TeamUser(String eventName, String teamName, String userName) {
        this.eventName = eventName;
        this.teamName = teamName;
        this.username = userName;
    }
    public String getEventName() { return eventName; }
    public String getTeamName() { return teamName; }
    public String getUserName() { return username; }
    public boolean isEvent(String eventName){
        return this.eventName.equals(eventName);
    }
    public boolean isTeam(String teamName){
        return this.teamName.equals(teamName);
    }
    public boolean isUsername(String username){
        return this.username.equals(username);
    }
}
