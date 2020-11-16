package band.mlgb.picalchemy.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Singleton

@Module
@DisableInstallInCheck
class TensorflowModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @ObsoleteCoroutinesApi
    @Provides
    @Singleton
    fun provideSingleThreadContext(): ExecutorCoroutineDispatcher {
        return newSingleThreadContext("TensorflowExecutor")
    }

}