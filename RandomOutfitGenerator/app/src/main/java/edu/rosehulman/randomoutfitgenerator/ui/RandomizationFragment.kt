package edu.rosehulman.randomoutfitgenerator.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentRandomizationBinding
import edu.rosehulman.randomoutfitgenerator.models.Closet
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel
import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import kotlin.random.Random

class RandomizationFragment: Fragment() {
    private lateinit var binding: FragmentRandomizationBinding
    private lateinit var model: ClosetViewModel
    private lateinit var userModel: UserViewModel

    private var topType = ""
    private var bottomType = ""
    private var fullBodyType = ""
    private var shoesType = ""
    private var styleType = ""
    private var weatherType = ""

    private var accessoriesTypes = ArrayList<String>()

    private lateinit var checkedAcc: BooleanArray
    private lateinit var checkedStyles: BooleanArray
    private lateinit var checkedWeathers: BooleanArray

    private var views = ArrayList<View>()
    private var lists = ArrayList<ArrayList<String>>()
    private var itemClickListeners = ArrayList<AdapterView.OnItemClickListener>()
    private var onClickListeners = ArrayList<View.OnClickListener>()

    companion object{
        const val fragmentNameClothes = "RandomizationFragmentClothes"
        const val fragmentNameRecent = "RandomizationFragmentRecent"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_randomization, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when(item.itemId){
            R.id.generate_outfit ->{

                if(styleType == ""){
                    styleType = userModel.user!!.defaultStyle
                }

                if(binding.fullBodyOutfitToggle.isChecked){
                    generateFullBodyOutfit()
                }else{
                    generateOutfit()
                }

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

        binding = FragmentRandomizationBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding.randomParentLayout.visibility = View.GONE

        model.addRecentOutfitsListener(fragmentNameRecent){}
        model.addClothingListener(fragmentNameClothes){
            binding.randomParentLayout.visibility = View.VISIBLE
        }

        setHasOptionsMenu(true)
        model.randomOutfit = true
        checkedAcc = BooleanArray(userModel.user!!.accessoriesTags.size){false}
        checkedStyles = BooleanArray(userModel.user!!.styles.size){false}
        checkedWeathers = BooleanArray(Closet.weathers.size){false}

        toggleVisibilities(binding.fullBodyOutfitToggle.isChecked)
        populateArrayLists()
        setupDropdownViews()
        setupAutoCompleteTextViewDialogs()

        binding.fullBodyOutfitToggle.setOnCheckedChangeListener(FullBodySwitchListener())

        return binding.root
    }

    private fun updateView(view: AutoCompleteTextView, list: ArrayList<String>){
        view.setTypeface(Typeface.DEFAULT_BOLD)
        view.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
        view.setText("${model.closet.toString(list)}")
    }

    private fun populateArrayLists(){

        views.add(binding.randomTop)
        views.add(binding.randomBottom)
        views.add(binding.randomFullBody)
        views.add(binding.randomShoes)
        views.add(binding.randomStyle)
        views.add(binding.randomWeather)
        views.add(binding.randomAccessories)

        lists.add(userModel.user!!.topsTags)
        lists.add(userModel.user!!.bottomsTags)
        lists.add(userModel.user!!.fullBodyTags)
        lists.add(userModel.user!!.shoesTags)
        lists.add(userModel.user!!.styles)

        var weathers = ArrayList<String>()
        Closet.weathers.forEach{weathers.add(it)}
        lists.add(weathers)
        lists.add(userModel.user!!.accessoriesTags)

        itemClickListeners.add(TopTypeListener())
        itemClickListeners.add(BottomTypeListener())
        itemClickListeners.add(FullBodyTypeListener())
        itemClickListeners.add(ShoesTypeListener())
        itemClickListeners.add(StyleTypeListener())
        itemClickListeners.add(WeatherTypeListener())

        onClickListeners.add(AccessoriesTypeListener())
    }

    private fun setupDropdownViews(){
        for(view in 0 until 6){
            val a = ArrayAdapter(
                requireContext(),
                R.layout.randomization_item_dropdown,
                lists[view]
            )

            var item = views[view] as AutoCompleteTextView
            item.setAdapter(a)
            item.onItemClickListener = itemClickListeners[view]
        }
    }

    private fun setupAutoCompleteTextViewDialogs(){
        for(view in 6 until 7){
            var item = views[view] as AutoCompleteTextView
            item.setOnClickListener(onClickListeners[view - 6])
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

    private fun generateOutfit(){

        Log.d(Constants.TAG,"$topType, $bottomType, $shoesType, $weatherType, $styleType")

        var topOptions = getOptionsList(topType, Closet.superCategories[0])
        var bottomOptions = getOptionsList(bottomType, Closet.superCategories[1])
        var shoeOptions = getOptionsList(shoesType, Closet.superCategories[3])

        var accessoryOptions = ArrayList<List<Clothing>>()

        if(accessoriesTypes.isEmpty()){
            accessoryOptions.add(getOptionsList("", Closet.superCategories[2]))
        }else{
            accessoriesTypes.forEach {
                accessoryOptions.add(getOptionsList(it, Closet.superCategories[2]))
            }
            Log.d(Constants.TAG, "${accessoryOptions.size}")
        }

        if((topOptions.isNotEmpty()) && (bottomOptions.isNotEmpty()) && (shoeOptions.isNotEmpty()) && (accessoryOptions.size > 0)){
            var outfitClothing = ArrayList<Clothing>()
            outfitClothing.add(topOptions.get(Random.nextInt(topOptions.size)))
            outfitClothing.add(bottomOptions.get(Random.nextInt(bottomOptions.size)))
            outfitClothing.add(shoeOptions.get(Random.nextInt(shoeOptions.size)))
            accessoryOptions.forEach { outfitClothing.add(it.get(Random.nextInt(it.size))) }

            var newOutfit: Outfit
            if(weatherType == ""){
                newOutfit = Outfit(outfitClothing, styleType, "Any", false)
            }else{
                newOutfit = Outfit(outfitClothing, styleType, weatherType, false)
            }
            model.addRecentOutfit(newOutfit)

            resetFields()

            findNavController().navigate(R.id.nav_outfit)
        }else{
            Toast.makeText(requireContext(), "You do not have any clothing matching these tags",Toast.LENGTH_LONG).show()
        }
    }

    private fun generateFullBodyOutfit(){
        var fullBodyOptions = getOptionsList(fullBodyType, Closet.superCategories[4])
        var shoeOptions = getOptionsList(shoesType, Closet.superCategories[3])

        var accessoryOptions = ArrayList<List<Clothing>>()

        if(accessoriesTypes.isEmpty()){
            accessoryOptions.add(getOptionsList("", Closet.superCategories[2]))
        }else{
            accessoriesTypes.forEach { accessoryOptions.add(getOptionsList(it, Closet.superCategories[2])) }
        }
        var outfitClothing = ArrayList<Clothing>()

        if((fullBodyOptions.isNotEmpty()) && (shoeOptions.isNotEmpty()) && (accessoryOptions.size > 0)){
            var outfitClothing = ArrayList<Clothing>()

            outfitClothing.add(fullBodyOptions.get(Random.nextInt(fullBodyOptions.size)))
            outfitClothing.add(shoeOptions.get(Random.nextInt(shoeOptions.size)))
            accessoryOptions.forEach { outfitClothing.add(it.get(Random.nextInt(it.size))) }

            var newOutfit: Outfit
            if(weatherType == ""){
                newOutfit = Outfit(outfitClothing, styleType, "Any", true)
            }else{
                newOutfit = Outfit(outfitClothing, styleType, weatherType, true)
            }
            model.addRecentOutfit(newOutfit)
            findNavController().navigate(R.id.nav_outfit)
        }else{
            Toast.makeText(requireContext(), "You do not have any clothing matching these tags",Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Creates a list of valid clothing options from the specified parameters
     */
    private fun getOptionsList(type: String, superCat: String): List<Clothing>{
        var list: List<Clothing>

        if(weatherType == ""){
            when (superCat) {
                Closet.superCategories[0] -> {
                    if (userModel.user!!.topsTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType) }
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                Closet.superCategories[1] -> {
                    if (userModel.user!!.bottomsTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType)}
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                Closet.superCategories[3] -> {
                    if (userModel.user!!.shoesTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType)}
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                Closet.superCategories[2] -> {
                    if (userModel.user!!.accessoriesTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType)}
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                else -> {
                    if (userModel.user!!.fullBodyTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType)}
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }
            }
        }else {
            when (superCat) {
                Closet.superCategories[0] -> {
                    if (userModel.user!!.topsTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType)}
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                Closet.superCategories[1] -> {
                    if (userModel.user!!.bottomsTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType) &&
                                    it.getWeathers().contains(weatherType)
                        }
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                Closet.superCategories[3] -> {
                    if (userModel.user!!.shoesTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType) &&
                                    it.getWeathers().contains(weatherType)
                        }
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                Closet.superCategories[2] -> {
                    if (userModel.user!!.accessoriesTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType)}
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }

                else -> {
                    if (userModel.user!!.fullBodyTags.contains(type)) {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSubCat() == type &&
                                    it.getStyles().contains(styleType) }
                    } else {
                        list = model.closet.clothing.filter { it: Clothing ->
                            it.getSuperCat() == superCat &&
                                    it.getStyles().contains(styleType)}
                    }
                }
            }
        }

        Log.d(Constants.TAG, "${list.size}")
        return list
    }

    private fun resetFields(){
        binding.randomTop.setText("Top")
        binding.randomBottom.setText("Bottom")
        binding.randomShoes.setText("Shoes")
        binding.randomAccessories.setText("Accessories")
        binding.randomFullBody.setText("Full Body")
        binding.randomStyle.setText("Style")
        binding.randomWeather.setText("Weather")

        topType = ""
        bottomType =""
        shoesType = ""
        weatherType = ""
        styleType = ""
        fullBodyType = ""
        accessoriesTypes.clear()
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
            var accessories = Array<String>(userModel.user!!.accessoriesTags.size){""}
            for(tag in 0 until userModel.user!!.accessoriesTags.size){
                accessories[tag] = userModel.user!!.accessoriesTags.get(tag)
            }


            AlertDialog.Builder(requireContext())
                .setTitle("Accessories Options")
                .setMultiChoiceItems(accessories, checkedAcc, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if(isChecked){
                        accessoriesTypes.add(userModel.user!!.accessoriesTags.get(which))
                        checkedAcc[which] = true
                    }else{
                        accessoriesTypes.remove(userModel.user!!.accessoriesTags.get(which))
                        checkedAcc[which] = false
                    }

                    updateView(v!! as AutoCompleteTextView, accessoriesTypes)
                })
                .show()
        }

    }

    inner class StyleTypeListener: AdapterView.OnItemClickListener{
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
            binding.randomStyle.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
            binding.randomStyle.setTypeface(Typeface.DEFAULT_BOLD)

            val type = binding.randomStyle.adapter.getItem(position)
            styleType = type as String
        }

    }

    inner class WeatherTypeListener: AdapterView.OnItemClickListener{
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
            binding.randomWeather.setTextColor(MaterialColors.getColor(requireContext(), R.attr.colorPrimaryVariant, Color.BLUE))
            binding.randomWeather.setTypeface(Typeface.DEFAULT_BOLD)

            val type = binding.randomStyle.adapter.getItem(position)
            weatherType = type as String
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

    override fun onDestroyView(){
        super.onDestroyView()
        model.removeListener(fragmentNameClothes)
        model.removeListener(fragmentNameRecent)
    }
}