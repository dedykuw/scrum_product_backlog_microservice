package com.ezscrum.microservice.productbacklog.Services.Story;

import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;

import java.util.List;
import java.util.Set;

public interface StoryServiceInterface {
    List<Story> getAllStories(String Filter, Long projectId);
    Story create(Story story);
    Story get(Long id);
    Story update(Long id, Story story);
    Boolean delete(Long id);
    Story attachTag(Long id, Long tagId);
    Set<Tag> getTags(Long id);
    Boolean unAttachTag(Long id, Long tagId);
    Story getByProjectAndId(Long projectId, Long id);
}
