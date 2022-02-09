package edu.rosehulman.randomoutfitgenerator.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class TagsAdapter(val fragment: Fragment, val tagType: String, val tagList: ArrayList<String>): RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        TODO("Not yet implemented")
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
    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.bind(tagList.get(position))
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = tagList.size

    inner class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tagValue: TextView = TODO()
        private val checkableTagValue: CheckBox = TODO()

        init{

            if(userModel.editUser){
                checkableTagValue = itemView.findViewById<CheckBox>(R.id.checkable_tag_value)

                checkableTagValue.setOnClickListener {
                    checkableTagValue.isChecked = !checkableTagValue.isChecked


                }
            }else{
                tagValue = itemView.findViewById<TextView>(R.id.tag_value)
            }

        }

        fun bind(s: String){
            if(userModel.editUser){
                checkableTagValue.setText(s)

            }
        }
    }
}