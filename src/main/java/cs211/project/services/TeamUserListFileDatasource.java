package cs211.project.services;

import cs211.project.models.TeamUser;
import cs211.project.models.collection.TeamUserList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamUserListFileDatasource implements Datasource<TeamUserList>{
    private String directoryName;
    private String fileName;
    public TeamUserListFileDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted(){
        File file = new File(directoryName);
        if (!file.exists()){
            file.mkdirs();
        }
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
    public TeamUserList readData() {
        TeamUserList teamUserList = new TeamUserList();
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
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;
                String[] data = line.split(",");
                String eventName = data[0].trim();
                String teamName = data[1].trim();
                String username = data[2].trim();

                teamUserList.addNewTeamUser(eventName, teamName, username);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  teamUserList;
    }

    @Override
    public void writeData(TeamUserList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (TeamUser teamUser : data.getTeamUser()) {
                String eventName = teamUser.getEventName();
                String teamName = teamUser.getTeamName();
                String username = teamUser.getUserName();

                writer.write(eventName + "," + teamName + "," + username);
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Cannot write " + filePath);
        }
    }

}
