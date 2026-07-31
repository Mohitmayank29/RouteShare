package com.mohit.mapsone.common

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class SnackbarType {
    object Success : SnackbarType()
    object Error : SnackbarType()
    object Info : SnackbarType()
    object Warning : SnackbarType()
}
data class SnackbarEvent(
    val message: String,
    val type: SnackbarType = SnackbarType.Info,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

enum class SnackbarDuration {
    Short, Long, Indefinite
}
class SnackbarManager {

    private val _events = MutableSharedFlow<SnackbarEvent>()
    val events = _events.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Main)

    fun showSnackbar(
        message: String,
        type: SnackbarType = SnackbarType.Info,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        scope.launch {
            println("🔵 EMITTING SNACKBAR: $message") // Add log
            _events.emit(
                SnackbarEvent(
                    message, type, actionLabel, onAction, duration
                )
            )
        }
    }

    fun success(msg: String) = showSnackbar(msg, SnackbarType.Success)
    fun error(msg: String) = showSnackbar(msg, SnackbarType.Error)
    fun warning(msg: String) = showSnackbar(msg, SnackbarType.Warning)
    fun info(msg: String) = showSnackbar(msg, SnackbarType.Info)
}
object SnackbarController {
    val manager = SnackbarManager()
}
@Composable
fun AppSnackbarHost(
    modifier: Modifier = Modifier,
    snackbarManager: SnackbarManager = SnackbarController.manager
) {
    val hostState = remember { SnackbarHostState() }
    var currentEvent by remember { mutableStateOf<SnackbarEvent?>(null) }

    LaunchedEffect(Unit) {
        snackbarManager.events.collect { event ->
            println("🟢 COLLECTING SNACKBAR EVENT: ${event.message}") // Add log

            currentEvent = event

            val duration = when (event.duration) {
                SnackbarDuration.Short -> SnackbarDuration.Short
                SnackbarDuration.Long -> SnackbarDuration.Long
                SnackbarDuration.Indefinite -> SnackbarDuration.Indefinite
            }

            val result = hostState.showSnackbar(
                message = event.message,
                actionLabel = event.actionLabel,
                duration = when (event.duration) {
                    SnackbarDuration.Short -> androidx.compose.material3.SnackbarDuration.Short
                    SnackbarDuration.Long -> androidx.compose.material3.SnackbarDuration.Long
                    SnackbarDuration.Indefinite -> androidx.compose.material3.SnackbarDuration.Indefinite
                }
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.onAction?.invoke()
            }
        }
    }

    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { data ->

            val bgColor = when (currentEvent?.type) {
                SnackbarType.Success -> Color(0xFF4CAF50)
                SnackbarType.Error -> Color(0xFFF44336)
                SnackbarType.Warning -> Color(0xFFFF9800)
                else -> Color(0xFF323232)
            }

            Snackbar(
                snackbarData = data,
                containerColor = bgColor,
                contentColor = Color.White
            )
        }
    )
}
