package edu.rosehulman.randomoutfitgenerator.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClothingEditBinding
import edu.rosehulman.randomoutfitgenerator.models.Closet
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.User
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel
import edu.rosehulman.randomoutfitgenerator.objects.Clothing

class ClothingEditFragment: Fragment() {
    private lateinit var binding: FragmentClothingEditBinding
    private lateinit var model: ClosetViewModel
    private lateinit var userModel: UserViewModel
    private var newSuperCat = ""
    private var newSubCat = ""
    private lateinit var checkedStyles: BooleanArray
    private var checkedWeathers = BooleanArray(Closet.weathers.size){false}
    private var originalStyles = ArrayList<String>()
    private var originalWeathers = ArrayList<String>()
    private var newItem = Clothing()

    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()){ isSuccess ->
            if(isSuccess){
                model.latestTmpUri?.let{uri ->
                    model.addPhotoFromUri(this, uri)
                }
            }
        }

    companion object{
        const val fragmentName = "ClothingEdit"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_clothing_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save_clothing -> {
                Log.d(Constants.TAG, "Trying to save item")
                saveClothing()
                findNavController().navigate(R.id.nav_closet)
                true
            }
            R.id.delete_clothing -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Are you sure?")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(android.R.string.ok){dialog, which ->
                        if(model.isNewImage){
                            model.isNewImage = false
                            Log.d(Constants.TAG, "New Item not Saved")
                        }else{
                            model.deleteCurrentClothing()
                        }
                        findNavController().popBackStack()
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                true
            }

            else -> {
                if(model.isNewImage){
                    model.isNewImage = false
                    model.deleteCurrentClothing()
                }else{
                    model.getCurrentItem().resetWeathers(originalWeathers)
                    model.getCurrentItem().resetStyles(originalStyles)
                }
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClothingEditBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        model.cameraTriggeredFragment = R.id.nav_clothing_edit
        checkedStyles = BooleanArray(userModel.user!!.styles.size){false}

        if(model.isNewImage){
            newItem = Clothing(
                Closet.superCategories[0],
                userModel.user!!.topsTags[0],
                arrayListOf<String>(),
                arrayListOf<String>(),
                model.newImageUri
            )

            originalWeathers = arrayListOf()
            originalStyles = arrayListOf()

            binding.clothingEditImage.load(newItem.image) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
        }else {
            originalWeathers = model.getCurrentItem().getWeathers()
            originalStyles = model.getCurrentItem().getStyles()

            binding.clothingEditImage.load(model.getCurrentItem().image) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

        setHasOptionsMenu(true)

        setupSpinnerAdapters()
        setInitialSpinnerValues()
        addSpinnerListeners()
        setupTextViews()

        binding.clothingEditImage.setOnClickListener {
            if(model.isNewImage){
                model.updateClothing(newItem)
                model.addClothingListener(fragmentName){
                    model.updateCurrentItem(model.closet.clothing.indexOfFirst { it.image == newItem.image })
                }
            }

            takePhoto()
        }

        return binding.root
    }

    private fun setInitialSpinnerValues(){

        if(model.isNewImage){
            binding.superCatSpinner.setSelection(0)
            binding.subCatSpinner.setSelection(0)
        }else {
            Log.d(Constants.TAG, "${model.getCurrentItem().getSubCat()}")
            Log.d(Constants.TAG, "Tag Indes = ${userModel.user!!.topsTags.indexOf(model.getCurrentItem().getSubCat())}")
            var currentItem = model.getCurrentItem()
            binding.superCatSpinner.setSelection(Closet.superCategories.indexOfFirst {
                it == currentItem.getSuperCat()
            }, true)

            when (model.getCurrentItem().getSuperCat()) {
                Closet.superCategories[0] -> binding.subCatSpinner.setSelection(userModel.user!!.topsTags.indexOf(currentItem.getSubCat()))
                Closet.superCategories[1] -> binding.subCatSpinner.setSelection(userModel.user!!.bottomsTags.indexOf(currentItem.getSubCat()))
                Closet.superCategories[2] -> binding.subCatSpinner.setSelection(userModel.user!!.accessoriesTags.indexOf(currentItem.getSubCat()))
                Closet.superCategories[3] -> binding.subCatSpinner.setSelection(userModel.user!!.shoesTags.indexOf(currentItem.getSubCat()))
                else -> binding.subCatSpinner.setSelection(userModel.user!!.fullBodyTags.indexOf(currentItem.getSubCat()))
            }
        }
    }

    private fun setupSpinnerAdapters(){
        val superCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            Closet.superCategories
        )
        superCatAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
        binding.superCatSpinner.adapter = superCatAdapter

        if(model.isNewImage){
            setSubCatAdapter(newItem.getSuperCat())
        }else {
            setSubCatAdapter(model.getCurrentItem().getSuperCat())
        }
}

    private fun setSubCatAdapter(superCat: String){
        val subCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            when(superCat){
                Closet.superCategories[0] -> userModel.user!!.topsTags
                Closet.superCategories[1] -> userModel.user!!.bottomsTags
                Closet.superCategories[2] -> userModel.user!!.accessoriesTags
                Closet.superCategories[3]-> userModel.user!!.shoesTags
                else -> userModel.user!!.fullBodyTags
            }
        )
        subCatAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
        binding.subCatSpinner.adapter = subCatAdapter
    }

    private fun addSpinnerListeners(){
        binding.superCatSpinner.onItemSelectedListener = SuperCatListener()
        binding.subCatSpinner.onItemSelectedListener = SubCatListener()
    }

    private fun saveClothing(){
        if(model.isNewImage){
            newItem.setSuperCat(newSuperCat)
            newItem.setSubCat(newSubCat)
        }else {
            if(newSuperCat == ""){
                newSuperCat = model.getCurrentItem().getSuperCat()
            }
            model.getCurrentItem().setSuperCat(newSuperCat)
            model.getCurrentItem().setSubCat(newSubCat)
        }
        model.updateClothing(newItem)
    }

    private fun setupTextViews(){
        var styles = Array<String>(userModel.user!!.styles.size){""}
        for(i in 0 until userModel.user!!.styles.size){
            styles[i] = userModel.user!!.styles[i]
        }

        if(model.isNewImage){
            findCheckedItems(newItem.getStyles(), styles, checkedStyles)
            findCheckedItems(newItem.getWeathers(), Closet.weathers, checkedWeathers)

            binding.stylesOptions.text = "Styles: ${model.closet.toString(newItem.getStyles())}"
            binding.weatherOptions.text = "Weathers: ${model.closet.toString(newItem.getWeathers())}"
        }else {
            findCheckedItems(model.getCurrentItem().getStyles(), styles, checkedStyles)
            findCheckedItems(model.getCurrentItem().getWeathers(), Closet.weathers, checkedWeathers)

            binding.stylesOptions.text = "Styles: ${model.closet.toString(model.getCurrentItem().getStyles())}"
            binding.weatherOptions.text = "Weathers: ${model.closet.toString(model.getCurrentItem().getWeathers())}"
        }

        binding.stylesOptions.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Styles")
                .setMultiChoiceItems(styles, checkedStyles, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->

                    if(model.isNewImage){
                        if (isChecked) {
                            newItem.addStyle(userModel.user!!.styles[which])
                        } else {
                            newItem.removeStyle(userModel.user!!.styles[which])
                        }

                        binding.stylesOptions.text =
                            "Styles: ${model.closet.toString(newItem.getStyles())}"
                    }else {
                        if (isChecked) {
                            model.getCurrentItem().addStyle(userModel.user!!.styles[which])
                        } else {
                            model.getCurrentItem().removeStyle(userModel.user!!.styles[which])
                        }

                        binding.stylesOptions.text =
                            "Styles: ${model.closet.toString(model.getCurrentItem().getStyles())}"
                    }
                })
                .show()

        }

        binding.weatherOptions.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Weathers")
                .setMultiChoiceItems(Closet.weathers, checkedWeathers, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->

                    if (model.isNewImage) {
                        if (isChecked) {
                            newItem.addWeather(Closet.weathers[which])
                        } else {
                            newItem.removeWeather(Closet.weathers[which])
                        }

                        binding.weatherOptions.text =
                            "Weathers: ${model.closet.toString(newItem.getWeathers())}"
                    }else{
                        if (isChecked) {
                            model.getCurrentItem().addWeather(Closet.weathers[which])
                        } else {
                            model.getCurrentItem().removeWeather(Closet.weathers[which])
                        }

                        binding.weatherOptions.text =
                            "Weathers: ${model.closet.toString(model.getCurrentItem().getWeathers())}"
                }
                })
                .show()

        }
    }

    private fun findCheckedItems(clothingItems: ArrayList<String>, closetItems: Array<String>, itemsChecked: BooleanArray){
        for (item in 0 until closetItems.size) {
            itemsChecked[item] = clothingItems.contains(closetItems[item])
        }
    }

    private fun takePhoto(){
        model.isNewImage = false

        lifecycleScope.launchWhenStarted{
            model.getTmpFileUri(fragment = this@ClothingEditFragment).let { uri ->
                model.latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    inner class SuperCatListener: AdapterView.OnItemSelectedListener{
        /**
         *
         * Callback method to be invoked when an item in this view has been
         * selected. This callback is invoked only when the newly selected
         * position is different from the previously selected position or if
         * there was no selected item.
         *
         * Implementers can call getItemAtPosition(position) if they need to access the
         * data associated with the selected item.
         *
         * @param parent The AdapterView where the selection happened
         * @param view The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id The row id of the item that is selected
         */
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val superCat = binding.superCatSpinner.getItemAtPosition(position)
            newSuperCat = superCat as String
            setSubCatAdapter(newSuperCat)
        }

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>?) {
            if(model.isNewImage){
                newSuperCat = newItem.getSuperCat()
            }else {
                newSuperCat = model.getCurrentItem().getSuperCat()
            }
        }


    }

    inner class SubCatListener: AdapterView.OnItemSelectedListener{
        /**
         *
         * Callback method to be invoked when an item in this view has been
         * selected. This callback is invoked only when the newly selected
         * position is different from the previously selected position or if
         * there was no selected item.
         *
         * Implementers can call getItemAtPosition(position) if they need to access the
         * data associated with the selected item.
         *
         * @param parent The AdapterView where the selection happened
         * @param view The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id The row id of the item that is selected
         */
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val subCat = binding.subCatSpinner.adapter.getItem(position)
            newSubCat = subCat as String
        }

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>?) {
            if(model.isNewImage){
                newSubCat = newItem.getSubCat()
            }else {
                newSubCat = model.getCurrentItem().getSubCat()
            }
        }
    }

}
