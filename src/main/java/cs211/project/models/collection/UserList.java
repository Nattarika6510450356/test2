package cs211.project.models.collection;

import cs211.project.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public void addNewUser(String username, String name, String password,String rank, String userImagePath, String loginTime) { // write data with no rank and loginTime
        name = name.trim();
        username = username.trim();
        password = password.trim();
        rank = rank.trim();
        userImagePath = userImagePath.trim();
        if (!name.equals("") && !username.equals("") && !password.equals("")) {
            User exist = findUserByUsername(username);
            if (exist == null) {
                users.add(new User(username.trim(), name.trim(), password.trim(),rank, userImagePath, loginTime));
            }
        }
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.isUsername(username)) {
                return user;
            }
        }
        return null;
    }

    public void changeUserLoginTime(String username) {
        User exist = findUserByUsername(username);
        if (exist != null) {
            exist.setLoginTime();
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }



}