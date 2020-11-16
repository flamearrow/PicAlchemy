package band.mlgb.picalchemy.inject

import android.content.Context

// This class is not used, just demonstrate how to inject with @Subcomponent's module
// that has different scope (ActivityScope) then the App @Component (Singleton)
class ToyComplicatedClass constructor(val context: Context)