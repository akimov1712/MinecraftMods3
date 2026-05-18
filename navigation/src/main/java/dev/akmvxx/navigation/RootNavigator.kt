package dev.akmvxx.navigation

import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.akmvxx.android.Navigator
import javax.inject.Inject

@ActivityRetainedScoped
class RootNavigator @Inject constructor() : Navigator(initial = RootNavKey.Splash)
