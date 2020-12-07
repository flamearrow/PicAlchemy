package band.mlgb.picalchemy.inject

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InputImageViewModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ResultImageViewModel