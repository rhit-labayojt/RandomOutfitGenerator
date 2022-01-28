package edu.rosehulman.randomoutfitgenerator.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentRandomizationBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class RandomizationFragment: Fragment() {
    private lateinit var binding: FragmentRandomizationBinding
    private lateinit var model: ClosetViewModel
    private var topType = ""

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_randomization, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRandomizationBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)
        setHasOptionsMenu(true)

        val a = ArrayAdapter(
            requireContext(),
            R.layout.randomization_item_dropdown,
            model.closet.topsTags.keys.toTypedArray()
        )
        binding.randomTop.setAdapter(a)
        binding.randomTop.onItemClickListener = topTypeListener()

        return binding.root
    }

    inner class topTypeListener: AdapterView.OnItemClickListener{
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         *
         *
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent The AdapterView where the click happened.
         * @param view The view within the AdapterView that was clicked (this
         * will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            binding.randomTop.setTextColor(getColor(R.color.tiffany_blue))
            binding.randomTop.setTypeface(Typeface.DEFAULT_BOLD)
            val type = binding.randomTop.adapter.getItem(position)
            topType = type as String
        }


    }
}