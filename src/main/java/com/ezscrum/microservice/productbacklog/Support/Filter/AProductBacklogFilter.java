package com.ezscrum.microservice.productbacklog.Support.Filter;

import com.ezscrum.microservice.productbacklog.Entities.Story;

import java.util.ArrayList;
import java.util.List;

public abstract class AProductBacklogFilter {
    protected List<Story> mStories = new ArrayList<Story>();
    protected String mCompareInfo = null;

    protected abstract List<Story> FilterStories();

    public List<Story> getStories() {
        return mStories;
    }

    public <E> AProductBacklogFilter(List<E> issues) {
        for (Object object : issues) {
            if (object instanceof Story) {
                Story story = (Story) object;
                mStories.add(story);
            }
        }
    }

    public <E> AProductBacklogFilter(ArrayList<E> issues, String compareinfo) {
        mCompareInfo = compareinfo;

        for (Object object : issues) {
            if (object instanceof Story) {
                Story story = (Story) object;
                mStories.add(story);
            }
        }

        if (issues.get(0) instanceof Story) {
            mStories = FilterStories();
        }
    }
}