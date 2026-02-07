package app.cicilan.navigation.form

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.core.net.toUri
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.cicilan.component.utils.afterInputNumberChanged
import app.cicilan.component.utils.afterInputStringChanged
import app.cicilan.component.utils.currentInstant
import app.cicilan.component.utils.digitNumberArranged
import app.cicilan.component.utils.dotPixel
import app.cicilan.component.utils.format
import app.cicilan.component.utils.getNumber
import app.cicilan.entities.ModalForm
import app.cicilan.navigation.BaseFragment
import app.cicilan.navigation.FormViewModel
import app.cicilan.navigation.R
import app.cicilan.navigation.databinding.DialogPickImageBinding
import app.cicilan.navigation.databinding.MainFormBinding
import com.canhub.cropper.parcelable
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormFragment : BaseFragment<MainFormBinding>(MainFormBinding::inflate) {
    private lateinit var fromStorage: ActivityResultLauncher<String>
    private lateinit var fromCamera: ActivityResultLauncher<String>
    private val viewModel: FormViewModel by viewModel()
    private val args: FormFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromStorage =
            registerForActivityResult(GetContent()) { uri ->
                openImageCropFragment(uri)
            }
        fromCamera =
            registerForActivityResult(FromCamera()) { uri ->
                openImageCropFragment(uri)
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("imageUri", viewModel.imageUri)
    }

    override fun renderView(bundle: Bundle?) {
        bundle
            ?.parcelable<Uri>("imageUri")
            ?.let {
                binding.saveImage.setImageURI(it)
                viewModel.imageUri = it
            }

        if (args.item != null) {
            loadData(args.item!!).also {
                viewModel.imageUri = args.item?.image?.toUri()
            }
        } else {
            binding.saveImage.layoutParams.height = 0.dotPixel()
        }

        with(binding) {
            toolbarForm.apply {
                title =
                    if (args.item == null) {
                        getString(R.string.add_data)
                    } else {
                        getString(R.string.edit_data)
                    }
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            setFragmentResultListener(CROP_IMAGE_RESULT) { _, bundle ->
                bundle.parcelable<Uri>(CROPPED_BITMAP).let {
                    viewModel.imageUri = it
                    with(binding) {
                        bgImage.visibility = View.GONE
                        saveImage.apply {
                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            setImageURI(it)
                        }
                    }
                }
            }
            saveImage.setOnClickListener { showImageChooser() }
            saveButton.setOnClickListener { doSave() }
        }
        validate()
    }

    private fun validate() =
        with(binding) {
            nameInput.apply {
                afterInputStringChanged {
                    saveButton.isEnabled = nameInput.text.toString().isNotEmpty()
                    nameInputLayout.error =
                        when {
                            it!!.isBlank() -> getString(R.string.fill_data)
                            else -> null
                        }
                }
            }
            thingInput.afterInputStringChanged {
                thingInputLayout.error =
                    when {
                        it!!.isBlank() -> getString(R.string.fill_data)
                        else -> null
                    }
            }
            categoryInput.afterInputStringChanged {
                categoryInputLayout.error =
                    when {
                        it!!.isBlank() -> getString(R.string.fill_data)
                        else -> null
                    }
            }
            priceInput.apply {
                digitNumberArranged(priceInputLayout)
                afterInputNumberChanged {
                    if (it != null) {
                        priceInputLayout.error =
                            when {
                                it < 500000 -> getString(R.string.form_min_harga)
                                it > 10000000 -> getString(R.string.form_max_harga)
                                else -> null
                            }
                    }
                }
            }
            firstPayInput.apply {
                digitNumberArranged(firstInputLayout)
                afterInputNumberChanged {
                    if (priceInput.text.getNumber() > 1) {
                        if (it != null) {
                            firstInputLayout.error =
                                when {
                                    it < 300000 -> getString(R.string.form_dp)
                                    it >= priceInput.text.getNumber() -> getString(R.string.form_dp_lessThan_harga)
                                    else -> null
                                }
                        }
                    } else {
                        firstInputLayout.error = getString(R.string.filled_price)
                    }
                }
            }
            periodPayInput.afterInputNumberChanged {
                if (it != null) {
                    periodInputLayout.error =
                        when {
                            it > 12 -> getString(R.string.form_date_tenggat)
                            else -> null
                        }
                }
            }
            tenggatPayInput.afterInputNumberChanged {
                if (it != null) {
                    tenggatInputLayout.error =
                        when {
                            it > 30 -> getString(R.string.form_date_periode)
                            else -> null
                        }
                }
            }
            setReminderSwitch.setOnCheckedChangeListener { _, state ->
                if (state) {
                    setReminderContent.apply {
                        visibility = View.VISIBLE
                        setTitleItem(currentInstant.format("d MMM yy HH:mm"))
                        setContentItem("Kustom")
                    }
                } else {
                    setReminderContent.apply {
                        visibility = View.GONE
                    }
                }
            }
        }

    private fun loadData(item: ModalForm) =
        with(binding) {
            if (item.image != null) {
                bgImage.visibility = View.GONE
                saveImage.apply {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setImageURI(item.image!!.toUri())
                }
            }
            nameInput.apply {
                setText("item.namaPenyicil") // FIXME: string literal
//                requestFocus()
                afterInputStringChanged { saveButton.isEnabled = this.text.toString().isNotEmpty() }
            }
            thingInput.setText(item.thing)
            categoryInput.setText(item.category, false)
            priceInput.digitNumberArranged(priceInputLayout)
            priceInput.setText(item.price.toString())
            firstPayInput.digitNumberArranged(firstInputLayout)
            firstPayInput.setText(item.uangMuka.toString())
            periodPayInput.setText(item.period)
            tenggatPayInput.setText(item.tenggatBayar)
        }

    private fun doSave() =
        with(binding) {
            val kategoriInput = categoryInput.text.toString()
            val penyicil = nameInput.text.toString()
            val barang = thingInput.text.toString()
            val harga = priceInput.text.getNumber()
            val dp = firstPayInput.text.getNumber()
            val periode = periodPayInput.text.getNumber()
            val tenggat = tenggatPayInput.text.toString()

            when {
                penyicil.isBlank() -> {
                    nameInputLayout.error = getString(R.string.fill_data)
                }

                barang.isBlank() -> {
                    thingInputLayout.error = getString(R.string.fill_data)
                }

                kategoriInput.isBlank() -> {
                    categoryInputLayout.error = getString(R.string.fill_data)
                }

                harga < 1 -> {
                    priceInputLayout.error = getString(R.string.fill_data)
                }

                harga < 500000 -> {
                    priceInputLayout.error = getString(R.string.form_min_harga)
                }

                harga > 10000000 -> {
                    priceInputLayout.error = getString(R.string.form_max_harga)
                }

                dp < 1 -> {
                    firstInputLayout.error = getString(R.string.fill_data)
                }

                dp < 300000 -> {
                    firstInputLayout.error = getString(R.string.form_dp)
                }

                dp > harga -> {
                    firstInputLayout.error = getString(R.string.form_dp_lessThan_harga)
                }

                periode < 1 -> {
                    periodInputLayout.error = getString(R.string.fill_data)
                }

                periode > 30 -> {
                    periodInputLayout.error = getString(R.string.form_date_tenggat)
                }

                tenggat.isBlank() -> {
                    tenggatInputLayout.error = getString(R.string.fill_data)
                }

                tenggat.toInt() > 12 -> {
                    tenggatInputLayout.error = getString(R.string.form_date_periode)
                }

                else -> {
                    val imageUri = viewModel.imageUri?.toString().takeIf { it != null }
                    val form =
                        ModalForm(
                            args.item?.id,
                            imageUri,
                            penyicil,
                            barang,
                            kategoriInput,
                            harga,
                            dp,
                            periode,
                            tenggat.toInt(),
                        )

                    viewModel.save(form)
                    findNavController().popBackStack()
                }
            }
        }

    private fun openImageCropFragment(uri: Uri?) {
        uri ?: return
        findNavController().navigate(FormFragmentDirections.actionFormToImageCrop(uri))
    }

    private fun showImageChooser() {
        val form = DialogPickImageBinding.inflate(layoutInflater)
        val dialogChooser =
            BottomSheetDialog(requireContext()).apply {
                setContentView(form.root)
                behavior.maxHeight = 480.dotPixel()
                dismissWithAnimation = true
            }

        with(form) {
            root.doOnPreDraw { dialogChooser.behavior.peekHeight = it.height }
            cameraButton.setOnClickListener {
                dialogChooser.dismiss()
                fromCamera.launch("image-captured.jpg")
            }
            galeriButton.setOnClickListener {
                dialogChooser.dismiss()
                fromStorage.launch("image/*")
            }
        }
        dialogChooser.show()
    }

    companion object {
        const val CROP_IMAGE_RESULT = "crop-image-result"
        const val CROPPED_BITMAP = "cropped-bitmap"
    }
}
