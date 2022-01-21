package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClothingEditBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory

class ClothingEditFragment: Fragment() {
    private lateinit var binding: FragmentClothingEditBinding
    private lateinit var model: ClosetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClothingEditBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)

        binding.clothingEditImage.load(model.currentItem.getImage()) {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }

        setInitialSpinnerValues()
        setupSpinnerAdapters()
        addSpinnerListeners()

        return binding.root
    }

    fun setInitialSpinnerValues(){
        binding.superCatSpinner.setSelection(SuperCategory.stringArray().indexOfFirst { it == SuperCategory.enumToString(model.currentItem.getSuperCat()) })

        when(model.currentItem.getSuperCat()){
            SuperCategory.Top -> binding.subCatSpinner.setSelection(model.closet.topsTags.indexOfFirst { it == model.currentItem.getSubCat() })
            SuperCategory.Bottom -> binding.subCatSpinner.setSelection(model.closet.bottomsTags.indexOfFirst { it == model.currentItem.getSubCat() })
            SuperCategory.Accessory -> binding.subCatSpinner.setSelection(model.closet.accessoriesTags.indexOfFirst { it == model.currentItem.getSubCat() })
            SuperCategory.Shoes -> binding.subCatSpinner.setSelection(model.closet.shoesTags.indexOfFirst { it == model.currentItem.getSubCat() })
            else -> binding.subCatSpinner.setSelection(model.closet.fullBodyTags.indexOfFirst { it == model.currentItem.getSubCat() })
        }
    }

    fun setupSpinnerAdapters(){
        val superCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            SuperCategory.stringArray()
        )
        superCatAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
        binding.superCatSpinner.adapter = superCatAdapter

        val subCatAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_selected,
            when(model.currentItem.getSuperCat()){
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

    fun addSpinnerListeners(){
        binding.superCatSpinner.onItemSelectedListener = SuperCatListener()
        binding.subCatSpinner.onItemSelectedListener = SubCatListener()
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
            model.currentItem.setSuperCat(superCat as String)
        }

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
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
            model.currentItem.setSubCat(subCat as String)
        }

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
}
