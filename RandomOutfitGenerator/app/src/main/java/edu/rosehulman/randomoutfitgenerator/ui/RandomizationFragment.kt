package edu.rosehulman.randomoutfitgenerator.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.color.MaterialColors
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentRandomizationBinding
import edu.rosehulman.randomoutfitgenerator.models.Closet
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel

class RandomizationFragment: Fragment() {
    private lateinit var binding: FragmentRandomizationBinding
    private lateinit var model: ClosetViewModel

    private var topType = ""
    private var bottomType = ""
    private var fullBodyType = ""
    private var shoesType = ""

    private lateinit var accessoriesTypes: MutableSet<String>
    private lateinit var styleTypes: MutableSet<String>
    private lateinit var weatherTypes: MutableSet<String>

    private lateinit var checkedAcc: BooleanArray
    private lateinit var checkedStyles: BooleanArray
    private lateinit var checkedWeathers: BooleanArray

    private var views = ArrayList<View>()
    private var lists = ArrayList<Map<String, Boolean>>()
    private var itemClickListeners = ArrayList<AdapterView.OnItemClickListener>()
    private var onClickListeners = ArrayList<View.OnClickListener>()

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

        checkedAcc = BooleanArray(model.closet.accessoriesTags.size){false}
        checkedStyles = BooleanArray(model.closet.styles.size){false}
        checkedWeathers = BooleanArray(Closet.weathers.size){false}

        toggleVisibilities(binding.fullBodyOutfitToggle.isChecked)
        populateArrayLists()
        setupDropdownViews()
        setupAutoCompleteTextViewDialogs()

        binding.fullBodyOutfitToggle.setOnCheckedChangeListener(FullBodySwitchListener())

        return binding.root
    }

    private fun updateView(view: AutoCompleteTextView, list: MutableSet<String>){
        view.setTypeface(Typeface.DEFAULT_BOLD)
        view.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
        view.setText("${model.closet.toString(list)}")
    }

    private fun populateArrayLists(){
        accessoriesTypes = mutableSetOf()
        styleTypes = mutableSetOf()
        weatherTypes = mutableSetOf()

        views.add(binding.randomTop)
        views.add(binding.randomBottom)
        views.add(binding.randomFullBody)
        views.add(binding.randomShoes)
        views.add(binding.randomAccessories)
        views.add(binding.randomStyles)
        views.add(binding.randomWeathers)

        lists.add(model.closet.topsTags)
        lists.add(model.closet.bottomsTags)
        lists.add(model.closet.fullBodyTags)
        lists.add(model.closet.shoesTags)
        lists.add(model.closet.accessoriesTags)
        lists.add(model.closet.styles)
        lists.add(Closet.weathers)

        itemClickListeners.add(TopTypeListener())
        itemClickListeners.add(BottomTypeListener())
        itemClickListeners.add(FullBodyTypeListener())
        itemClickListeners.add(ShoesTypeListener())

        onClickListeners.add(AccessoriesTypeListener())
        onClickListeners.add(StylesTypeListener())
        onClickListeners.add(WeatherTypeListener())
    }

    private fun setupDropdownViews(){
        for(view in 0 until 4){
            val a = ArrayAdapter(
                requireContext(),
                R.layout.randomization_item_dropdown,
                lists[view].keys.toTypedArray()
            )

            var item = views[view] as AutoCompleteTextView
            item.setAdapter(a)
            item.onItemClickListener = itemClickListeners[view]
        }
    }

    private fun setupAutoCompleteTextViewDialogs(){
        for(view in 4 until 7){
            var item = views[view] as AutoCompleteTextView
            item.setOnClickListener(onClickListeners[view - 4])
        }
    }

    private fun toggleVisibilities(isChecked: Boolean){
        if(isChecked){
            binding.randomTopParent.visibility = View.GONE
            binding.randomBottomParent.visibility = View.GONE
            binding.randomFullBodyParent.visibility = View.VISIBLE
        }else{
            binding.randomTopParent.visibility = View.VISIBLE
            binding.randomBottomParent.visibility = View.VISIBLE
            binding.randomFullBodyParent.visibility = View.GONE
        }
    }

    inner class TopTypeListener: AdapterView.OnItemClickListener{
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
            binding.randomTop.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
            binding.randomTop.setTypeface(Typeface.DEFAULT_BOLD)

            val type = binding.randomTop.adapter.getItem(position)
            topType = type as String
        }


    }

    inner class BottomTypeListener: AdapterView.OnItemClickListener{
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
            binding.randomBottom.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
            binding.randomBottom.setTypeface(Typeface.DEFAULT_BOLD)

            val type = binding.randomBottom.adapter.getItem(position)
            bottomType = type as String
        }


    }

    inner class FullBodyTypeListener: AdapterView.OnItemClickListener{
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
            binding.randomFullBody.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
            binding.randomFullBody.setTypeface(Typeface.DEFAULT_BOLD)

            val type = binding.randomFullBody.adapter.getItem(position)
            fullBodyType = type as String
        }


    }

    inner class ShoesTypeListener: AdapterView.OnItemClickListener{
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
            binding.randomShoes.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
            binding.randomShoes.setTypeface(Typeface.DEFAULT_BOLD)

            val type = binding.randomShoes.adapter.getItem(position)
            shoesType = type as String
        }


    }

    inner class AccessoriesTypeListener: View.OnClickListener{
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        override fun onClick(v: View?) {
            var arraySize = model.closet.accessoriesTags.keys.toTypedArray().size

            AlertDialog.Builder(requireContext())
                .setTitle("Accessories Options")
                .setMultiChoiceItems(model.closet.accessoriesTags.keys.toTypedArray(), checkedAcc, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        accessoriesTypes.add(model.closet.accessoriesTags.keys.toTypedArray()[which])
                        checkedAcc[which] = true
                    }else{
                        accessoriesTypes.remove(model.closet.accessoriesTags.keys.toTypedArray()[which])
                        checkedAcc[which] = false
                    }

                    updateView(v!! as AutoCompleteTextView, accessoriesTypes)
                })
                .show()
        }

    }

    inner class StylesTypeListener: View.OnClickListener{
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        override fun onClick(v: View?) {
            var arraySize = model.closet.styles.keys.toTypedArray().size

            AlertDialog.Builder(requireContext())
                .setTitle("Style Options")
                .setMultiChoiceItems(model.closet.styles.keys.toTypedArray(), checkedStyles, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        styleTypes.add(model.closet.styles.keys.toTypedArray()[which])
                        checkedStyles[which] = true
                    }else{
                        styleTypes.remove(model.closet.styles.keys.toTypedArray()[which])
                        checkedStyles[which] = false
                    }

                    updateView(v!! as AutoCompleteTextView, styleTypes)
                })
                .show()
        }

    }

    inner class WeatherTypeListener: View.OnClickListener{
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        override fun onClick(v: View?) {
            var arraySize = Closet.weathers.keys.toTypedArray().size

            AlertDialog.Builder(requireContext())
                .setTitle("Weather Options")
                .setMultiChoiceItems(Closet.weathers.keys.toTypedArray(), checkedWeathers, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        weatherTypes.add(Closet.weathers.keys.toTypedArray()[which])
                        checkedWeathers[which] = true
                    }else{
                        weatherTypes.remove(Closet.weathers.keys.toTypedArray()[which])
                        checkedWeathers[which] = false
                    }

                    updateView(v!! as AutoCompleteTextView, weatherTypes)
                })
                .show()
        }

    }

    inner class FullBodySwitchListener: CompoundButton.OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            toggleVisibilities(isChecked)
        }

    }
}