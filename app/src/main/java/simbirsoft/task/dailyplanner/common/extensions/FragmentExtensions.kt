package simbirsoft.task.dailyplanner.common.extensions

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme

fun Fragment.composeView(content: @Composable () -> Unit): View {
    return ComposeView(requireContext()).apply {
        setContent {
            DailyPlannerTheme { content.invoke() }
        }
    }
}

fun Fragment.withArgs(block: Bundle.() -> Unit): Fragment {
    val args = bundleOf()
    block.invoke(args)
    return this.apply {
        arguments = args
    }
}