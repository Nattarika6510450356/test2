package cs211.project.models;

public class TeamComment {
    private String username;
    private String eventName;
    private String teamName;
    private String activityName;
    private String comment;

    public TeamComment(String username, String eventName, String teamName, String activityName, String comment) {
        this.username = username;
        this.eventName = eventName;
        this.teamName = teamName;
        this.activityName = activityName;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getComment() {
        return comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public boolean isActivityName(String activityName){
        return this.activityName.equals(activityName);
    }
}
