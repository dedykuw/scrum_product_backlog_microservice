package com.ezscrum.microservice.productbacklog.Services.Project;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;

import java.util.List;
import java.util.Set;

public interface ProjectServiceInterface {
    List<Project> getAllProjects();
    Project create(Project project);
    Project get(Long id);
    Project update(Long id, Project project);
    Boolean delete(Long id);
    Boolean checkExistByProjectName(String name);
    Project getByName(String name);
}
