package cs211.project.models;

import javafx.collections.ObservableList;

public class TimeSchedule {
    private String activityName;
    private String activityDetail;
    private String activityStart;
    private String activityFinish;
    private String scheduleInEvent;
    private String inTeam;
    private String activityStatus; // active or finished

    public TimeSchedule(String activityName, String activityDetail, String activityStart, String activityFinish, String scheduleInEvent, String inTeam, String activityStatus) {
        this.activityName = activityName;
        this.activityDetail = activityDetail;
        this.activityStart = activityStart;
        this.activityFinish = activityFinish;
        this.scheduleInEvent = scheduleInEvent;
        this.inTeam = inTeam;
        this.activityStatus = activityStatus;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getActivityDetail() {
        return activityDetail;
    }

    public String getActivityStart() {
        return activityStart;
    }

    public String getActivityFinish() {
        return activityFinish;
    }

    public String getScheduleInEvent() {
        return scheduleInEvent;
    }

    public String getInTeam() {
        return inTeam;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public boolean isActivityName(String activityName) {
        return this.activityName.equals(activityName);
    }

    public void setActivityName(String activityNameText) {
        this.activityName = activityNameText;
    }

    public void setScheduleInEvent(String scheduleInEvent) {
        this.scheduleInEvent = scheduleInEvent;
    }

    public void setActivityDetail(String activityDetailTex) {
        this.activityDetail = activityDetailTex;
    }

    public void setActivityStart(String activityStartText) {
        this.activityStart = activityStartText;
    }

    public void setctivityFinish(String activityFinishText) {
        this.activityFinish = activityFinishText;
    }

    public TimeSchedule getActivities() {
        return null;
    }

    public void removeAll(ObservableList<TimeSchedule> selectedItems) {
    }
}
