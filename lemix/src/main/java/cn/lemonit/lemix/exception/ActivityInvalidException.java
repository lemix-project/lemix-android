package cn.lemonit.lemix.exception;

public class ActivityInvalidException extends Exception {

    public ActivityInvalidException(String activityName) {
        super("the activity is invalid: " + activityName);
    }
}
