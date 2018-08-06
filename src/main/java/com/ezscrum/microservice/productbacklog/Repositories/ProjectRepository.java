package com.ezscrum.microservice.productbacklog.Repositories;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Boolean existsByName(String name);
    Project getByName(String name);
}
