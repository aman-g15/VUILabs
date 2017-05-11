package ai.api.sample.model;

import java.util.Date;

/**
 * Created by amangupta on 11/05/17.
 */

public class CalendarBuilderObject {

    private String taskTitle;
    private String taskTodo;
    private Long date;
    private Long day;
    private Long month;
    private Long year;
    private Long hour;
    private Long minute;
    private Long second;

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }

    public Long getMinute() {
        return minute;
    }

    public void setMinute(Long minute) {
        this.minute = minute;
    }

    public Long getSecond() {
        return second;
    }

    public void setSecond(Long second) {
        this.second = second;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    private long time;

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskTodo() {
        return taskTodo;
    }

    public void setTaskTodo(String taskTodo) {
        this.taskTodo = taskTodo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
