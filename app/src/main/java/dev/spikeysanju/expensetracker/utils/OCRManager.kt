package dev.spikeysanju.expensetracker.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object OCRManager {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun extractTextFromImage(
        context: Context,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, imageUri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onSuccess(visionText.text)
                }
                .addOnFailureListener { e ->
                    onError(e)
                }
        } catch (e: Exception) {
            onError(e)
        }
    }

    /**
     * A simple helper to try and find a currency amount in the scanned text.
     */
    fun findAmountInText(text: String): Double? {
        val regex = Regex("(\\d+[.,]\\d{2})")
        val match = regex.find(text)
        return match?.value?.replace(",", ".")?.toDoubleOrNull()
    }
}
