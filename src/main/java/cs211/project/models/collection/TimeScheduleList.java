package cs211.project.models.collection;

import cs211.project.models.TimeSchedule;

import java.util.ArrayList;

public class TimeScheduleList {
    private ArrayList<TimeSchedule> timeSchedules;
    public TimeScheduleList(){
        timeSchedules = new ArrayList<>();
    }

    public void addNewTimeSchedule(String activityName, String activityDetail, String activityStart, String activityFinish, String scheduleInEvent, String inTeam,String actvityStatus){
        activityName = activityName.trim();
        if (!activityName.equals("") && !activityDetail.equals("") && !activityStart.equals("") && !activityFinish.equals("")){
            TimeSchedule exist = findActivityByName(activityName);
            if (exist == null){
                timeSchedules.add(new TimeSchedule(activityName, activityDetail, activityStart, activityFinish, scheduleInEvent, inTeam, actvityStatus));
            }
        }
    }

    public TimeSchedule findActivityByName(String activityName){
        for (TimeSchedule timeSchedule: timeSchedules){
            if (timeSchedule.isActivityName(activityName)){
                return timeSchedule;
            }
        }
        return null;
    }

    public boolean isEmpty(){
        return timeSchedules.isEmpty();
    }

    public ArrayList<TimeSchedule> getTimeSchedules(){ return timeSchedules; }

}
