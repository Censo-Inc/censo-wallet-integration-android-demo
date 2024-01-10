package co.censo.censowalletintegrationdemo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.censo.walletintegration.CensoWalletIntegration
import co.censo.walletintegration.Session

class MainViewModel : ViewModel() {
    private val sdk = CensoWalletIntegration()
    private lateinit var session: Session

    val link: MutableState<String?> = mutableStateOf(null)
    val result: MutableState<Boolean?> = mutableStateOf(null)

    val errorMessage: MutableState<String?> = mutableStateOf(null)
    val showError: MutableState<Boolean> = mutableStateOf(false)

    fun initiateSession() {
        try {
            session = sdk.initiate(
                onFinished = {
                    result.value = it
                }
            )

            link.value = session.connect(
                onConnected = {
                    try {
                        session.phrase(binaryPhrase = "66c6a14c56cd7435d51a61b5aac215824dddd81917f6f80ed10bbf037c8e3676")
                    } catch (e: Exception) {
                        errorMessage.value = e.localizedMessage ?: "Something went wrong"
                        showError.value = true
                    }
                }
            )
        } catch (e: Exception) {
            errorMessage.value = e.localizedMessage ?: "Something went wrong"
            showError.value = true
        }
    }

    fun dismissAlert() {
        showError.value = false
        errorMessage.value = null
    }
}