package edu.rosehulman.randomoutfitgenerator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.objects.Outfit

class OutfitDisplayAdapter(val fragment: Fragment, val itemList: ArrayList<Outfit?>): RecyclerView.Adapter<OutfitDisplayAdapter.CarouselViewHolder>() {
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
    ): OutfitDisplayAdapter.CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.outfit_image_view, parent, false)
        return CarouselViewHolder(view)
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
    override fun onBindViewHolder(holder: OutfitDisplayAdapter.CarouselViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = itemList.size

    inner class CarouselViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val layout: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.carousel_holder)
        private val top: ImageView = itemView.findViewById<ImageView>(R.id.home_top)
        private val bottom: ImageView = itemView.findViewById<ImageView>(R.id.home_bottom)
        private val shoes: ImageView = itemView.findViewById<ImageView>(R.id.home_shoes)
        private val accessories: ImageView = itemView.findViewById<ImageView>(R.id.home_accessories)
        private val fullBody: ImageView = itemView.findViewById<ImageView>(R.id.home_full_body)

        init{
            layout.setOnClickListener{
                if(model.closet.savedOutfits.contains(itemList[adapterPosition])){
                    model.updateCurrentSavedOutfit(model.closet.savedOutfits.indexOfFirst{it.id == itemList[adapterPosition]!!.id})
                    model.randomOutfit = false
                }else {
                    model.updateCurrentRecentOutfit(model.closet.recentOutfits.indexOfFirst{it!!.id == itemList[adapterPosition]!!.id})
                    model.randomOutfit = true
                }
                fragment.findNavController().navigate(R.id.nav_outfit)
            }
        }

        fun bind(fit: Outfit?){
            setImages(fit!!)
        }

        private fun setImages(fit: Outfit){
            if(fit.isFullBody){
                top.visibility = View.INVISIBLE
                bottom.visibility = View.INVISIBLE
                fullBody.visibility = View.VISIBLE

                fullBody.load(fit.fullBodyImage) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation())
                }
            }else{
                top.visibility = View.VISIBLE
                bottom.visibility = View.VISIBLE
                fullBody.visibility = View.INVISIBLE

                top.load(fit.top) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation())
                }

                bottom.load(fit.bottom) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation())
                }
            }

            accessories.load(fit.accessories[0]) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }

            shoes.load(fit.shoes) {
                crossfade(true)
                transformations(RoundedCornersTransformation())
            }
        }
    }
}