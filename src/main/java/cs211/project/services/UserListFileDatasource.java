package cs211.project.services;
import cs211.project.models.User;
import cs211.project.models.collection.UserList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;

    public UserListFileDatasource(String directoryName, String fileName) {
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
    public UserList readData() {
        UserList users = new UserList();
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
                String username = data[0].trim();
                String name = data[1].trim();
                String password = data[2].trim();
                String rank = data[3].trim();
                String userImagePath = data[4].trim();
                String loginTime = data[5].trim();

                users.addNewUser(username, name, password, rank, userImagePath, loginTime);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

@Override
public void writeData(UserList data) {
    String filePath = directoryName + File.separator + fileName;
    File file = new File(filePath);
    FileWriter fileWriter = null;
    try {
        fileWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        for (User user: data.getUsers()){
            String line = user.getUsername() + ","
                        + user.getName() + ","
                        + user.getPassword() + ","
                        + user.getRank() + ","
                        + user.getUserImagePath() + ","
                        + user.getLoginTime();
            writer.append(line);
            writer.newLine();
        }
        writer.close();
    } catch (IOException e) {
        System.err.println("Cannot write " + filePath);
    }
}

}
