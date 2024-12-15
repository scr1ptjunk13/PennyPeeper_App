package com.example.pp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pp.ui.theme.PpTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PennyPeeperViewModel by viewModels()
    private lateinit var mediaProjectionManager: MediaProjectionManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            viewModel.onPermissionsGranted()
        }
    }

    private val startMediaProjection = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, ScreenshotService::class.java).apply {
                putExtra("resultCode", result.resultCode)
                putExtra("data", result.data)
            }
            startService(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)

        setContent {
            PpTheme {
                PennyPeeperApp(
                    viewModel = viewModel,
                    onRequestScreenshot = ::requestScreenshot
                )
            }
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            viewModel.onPermissionsGranted()
        }
    }

    private fun requestScreenshot() {
        startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PennyPeeperApp(
    viewModel: PennyPeeperViewModel,
    onRequestScreenshot: () -> Unit
) {
    val count by viewModel.count.collectAsStateWithLifecycle()
    val canTakeScreenshot by viewModel.canTakeScreenshot.collectAsStateWithLifecycle()

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
                    onClick = { viewModel.decrement() }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Decrease")
                }

                Button(
                    onClick = { viewModel.increment() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Increase")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRequestScreenshot,
                enabled = canTakeScreenshot
            ) {
                Text("Take Screenshot")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun PennyPeeperAppPreview() {
    PpTheme {
        PennyPeeperApp(
            viewModel = PennyPeeperViewModel(),
            onRequestScreenshot = {}
        )
    }
}

