package cs211.project.services;

import cs211.project.models.TimeSchedule;
import cs211.project.models.collection.TimeScheduleList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TimeScheduleListFileDatasource implements EventDatasource<TimeScheduleList>{
    private String directoryName;
    private String fileName;
    private String oldEventName;
    private String newEventName;


    public TimeScheduleListFileDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted(){
        File file = new File(directoryName);
        if (!file.exists()){ file.mkdirs(); }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TimeScheduleList readData(){
        TimeScheduleList timeSchedule = new TimeScheduleList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream, StandardCharsets.UTF_8
        );

        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line = "";
        try {
            while ((line = buffer.readLine()) != null){
                if (line.equals("")) continue;
                String[] data = line.split(",");
                String activityName = data[0].trim();
                String activityDetail = data[1];
                String activityStart = data[2].trim();
                String activityFinish = data[3].trim();
                String scheduleInEvent = data[4].trim();
                String inTeam = data[5].trim();
                String activityStatus = data[6].trim();

                timeSchedule.addNewTimeSchedule(activityName, activityDetail, activityStart, activityFinish, scheduleInEvent, inTeam, activityStatus);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return timeSchedule;
    }

    @Override
    public void writeData(TimeScheduleList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (TimeSchedule timeSchedule : data.getTimeSchedules()) {
                // Check if the schedule's scheduleInEvent matches the old eventName
                if (timeSchedule.getScheduleInEvent().equals(oldEventName)) {
                    // Update the schedule's scheduleInEvent to the new eventName
                    timeSchedule.setScheduleInEvent(newEventName);
                }

                String line = timeSchedule.getActivityName() + ","
                        + timeSchedule.getActivityDetail() + ","
                        + timeSchedule.getActivityStart() + ","
                        + timeSchedule.getActivityFinish() + ","
                        + timeSchedule.getScheduleInEvent() + ","
                        + timeSchedule.getInTeam() + ","
                        + timeSchedule.getActivityStatus();
                writer.append(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }

    @Override
    public void updateEventName(String oldEventName, String newEventName) {
        TimeScheduleList timeSchedules = readData();
        boolean updated = false;

        for (TimeSchedule timeSchedule : timeSchedules.getTimeSchedules()) {
            if (timeSchedule.getScheduleInEvent().equals(oldEventName)) {
                timeSchedule.setScheduleInEvent(newEventName);
                updated = true;
            }
        }

        if (updated) {
            writeData(timeSchedules);
        }
    }

}
