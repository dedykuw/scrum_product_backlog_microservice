package com.ezscrum.microservice.productbacklog;

import com.ezscrum.microservice.productbacklog.DatabbaseEnum.StoryEnum;
import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Repositories.ProjectRepository;
import com.ezscrum.microservice.productbacklog.Repositories.StoryRepository;
import com.ezscrum.microservice.productbacklog.Repositories.TagRepository;
import com.ezscrum.microservice.productbacklog.Services.Story.StoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.*;

import static java.util.Arrays.asList;

public class DummyData {
    private StoryRepository storyRepository;
    private ProjectRepository projectRepository;
    private TagRepository tagRepository;
    private StoryServiceImpl storyService;
    private Long projectId;
    private List<Tag> generatedTags;
    private List<Story> generatedStories;
    private Project generatedProject;

    public DummyData(ProjectRepository projectRepository,
                     StoryRepository storyRepository,
                     TagRepository tagRepository,
                     StoryServiceImpl storyService) {
        this.tagRepository = tagRepository;
        this.storyRepository = storyRepository;
        this.projectRepository = projectRepository;
        this.storyService = storyService;
        this.generatedTags = new ArrayList<>();
        this.generatedStories= new ArrayList<>();

    }
    public void prepareData(){
        generatedProject = pushProjectData();
        projectId = generatedProject.getId();
        generatedTags = pushTagData();
        generatedStories = pushStories();
        //attachStoriesWithTag();
    }

    public List<Tag> getGeneratedTags() {
        return generatedTags;
    }

    public List<Story> getGeneratedStories() {
        return generatedStories;
    }

    public Project getGeneratedProject() {
        return generatedProject;
    }

    private Project pushProjectData(){
        Project project = new Project();
        project.setName("Dummy project");
        project.setDisplayName("Dummy Project");
        project.setProductOwner("Dedy Kurniawan");
        project.setComment("this is just a sample project");
        project.setAttachMaxSize((long) 2);
        return projectRepository.save(project);
    }
    private List<Tag> pushTagData(){
        List<Tag> tags = new ArrayList<>();
        getTags().forEach((name)->{
            Tag newTag = new Tag();
            newTag.setName(name);
            newTag.setProject(projectRepository.getOne(projectId));
            tags.add(newTag);
        });
        return tagRepository.saveAll(tags);
    }
    private List<Story> pushStories(){
        return storyRepository.saveAll(getStories());
    }
    public List<String> getTags(){
        List<String> tags = asList("Enhancements", "Performance Tuning", "Test Result", "Bugs Fixing");
        return  tags;
    }
    private List<Story> getStories(){
        List<Story> stories = new ArrayList<Story>();
        for(Integer i=0; i <3;i++){
            for(Integer b=0; b <=10;b++){
                Story story = new Story();
                story = setStory(story, i, b);
                stories.add(story);
            }
        }
        return stories;
    };
    private Story setStory(Story story, Integer identifier, Integer idNumber){
        if ((identifier == 0)) {
            story = createBackloggedStory(story, idNumber);
        }
        if ((identifier==1)){
            story = createDetailedStory(story, idNumber);
        }
        if ((identifier==2)){
            story = createDoneStory(story, idNumber);
        }
        return story;
    }
    private Story createBackloggedStory(Story story, Integer identifier){
        story.setName("new backlogged story "+identifier.toString());
        story.setHowToDemo("How to demo for backlogged story no. "+identifier.toString());
        story.setNotes("notes for the backlogged story no. "+identifier.toString());
        story.setValue(0);
        story.setEstimate(0);
        story.setImportance(0);
        story.setStatus(StoryEnum.STATUS_UNCHECK);
        story.setProject(projectRepository.getOne(projectId));
        return story;
    }
    private Story createDetailedStory(Story story, Integer identifier){
        story.setName("new detailed story no. "+identifier.toString());
        story.setHowToDemo("How to demo for detaild story no. "+identifier.toString());
        story.setNotes("notes for the detailed story no. "+identifier.toString());
        story.setValue(3);
        story.setEstimate(5);
        story.setImportance(2);
        story.setStatus(StoryEnum.STATUS_UNCHECK);
        story.setProject(projectRepository.getOne(projectId));
        return story;
    }
    private Story createDoneStory(Story story, Integer identifier){
        story.setName("new sample done story no. "+identifier.toString());
        story.setHowToDemo("How to demo for done story no. "+identifier.toString());
        story.setNotes("notes for the done story no. "+identifier.toString());
        story.setValue(3);
        story.setEstimate(5);
        story.setImportance(2);
        story.setStatus(StoryEnum.STATUS_DONE);
        story.setProject(projectRepository.getOne(projectId));
        return story;
    }
    private void attachStoriesWithTag(){
        Tag tag = generatedTags.get(getRandomNumber(generatedTags.size()-1, 0));
        getGeneratedStories().forEach((story)-> storyService.attachTag(story.getId(), tag.getId()));

    }
    private Integer getRandomNumber(Integer upperBound, Integer lowerBound){
        Random random = new Random();
        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }

}
