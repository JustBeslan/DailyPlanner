package simbirsoft.task.dailyplanner.presentation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.ui.ThemePreviewParameter
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme

@Composable
fun PlanBlock(
    name: String,
    timeStart: String,
    timeEnd: String,
    modifier: Modifier = Modifier,
) {
    val blockSize = DpSize(
        width = 150.dp,
        height = 80.dp
    )
    val shapeBlock = RoundedCornerShape(5.dp)

    Column(
        modifier = modifier
            .size(blockSize)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = shapeBlock
            )
            .clip(shape = shapeBlock),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = shapeBlock
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(2.dp),
                text = stringResource(R.string.time_range_pattern, timeStart, timeEnd),
                color = MaterialTheme.colorScheme.primary,
                fontSize = TextUnit(15f, TextUnitType.Sp),
                fontStyle = FontStyle.Italic
            )
        }
        Text(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 5.dp,
                    bottom = 5.dp,
                ),
            text = name,
            color = MaterialTheme.colorScheme.background,
            overflow = TextOverflow.Ellipsis,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DealBlockPreview(
    @PreviewParameter(ThemePreviewParameter::class) inDark: Boolean
) = DailyPlannerTheme(inDark) {
    PlanBlock(
        name = "Nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
        timeStart = "10:40",
        timeEnd = "10:50"
    )
}