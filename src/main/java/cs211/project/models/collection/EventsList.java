package cs211.project.models.collection;

import cs211.project.models.Events;

import java.util.ArrayList;

public class EventsList {
    private ArrayList<Events> events;
    public EventsList() {
        events = new ArrayList<>();
    }

    public void addNewEvent(String eventName , String eventDetail, Integer maxSeat, Integer availableSeat, String startDate, String finishDate, String eventImagePath, String eventCreatorUsername, String status) {
        eventName = eventName.trim();
        eventDetail = eventDetail.trim();
        eventCreatorUsername = eventCreatorUsername.trim();
        if (!eventName.equals("") && !eventDetail.equals("") && maxSeat!=null && !startDate.equals("") && !finishDate.equals("") && !eventImagePath.equals("") && !eventCreatorUsername.equals("") ) {
            Events exist = findEventByName(eventName);
            if (exist == null) {
                events.add(new Events(eventName,eventDetail,maxSeat,availableSeat,startDate,finishDate,eventImagePath,eventCreatorUsername,status));
            }
        }
    }

    public Events findEventByName(String eventName) {
        for (Events event : events) {
            if (event.isEventName(eventName)) {
                return event;
            }
        }
        return null;
    }

    public Events findEventByUsername(String eventCreatorUsername){
        for (Events event: events){
            if (event.isCreatorUsername(eventCreatorUsername)){
                return event;
            }
        }
        return null;
    }

    public boolean isEmpty(){
        return events.isEmpty();
    }
    public ArrayList<Events> getEvents() {
        return events;
    }
    public String getEventNameByObj(Events events){
        return events.getEventName();
    }

}
