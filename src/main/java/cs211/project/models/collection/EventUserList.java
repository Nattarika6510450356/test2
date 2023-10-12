package cs211.project.models.collection;

import cs211.project.models.EventUser;

import java.util.ArrayList;

public class EventUserList {
    private ArrayList<EventUser> eventUserLists;
    private EventUser eventUser;
    private String eventName;
    public EventUserList() {
        eventUserLists = new ArrayList<>();
    }
    public void addEventUser(String event , String user) {
        event = event.trim();
        user = user.trim();
        if (!event.equals("") && !user.equals("")) {
            EventUser exist = findEventUser(event, user);
            if (exist == null) {
                eventUserLists.add(new EventUser(event, user));
            }
        }
    }
    public EventUser findEventUser(String event, String user) {
        for (EventUser eventUser : eventUserLists) {
            if (eventUser.isEvent(event) && eventUser.isUser(user)) {
                return eventUser;
            }
        }
        return null;
    }

    public ArrayList<EventUser> getEventUser() {
        return eventUserLists;
    }
}
