package com.ezscrum.microservice.productbacklog.Services.Tag;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;

import java.util.List;
import java.util.Set;

public interface TagServiceInterface {
    Tag create(Tag tag);
    Tag update(Long id, Tag tagData);
    Boolean delete(Long id);
    Boolean unAttachStory(Long id, Long storyId);
    List<Tag> getByProjectId(Long id);
    Tag getTagByName(String name, Long projectId);
}
