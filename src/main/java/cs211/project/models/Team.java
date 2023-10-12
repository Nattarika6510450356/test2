package cs211.project.models;

public class Team {

    private String teamName;
    private Integer teamMaxSeat;
    private Integer availableSeat;
    private String registrationOpenDate;
    private String registrationCloseDate;
    private String teamInEvent; //eventName

    public Team(String teamName, int teamMaxSeat, int availableSeat, String registrationOpenDate, String registrationCloseDate, String teamInEvent) {
        this.teamName = teamName;
        this.teamMaxSeat = teamMaxSeat;
        this.availableSeat = availableSeat;
        this.registrationOpenDate = registrationOpenDate;
        this.registrationCloseDate = registrationCloseDate;
        this.teamInEvent = teamInEvent;
    }

    public String getTeamName() { return teamName; }

    public int getTeamMaxSeat() { return teamMaxSeat; }

    public String getRegistrationOpenDate() { return registrationOpenDate; }

    public String getRegistrationCloseDate() { return registrationCloseDate; }

    public String getTeamInEvent() { return teamInEvent; }

    public Integer getAvailableSeat() { return availableSeat; }
    public void setAvailableSeat(Integer availableSeat) {
        this.availableSeat = this.availableSeat;
    }

    public void setTeamInEvent(String teamInEvent) {
        this.teamInEvent = teamInEvent;
    }

    public void userJoin() {
        availableSeat--;
    }
    public void userBan() {
        availableSeat++;
    }
    public boolean isTeamName(String teamName){
        return this.teamName.equals(teamName);
    }
}
