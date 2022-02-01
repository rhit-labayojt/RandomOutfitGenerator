package edu.rosehulman.randomoutfitgenerator.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit

class ClosetViewModel: ViewModel() {
    lateinit var clothingRef: CollectionReference
    lateinit var recentOutfitsRef: CollectionReference
    lateinit var savedOutfitsRef: CollectionReference

    var subscriptions = HashMap<String, ListenerRegistration>()
    var closet = Closet()
    var currentItem: Clothing? = null
    var currentOutfit: Outfit? = null

    fun addClothingListener(fragmentName: String, observer: () -> Unit){
        val uid = Firebase.auth.currentUser!!.uid
        clothingRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid).collection(Closet.CLOTHING_COLLECTION_PATH)

        val subscription = clothingRef
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                e?.let{
                    Log.d(Constants.TAG, "ERROR: $e")
                    return@addSnapshotListener
                }

                closet.clothing.clear()
                snapshot?.documents?.forEach {
                    closet.clothing.add(Clothing.from(it))
                }
                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun addSavedOutfitsListener(fragmentName: String, observer: () -> Unit){
        val uid = Firebase.auth.currentUser!!.uid
        savedOutfitsRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid).collection(Closet.SAVED_OUTFITS_COLLECTION_PATH)

        val subscription = savedOutfitsRef
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                e?.let{
                    Log.d(Constants.TAG, "ERROR: $e")
                    return@addSnapshotListener
                }

                closet.savedOutfits.clear()
                snapshot?.documents?.forEach {
                    closet.savedOutfits.add(Outfit.from(it))
                }
                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun addRecentOutfitsListener(fragmentName: String, observer: () -> Unit){
        val uid = Firebase.auth.currentUser!!.uid
        recentOutfitsRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid).collection(Closet.RECENT_OUTFITS_COLLECTION_PATH)

        val subscription = recentOutfitsRef
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                e?.let{
                    Log.d(Constants.TAG, "ERROR: $e")
                    return@addSnapshotListener
                }

                var idx = 0
                snapshot?.documents?.forEach {
                    closet.recentOutfits[idx] = Outfit.from(it)
                }
                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String){
        subscriptions[fragmentName]?.remove() // tell firebase to stop listening
        subscriptions.remove(fragmentName) // remove listener from HashMap
    }

    fun updateCurrentItem(clothes: Clothing){
        currentItem = clothes
    }

    fun updateClothing(){
        clothingRef.document(currentItem!!.id).set(currentItem!!)
    }

    fun deleteCurrentClothing(){
        clothingRef.document(currentItem!!.id).delete()
    }

    fun updateCurrentOutfit(fit: Outfit){
        currentOutfit = fit
    }

    fun deleteCurrentOutfit(){
        savedOutfitsRef.document(currentOutfit!!.id).delete()
    }

}