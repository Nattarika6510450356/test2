package cs211.project.services;

import cs211.project.models.TeamComment;
import cs211.project.models.collection.TeamCommentList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamCommentListFileDatasource implements Datasource<TeamCommentList>{
    private String directoryName;
    private String fileName;

    public TeamCommentListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileExisted();
    }
    private void checkFileExisted(){
        File file = new File(directoryName);
        if (!file.exists()){ file.mkdirs(); }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TeamCommentList readData() {
        TeamCommentList teamComments = new TeamCommentList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
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
                String username = data[0];
                String eventName = data[1].trim();
                String teamName = data[2].trim();
                String activityName = data[3].trim();
                String comment = data[4];

                teamComments.addNewComment(username, eventName, teamName, activityName, comment);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return teamComments;
    }

    @Override
    public void writeData(TeamCommentList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try (FileWriter fileWriter = new FileWriter(file,true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            for (TeamComment teamComment : data.getTeamComments()) {
                String line = teamComment.getUsername() + ","
                        + teamComment.getEventName() + ","
                        + teamComment.getTeamName() + ","
                        + teamComment.getActivityName() + ","
                        + teamComment.getComment();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}





