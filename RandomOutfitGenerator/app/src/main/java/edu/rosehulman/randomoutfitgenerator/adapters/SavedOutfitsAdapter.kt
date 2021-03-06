package edu.rosehulman.randomoutfitgenerator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import edu.rosehulman.randomoutfitgenerator.objects.SavedOutfitStyles

class SavedOutfitsAdapter(val fragment: Fragment, val styles: ArrayList<SavedOutfitStyles>): RecyclerView.Adapter<SavedOutfitsAdapter.SavedOutfitsViewHolder>() {
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
    ): SavedOutfitsAdapter.SavedOutfitsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_outfits_list, parent, false)
        return SavedOutfitsViewHolder(view)
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
    override fun onBindViewHolder(
        holder: SavedOutfitsAdapter.SavedOutfitsViewHolder,
        position: Int
    ) {
        holder.bind(styles.get(position))
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = styles.size

    inner class SavedOutfitsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var style: TextView = itemView.findViewById<TextView>(R.id.outfit_style)
        private var outfits: RecyclerView = itemView.findViewById<RecyclerView>(R.id.outfit_grid)

        init{
            style.setOnClickListener{
                Log.d(Constants.TAG, "Clicked ${styles[adapterPosition].style}")
                toggleVisibility()
                notifyDataSetChanged()
            }

            outfits.visibility = View.GONE
        }

        fun bind(currentStyle: SavedOutfitStyles){

            var outfitList = model.closet.savedOutfits.filter{it.style == currentStyle.style}
            Log.d(Constants.TAG, "${outfitList.size}")

            if(outfitList.isEmpty()){
                Log.d(Constants.TAG, "${currentStyle.style} is GONE")
                style.visibility = View.GONE
                outfits.visibility = View.GONE
            }else{
                Log.d(Constants.TAG, "${currentStyle.style} is VISIBLE")
                style.visibility = View.VISIBLE

                style.setText(currentStyle.style)
                var adapter = OutfitDisplayAdapter(fragment, outfitList as ArrayList<Outfit?>)
                outfits.adapter = adapter
                outfits.layoutManager = GridLayoutManager(fragment.requireContext(), 3, GridLayoutManager.VERTICAL, false)

                if(currentStyle.isVisible){
                    outfits.visibility = View.VISIBLE
                }else{
                    outfits.visibility = View.GONE
                }
            }


        }

        private fun toggleVisibility(){
            styles[adapterPosition].isVisible = !styles[adapterPosition].isVisible
        }
    }
}