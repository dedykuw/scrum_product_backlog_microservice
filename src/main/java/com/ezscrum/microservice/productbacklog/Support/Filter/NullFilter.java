package com.ezscrum.microservice.productbacklog.Support.Filter;

import com.ezscrum.microservice.productbacklog.Entities.Story;

import java.util.List;

public class NullFilter extends AProductBacklogFilter {

    public <E> NullFilter(List<E> issues) {
        super(issues);
    }

    @Override
    protected List<Story> FilterStories() {
        return super.mStories;
    }

}
