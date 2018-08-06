package com.ezscrum.microservice.productbacklog.Entities;
import com.ezscrum.microservice.productbacklog.DatabbaseEnum.StoryEnum;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = StoryEnum.TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdTime", "updatedTime"}, allowGetters = true)

public class Story{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = StoryEnum.PROJECT_ID)
    private Project project;

    private Long sprintId = StoryEnum.DEFAULT_VALUE;
    private Long serialId = StoryEnum.DEFAULT_VALUE;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Story name must not be blank!")
    private String name;
    private Integer status = StoryEnum.STATUS_UNCHECK;

    @Column(nullable = false)
    @NotNull(message = "Importance must not be blank!")
    private Integer importance;

    @Column(nullable = false)
    @NotNull(message = "Estime must not be blank!")
    private Integer estimate;

    @Column(nullable = false)
    @NotNull(message = "Story value must not be blank!")
    private Integer value;

    @Column(columnDefinition = "TEXT")
    private String notes;
    @Column(columnDefinition = "TEXT")
    private String howToDemo;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdTime;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedTime;

    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
    @JoinTable(name = "story_tag", joinColumns = @JoinColumn(name = "story_id") , inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    public Set<Tag> getTags() {
        return tags;
    }
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getHowToDemo() {
        return howToDemo;
    }

    public void setHowToDemo(String howToDemo) {
        this.howToDemo = howToDemo;
    }
    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
