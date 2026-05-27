package dev.akmvxx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.akmvxx.ui.AppColors

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape? = null,
    minHeight: Dp = 52.dp,
    contentPadding: Dp = 20.dp,
) {
    val isMultiline = !singleLine || minLines > 1 || maxLines > 1
    val resolvedShape = shape ?: if (isMultiline) RoundedCornerShape(20.dp) else CircleShape
    val verticalPadding = if (isMultiline) 14.dp else 0.dp
    val rowAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .clip(resolvedShape)
            .background(AppColors.BackgroundSecondary)
            .border(width = 1.dp, color = AppColors.Outlined, shape = resolvedShape)
            .padding(horizontal = contentPadding, vertical = verticalPadding),
        verticalAlignment = rowAlignment,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = AppColors.TextWhite.copy(alpha = 0.55f),
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(12.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty() && placeholder.isNotEmpty()) {
                Text(
                    text = placeholder,
                    color = AppColors.TextWhite.copy(alpha = 0.4f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                enabled = enabled,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    color = AppColors.TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                ),
                cursorBrush = SolidColor(AppColors.Primary),
            )
        }
        if (trailingIcon != null) {
            Spacer(Modifier.width(12.dp))
            val iconModifier = if (onTrailingIconClick != null) {
                Modifier
                    .size(20.dp)
                    .clickable(onClick = onTrailingIconClick)
            } else {
                Modifier.size(20.dp)
            }
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = AppColors.TextWhite.copy(alpha = 0.55f),
                modifier = iconModifier,
            )
        }
    }
}
