package com.ezscrum.microservice.productbacklog.Services.Project;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Exceptions.ResourceNotFoundException;
import com.ezscrum.microservice.productbacklog.Repositories.ProjectRepository;
import com.ezscrum.microservice.productbacklog.Repositories.StoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectServiceImpl implements ProjectServiceInterface {
    ProjectRepository projectRepository;
    StoryRepository storyRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, StoryRepository storyRepository) {
        this.projectRepository = projectRepository;
        this.storyRepository = storyRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project get(Long id) {
        return findOne(id);
    }

    @Override
    public Project update(Long id, Project project) {
        Project updatedProject = findOne(id);
        updatedProject.setAttachMaxSize(project.getAttachMaxSize());
        updatedProject.setComment(project.getComment());
        updatedProject.setDisplayName(project.getDisplayName());
        updatedProject.setName(project.getName());
        updatedProject.setProductOwner(project.getProductOwner());
        return projectRepository.save(updatedProject);
    }

    @Override
    public Boolean delete(Long id) {
        projectRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean checkExistByProjectName(String name) {
        return projectRepository.existsByName(name);
    }

    @Override
    public Project getByName(String name) {
        return projectRepository.getByName(name);
    }

    private Project findOne(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    };
}
