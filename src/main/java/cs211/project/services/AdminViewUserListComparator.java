package cs211.project.services;

import cs211.project.models.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class AdminViewUserListComparator implements Comparator<User> {
    private SimpleDateFormat loginTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    @Override
    public int compare(User user1, User user2) {
        try {
            Date loginDate1 = loginTimeFormat.parse(user1.getLoginTime());
            Date loginDate2 = loginTimeFormat.parse(user2.getLoginTime());
            return loginDate2.compareTo(loginDate1);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}