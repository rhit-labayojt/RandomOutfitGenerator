package edu.rosehulman.randomoutfitgenerator.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClothingEditBinding
import edu.rosehulman.randomoutfitgenerator.models.Closet
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class ClothingEditFragment: Fragment() {
    private lateinit var binding: FragmentClothingEditBinding
    private lateinit var model: ClosetViewModel
    private var newSuperCat = ""
    private var newSubCat = ""
    private lateinit var checkedStyles: BooleanArray
    private var checkedWeathers = BooleanArray(Closet.weathers.size){false}
    private var stylesToAdd = ArrayList<String>()
    private var stylesToRemove = ArrayList<String>()
    private var weathersToAdd = ArrayList<String>()
    private var weathersToRemove = ArrayList<String>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_clothing_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save_clothing -> {
                saveClothing()
                findNavController().navigate(R.id.nav_closet)
                true
            }
            R.id.delete_clothing -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Are you sure?")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(android.R.string.ok){dialog, which ->
                        model.deleteCurrentClothing()
                        findNavController().popBackStack()
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClothingEditBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)
        checkedStyles = BooleanArray(model.closet.styles.size){false}

        setHasOptionsMenu(true)

        binding.clothingEditImage.load(model.getCurrentItem().getImage()) {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }

        setupSpinnerAdapters()
        setInitialSpinnerValues()
        addSpinnerListeners()
        setupTextViews()

        return binding.root
    }

    private fun setInitialSpinnerValues(){
        binding.superCatSpinner.setSelection(Closet.superCategories.indexOfFirst { it == model.getCurrentItem().getSuperCat() })

        when(model.getCurrentItem().getSuperCat()){
            Closet.superCategories[0] -> binding.subCatSpinner.setSelection(model.closet.topsTags.indexOfFirst { it == model.getCurrentItem().getSubCat() })
            Closet.superCategories[1] -> binding.subCatSpinner.setSelection(model.closet.bottomsTags.indexOfFirst { it == model.getCurrentItem().getSubCat() })
            Closet.superCategories[2]-> binding.subCatSpinner.setSelection(model.closet.accessoriesTags.indexOfFirst { it == model.getCurrentItem().getSubCat() })
            Closet.superCategories[3] -> binding.subCatSpinner.setSelection(model.closet.shoesTags.indexOfFirst { it == model.getCurrentItem().getSubCat() })
            else -> binding.subCatSpinner.setSelection(model.closet.fullBodyTags.indexOfFirst { it == model.getCurrentItem().getSubCat() })
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

        setSubCatAdapter(model.getCurrentItem().getSuperCat())
    }

    private fun setSubCatAdapter(superCat: String){
        val subCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            when(superCat){
                Closet.superCategories[0] -> model.closet.topsTags
                Closet.superCategories[1] -> model.closet.bottomsTags
                Closet.superCategories[2] -> model.closet.accessoriesTags
                Closet.superCategories[3]-> model.closet.shoesTags
                else -> model.closet.fullBodyTags
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
        model.getCurrentItem().setSuperCat(newSuperCat)
        model.getCurrentItem().setSubCat(newSubCat)

        stylesToAdd.forEach { model.getCurrentItem().addStyle(it) }
        stylesToRemove.forEach { model.getCurrentItem().removeStyle(it) }

        weathersToAdd.forEach { model.getCurrentItem().addWeather(it) }
        weathersToRemove.forEach { model.getCurrentItem().removeWeather(it) }
        model.updateClothing()
    }

    private fun setupTextViews(){
        findCheckedItems(model.getCurrentItem().getStyles(), model.closet.styles as Array<String>, checkedStyles)
        findCheckedItems(model.getCurrentItem().getWeather(), Closet.weathers, checkedWeathers)

        binding.stylesOptions.text = "Styles: ${model.closet.toString(model.getCurrentItem().getStyles())}"
        binding.weatherOptions.text = "Weathers: ${model.closet.toString(model.getCurrentItem().getWeather())}"

        binding.stylesOptions.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Styles")
                .setMultiChoiceItems(model.closet.styles as Array<String>, checkedStyles, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        stylesToAdd.add(model.closet.styles.get(which))
                    }else{
                        stylesToRemove.add(model.closet.styles.get(which))
                    }
                })
                .show()

        }

        binding.weatherOptions.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Weathers")
                .setMultiChoiceItems(Closet.weathers, checkedWeathers, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        weathersToAdd.add(Closet.weathers.get(which))
                    }else{
                        weathersToRemove.add(Closet.weathers.get(which))
                    }
                })
                .show()

        }
    }

    private fun findCheckedItems(clothingItems: ArrayList<String>, closetItems: Array<String>, itemsChecked: BooleanArray){
        for(item in 0 until closetItems.size){
            itemsChecked[item] = clothingItems.contains(closetItems[item])
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
            newSuperCat = model.getCurrentItem().getSuperCat()
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
            newSubCat = model.getCurrentItem().getSubCat()
        }
    }
}
