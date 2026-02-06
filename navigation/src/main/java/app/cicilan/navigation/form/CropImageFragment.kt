package app.cicilan.navigation.form

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.cicilan.component.utils.showMessage
import app.cicilan.navigation.BaseFragment
import app.cicilan.navigation.R
import app.cicilan.navigation.databinding.MainImageCropBinding

class CropImageFragment : BaseFragment<MainImageCropBinding>(MainImageCropBinding::inflate) {
    private val args: CropImageFragmentArgs by navArgs()

    override fun renderView(bundle: Bundle?) {
        with(binding) {
            toolbarCropImage.apply {
                inflateMenu(R.menu.crop_option_menu)
                setNavigationOnClickListener {
                    showMessage(getString(R.string.cancel_direction))
                    findNavController().navigateUp()
                }
                setOnMenuItemClickListener {
                    with(cropImageView) {
                        if (it.itemId == R.id.rotateButton) rotateImage(90)
                    }
                    true
                }
            }
            cropButton.setOnClickListener {
                with(cropImageView) {
                    val formatImage =
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                            Bitmap.CompressFormat.JPEG
                        } else {
                            Bitmap.CompressFormat.WEBP_LOSSY
                        }

                    croppedImageAsync(formatImage, 90, 680, 480)
                }
            }
            with(cropImageView) {
                setImageUriAsync(args.uri)
                setOnCropImageCompleteListener { _, result ->
                    if (!result.isSuccessful) {
                        showMessage(result.error?.localizedMessage)
                        return@setOnCropImageCompleteListener
                    }
                    setFragmentResult(
                        CROP_IMAGE_RESULT,
                        Bundle()
                            .apply { putParcelable(CROPPED_BITMAP, result.uriContent) },
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }

    companion object {
        // Image Crop Fragment
        const val CROP_IMAGE_RESULT = "crop_image_result"
        const val CROPPED_BITMAP = "bitmap"
    }
}
