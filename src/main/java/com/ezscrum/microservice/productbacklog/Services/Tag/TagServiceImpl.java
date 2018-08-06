package com.ezscrum.microservice.productbacklog.Services.Tag;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Exceptions.ResourceNotFoundException;
import com.ezscrum.microservice.productbacklog.Repositories.ProjectRepository;
import com.ezscrum.microservice.productbacklog.Repositories.StoryRepository;
import com.ezscrum.microservice.productbacklog.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class TagServiceImpl implements TagServiceInterface {

    private StoryRepository storyRepository;
    private TagRepository tagRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public TagServiceImpl(
            StoryRepository storyRepository,
            TagRepository tagRepository,
            ProjectRepository projectRepository) {
        this.storyRepository = storyRepository;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
    }



    @Override
    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagByName(String name, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        Tag result = null;
        result = tagRepository.getFirstByNameAndProject(name, project);
        if (result == null) throw  new ResourceNotFoundException("Tag", "name", name);
        return result;
    }

    @Override
    public Tag update(Long id, Tag tagData) {
        Tag tag = findOneTag(id);
        tag.setName(tagData.getName());
        return tagRepository.save(tag);
    }

    @Override
    public Boolean delete(Long id) {
        Tag tag = findOneTag(id);
        if (tag.getStories().size() > 0 ) {
            tag.getStories().forEach((story)->{
                unAttachStory(id, story.getId());
            });
        }
        tagRepository.delete(tag);
        return true;
    }


    @Override
    public Boolean unAttachStory(Long id, Long storyId) {
        Tag tag = findOneTag(id);
        Story story = findOneStory(storyId);
        story.getTags().remove(tag);
        tag.getStories().remove(story);
        Tag taggedStory = tagRepository.save(tag);
        return true;
    }

    @Override
    public List<Tag> getByProjectId(Long id) {
        return tagRepository.findByProjectId(id);
    }

    private Tag findOneTag(Long id){
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
    }
    private Story findOneStory(Long storyId) {
        return storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", storyId));
    }

}
