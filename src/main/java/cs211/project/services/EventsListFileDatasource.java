package cs211.project.services;

import cs211.project.models.Events;
import cs211.project.models.collection.EventsList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EventsListFileDatasource implements Datasource<EventsList> {
    private String directoryName;
    private String fileName;

    public EventsListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public EventsList readData() {
        EventsList events = new EventsList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;
                String[] data = line.split(",");
                String eventName = data[0].trim();
                String eventDetail = data[1].trim();
                Integer maxSeat = Integer.parseInt(data[2]);
                Integer availableSeat = Integer.parseInt(data[3]);
                String startDate = data[4].trim();
                String finishDate = data[5].trim();
                String eventImagePath = data[6].trim();
                String eventCreatorUsername = data[7].trim();
                String status = data[8].trim();

                events.addNewEvent(eventName , eventDetail, maxSeat, availableSeat, startDate, finishDate, eventImagePath, eventCreatorUsername, status);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return events;
    }

    @Override
    public void writeData(EventsList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (Events events: data.getEvents()){
                String line = events.getEventName() + ","
                        + events.getEventDetail() + ","
                        + events.getMaxSeat() + ","
                        + events.getAvailableSeat() + ","
                        + events.getStartDate() + ","
                        + events.getFinishDate()+ ","
                        + events.getEventImagePath()+ ","
                        + events.getEventCreatorUsername()+ ","
                        + events.getStatus();
                writer.append(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }

}

