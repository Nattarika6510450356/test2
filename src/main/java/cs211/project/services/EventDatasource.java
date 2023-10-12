package cs211.project.services;

public interface EventDatasource<T> {
    T readData();
    void writeData(T data);
    void updateEventName(String oldEventName, String newEventNameText);

}