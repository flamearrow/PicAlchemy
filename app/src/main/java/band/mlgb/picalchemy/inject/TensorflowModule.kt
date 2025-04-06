package band.mlgb.picalchemy.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TensorflowModule {

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideSingleThreadContext(): ExecutorCoroutineDispatcher {
        return newSingleThreadContext("TensorflowExecutor")
    }

}