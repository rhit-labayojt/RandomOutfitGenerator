package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClothingEditBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory

class ClothingEditFragment: Fragment() {
    private lateinit var binding: FragmentClothingEditBinding
    private lateinit var model: ClosetViewModel
    private var newSuperCat = ""
    private var newSubCat = ""

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
                        model.deleteCurrentItem()
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

        setHasOptionsMenu(true)

        binding.clothingEditImage.load(model.currentItem.getImage()) {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }

        setInitialSpinnerValues()
        setupSpinnerAdapters()
        addSpinnerListeners()

        return binding.root
    }

    private fun setInitialSpinnerValues(){
        binding.superCatSpinner.setSelection(SuperCategory.stringArray().indexOfFirst { it == SuperCategory.enumToString(model.currentItem.getSuperCat()) })

        when(model.currentItem.getSuperCat()){
            SuperCategory.Top -> binding.subCatSpinner.setSelection(model.closet.topsTags.indexOfFirst { it == model.currentItem.getSubCat() })
            SuperCategory.Bottom -> binding.subCatSpinner.setSelection(model.closet.bottomsTags.indexOfFirst { it == model.currentItem.getSubCat() })
            SuperCategory.Accessory -> binding.subCatSpinner.setSelection(model.closet.accessoriesTags.indexOfFirst { it == model.currentItem.getSubCat() })
            SuperCategory.Shoes -> binding.subCatSpinner.setSelection(model.closet.shoesTags.indexOfFirst { it == model.currentItem.getSubCat() })
            else -> binding.subCatSpinner.setSelection(model.closet.fullBodyTags.indexOfFirst { it == model.currentItem.getSubCat() })
        }
    }

    private fun setupSpinnerAdapters(){
        val superCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            SuperCategory.stringArray()
        )
        superCatAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
        binding.superCatSpinner.adapter = superCatAdapter

        setSubCatAdapter(model.currentItem.getSuperCat())
    }

    private fun setSubCatAdapter(superCat: SuperCategory){
        val subCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            when(superCat){
                SuperCategory.Top -> model.closet.topsTags
                SuperCategory.Bottom -> model.closet.bottomsTags
                SuperCategory.Shoes -> model.closet.shoesTags
                SuperCategory.Accessory -> model.closet.accessoriesTags
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
        model.currentItem.setSuperCat(newSuperCat)
        model.currentItem.setSubCat(newSubCat)
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
            setSubCatAdapter(SuperCategory.stringToEnum(newSuperCat))
        }

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>?) {
            newSuperCat = SuperCategory.enumToString(model.currentItem.getSuperCat())
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
            val subCat = binding.subCatSpinner.getItemAtPosition(position)
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
            newSubCat = model.currentItem.getSubCat()
        }
    }
}
