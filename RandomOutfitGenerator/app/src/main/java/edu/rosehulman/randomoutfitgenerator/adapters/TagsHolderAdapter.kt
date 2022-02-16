package edu.rosehulman.randomoutfitgenerator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel
import edu.rosehulman.randomoutfitgenerator.ui.UserEditFragment

class TagsHolderAdapter(val fragment: UserEditFragment, val tagTypes: Array<String>, val tagsAdapters: ArrayList<TagsAdapter>): RecyclerView.Adapter<TagsHolderAdapter.TagsHolderViewHolder>() {
   private val userModel = ViewModelProvider(fragment.requireActivity()).get(UserViewModel::class.java)
    private val model = ViewModelProvider(fragment.requireActivity()).get(ClosetViewModel::class.java)
    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TagsHolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tags_holder_layout, parent, false)
        return TagsHolderViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: TagsHolderViewHolder, position: Int) {
        holder.bind(tagTypes[position], tagsAdapters[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = tagTypes.size

    fun update(){
        notifyDataSetChanged()
        tagsAdapters.forEach { it.update() }
    }

    inner class TagsHolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tagLabel: TextView = itemView.findViewById<TextView>(R.id.tag_label)
        private val userEditTags: RecyclerView = itemView.findViewById<RecyclerView>(R.id.user_edit_tags)
        private val delete: Button = itemView.findViewById<Button>(R.id.delete_tag)
        private val add: Button = itemView.findViewById<Button>(R.id.add_tag)
        private val newTag: EditText = itemView.findViewById<EditText>(R.id.new_tag)
        private val setDefaultStyle: Button = itemView.findViewById<Button>(R.id.set_default_style)

        init {

            delete.setOnClickListener {
                deleteTags()
                notifyDataSetChanged()
            }

            add.setOnClickListener{
                addTag()
                notifyDataSetChanged()
            }

            setDefaultStyle.setOnClickListener {
                setDefault()
                notifyDataSetChanged()
            }

            if(userModel.editUser){
                delete.visibility = View.VISIBLE
                add.visibility = View.VISIBLE
                newTag.visibility = View.VISIBLE
            }else{
                delete.visibility = View.GONE
                add.visibility = View.GONE
                newTag.visibility = View.GONE
                setDefaultStyle.visibility = View.GONE
            }

            userEditTags.layoutManager = LinearLayoutManager(fragment.requireContext())
            userEditTags.setHasFixedSize(true)
            userEditTags.addItemDecoration(DividerItemDecoration(fragment.requireContext(), DividerItemDecoration.VERTICAL))


        }

        private fun addTag(){
            if(newTag.text.toString() == ""){
                Toast.makeText(fragment.requireContext(), "You did not enter a valid tag", Toast.LENGTH_LONG).show()
            }else {

                when (tagTypes[adapterPosition]) {
                    "Styles" -> {
                        if (userModel.user!!.styles.contains(newTag.text.toString())) {
                            Toast.makeText(
                                fragment.requireContext(),
                                "You already have this tag",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            userModel.user!!.styles.add(newTag.text.toString())
                        }
                    }

                    "Tops" -> {
                        if (userModel.user!!.topsTags.contains(newTag.text.toString())) {
                            Toast.makeText(
                                fragment.requireContext(),
                                "You already have this tag",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            userModel.user!!.topsTags.add(newTag.text.toString())
                        }
                    }

                    "Bottoms" -> {
                        if (userModel.user!!.bottomsTags.contains(newTag.text.toString())) {
                            Toast.makeText(
                                fragment.requireContext(),
                                "You already have this tag",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            userModel.user!!.bottomsTags.add(newTag.text.toString())
                        }
                    }

                    "Shoes" -> {
                        if (userModel.user!!.shoesTags.contains(newTag.text.toString())) {
                            Toast.makeText(
                                fragment.requireContext(),
                                "You already have this tag",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            userModel.user!!.shoesTags.add(newTag.text.toString())
                        }
                    }

                    "Accessories" -> {
                        if (userModel.user!!.accessoriesTags.contains(newTag.text.toString())) {
                            Toast.makeText(
                                fragment.requireContext(),
                                "You already have this tag",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            userModel.user!!.accessoriesTags.add(newTag.text.toString())
                        }
                    }

                    else -> {
                        if (userModel.user!!.fullBodyTags.contains(newTag.text.toString())) {
                            Toast.makeText(
                                fragment.requireContext(),
                                "You already have this tag",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            userModel.user!!.fullBodyTags.add(newTag.text.toString())
                        }
                    }
                }
                newTag.setText("")
                notifyDataSetChanged()
            }
        }

        private fun deleteTags(){
            when(tagTypes[adapterPosition]){
                "Styles" -> {
                    if(userModel.tagChanges.get("Styles")!!.size < 1){
                        Toast.makeText(fragment.requireContext(), "No ${tagTypes[adapterPosition]} were selected for deletion", Toast.LENGTH_LONG).show()
                    }else{
                        userModel.user!!.styles.removeAll(userModel.tagChanges.get("Styles")!!.toList())
                    }
                }

                "Tops" -> {
                    if(userModel.tagChanges.get("Tops")!!.size < 1){
                        Toast.makeText(fragment.requireContext(), "No ${tagTypes[adapterPosition]} were selected for deletion", Toast.LENGTH_LONG).show()
                    }else{
                        userModel.user!!.topsTags.removeAll(userModel.tagChanges.get("Tops")!!.toList())
                    }
                }

                "Bottoms" -> {
                    if(userModel.tagChanges.get("Bottoms")!!.size < 1){
                        Toast.makeText(fragment.requireContext(), "No ${tagTypes[adapterPosition]} were selected for deletion", Toast.LENGTH_LONG).show()
                    }else{
                        userModel.user!!.bottomsTags.removeAll(userModel.tagChanges.get("Bottoms")!!.toList())
                    }
                }

                "Shoes" -> {
                    if(userModel.tagChanges.get("Shoes")!!.size < 1){
                        Toast.makeText(fragment.requireContext(), "No ${tagTypes[adapterPosition]} were selected for deletion", Toast.LENGTH_LONG).show()
                    }else{
                        userModel.user!!.shoesTags.removeAll(userModel.tagChanges.get("Shoes")!!.toList())
                    }
                }

                "Accessories" -> {
                    if(userModel.tagChanges.get("Accessories")!!.size < 1){
                        Toast.makeText(fragment.requireContext(), "No ${tagTypes[adapterPosition]} were selected for deletion", Toast.LENGTH_LONG).show()
                    }else{
                        userModel.user!!.accessoriesTags.removeAll(userModel.tagChanges.get("Accessories")!!.toList())
                    }
                }

                else -> {
                    if(userModel.tagChanges.get("Full Body")!!.size < 1){
                        Toast.makeText(fragment.requireContext(), "No ${tagTypes[adapterPosition]} were selected for deletion", Toast.LENGTH_LONG).show()
                    }else{
                        userModel.user!!.fullBodyTags.removeAll(userModel.tagChanges.get("Full Body")!!.toList())
                    }
                }
            }

            deleteTagsFromModel(tagTypes[adapterPosition])

            notifyDataSetChanged()
        }

        private fun setDefault(){
            if(userModel.tagChanges.get("defaultStyle")!!.size > 1){
                Toast.makeText(fragment.requireContext(), "You can only have one default style", Toast.LENGTH_LONG).show()
            }else if(userModel.tagChanges.get("defaultStyle")!!.size < 1){
                Toast.makeText(fragment.requireContext(), "You have not selected a style", Toast.LENGTH_LONG).show()
            }else{
                userModel.user!!.defaultStyle = userModel.tagChanges.get("defaultStyle")!!.get(0)
                userModel.tagChanges.get("defaultStyle")!!.clear()
            }

            notifyDataSetChanged()
        }

        fun bind(s: String, adapter: TagsAdapter){
            tagLabel.setText(s)

            if(tagTypes[adapterPosition] == "Styles" && userModel.editUser) {
                setDefaultStyle.visibility = View.VISIBLE
            }else{
                setDefaultStyle.visibility = View.GONE
            }

            userEditTags.adapter = adapter


            if(userModel.editUser){
                delete.visibility = View.VISIBLE
                add.visibility = View.VISIBLE
                newTag.visibility = View.VISIBLE
            }else{
                delete.visibility = View.GONE
                add.visibility = View.GONE
                newTag.visibility = View.GONE
                setDefaultStyle.visibility = View.GONE
            }

        }

        fun deleteTagsFromModel(tag: String){
            when(tag){
                "Styles" -> {
                    userModel.tagChanges.get(tag)!!.forEach { style: String ->
                        model.closet.savedOutfits.filter{it.style == style}.forEach {
                            model.setCurrentSavedOutfit(it)
                            it.style = "Other"
                            model.updateSavedOutfit()
                        }
                        model.closet.recentOutfits.filter{it!!.style == style}.forEach {
                            model.setCurrentRecentOutfit(it!!)
                            it!!.style = "Other"
                            model.updateRecentOutfit()
                        }
                        model.closet.clothing.filter{it.getStyles().contains(style)}.forEach {
                            model.setCurrentItem(it)
                            model.getCurrentItem().removeStyle(style)
                            model.updateClothing(it)
                        }
                    }
                }

                "Tops" -> userModel.tagChanges.get(tag)!!.forEach {subCat ->
                    model.closet.clothing.filter{it.getSuperCat() == "Top" && it.getSubCat() == subCat}.forEach {
                        model.setCurrentItem(it)
                        model.getCurrentItem().setSubCat(userModel.user!!.topsTags[0])
                        model.updateClothing(it)
                    }
                }

                "Bottoms" -> userModel.tagChanges.get(tag)!!.forEach {subCat ->
                    model.closet.clothing.filter{it.getSuperCat() == "Bottom" && it.getSubCat() == subCat}.forEach {
                        model.setCurrentItem(it)
                        model.getCurrentItem().setSubCat(userModel.user!!.bottomsTags[0])
                        model.updateClothing(it)
                    }
                }

                "Shoes" -> userModel.tagChanges.get(tag)!!.forEach { subCat ->
                    model.closet.clothing.filter { it.getSuperCat() == "Shoes" && it.getSubCat() == subCat }
                        .forEach {
                            model.setCurrentItem(it)
                            model.getCurrentItem().setSubCat(userModel.user!!.shoesTags[0])
                            model.updateClothing(it)
                        }
                }

                "Accessories" -> userModel.tagChanges.get(tag)!!.forEach {subCat ->
                    model.closet.clothing.filter{it.getSuperCat() == "Accessory" && it.getSubCat() == subCat}.forEach {
                        model.setCurrentItem(it)
                        model.getCurrentItem().setSubCat(userModel.user!!.accessoriesTags[0])
                        model.updateClothing(it)
                    }
                }

                else -> userModel.tagChanges.get(tag)!!.forEach {subCat ->
                    model.closet.clothing.filter{it.getSuperCat() == "Full Body" && it.getSubCat() == subCat}.forEach {
                        model.setCurrentItem(it)
                        model.getCurrentItem().setSubCat(userModel.user!!.fullBodyTags[0])
                        model.updateClothing(it)
                    }
                }
            }
        }
    }
}