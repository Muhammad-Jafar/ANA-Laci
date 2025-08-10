package app.cicilan.navigation.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class FromCamera : ActivityResultContract<String, Uri?>() {
    private lateinit var imageUri: Uri

    override fun createIntent(context: Context, input: String): Intent {
        val image = File(context.cacheDir, input)
        imageUri = FileProvider.getUriForFile(context, "app.cicilan", image)
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
        imageUri.takeIf { resultCode == Activity.RESULT_OK }
}

class FromGallery : ActivityResultContract<String, Uri?>() {
    private lateinit var imageUri: Uri

    override fun createIntent(context: Context, input: String): Intent {
        val image = File(context.cacheDir, input)
        imageUri = FileProvider.getUriForFile(context, "app.cicilan", image)
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
        imageUri.takeIf { resultCode == Activity.RESULT_OK }
}

fun Context.convertCacheImageToExternalFileImage(cacheImageUri: Uri, userName: String): Uri {
    val fileName = "$userName-${Calendar.getInstance().timeInMillis}.png"
    val parentPath = this.getExternalFilesDirs(Environment.DIRECTORY_PICTURES)
        .first().absolutePath
    val destinationFile = File(parentPath, fileName)
    FileOutputStream(destinationFile).use { out ->
        this.contentResolver.openInputStream(cacheImageUri).use { it?.copyTo(out) }
    }
    return destinationFile.toUri()
}
