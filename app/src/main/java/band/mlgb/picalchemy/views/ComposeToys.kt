package band.mlgb.picalchemy.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import band.mlgb.picalchemy.views.theme.PicAlchemyTheme
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ButtonSection() {
    Column {
        Text("Regular Buttons:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {}) {
                Text("Button")
            }
            ElevatedButton(onClick = {}) {
                Text("Elevated")
            }
            FilledTonalButton(onClick = {}) {
                Text("Tonal")
            }
            OutlinedButton(onClick = {}) {
                Text("Outlined")
            }
            TextButton(onClick = {}) {
                Text("Text")
            }
        }
    }
}

@Composable
fun IconButtonSection() {
    Column {
        Text("Icon Buttons:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, "Settings")
            }
            FilledIconButton(onClick = {}) {
                Icon(Icons.Default.Add, "Add")
            }
            FilledTonalIconButton(onClick = {}) {
                Icon(Icons.Default.Favorite, "Favorite")
            }
            OutlinedIconButton(onClick = {}) {
                Icon(Icons.Default.Settings, "Settings")
            }
        }
    }
}

@Composable
fun ToggleButtonSection() {
    Column {
        Text("Toggle Icon Buttons:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var checked by remember { mutableStateOf(false) }

            IconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = if (checked) "Checked" else "Unchecked"
                )
            }

            FilledIconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = if (checked) "Checked" else "Unchecked"
                )
            }

            FilledTonalIconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = if (checked) "Checked" else "Unchecked"
                )
            }
        }
    }
}

@Composable
fun ButtonWithIconSection() {
    Column {
        Text("Buttons with Icons:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {}) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Settings")
            }
            FilledTonalButton(onClick = {}) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Like")
            }
        }
    }
}

@Composable
fun ElevatedIconButtonSection() {
    Column {
        Text("Elevated Icon Buttons:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shadowElevation = 8.dp,
                shape = CircleShape
            ) {
                FilledIconButton(onClick = {}) {
                    Icon(Icons.Default.Add, "Add")
                }
            }

            Surface(
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.small
            ) {
                FilledTonalIconButton(onClick = {}) {
                    Icon(Icons.Default.Favorite, "Favorite")
                }
            }

            Surface(
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.small
            ) {
                OutlinedIconButton(onClick = {}) {
                    Icon(Icons.Default.Settings, "Settings")
                }
            }
        }
    }
}

@Composable
fun ShapeComparisonSection() {
    Column {
        Text("Shape Comparison:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(onClick = {}) {
                Icon(Icons.Default.Add, "Default circular shape")
            }

            FilledIconButton(
                onClick = {},
                shape = MaterialTheme.shapes.small
            ) {
                Icon(Icons.Default.Add, "Square shape")
            }
        }
    }
}

@Composable
fun FabSection() {
    Column {
        Text("Floating Action Buttons:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallFloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, "Small FAB")
            }
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Edit, "Regular FAB")
            }
            LargeFloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, "Large FAB")
            }
            ExtendedFloatingActionButton(
                onClick = {},
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Extended") }
            )
        }
    }
}

@Composable
fun BadgeSection() {
    Column {
        Text("Badges:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Badge {
                Text("123")
            }

            BadgedBox(
                badge = { Badge { Text("8") } }
            ) {
                Icon(Icons.Default.Info, "Info with badge")
            }

            BadgedBox(
                badge = { Badge {} }  // Dot badge
            ) {
                FilledIconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, "Notifications")
                }
            }
        }
    }
}

