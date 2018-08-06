package com.ezscrum.microservice.productbacklog;

import com.ezscrum.microservice.productbacklog.DatabbaseEnum.ProjectEnum;
import com.ezscrum.microservice.productbacklog.DatabbaseEnum.StoryEnum;
import com.ezscrum.microservice.productbacklog.DatabbaseEnum.TagEnum;
import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductBacklogTestData {
    public String projectName = "Sprint initialization";
    private JSONObject projectMockJson;
    private JSONObject storyMockJson;
    private JSONObject tagMockJson;
    private JSONObject checkProjectMockJson;
    private JSONObject updateProjectMockJson;

    public ProductBacklogTestData(){
        setCheckProjectMockJson();
        setUpdateProjectMockJson();
    }

    public class ProjectData{
        public static final String NAME = "Sprint initialization";
        public static final String DISPLAY_NAME = "SPRINT INITIALIZATION";
        public static final String COMMENT  = "This is our first sprint";
        public static final String PRODUCT_OWNER= "Dedy Kurniawan";
        public static final String ATTACH_MAX_SIZE = "2";
        public static final String UPDATED_NAME = "Updated Sprint Initialization";
        public static final String UPDATED_DISPLAY_NAME = " UPDATED SPRINT INITIALIZATION";
        public static final String UPDATED_COMMENT  = "Updated  this is our first sprint";
        public static final String UPDATED_PRODUCT_OWNER= "Updated Dedy Kurniawan";
        public static final String UPDATED_ATTACH_MAX_SIZE = "3";
    }
    public static class AvailableEndPoints{
        public static final String GET_SINGLE_PROJECT = "/project/get/";
        public static final String CREATE_PROJECT = "/project/create";
        public static final String CHECK_PROJECT_BY_NAME  = "/project/check_by_name";
        public static final String UPDATE_PROJECT  = "/project/update/";
        public static final String DELETE_PROJECT  = "/project/delete/";
        public static final String CREATE_STORY  = "/story/create/";
        public static final String UPDATE_STORY  = "/story/update/";
        public static final String DELETE_STORY  = "/story/delete/";
        public static final String CREATE_TAG = "/tag/create/";
        public static final String DELETE_TAG = "/tag/delete/";
        public static final String UPDATE_TAG = "/tag/update/";
        public static String createGetStoryInProjectUrl(String projectId, String storyId){
            return "/project/" +projectId+ "/story/" +storyId;
        }
        public static String createAttachTagUrl(String storyId, String tagId){
            return "/story/" +storyId+ "/attach_tag/" +tagId;
        }
        public static String createUnAttachTagUrl(String storyId, String tagId){
            return "/story/" +storyId+ "/un_attach_tag/" +tagId;
        }
        public static String createGetSingleTagUrl(String projectId, String tagName){
            return "/project/" +projectId+ "/tag/" +tagName;
        }
        public static String createRetrieveStoriesFilterUrl(String projectId, String filterType){
            return "/project/" +projectId+ "/stories/" +filterType;
        }
        public static String retrieveListOfTags(String projectId){
            return "/project/" +projectId+ "/tags/";
        }
    }
    public JSONObject getProjectMockJson() {
        return projectMockJson;
    }

    public JSONObject getUpdateProjectMockJson() {
        return updateProjectMockJson;
    }

    public JSONObject setProjectMockJson() {
        projectMockJson = new JSONObject();
        try {
            projectMockJson.put(ProjectEnum.NAME, this.projectName);
            projectMockJson.put(ProjectEnum.DISPLAY_NAME_CC, ProjectData.DISPLAY_NAME);
            projectMockJson.put(ProjectEnum.COMMENT, ProjectData.COMMENT);
            projectMockJson.put(ProjectEnum.PRODUCT_OWNER_CC, ProjectData.PRODUCT_OWNER);
            projectMockJson.put(ProjectEnum.ATTACH_MAX_SIZE_CC, ProjectData.ATTACH_MAX_SIZE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return projectMockJson;
    }

    public JSONObject setUpdateProjectMockJson() {
        updateProjectMockJson = new JSONObject();
        try {
            updateProjectMockJson.put(ProjectEnum.NAME, ProjectData.UPDATED_NAME);
            updateProjectMockJson.put(ProjectEnum.DISPLAY_NAME_CC, ProjectData.UPDATED_DISPLAY_NAME);
            updateProjectMockJson.put(ProjectEnum.COMMENT, ProjectData.UPDATED_COMMENT);
            updateProjectMockJson.put(ProjectEnum.PRODUCT_OWNER_CC, ProjectData.UPDATED_PRODUCT_OWNER);
            updateProjectMockJson.put(ProjectEnum.ATTACH_MAX_SIZE_CC, ProjectData.UPDATED_ATTACH_MAX_SIZE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return updateProjectMockJson;
    }

    public JSONObject getStoryMockJson() {
        return storyMockJson;
    }
    public void setStoryMockJson(String projectId){
        storyMockJson = new JSONObject();
        try {
            storyMockJson.put(StoryEnum.NAME, "first story in sprint initialization");
            storyMockJson.put(StoryEnum.ESTIMATE, 11);
            storyMockJson.put(StoryEnum.VALUE, 9);
            storyMockJson.put(StoryEnum.HOW_TO_DEMO_CC, "the exhibition will be held at 3rf floor");
            storyMockJson.put(StoryEnum.IMPORTANCE, 9);
            storyMockJson.put(StoryEnum.NOTES, "Dont forget to prepare the laptop");
            projectMockJson.put(ProjectEnum.ID, projectId);
            storyMockJson.put("project", projectMockJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setUpdateStoryMockJson(String projectId){
        storyMockJson = new JSONObject();
        try {
            storyMockJson.put(StoryEnum.NAME, "first story in sprint initialization updated");
            storyMockJson.put(StoryEnum.ESTIMATE, 12);
            storyMockJson.put(StoryEnum.VALUE, 8);
            storyMockJson.put(StoryEnum.HOW_TO_DEMO_CC, "the exhibition will be held at 6rf floor updated");
            storyMockJson.put(StoryEnum.IMPORTANCE, 10);
            storyMockJson.put(StoryEnum.NOTES, "Dont forget to prepare the everything");
            projectMockJson.put(ProjectEnum.ID, projectId);
            storyMockJson.put("project", projectMockJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTagMockJson(String projectId){
        tagMockJson = new JSONObject();
        try {
            tagMockJson.put(TagEnum.NAME, "Enhancements");
            projectMockJson.put(ProjectEnum.ID, projectId);
            tagMockJson.put("project", projectMockJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUpdateTagMockJson(String projectId){
        tagMockJson = new JSONObject();
        try {
            tagMockJson.put(TagEnum.NAME, "Bug Fixing");
            projectMockJson.put(ProjectEnum.ID, projectId);
            tagMockJson.put("project", projectMockJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getTagMockJson() {
        return tagMockJson;
    }

    public JSONObject getCheckProjectMockJson() {
        return checkProjectMockJson;
    }

    public void setCheckProjectMockJson() {
        checkProjectMockJson = new JSONObject();
        try {
            checkProjectMockJson.put(ProjectEnum.NAME, projectName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
