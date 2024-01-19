package simbirsoft.task.dailyplanner.presentation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import simbirsoft.task.dailyplanner.common.model.ImmutableList
import simbirsoft.task.dailyplanner.common.ui.ThemePreviewParameter
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme
import simbirsoft.task.dailyplanner.presentation.model.PlanModel

@Composable
fun HourBlock(
    plansForHour: ImmutableList<PlanModel>,
    onPlanClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .height(90.dp),
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = plansForHour.list, key = { requireNotNull(it.id) }) {
                PlanBlock(
                    modifier = Modifier
                        .clickable { onPlanClick.invoke(requireNotNull(it.id)) },
                    name = it.name,
                    timeStart = it.timeStart,
                    timeEnd = it.timeEnd
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HourBlockPreview(
    @PreviewParameter(ThemePreviewParameter::class) inDark: Boolean
) = DailyPlannerTheme(inDark) {
    HourBlock(
        onPlanClick = {},
        plansForHour = ImmutableList(
            list = listOf(
                PlanModel(
                    id = 1,
                    name = "Name 1",
                    timeStart = "10:40",
                    timeEnd = "10:45"
                ),
                PlanModel(
                    id = 2,
                    name = "Name 2",
                    timeStart = "10:45",
                    timeEnd = "10:50"
                ),
                PlanModel(
                    id = 3,
                    name = "Name",
                    timeStart = "10:50",
                    timeEnd = "10:55"
                ),
                PlanModel(
                    id = 4,
                    name = "Long task name",
                    timeStart = "10:55",
                    timeEnd = "10:59"
                ),
            )
        )
    )
}