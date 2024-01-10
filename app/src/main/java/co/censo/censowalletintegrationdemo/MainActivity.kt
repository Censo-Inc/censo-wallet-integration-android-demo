package co.censo.censowalletintegrationdemo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.censo.censowalletintegrationdemo.ui.theme.CensoWalletIntegrationDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CensoWalletIntegrationDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val link = viewModel.link
    val result = viewModel.result

    val errorMessage = viewModel.errorMessage
    val showError = viewModel.showError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Censo Wallet Integration SDK Demo",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = viewModel::initiateSession
        ) {
            Text(text = "Export Seed Phrase")
        }
        if (link.value != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tap Here to Export",
                modifier = Modifier.clickable {
                    try {
                        uriHandler.openUri(link.value ?: "")
                    } catch (e: Exception) {
                        showUnableOpenLinkToast(context)
                        copyToClipboard(context, link.value ?: "Missing")
                    }
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
        if (result.value != null) {
            Spacer(modifier = Modifier.height(24.dp))
            val iconTextPair = if (result.value == true) {
                Pair(Icons.Default.Check, "Export Successful")
            } else {
                Pair(Icons.Default.Warning, "Export Failed")
            }

            Row {
                Icon(imageVector = iconTextPair.first, contentDescription = "")
                Text(text = iconTextPair.second)
            }
        }
    }

    if (showError.value) {
        AlertDialog(
            onDismissRequest = viewModel::dismissAlert,
            title = { Text("Error") },
            text = { Text(errorMessage.value ?: "Something went wrong") },
            confirmButton = {
                Button(
                    onClick = viewModel::dismissAlert
                ) {
                    Text("OK")
                }
            }
        )
    }
}

fun showUnableOpenLinkToast(context: Context) {
    Toast.makeText(
        context,
        "Unable to open link, copied to your keyboard",
        Toast.LENGTH_LONG
    ).show()
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    clipboard.setPrimaryClip(clip)
}