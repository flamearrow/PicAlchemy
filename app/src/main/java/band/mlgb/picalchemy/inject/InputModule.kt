package band.mlgb.picalchemy.inject

import band.mlgb.picalchemy.viewModels.ImageViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped

// provides two image models, one for inputImageViewModel, which has ActivityRetainedScoped
// as it lives across both fragments
// another for resultImageViewModel, which has FragmentScoped as it only used in AlchemyFragment

@Module
@InstallIn(ActivityRetainedComponent::class)
class InputModule {

    @Provides
    @ActivityRetainedScoped
    @InputImageViewModel
    fun providesInputImageViewModel() = ImageViewModel()
}

@Module
@InstallIn(FragmentComponent::class)
class ResultModule {
    // provides two image models, one for inputImageViewModel, which has ActivityRetainedScoped
    // as it lives across both fragments
    // another for resultImageViewModel, which has FragmentScoped as it only used in AlchemyFragment

    @Provides
    @FragmentScoped
    @ResultImageViewModel
    fun providesResultImageViewModel() = ImageViewModel()
}

