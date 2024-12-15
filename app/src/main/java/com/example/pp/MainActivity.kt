package com.example.pp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pp.ui.theme.PpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PpTheme {
                PennyPeeperApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PennyPeeperApp() {
    var count by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PennyPeeper") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Count: $count",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { if (count > 0) count-- }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Decrease")
                }

                Button(
                    onClick = { count++ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Increase")
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 640,
    name = "Light Mode"
)
@Composable
private fun PennyPeeperAppPreview() {
    PpTheme {
        PennyPeeperApp()
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 640,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
private fun PennyPeeperAppDarkPreview() {
    PpTheme {
        PennyPeeperApp()
    }
}

