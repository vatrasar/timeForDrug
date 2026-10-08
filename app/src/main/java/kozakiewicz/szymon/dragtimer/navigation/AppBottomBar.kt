package kozakiewicz.szymon.dragtimer.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Global bottom navigation bar providing primary tabs between Tracker, History, and Statistics.
 *
 * Usage: NavController handles tab switching and active destination state.
 * Key UI elements: NavigationBar container, Tracker tab, History tab, Statistics tab.
 * Used In: MainAppScaffold.
 */
@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(Screen.Tracker, Screen.History, Screen.Stats)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isTopLevelDestination = items.any { screen ->
        currentDestination?.hierarchy?.any {
            it.route?.contains(screen.route::class.simpleName ?: "") == true
        } == true
    }

    if (!isTopLevelDestination) {
        return
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = modifier.testTag("appBottomBar")
    ) {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route?.contains(screen.route::class.simpleName ?: "") == true
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(id = screen.titleRes)
                    )
                },
                label = { Text(stringResource(id = screen.titleRes)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(screen.testTag)
            )
        }
    }
}
