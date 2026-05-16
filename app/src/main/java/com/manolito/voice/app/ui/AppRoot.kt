package com.manolito.voice.app.ui

import androidx.compose.runtime.Composable
import com.manolito.voice.app.navigation.AppNavHost
import com.manolito.voice.core.designsystem.theme.ManolitoTheme

@Composable
fun AppRoot() {
    ManolitoTheme {
        AppNavHost()
    }
}
