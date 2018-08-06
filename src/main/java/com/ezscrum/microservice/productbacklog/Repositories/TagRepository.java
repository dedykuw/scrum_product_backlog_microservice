package com.ezscrum.microservice.productbacklog.Repositories;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByProjectId(Long id);
    Tag getFirstByNameAndProject(String name, Project project);
    List<Tag> findByIdIn(List<Long> ids);
}