@Composable
fun ChipSection() {
    Column {
        Text("Chips:")
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Assist Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text("Assist") },
                    leadingIcon = { Icon(Icons.Default.Info, null) }
                )
            }

            // Filter Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var selected by remember { mutableStateOf(false) }
                FilterChip(
                    selected = selected,
                    onClick = { selected = !selected },
                    label = { Text("Filter") },
                    leadingIcon = {
                        Icon(
                            if (selected) Icons.Default.Check else Icons.Default.Add,
                            null
                        )
                    }
                )
            }

            // Input Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var selected by remember { mutableStateOf(true) }
                InputChip(
                    selected = selected,
                    onClick = { selected = !selected },
                    label = { Text("Input") },
                    avatar = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            // Suggestion Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(
                    onClick = { },
                    label = { Text("Suggestion") },
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CardSection() {
    Column {
        Text("Cards:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                onClick = {},
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    null,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                )
            }

            ElevatedCard(
                onClick = {},
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    null,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                )
            }

            OutlinedCard(
                onClick = {},
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    null,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun SliderSection() {
    Column {
        Text("Sliders:")
        var sliderPosition by remember { mutableStateOf(0f) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Basic Slider
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                modifier = Modifier.weight(1f)
            )
            Text(String.format(Locale.getDefault(), "%.1f", sliderPosition))
        }
    }
}

@Composable
fun SwitchSection() {
    Column {
        Text("Switches:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var checked by remember { mutableStateOf(false) }
            Switch(
                checked = checked,
                onCheckedChange = { checked = it }
            )

            // Switch with label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Label")
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }
    }
}

@Composable
fun SelectionControlsSection() {
    Column {
        Text("Selection Controls:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            var checked by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
                Text("Checkbox")
            }
        }

        // Radio Group
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Radio Group:")

            val radioOptions = listOf("Option 1", "Option 2", "Option 3")
            var selectedOption by remember { mutableStateOf(radioOptions[0]) }

            radioOptions.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option }
                    )
                    Text(option)
                }
            }

            // Show selected value
            Text(
                text = "Selected: $selectedOption",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TextFieldSection() {
    Column {
        Text("Text Fields:")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var text by remember { mutableStateOf("") }

            // Basic TextField
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Basic TextField") },
                modifier = Modifier.fillMaxWidth()
            )

            // Outlined TextField
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Outlined TextField") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PinInputSection(pinLength: Int = 4) {
    var pins by remember { mutableStateOf(List(pinLength) { "" }) }
    Column {
        Text("$pinLength-Letter Pin Input:")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Create focus requesters and pin values
            val focusRequesters = remember { List(pinLength) { FocusRequester() } }

            // Create pin input fields
            for (index in 0 until pinLength) {
                OutlinedTextField(
                    value = pins[index],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            // Update the pin at current index
                            pins = pins.toMutableList().also { it[index] = newValue }

                            // If a character is entered, move focus to next field
                            if (newValue.isNotEmpty() && index < pinLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier
                        .size(56.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown &&
                                keyEvent.key == Key.Backspace &&
                                pins[index].isEmpty() &&
                                index > 0
                            ) {
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true
                )
            }

            // Request focus for first field when shown
            LaunchedEffect(Unit) {
                focusRequesters[0].requestFocus()
            }
        }

        // Show combined pin
        Text(
            text = "Pin: ${pins.joinToString("")}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SnackbarSection() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column {
        Text("Snackbars:")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Basic Snackbar Button
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "This is a basic Snackbar"
                        )
                    }
                }
            ) {
                Text("Show Basic Snackbar")
            }

            // Snackbar with Action Button
            Button(
                onClick = {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Snackbar with action",
                            actionLabel = "Undo",
                            withDismissAction = true,
                            duration = androidx.compose.material3.SnackbarDuration.Long
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                // Handle action
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Action performed!"
                                    )
                                }
                            }

                            SnackbarResult.Dismissed -> {
                                // Handle dismiss
                            }
                        }
                    }
                }
            ) {
                Text("Show Snackbar with Action")
            }
        }

        // Snackbar Host to display the Snackbars
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Add preview for SnackbarSection
@Preview(showBackground = true)
@Composable
fun SnackbarSectionPreview() {
    PicAlchemyTheme {
        Surface {
            SnackbarSection()
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun ButtonSectionPreview() {
    PicAlchemyTheme {
        Surface {
            ButtonSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IconButtonSectionPreview() {
    PicAlchemyTheme {
        Surface {
            IconButtonSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleButtonSectionPreview() {
    PicAlchemyTheme {
        Surface {
            ToggleButtonSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonWithIconSectionPreview() {
    PicAlchemyTheme {
        Surface {
            ButtonWithIconSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ElevatedIconButtonSectionPreview() {
    PicAlchemyTheme {
        Surface {
            ElevatedIconButtonSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShapeComparisonSectionPreview() {
    PicAlchemyTheme {
        Surface {
            ShapeComparisonSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FabSectionPreview() {
    PicAlchemyTheme {
        Surface {
            FabSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BadgeSectionPreview() {
    PicAlchemyTheme {
        Surface {
            BadgeSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipSectionPreview() {
    PicAlchemyTheme {
        Surface {
            ChipSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardSectionPreview() {
    PicAlchemyTheme {
        Surface {
            CardSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SliderSectionPreview() {
    PicAlchemyTheme {
        Surface {
            SliderSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchSectionPreview() {
    PicAlchemyTheme {
        Surface {
            SwitchSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectionControlsSectionPreview() {
    PicAlchemyTheme {
        Surface {
            SelectionControlsSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldSectionPreview() {
    PicAlchemyTheme {
        Surface {
            TextFieldSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PinInputSectionPreview() {
    PicAlchemyTheme {
        Surface {
            PinInputSection()
        }
    }
}