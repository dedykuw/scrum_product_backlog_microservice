package com.ezscrum.microservice.productbacklog.Support.Filter;

import com.ezscrum.microservice.productbacklog.DatabbaseEnum.StoryEnum;
import com.ezscrum.microservice.productbacklog.Entities.Story;

import java.util.ArrayList;
import java.util.List;

public class DoneFilter extends AProductBacklogFilter {

    public DoneFilter(List<Story> stories) {
        super(stories);
        mStories = FilterStories();
    }

    @Override
    protected List<Story> FilterStories() {
        List<Story> stories = mStories;
        List<Story> fileredStories = new ArrayList<Story>();
        for (Story story : stories) {
                if (story.getStatus() == StoryEnum.STATUS_DONE) {
                    fileredStories.add(story);
                }
        }
        return fileredStories;
    }

}
