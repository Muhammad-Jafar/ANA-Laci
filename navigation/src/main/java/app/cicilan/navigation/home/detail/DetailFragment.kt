package app.cicilan.navigation.home.detail

import android.content.res.ColorStateList.valueOf
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.cicilan.component.utils.addAutoConverterToMoneyFormat
import app.cicilan.component.utils.afterInputNumberChanged
import app.cicilan.component.utils.currentInstant
import app.cicilan.component.utils.dotPixel
import app.cicilan.component.utils.format
import app.cicilan.component.utils.getNumber
import app.cicilan.component.utils.popupDialog
import app.cicilan.component.utils.runWhenResumed
import app.cicilan.component.utils.showMessage
import app.cicilan.component.utils.showSoftKeyboard
import app.cicilan.component.utils.toRupiah
import app.cicilan.entities.Item
import app.cicilan.entities.ItemLog
import app.cicilan.navigation.BaseFragment
import app.cicilan.navigation.DetailViewModel
import app.cicilan.navigation.R
import app.cicilan.navigation.databinding.DialogInputNominalBinding
import app.cicilan.navigation.databinding.MainDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class DetailFragment : BaseFragment<MainDetailBinding>(MainDetailBinding::inflate) {
    private val viewModel: DetailViewModel by viewModel()
    private val args: DetailFragmentArgs by navArgs()

    override fun renderView(bundle: Bundle?) {
        binding.toolbarDetail.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            inflateMenu(R.menu.detail_option_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        val status = viewModel.getCicilanById(args.cicilanId).value.status
                        if (status == "YES") {
                            showMessage(getString(R.string.edit_data_lunas))
                        }
                        findNavController()
                            .navigate(DetailFragmentDirections.actionDetailBerjalanToForm())
                    }

                    R.id.delete -> {
                        popupDialog(
                            getString(R.string.delete_button),
                            getString(R.string.confirm_delete_data),
                        ).setPositiveButton(getString(R.string.delete_button)) { dialog, _ ->
                            viewModel.deleteCicilan(args.cicilanId)
                            dialog.dismiss()
                            showMessage(getString(R.string.deleted_successfully))
                            findNavController().navigateUp()
                        }.show()
                    }

                    R.id.log -> {
                        val destinationLog = DetailFragmentDirections.actionDetailBerjalanToLog(args.cicilanId)
                        findNavController().navigate(destinationLog)
                    }
                }
                true
            }
        }

        runWhenResumed {
            viewModel
                .getCicilanById(args.cicilanId)
                .collect { setData(it) }
        }
    }

    private fun setData(data: Item) {
        with(binding) {
            with(data) {
                // Header section
                toolbarDetail.title = name
                avatarPreview.apply {
                    scaleType =
                        if (image != null) {
                            setImageURI(image?.toUri())
                            ImageView.ScaleType.CENTER_CROP
                        } else {
                            setImageResource(R.drawable.icon_bg_image)
                            ImageView.ScaleType.CENTER
                        }
                }
                sectionName.setContentLayout(name)
                sectionCategory.setContentLayout(category)
                sectionThingPrice.setContentLayout(price.toRupiah())
                sectionFirstPay.setContentLayout(uangMuka.toRupiah())
                sectionCreatedAt.setContentLayout(createdAt.format("d.MM.YY"))
                sectionDoneAt.setContentLayout(doneAt?.toString() ?: "-")

                // Progressbar section
                val persen =
                    if (nominalLunas != 0) {
                        ((nominalLunas.toFloat() / nominalBayar.toFloat()) * 100F).roundToInt()
                    } else {
                        0
                    }
                dataPersen.text = persen.toString()
                targetProgressBar.apply {
                    max = nominalBayar
                    setProgressCompat(nominalLunas, true)
                }
                sectionLunas.apply {
                    setTextColor(valueOf(MaterialColors.getColor(rootView, R.attr.colorOnLeafContainer)))
                    setContentLayout(nominalLunas.toRupiah())
                }
                sectionLunasYet.apply {
                    setTextColor(valueOf(MaterialColors.getColor(rootView, R.attr.colorOnRoseContainer)))
                    setContentLayout((nominalBayar - nominalLunas).toRupiah())
                }

                // Laba section
                sectionLabaPerBulan.setContentItem(labaPerBulan.toRupiah() + getString(R.string.per_bulan))
                sectionTotalLaba.setContentItem("+ ".plus(totalLaba))

                // Debt section
                sectionNominalPerBulan.setContentItem(nominalPerBulan.toRupiah())
                sectionTotalPerBulan.setContentItem(nominalPerBulan.toRupiah())
                sectionPeriode.setContentItem(period.toString())
                sectionTenggat.setContentItem(tenggatBayar.toString())

                payDebtButton.apply {
                    if (data.status == "YES") {
                        isClickable = false
                        text = getString(R.string.lunas_button_desc)
                    } else {
                        setOnClickListener {
                            pay(nominalLunas, nominalBayar, nominalPerBulan)
                        }
                    }
                }
            }
        }
    }

    private fun pay(
        lunas: Int,
        utang: Int,
        nominalBayar: Int,
    ) {
        val inputForm = DialogInputNominalBinding.inflate(layoutInflater)
        val dialog =
            BottomSheetDialog(requireContext()).apply {
                setContentView(inputForm.root)
                behavior.maxHeight = 800.dotPixel()
                dismissWithAnimation = true
            }

        with(inputForm) {
            root.doOnPreDraw { dialog.behavior.peekHeight = it.height }

            fun validateInput(): Boolean =
                nominalInput.text.getNumber().let {
                    if (it < (utang - lunas)) {
                        nominalInputLayout.error =
                            when {
                                it < 1 -> getString(R.string.fill_data)
                                it < 100000 -> getString(R.string.input_min_limit)
                                it > (utang - lunas) -> getString(R.string.input_fill_over)
                                else -> return true
                            }
                    } else {
                        nominalInputLayout.error =
                            when {
                                it > (utang - lunas) -> getString(R.string.input_fill_over)
                                else -> return true
                            }
                    }
                    return true
                }
            with(nominalInput) {
                requestFocus()
                showSoftKeyboard()
                afterInputNumberChanged {
                    doInputButton.isEnabled = nominalInput.text.getNumber() > 1
                    validateInput()
                }
                addAutoConverterToMoneyFormat(nominalInputLayout)
                hint = nominalBayar.toRupiah()
            }

            noteInput.apply {
                hint = getString(R.string.note_here)
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        nominalInput.text.getNumber().let {
                            if (it < (utang - lunas)) {
                                when {
                                    it < 1 -> {
                                        getString(R.string.fill_data)
                                    }

                                    it < 100000 -> {
                                        showMessage(getString(R.string.input_min_limit))
                                    }

                                    it > (utang - lunas) -> {
                                        showMessage(getString(R.string.input_fill_over))
                                    }

                                    else -> {
                                        storeLog(it, noteInput.text.toString())
                                        dialog.dismiss()
                                    }
                                }
                            } else {
                                when {
                                    it > (utang - lunas) -> {
                                        getString(R.string.input_fill_over)
                                    }

                                    else -> {
                                        storeLog(it, noteInput.text.toString())
                                        dialog.dismiss()
                                    }
                                }
                            }
                        }
                    }
                    false
                }
                doInputButton.setOnClickListener {
                    nominalInput.text.getNumber().let {
                        if (it < (utang - lunas)) {
                            nominalInputLayout.apply {
                                startAnimation(
                                    AnimationUtils.loadAnimation(
                                        requireContext(),
                                        R.anim.shake,
                                    ),
                                )
                                // vibrate()
                            }
                            when {
                                it < 1 -> {
                                    getString(R.string.fill_data)
                                }

                                it < 100000 -> {
                                    getString(R.string.input_min_limit)
                                }

                                it > (utang - lunas) -> {
                                    getString(R.string.input_fill_over)
                                }

                                else -> {
                                    storeLog(it, noteInput.text.toString())
                                    dialog.dismiss()
                                }
                            }
                        } else {
                            nominalInputLayout.apply {
                                startAnimation(
                                    AnimationUtils.loadAnimation(
                                        requireContext(),
                                        R.anim.shake,
                                    ),
                                )
                                // vibrate()
                            }
                            when {
                                it > (utang - lunas) -> {
                                    getString(R.string.input_fill_over)
                                }

                                else -> {
                                    storeLog(it, noteInput.text.toString())
                                    dialog.dismiss()
                                }
                            }
                        }
                    }
                }
            }
            cancelButton.setOnClickListener { dialog.dismiss() }
        }
        dialog.show()
    }

    private fun storeLog(
        amount: Int,
        note: String,
    ) {
        val log =
            ItemLog(
                null,
                args.cicilanId,
                currentInstant,
                amount,
                note,
            )
        viewModel.storeCicilanLog(log)
    }
}
