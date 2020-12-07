package band.mlgb.picalchemy.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class TensorflowModule {

    @ObsoleteCoroutinesApi
    @Provides
    @Singleton
    fun provideSingleThreadContext(): ExecutorCoroutineDispatcher {
        return newSingleThreadContext("TensorflowExecutor")
    }

}