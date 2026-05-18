package dev.akmvxx.navigation

import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.akmvxx.android.Navigator
import javax.inject.Inject

@ActivityRetainedScoped
class TabsNavigator @Inject constructor() : Navigator(initial = TabsNavKey.Browse)
