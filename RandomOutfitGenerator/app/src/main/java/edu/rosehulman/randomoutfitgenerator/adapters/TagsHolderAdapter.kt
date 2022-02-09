package edu.rosehulman.randomoutfitgenerator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel
import edu.rosehulman.randomoutfitgenerator.ui.UserEditFragment

class TagsHolderAdapter(val fragment: UserEditFragment, val tagTypes: Array<String>, val tagsAdapters: ArrayList<TagsAdapter>): RecyclerView.Adapter<TagsHolderAdapter.TagsHolderViewHolder>() {
   private val userModel = ViewModelProvider(fragment.requireActivity()).get(UserViewModel::class.java)

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
        holder.bind()
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = tagTypes.size

    inner class TagsHolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tagLabel: TextView = itemView.findViewById<TextView>(R.id.tag_label)
        private val userEditTags: RecyclerView = itemView.findViewById<RecyclerView>(R.id.user_edit_tags)
        private val delete: Button = itemView.findViewById<Button>(R.id.delete_tag)
        private val add: Button = itemView.findViewById<Button>(R.id.add_tag)
        private val newTag: EditText = itemView.findViewById<EditText>(R.id.new_tag)
        private val setDefaultStyle: Button = itemView.findViewById<Button>(R.id.set_default_style)

        init {
            if(userModel.editUser){
                delete.visibility = View.VISIBLE
                add.visibility = View.VISIBLE
                newTag.visibility = View.VISIBLE

                if(tagTypes[adapterPosition] == "Styles") {
                    setDefaultStyle.visibility = View.VISIBLE

                    setDefaultStyle.setOnClickListener {
                        setDefault()
                    }
                }else{
                    setDefaultStyle.visibility = View.GONE
                }

                delete.setOnClickListener {
                    deleteTags()
                }

                add.setOnClickListener{
                    addTag()
                }
            }else{
                delete.visibility = View.GONE
                add.visibility = View.GONE
                newTag.visibility = View.GONE
                setDefaultStyle.visibility = View.GONE
            }

        }

        private fun addTag(){
            if(newTag.text.toString() == ""){
                Toast.makeText(fragment.requireContext(), "You did not enter a valid tag", Toast.LENGTH_LONG)
            }

            when(tagTypes[adapterPosition]){
                "Tops" -> {
                    if(userModel.user!!.topsTags.contains(newTag.text.toString())){
                        Toast.makeText(fragment.requireContext(), "You already have this tag", Toast.LENGTH_LONG)
                    }else{
                        userModel.user!!.topsTags.add(newTag.text.toString())
                    }
                }

                "Bottoms" -> {
                    if(userModel.user!!.bottomsTags.contains(newTag.text.toString())){
                        Toast.makeText(fragment.requireContext(), "You already have this tag", Toast.LENGTH_LONG)
                    }else{
                        userModel.user!!.bottomsTags.add(newTag.text.toString())
                    }
                }

                "Shoes" -> {
                    if(userModel.user!!.shoesTags.contains(newTag.text.toString())){
                        Toast.makeText(fragment.requireContext(), "You already have this tag", Toast.LENGTH_LONG)
                    }else{
                        userModel.user!!.shoesTags.add(newTag.text.toString())
                    }
                }

                "Accessories" -> {
                    if(userModel.user!!.accessoriesTags.contains(newTag.text.toString())){
                        Toast.makeText(fragment.requireContext(), "You already have this tag", Toast.LENGTH_LONG)
                    }else{
                        userModel.user!!.accessoriesTags.add(newTag.text.toString())
                    }
                }

                else -> {
                    if(userModel.user!!.fullBodyTags.contains(newTag.text.toString())){
                        Toast.makeText(fragment.requireContext(), "You already have this tag", Toast.LENGTH_LONG)
                    }else{
                        userModel.user!!.fullBodyTags.add(newTag.text.toString())
                    }
                }
            }
        }

        private fun deleteTags(){
            when(tagTypes[adapterPosition]){
                "Tops" -> {
                    userModel.user!!.topsTags.removeAll(userModel.tagChanges.get("Tops")!!.toList())
                }

                "Bottoms" -> {
                    userModel.user!!.topsTags.removeAll(userModel.tagChanges.get("Bottoms")!!.toList())
                }

                "Shoes" -> {
                    userModel.user!!.topsTags.removeAll(userModel.tagChanges.get("Shoes")!!.toList())
                }

                "Accessories" -> {
                    userModel.user!!.topsTags.removeAll(userModel.tagChanges.get("Accessories")!!.toList())
                }

                else -> {
                    userModel.user!!.topsTags.removeAll(userModel.tagChanges.get("Full Body")!!.toList())
                }
            }
        }

        private fun setDefault(){
            if(userModel.tagChanges.get("defaultStyle")!!.size > 1){
                Toast.makeText(fragment.requireContext(), "You can only have one default style", Toast.LENGTH_LONG)
            }else{
                userModel.user!!.defaultStyle = userModel.tagChanges.get("defaultStyle")!!.get(0)
            }
        }

        fun bind(){
            tagLabel.setText(tagTypes[adapterPosition])
            userEditTags.adapter = tagsAdapters[adapterPosition]
            userEditTags.layoutManager = LinearLayoutManager(fragment.requireContext())
            userEditTags.setHasFixedSize(true)
            userEditTags.addItemDecoration(DividerItemDecoration(fragment.requireContext(), DividerItemDecoration.VERTICAL))

        }
    }
}