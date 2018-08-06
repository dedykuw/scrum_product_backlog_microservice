package com.ezscrum.microservice.productbacklog.Support.Filter;

import com.ezscrum.microservice.productbacklog.DatabbaseEnum.StoryEnum;
import com.ezscrum.microservice.productbacklog.Entities.Story;

import java.util.ArrayList;
import java.util.List;

public class DetailedFilter extends AProductBacklogFilter {

    public DetailedFilter(List<Story> stories) {
        super(stories);
        mStories = FilterStories();
    }

    @Override
    protected List<Story> FilterStories() {
        List<Story> stories = mStories;
        List<Story> fileredStories = new ArrayList<Story>();
        for (Story story : stories) {
            if (story.getValue() > 0 && story.getEstimate() > 0 && story.getImportance() > 0) {
                if (story.getStatus() == StoryEnum.STATUS_UNCHECK) {
                    fileredStories.add(story);
                }
            }
        }
        return fileredStories;
    }

}
