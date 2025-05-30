package vip.com.vipmeetings.models;

public class TimerModel {

    boolean isRefreshMobile;
    boolean isRefreshEvacuation;
    long timeRemaining;


    public boolean isRefreshMobile() {
        return isRefreshMobile;
    }

    public void setRefreshMobile(boolean refreshMobile) {
        isRefreshMobile = refreshMobile;
    }

    public boolean isRefreshEvacuation() {
        return isRefreshEvacuation;
    }

    public void setRefreshEvacuation(boolean refreshEvacuation) {
        isRefreshEvacuation = refreshEvacuation;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}
