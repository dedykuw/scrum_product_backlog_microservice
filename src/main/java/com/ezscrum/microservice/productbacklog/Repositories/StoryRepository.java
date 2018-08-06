package com.ezscrum.microservice.productbacklog.Repositories;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByProjectId(Long id);
    Story getFirstByProjectAndSerialId(Project project, Long serialId);
    Story getStoryByProjectAndId(Project project, Long id);
    @Query(value = "SELECT * FROM story t where t.project_id = :projectId AND t.id = :id LIMIT 1", nativeQuery=true)
    public Story getStoryByProjectAndId(@Param("projectId") Long projectId, @Param("id") Long id);
}
