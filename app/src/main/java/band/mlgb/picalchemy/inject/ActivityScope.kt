package band.mlgb.picalchemy.inject

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
@Deprecated(message = "replaced with hilt")
annotation class ActivityScope
