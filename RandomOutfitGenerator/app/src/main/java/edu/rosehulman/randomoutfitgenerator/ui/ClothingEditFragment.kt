package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentClothingEditBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory

class ClothingEditFragment: Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentClothingEditBinding
    private var model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClothingEditBinding.inflate(inflater, container, false)

        val superCatAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_selected, SuperCategory.stringArray() as List<String>)
        superCatAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown)
        binding.superCatSpinner.setAdapter(superCatAdapter)
        binding.superCatSpinner.onItemSelectedListener = this


        return binding.root
    }

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