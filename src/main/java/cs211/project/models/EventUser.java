package cs211.project.models;

public class EventUser {
    private String event;
    private String user;

    public EventUser(String event, String user) {
        this.event = event;
        this.user = user;
    }

    public String getEvent() {
        return event;
    }

    public String getUser() {
        return user;
    }

    public boolean isEvent(String event) {
        return this.event.equals(event);
    }

    public boolean isUser(String user) {
        return this.user.equals(user);
    }
}
