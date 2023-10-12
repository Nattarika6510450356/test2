package cs211.project.models;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
public class User {
    private String username;
    private String name;
    private String password;
    private String rank; //admin or user
    private String userImagePath;
    private String loginTime;

    public User(String username, String name, String password, String rank, String userImagePath, String loginTime) { // write data with no rank and loginTime
        this.username = username;
        this.name = name;
        this.password = password;
        this.rank = rank;
        this.userImagePath = userImagePath;
        this.loginTime = loginTime;
    }

    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getRank() {
        return rank;
    }
    public String getUserImagePath() { return userImagePath; }
    public String  getLoginTime() {
       return loginTime;
    }

    public void setLoginTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime tempTime = LocalDateTime.now();
        this.loginTime = tempTime.format(format);
    }
    public void setImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsername(String username) {
        return this.username.equals(username);
    }

    @Override
    public String toString() {
        return "";
    }

}
