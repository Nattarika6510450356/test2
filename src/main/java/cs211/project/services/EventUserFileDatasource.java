package cs211.project.services;

import cs211.project.models.EventUser;
import cs211.project.models.Events;
import cs211.project.models.TeamUser;
import cs211.project.models.collection.EventUserList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventUserFileDatasource implements Datasource<EventUserList> {
    private String directoryName;
    private String fileName;

    public EventUserFileDatasource(String directoryName, String fileName) {
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
    public EventUserList readData() {
        EventUserList eventUserList = new EventUserList();
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
                String event = data[0].trim();
                String user = data[1].trim();

                eventUserList.addEventUser(event, user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return eventUserList;
    }

    @Override
    public void writeData(EventUserList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (EventUser eventUser: data.getEventUser()) {
                String line = eventUser.getEvent() + ","
                        + eventUser.getUser();
                writer.append(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }


}

