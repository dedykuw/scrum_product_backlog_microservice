package com.ezscrum.microservice.productbacklog.Controllers;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Exceptions.ValidationError;
import com.ezscrum.microservice.productbacklog.Exceptions.ValidationErrorBuilder;
import com.ezscrum.microservice.productbacklog.Services.Project.ProjectServiceImpl;
import com.ezscrum.microservice.productbacklog.Services.Story.StoryServiceImpl;
import com.ezscrum.microservice.productbacklog.Services.Tag.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    ProjectServiceImpl projectService;
    StoryServiceImpl storyService;
    TagServiceImpl tagService;

    @Autowired
    public ProjectController(
            ProjectServiceImpl projectService,
            StoryServiceImpl storyService,
            TagServiceImpl tagService) {
        this.projectService = projectService;
        this.storyService =  storyService;
        this.tagService = tagService;
    }

    @GetMapping("/projects/")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody Project project, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        Project createdProject = projectService.create(project);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/get/{id}")
    public Project get(@PathVariable(value = "id") Long id) {
        return projectService.get(id);
    }

    @PostMapping("/check_by_name")
    public Boolean checkByName(@RequestParam("name") String name) {
        return projectService.checkExistByProjectName(name);
    }

    @PostMapping("/get_by_name")
    public Project getByName(@RequestParam("name") String name) {
        return projectService.getByName(name);
    }

    @GetMapping("/{id}/stories/{filterType}")
    public List<Story> getStories(@PathVariable(value = "id") Long id, @PathVariable(value = "filterType") String filterType) {
        return storyService.getAllStories(filterType, id);
    }

    @GetMapping("/{id}/tags")
    public List<Tag> getTags(@PathVariable(value = "id") Long id) {
        return tagService.getByProjectId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id, @Valid @RequestBody Project project, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        Project createdProject = projectService.update(id, project);
        return ResponseEntity.ok(createdProject);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        return projectService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/{projectId}/story/{id}")
    public Story getByProjectAndSerial(@PathVariable(value = "projectId") Long projectId, @PathVariable(value = "id") Long id) {
        return storyService.getByProjectAndId(projectId, id);
    }

    @GetMapping("/{projectId}/tag/{name}")
    public Tag getTagByName(@PathVariable(value = "projectId") Long projectId, @PathVariable(value = "name") String name) {
        return tagService.getTagByName(name, projectId);
    }

}
