package cs211.project.models.collection;

import cs211.project.models.TeamComment;

import java.util.ArrayList;

public class TeamCommentList {
    private ArrayList<TeamComment> teamComments;

    public TeamCommentList(){ teamComments = new ArrayList<>(); }

    public void addNewComment(String username, String eventName, String teamName, String activityName, String comment){
        if (!username.equals("") && !eventName.equals("") && !teamName.equals("") && !activityName.equals("") && !comment.equals("")){
            TeamComment exist = findActivityCommentByActivityName(activityName);
            teamComments.add(new TeamComment(username, eventName, teamName, activityName, comment));
        }
    }

    public TeamComment findActivityCommentByActivityName(String activityName){
        for (TeamComment teamComment: teamComments){
            if (teamComment.isActivityName(activityName)){
                return teamComment;
            }
        }
        return null;
    }

    public ArrayList<TeamComment> getTeamComments() {
        return teamComments;
    }
    public boolean isEmpty(){
        return teamComments.isEmpty();
    }
}

