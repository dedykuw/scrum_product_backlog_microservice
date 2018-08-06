package com.ezscrum.microservice.productbacklog.Logic;

import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Repositories.StoryRepository;
import com.ezscrum.microservice.productbacklog.Support.Filter.AProductBacklogFilter;
import com.ezscrum.microservice.productbacklog.Support.Filter.ProductBacklogFilterFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class StoryLogic {
    StoryRepository storyRepository;

    public StoryLogic(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public List<Story> getStoriesByFilterType(String filterType, Long projectId){
        List<Story> allStories = storyRepository.findByProjectId(projectId);
        AProductBacklogFilter filter = ProductBacklogFilterFactory.getInstance().getPBFilterFilter(filterType, allStories);
        return  filter.getStories();
    }
}
