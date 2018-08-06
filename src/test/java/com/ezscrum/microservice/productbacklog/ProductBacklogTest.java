package com.ezscrum.microservice.productbacklog;

import com.ezscrum.microservice.productbacklog.DatabbaseEnum.ProjectEnum;
import com.ezscrum.microservice.productbacklog.DatabbaseEnum.StoryEnum;
import com.ezscrum.microservice.productbacklog.DatabbaseEnum.TagEnum;
import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Repositories.ProjectRepository;
import com.ezscrum.microservice.productbacklog.Repositories.StoryRepository;
import com.ezscrum.microservice.productbacklog.Repositories.TagRepository;
import com.ezscrum.microservice.productbacklog.Services.Story.StoryServiceImpl;
import com.ezscrum.microservice.productbacklog.Support.Filter.FilterEnum;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import groovy.util.slurpersupport.GPathResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Filter;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ProductBacklogTest {

    private ProductBacklogTestData productBacklogTestData;
    private ProductBacklogTestData.AvailableEndPoints availableEndPoints;
    private Integer projectId;
    @Before
    public void setUp(){
        productBacklogTestData = new ProductBacklogTestData();
        availableEndPoints = new ProductBacklogTestData.AvailableEndPoints();

        productBacklogTestData.setProjectMockJson();
        JSONObject projectJson = productBacklogTestData.getProjectMockJson();
        projectId = getProjectId(projectJson);
    }
    @After
    public void tearDown(){
        tagRepository.deleteAll();
        storyRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private StoryServiceImpl storyService;

    //the client check the project is not exist, by make a post request with project name
    @Test
    public void checkProjectNotExistByName(){
        Response response =
                given().port(port).contentType("application/json").body(productBacklogTestData.getCheckProjectMockJson().toString())
                        .when().post(availableEndPoints.CHECK_PROJECT_BY_NAME)
                        .then().extract().response();
        Assert.assertFalse(Boolean.valueOf(response.toString()));
    }

    //client makes a post request to /project/create a project
    @Test
    public void createProjectTest(){
        productBacklogTestData.setProjectMockJson();
        Integer projectId = getProjectId(productBacklogTestData.getProjectMockJson());
        assertNotNull(projectId);
    }

    //client makes a post request to /project/create a project minus project name & display name- resulsting in failed status
    @Test
    public void createProjectTestWithWrongParams(){
        JSONObject projectData = productBacklogTestData.setProjectMockJson();
        projectData.remove(ProjectEnum.NAME);
        Integer statusCode = createProject(projectData.toString()).getStatusCode();
        assertTrue("Test passed, failing to create a project with blank name", statusCode ==  400);
        projectData.remove(ProjectEnum.DISPLAY_NAME_CC);
        statusCode = createProject(projectData.toString()).getStatusCode();
        assertTrue("Test passed, failing to create a project with blank name", statusCode ==  400);
    }

    //the client retrieves the existing project with spesific id
    @Test
    public void retrieveSingleProjectWithId(){
        String url = availableEndPoints.GET_SINGLE_PROJECT + projectId.toString();
                given().port(port)
                        .when()
                            .get(url)
                        .then()
                            .contentType(JSON)
                            .statusCode(200)
                            .body(ProjectEnum.NAME, equalTo(ProductBacklogTestData.ProjectData.NAME))
                            .body(ProjectEnum.DISPLAY_NAME_CC, equalTo(ProductBacklogTestData.ProjectData.DISPLAY_NAME))
                            .body(ProjectEnum.COMMENT, equalTo(ProductBacklogTestData.ProjectData.COMMENT))
                            .body(ProjectEnum.PRODUCT_OWNER_CC, equalTo(ProductBacklogTestData.ProjectData.PRODUCT_OWNER));
    }
    //test to retrieve the not exist project, the system should return 404 not found
    @Test
    public void testProjectNotExist(){
        String url = availableEndPoints.GET_SINGLE_PROJECT + "23213424";
                given().port(port)
                        .when()
                            .get(url)
                        .then()
                            .statusCode(404);
    }
    //test to retrieve the not exist story,  the system should return 404 not found
    @Test
    public void testStoryNotExist(){
        String url = availableEndPoints.createGetStoryInProjectUrl("1234", "11432");
                given().port(port)
                        .when()
                            .get(url)
                        .then()
                            .statusCode(404);
    }
    //test to retrieve the not exist tag, the system should return 404 not found
    @Test
    public void testTagNotExist(){
        String url = availableEndPoints.createGetSingleTagUrl("1234", "11432");
                given().port(port)
                        .when()
                            .get(url)
                        .then()
                            .statusCode(404);
    }
    //the client  updates the existing project data with spesific id
    @Test
    public void updateSingleProjectWithId(){
        productBacklogTestData.setUpdateProjectMockJson();
        String url = availableEndPoints.UPDATE_PROJECT + projectId.toString();
                given().port(port).contentType("application/json").body(productBacklogTestData.getUpdateProjectMockJson().toString())
                        .when().put(url)
                        .then()
                            .contentType(JSON)
                            .statusCode(200)
                            .body(ProjectEnum.NAME, equalTo(ProductBacklogTestData.ProjectData.UPDATED_NAME))
                            .body(ProjectEnum.DISPLAY_NAME_CC, equalTo(ProductBacklogTestData.ProjectData.UPDATED_DISPLAY_NAME))
                            .body(ProjectEnum.COMMENT, equalTo(ProductBacklogTestData.ProjectData.UPDATED_COMMENT))
                            .body(ProjectEnum.PRODUCT_OWNER_CC, equalTo(ProductBacklogTestData.ProjectData.UPDATED_PRODUCT_OWNER));
    }
    //the client is failed to update the existing project data with wrong params
    @Test
    public void updateSingleProjectWithWrongParams(){
        JSONObject updateData = productBacklogTestData.getUpdateProjectMockJson();
        updateData.remove(ProjectEnum.NAME);
        String url = availableEndPoints.UPDATE_PROJECT + projectId.toString();
                given().port(port).contentType("application/json").body(updateData.toString())
                        .when().put(url)
                        .then()
                            .contentType(JSON)
                            .statusCode(400);
    }
    //the client removes the project data with spesific id
    @Test
    public void removeSingleProjectWithId(){
        String url = availableEndPoints.DELETE_PROJECT + projectId.toString();
                given().port(port)
                        .when().delete(url)
                        .then().statusCode(200);
    }
    //the client sends a post to create a story inside the existing project
    @Test
    public void createSingleStoryTest(){
        productBacklogTestData.setStoryMockJson(projectId.toString());
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();
        Response response = createSingleStory(storyJSON.toString());
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
        try {
            assertEquals("Test name", storyJSON.get(StoryEnum.NAME), response.path(StoryEnum.NAME));
            assertEquals("Test how to demo", storyJSON.get(StoryEnum.HOW_TO_DEMO_CC), response.path(StoryEnum.HOW_TO_DEMO_CC));
            assertEquals("Test importance value", storyJSON.get(StoryEnum.IMPORTANCE), response.path(StoryEnum.IMPORTANCE));
            assertEquals("Test estimate value", storyJSON.get(StoryEnum.ESTIMATE), response.path(StoryEnum.ESTIMATE));
            assertEquals("Test estimate value", storyJSON.get(StoryEnum.VALUE), response.path(StoryEnum.VALUE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //create a story with wrong paramaters
    @Test
    public void createSingleStoryTestWithWrongParamaters(){
        productBacklogTestData.setStoryMockJson(projectId.toString());
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();
        storyJSON.remove(StoryEnum.VALUE);
        storyJSON.remove(StoryEnum.NAME);
        storyJSON.remove(StoryEnum.IMPORTANCE);
        Response response = createSingleStory(storyJSON.toString());
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 400", 400, response.getStatusCode());

    }
    //update  a story with wrong parameters
    @Test
    public void updateSingleStoryTestWithWrongParams(){
        Integer storyId = getStoryId(projectId);

        productBacklogTestData.setUpdateStoryMockJson(projectId.toString());
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();
        storyJSON.remove(StoryEnum.VALUE);
        storyJSON.remove(StoryEnum.NAME);
        storyJSON.remove(StoryEnum.IMPORTANCE);

        String url = availableEndPoints.UPDATE_STORY + storyId.toString();
        given().port(port).contentType("application/json").body(storyJSON.toString())
                .when().put(url)
                .then()
                .contentType(JSON)
                .statusCode(400);
    }

    //update  a story with wrong paramss
    @Test
    public void updateSingleStoryTest(){

        Integer storyId = getStoryId(projectId);
        productBacklogTestData.setUpdateStoryMockJson(projectId.toString());
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();

        String url = availableEndPoints.UPDATE_STORY + storyId.toString();
        Response response = given().port(port).contentType("application/json").body(storyJSON.toString())
                                    .when().put(url)
                                    .then().contentType(JSON)
                                    .extract().response();
        assertEquals("Response should 200", 200, response.getStatusCode());
        try {
            assertEquals("Test update name", storyJSON.get(StoryEnum.NAME), response.path(StoryEnum.NAME));
            assertEquals("Test update how to demo", storyJSON.get(StoryEnum.HOW_TO_DEMO_CC), response.path(StoryEnum.HOW_TO_DEMO_CC));
            assertEquals("Test update importance value", storyJSON.get(StoryEnum.IMPORTANCE), response.path(StoryEnum.IMPORTANCE));
            assertEquals("Test update estimate value", storyJSON.get(StoryEnum.ESTIMATE), response.path(StoryEnum.ESTIMATE));
            assertEquals("Test update estimate value", storyJSON.get(StoryEnum.VALUE), response.path(StoryEnum.VALUE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //retrieve single story with id
    @Test
    public void retrieveSingleStoryWithId() throws JSONException {
        Integer storyId = getStoryId(projectId);
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();

        String url = availableEndPoints.createGetStoryInProjectUrl(projectId.toString(), storyId.toString());
        Response response = given().port(port)
                            .when().get(url)
                            .then().contentType(JSON).extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
        try {
            assertEquals("Test name", storyJSON.get(StoryEnum.NAME), response.path(StoryEnum.NAME));
            assertEquals("Test how to demo", storyJSON.get(StoryEnum.HOW_TO_DEMO_CC), response.path(StoryEnum.HOW_TO_DEMO_CC));
            assertEquals("Test importance value", storyJSON.get(StoryEnum.IMPORTANCE), response.path(StoryEnum.IMPORTANCE));
            assertEquals("Test estimate value", storyJSON.get(StoryEnum.ESTIMATE), response.path(StoryEnum.ESTIMATE));
            assertEquals("Test estimate value", storyJSON.get(StoryEnum.VALUE), response.path(StoryEnum.VALUE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //delete single story with id
    @Test
    public void removeSingleStoryWithId() throws JSONException {
        Integer storyId = getStoryId(projectId);
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();

        String url = availableEndPoints.DELETE_STORY + storyId.toString();
        Response response = given().port(port)
                            .when().delete(url)
                            .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
    }

    //the client sends a ttpost data to create a tag item inside the existing project
    @Test
    public void createSingleTagInProject(){

        productBacklogTestData.setTagMockJson(projectId.toString());
        JSONObject tagJson = productBacklogTestData.getTagMockJson();

        Response response = createSingleTag(tagJson.toString());
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
        try {
            assertEquals("Test name", tagJson.get(TagEnum.NAME), response.path(TagEnum.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }    //the client sends a ttpost data to create a tag item inside the existing project
    @Test
    public void getTagByName() throws JSONException {


        productBacklogTestData.setTagMockJson(projectId.toString());
        JSONObject tagJson = productBacklogTestData.getTagMockJson();

        Response response = createSingleTag(tagJson.toString());
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);

        String url = availableEndPoints.createGetSingleTagUrl(projectId.toString(), tagJson.getString(TagEnum.NAME));
        Response tagResponse = given().port(port)
                .when().get(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> tagResponse.getStatusCode() > 0);
        assertEquals("Response should 200", 200, tagResponse.getStatusCode());
        assertEquals("Test Tag update name", tagJson.get(TagEnum.NAME), tagResponse.path(TagEnum.NAME));

    }
    //delete single tag with id
    @Test
    public void removeSingleTagWithId() throws JSONException {
        Integer tagId = getTagId(projectId);

        String url = availableEndPoints.DELETE_TAG+ tagId.toString();
        Response response = given().port(port)
                .when().delete(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
    }
    //update  a tag with id
    @Test
    public void updateSingleTagWithId(){

        Integer tagId = getTagId(projectId);
        productBacklogTestData.setUpdateTagMockJson(projectId.toString());
        JSONObject tagJSON = productBacklogTestData.getTagMockJson();

        String url = availableEndPoints.UPDATE_TAG + tagId.toString();
        Response response = given().port(port).contentType("application/json").body(tagJSON.toString())
                .when().put(url)
                .then().contentType(JSON)
                .extract().response();
        assertEquals("Response should 200", 200, response.getStatusCode());
        try {
            assertEquals("Test Tag update name", tagJSON.get(TagEnum.NAME), response.path(TagEnum.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //attach the tag to story with id
    @Test
    public void attachUnAttachTheTagToStory() throws JSONException {

        Integer tagId = getTagId(projectId);
        Integer storyId = getStoryId(projectId);
        JSONObject tagJSON = productBacklogTestData.getTagMockJson();
        tagJSON.put(TagEnum.ID, tagId.toString());
        String url = availableEndPoints.createAttachTagUrl(storyId.toString(), tagId.toString());
        Response response =
                            given().port(port)
                                .when().get(url)
                                .then().contentType(JSON)
                                .extract().response();
        assertEquals("Response should 200", 200, response.getStatusCode());
        ArrayList<JSONObject> tags = response.path("tags");
        assertEquals(true, tags.size() > 0);

        url = availableEndPoints.createUnAttachTagUrl(storyId.toString(), tagId.toString());
                response =
                            given().port(port)
                                .when().get(url)
                                .then().contentType(JSON)
                                .extract().response();
        assertEquals("Response should 200", 200, response.getStatusCode());

    }
    //remove the tag with attached story
    @Test
    public void removeTagWithAttachedStory() throws JSONException {

        Integer tagId = getTagId(projectId);
        Integer storyId = getStoryId(projectId);
        JSONObject tagJSON = productBacklogTestData.getTagMockJson();
        tagJSON.put(TagEnum.ID, tagId.toString());
        String url = availableEndPoints.createAttachTagUrl(storyId.toString(), tagId.toString());
        Response response =
                            given().port(port)
                                .when().get(url)
                                .then().contentType(JSON)
                                .extract().response();
        assertEquals("Response should 200", 200, response.getStatusCode());
        ArrayList<JSONObject> tags = response.path("tags");
        assertEquals(true, tags.size() > 0);


        url = availableEndPoints.DELETE_TAG+ tagId.toString();
        Response tagResponse = given().port(port)
                .when().delete(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> tagResponse.getStatusCode() > 0);
        assertEquals("Response should 200", 200, tagResponse.getStatusCode());



    }
    // retrieve stories by status  done
    @Test
    public void filterDoneStoriesTest(){
        DummyData dummyData = initializeListData();
        Project project = dummyData.getGeneratedProject();

        String url = availableEndPoints.createRetrieveStoriesFilterUrl(project.getId().toString(), FilterEnum.DONE);
        Response listStories = given().port(port)
                .when().get(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> listStories.getStatusCode() > 0);
        assertEquals("Response should 200", 200, listStories.getStatusCode());
        List<HashMap> responseBody = listStories.jsonPath().get();
        assertTrue("Status each story should : DONE", responseBody.size() == 11);
        responseBody.forEach((story)->{
            Integer status = (Integer) story.get(StoryEnum.STATUS);
            assertTrue("Status each story should : DONE", StoryEnum.STATUS_DONE == status);
        });

    }
    // retrieve stories by filter text all
    @Test
    public void filterAllStoriesTest(){
        DummyData dummyData = initializeListData();
        Project project = dummyData.getGeneratedProject();

        String url = availableEndPoints.createRetrieveStoriesFilterUrl(project.getId().toString(), FilterEnum.ALL);
        Response listStories = given().port(port)
                .when().get(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> listStories.getStatusCode() > 0);
        assertEquals("Response should 200", 200, listStories.getStatusCode());
        List<HashMap> responseBody = listStories.jsonPath().get();
        assertTrue("Status each story should : DONE", responseBody.size() == 33);
    }

    // retrieve stories by status backlogged
    @Test
    public void filterBackloggedStoriesTest(){
        DummyData dummyData = initializeListData();
        Project project = dummyData.getGeneratedProject();

        String url = availableEndPoints.createRetrieveStoriesFilterUrl(project.getId().toString(), FilterEnum.BACKLOG);
        Response listStories = given().port(port)
                .when().get(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> listStories.getStatusCode() > 0);
        assertEquals("Response should 200", 200, listStories.getStatusCode());
        List<HashMap> responseBody = listStories.jsonPath().get();
        assertTrue("Size should be the total size of the dummy data", responseBody.size() == 11);
        responseBody.forEach((story)->{
            Integer status = (Integer) story.get(StoryEnum.STATUS);
            Integer value = (Integer) story.get(StoryEnum.VALUE);
            Integer importance = (Integer) story.get(StoryEnum.IMPORTANCE);
            Integer estimate = (Integer) story.get(StoryEnum.ESTIMATE);
            assertTrue("Status each story should : be UNCHECK", StoryEnum.STATUS_UNCHECK == status);
            assertTrue("With value 0", 0 == value);
            assertTrue("Importance 0", 0 == importance);
            assertTrue("Estimate 0", 0 == estimate);
        });

    }
    // retrieve stories by status backlogged
    @Test
    public void filterDetailedStoriesTest(){
        DummyData dummyData = initializeListData();
        Project project = dummyData.getGeneratedProject();

        String url = availableEndPoints.createRetrieveStoriesFilterUrl(project.getId().toString(), FilterEnum.DETAIL);
        Response listStories = given().port(port)
                .when().get(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> listStories.getStatusCode() > 0);
        assertEquals("Response should 200", 200, listStories.getStatusCode());
        List<HashMap> responseBody = listStories.jsonPath().get();
        assertTrue("Size should be the total size of the dummy data", responseBody.size() == 11);
        responseBody.forEach((story)->{
            Integer status = (Integer) story.get(StoryEnum.STATUS);
            Integer value = (Integer) story.get(StoryEnum.VALUE);
            Integer importance = (Integer) story.get(StoryEnum.IMPORTANCE);
            Integer estimate = (Integer) story.get(StoryEnum.ESTIMATE);
            assertTrue("Status each story should : be UNCHECK", StoryEnum.STATUS_UNCHECK == status);
            assertTrue("With value should > 0",  value > 0);
            assertTrue("Importance 0 should > 0", importance > 0);
            assertTrue("Estimate 0 should > 0", estimate > 0);
        });

    }
    // retrieve list of tags
    @Test
    public void retrieveListOfTagsTest(){
        DummyData dummyData = initializeListData();
        Project project = dummyData.getGeneratedProject();
        List<String> tagStringArray = dummyData.getTags();
        String url = availableEndPoints.retrieveListOfTags(project.getId().toString());
        Response listOfTags = given().port(port)
                .when().get(url)
                .then().extract().response();
        await().atMost(5, TimeUnit.SECONDS).until(() -> listOfTags.getStatusCode() > 0);
        assertEquals("Response should 200", 200, listOfTags.getStatusCode());
        List<HashMap> responseBody = listOfTags.jsonPath().get();
        assertTrue("Size should be the total size of the dummy data", responseBody.size() == 4);
        responseBody.forEach((tag)->{
            String name = (String) tag.get(TagEnum.NAME);
            assertTrue("Tag name should equal with tag dummy data", tagStringArray.contains(name));
        });

    }
    private DummyData initializeListData(){
        DummyData dummyData = new DummyData(projectRepository, storyRepository, tagRepository, storyService);
        dummyData.prepareData();
        return  dummyData;
    }
    private Integer getStoryId(Integer projectId){
        productBacklogTestData.setStoryMockJson(projectId.toString());
        JSONObject storyJSON = productBacklogTestData.getStoryMockJson();
        Response response = createSingleStory(storyJSON.toString());
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
        return response.path(StoryEnum.ID);
    }

    private Integer getTagId(Integer projectId){
        productBacklogTestData.setTagMockJson(projectId.toString());
        JSONObject tagJson = productBacklogTestData.getTagMockJson();
        Response response = createSingleTag(tagJson.toString());
        await().atMost(5, TimeUnit.SECONDS).until(() -> response.getStatusCode() > 0);
        assertEquals("Response should 200", 200, response.getStatusCode());
        return response.path(TagEnum.ID);
    }
    private Response createSingleStory(String storyJSONString){
        Response response =
                given().port(port).contentType("application/json").body(storyJSONString)
                        .when()
                        .post(availableEndPoints.CREATE_STORY)
                        .then()
                        .contentType(JSON)
                        .extract().response();
        return  response;
    }
    private Response createSingleTag(String tagJSONString){
        Response response =
                given().port(port).contentType("application/json").body(tagJSONString)
                        .when()
                            .post(availableEndPoints.CREATE_TAG)
                        .then()
                            .contentType(JSON)
                            .extract().response();
        return  response;
    }
    private Integer getProjectId(JSONObject projectJson){
        Integer projectId = createProject(projectJson.toString()).path(ProjectEnum.ID);
        await().atMost(5, TimeUnit.SECONDS).until(() -> projectId >= 0);
        assertNotNull(projectId);
        return  projectId;
    }
    private Response createProject(String projectJSONString){
        Response response =
                given().port(port).contentType("application/json").body(projectJSONString)
                        .when()
                        .post(availableEndPoints.CREATE_PROJECT)
                        .then()
                        .contentType(JSON)
                        .extract().response();
        return  response;
    }

}
