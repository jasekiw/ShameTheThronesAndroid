package com.jasekiw.shamethethrones.providers.restroom;

import com.jasekiw.shamethethrones.providers.api.RatingApi;
import com.jasekiw.shamethethrones.providers.api.RestroomApi;
import dagger.Module;
import dagger.Provides;

@Module
public class RestroomModule {
    @Provides
    AddRestroomFragmentController provideAddRestroomController(RestroomApi api) {
        return new AddRestroomFragmentController(api);
    }

    @Provides
    RestroomFragmentController provideRestroomFragmentController(RatingApi api, RatingsFragmentController controller) {
        return new RestroomFragmentController(api, controller);
    }
    @Provides
    RatingsFragmentController provideRatingsFragmentController(RatingApi api) {
        return new RatingsFragmentController(api);
    }

}
