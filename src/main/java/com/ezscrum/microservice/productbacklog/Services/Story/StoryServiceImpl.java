package com.ezscrum.microservice.productbacklog.Services.Story;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Exceptions.ResourceNotFoundException;
import com.ezscrum.microservice.productbacklog.Logic.StoryLogic;
import com.ezscrum.microservice.productbacklog.Repositories.ProjectRepository;
import com.ezscrum.microservice.productbacklog.Repositories.StoryRepository;
import com.ezscrum.microservice.productbacklog.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StoryServiceImpl implements StoryServiceInterface {
    private StoryRepository storyRepository;
    private TagRepository tagRepository;
    private StoryLogic storyLogic;
    private ProjectRepository projectRepository;
    @Autowired
    public StoryServiceImpl(
            StoryRepository storyRepository,
            TagRepository tagRepository,
            StoryLogic storyLogic,
            ProjectRepository projectRepository) {
        this.storyRepository = storyRepository;
        this.tagRepository =  tagRepository;
        this.storyLogic = storyLogic;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Story> getAllStories(String filter, Long projectId) {
        return storyLogic.getStoriesByFilterType(filter, projectId);
    }

    @Override
    public Story create(Story story) {
        return storyRepository.save(story);
    }

    @Override
    public Story get(Long id) {
        return findOne(id);
    }


    @Override
    public Story getByProjectAndId(Long projectId, Long id) {
        Project project = projectRepository.getOne(projectId);
        Story result = storyRepository.getStoryByProjectAndId(project, id);
        if (result == null)throw new ResourceNotFoundException("Story", "id", id);
        return result ;
    }

    @Override
    public Story update(Long id, Story storyData) {
        Story story = findOne(id);
        //story.setProject(storyData.getProjectId());
        story.setSprintId(storyData.getSprintId());
        story.setName(storyData.getName());
        story.setStatus(storyData.getStatus());
        story.setImportance(storyData.getImportance());
        story.setEstimate(storyData.getEstimate());
        story.setValue(storyData.getValue());
        story.setNotes(storyData.getNotes());
        story.setHowToDemo(storyData.getHowToDemo());
        if (storyData.getTags() != null){
            if (story.getTags() != null) story.getTags().removeAll(story.getTags());
            storyData.getTags().forEach((storyM)-> {
                story.getTags().add(findOneTag(storyM.getId()));
            });
        }
        Story updatedStory = storyRepository.save(story);
        return updatedStory;
    }

    @Override
    public Boolean delete(Long id) {
        Boolean data;
        try {
            Story story = findOne(id);
            storyRepository.delete(story);
            data = true;
        } catch (Exception e) {
            e.printStackTrace();
            data = false;
        }
        return data;
    }

    @Override
    public Set<Tag> getTags(Long id) {
        Story story = findOne(id);
        return story.getTags();
    }

    @Override
    public Story attachTag(Long id, Long tagId) {
        Story story = findOne(id);
        Tag tag  = findOneTag(tagId);
        tag.getStories().add(story);
        story.getTags().add(tag);
        Story taggedStory = storyRepository.save(story);
        return taggedStory;
    }

    @Override
    public Boolean unAttachTag(Long id, Long tagId) {
        Story story = findOne(id);
        Tag tag  = findOneTag(tagId);
        tag.getStories().remove(story);
        story.getTags().remove(tag);
        Story taggedStory = storyRepository.save(story);
        return true;
    }


    private Story findOne(Long id){
        return storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", id));
    };
    private Tag findOneTag(Long id){
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
    }

}
