package com.ezscrum.microservice.productbacklog.Support.Filter;

import com.ezscrum.microservice.productbacklog.Entities.Story;

import java.util.List;

public class ProductBacklogFilterFactory {
    private static ProductBacklogFilterFactory factory = null;

    private ProductBacklogFilterFactory() {
        // empty
    }

    public static ProductBacklogFilterFactory getInstance() {
        if (factory == null) {
            return new ProductBacklogFilterFactory();
        }

        return factory;
    }

    public AProductBacklogFilter getPBFilterFilter(String type, List<Story> stories) {
        if (type.equals(FilterEnum.ALL)){
            return new NullFilter(stories);
        }
        else if (type.equals(FilterEnum.BACKLOG)) {
            return new BacklogedFilter(stories);
        }
        else if (type.equals(FilterEnum.DONE)) {
            return new DoneFilter(stories);
        }
        else if (type.equals(FilterEnum.DETAIL)) {
            return new DetailedFilter(stories);
        }
        else {
            return new NullFilter(stories);
        }
    }
}

