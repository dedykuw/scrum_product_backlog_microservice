package com.ezscrum.microservice.productbacklog.DatabbaseEnum;

public class StoryEnum {
    public static final String TABLE_NAME = "Story";
    public static final String ID = "id";
    public static final String SERIAL_ID = "serial_id";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String ESTIMATE = "estimate";
    public static final String IMPORTANCE = "importance";
    public static final String VALUE = "value";
    public static final String NOTES = "notes";
    public static final String HOW_TO_DEMO = "how_to_demo";
    public static final String HOW_TO_DEMO_CC = "howToDemo";
    public static final String PROJECT_ID = "project_id";
    public static final String SPRINT_ID = "sprint_id";
    public static final String SPRINT_SERIAL_ID = "sprint_serial_id";
    public static final String CREATE_TIME = "created_time";
    public static final String UPDATE_TIME = "updated_time";

    public final static int STATUS_UNCHECK = StatusEnum.NEW;
    public final static int STATUS_DONE = StatusEnum.CLOSED;
    public final static int NO_PARENT = -1;
    public final static Long DEFAULT_VALUE = Long.valueOf("-1");
}
