package edu.rosehulman.randomoutfitgenerator.models

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import edu.rosehulman.randomoutfitgenerator.BuildConfig
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import java.lang.Math.abs
import kotlin.random.Random
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ClosetViewModel: ViewModel() {
    lateinit var clothingRef: CollectionReference
    lateinit var recentOutfitsRef: CollectionReference
    lateinit var savedOutfitsRef: CollectionReference

    var subscriptions = HashMap<String, ListenerRegistration>()
    var closet = Closet()
    var newImageUri = ""
    var isNewImage = false
    var latestTmpUri: Uri? = null
    var cameraTriggeredFragment = R.id.nav_closet
    var currentOutfit: Outfit? = null
    var randomOutfit = true

    private var currentItemIndex = 0
    private var currentSavedOutfitIndex = 0
    private var currentRecentOutfitIndex = 0
    private var recentOutfitIndexToAdd = 0
    var newItem: Clothing? = null
    var clothingImage: ImageView? = null

    var storageImagesRef = Firebase.storage
        .reference
        .child("images")

    fun getCurrentItem() = closet.clothing.get(currentItemIndex)

    fun updateImage(){
        if(newItem != null){
            newItem!!.image = newImageUri
            closet.clothing.add(newItem!!)
            currentItemIndex = closet.clothing.size - 1
            Log.d(Constants.TAG,"Current Item Image ${closet.clothing[currentItemIndex].image}")

            clothingImage!!.load(newItem!!.image) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }else{
            getCurrentItem().image = newImageUri

            clothingImage!!.load(getCurrentItem().image) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    fun addClothingListener(fragmentName: String, observer: () -> Unit){
        val uid = Firebase.auth.currentUser!!.uid
        clothingRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid).collection(Closet.CLOTHING_COLLECTION_PATH)

        val subscription = clothingRef
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                e?.let{
                    Log.d(Constants.TAG, "ERROR: $e")
                    return@addSnapshotListener
                }
                Log.d(Constants.TAG, "Snapshot Length: ${snapshot?.size()}")
                closet.clothing.clear()
                snapshot?.documents?.forEach {
                    closet.clothing.add(Clothing.from(it))
                }
                Log.d(Constants.TAG, "Clothing Length: ${closet.clothing.size}")
                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun addSavedOutfitsListener(fragmentName: String, observer: () -> Unit){
        val uid = Firebase.auth.currentUser!!.uid
        savedOutfitsRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid).collection(Closet.SAVED_OUTFITS_COLLECTION_PATH)

        val subscription = savedOutfitsRef
            .orderBy(Outfit.CREATED_KEY, Query.Direction.DESCENDING)
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
            .orderBy(Outfit.CREATED_KEY, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                e?.let{
                    Log.d(Constants.TAG, "ERROR: $e")
                    return@addSnapshotListener
                }

                closet.recentOutfits.clear()
                snapshot?.documents?.forEach {
                    closet.recentOutfits.add(Outfit.from(it))
                }
                recentOutfitIndexToAdd = closet.recentOutfits.size
                observer()
            }

        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String){
        subscriptions[fragmentName]?.remove() // tell firebase to stop listening
        subscriptions.remove(fragmentName) // remove listener from HashMap
    }

    fun updateCurrentItem(pos: Int){
        currentItemIndex = pos
    }

    fun setCurrentItem(item: Clothing){
        currentItemIndex = closet.clothing.indexOfFirst{it.id == item.id}
    }

    fun updateClothing(item: Clothing){
        var currentItem: Clothing

        if(isNewImage){
            Log.d(Constants.TAG, "Adding new item")
            currentItem = item
            Log.d(Constants.TAG,"Image Saved: ${currentItem.image}")
            clothingRef.add(currentItem)
            isNewImage = false
        }else {
            currentItem = getCurrentItem()
            clothingRef.document(currentItem.id).set(currentItem)
        }
    }

    fun deleteCurrentClothing(){
        if(getCurrentItem().id == ""){
            closet.removeClothing(getCurrentItem())
        }else {
            clothingRef.document(getCurrentItem().id).delete()
        }
    }

    fun deleteCurrentOutfit(){
        savedOutfitsRef.document(currentOutfit!!.id).delete()
    }

    fun saveCurrentOutfit(){
        closet.savedOutfits.add(currentOutfit!!)
        savedOutfitsRef.add(currentOutfit!!)
    }

    fun setCurrentSavedOutfit(fit: Outfit){
        currentSavedOutfitIndex = closet.savedOutfits.indexOfFirst { it.id == fit.id }
    }

    fun setCurrentRecentOutfit(fit: Outfit){
        currentRecentOutfitIndex = closet.recentOutfits.indexOfFirst { it!!.id == fit.id }
    }

    fun updateCurrentSavedOutfit(pos: Int){
        currentSavedOutfitIndex = pos
        currentOutfit = closet.savedOutfits[currentSavedOutfitIndex]
    }

    fun updateCurrentRecentOutfit(idx: Int){
        currentRecentOutfitIndex = idx
        currentOutfit = closet.recentOutfits[currentRecentOutfitIndex]
    }

    fun updateRecentOutfit(){
        recentOutfitsRef.document(closet.recentOutfits[currentRecentOutfitIndex]!!.id).set(closet.recentOutfits[currentRecentOutfitIndex]!!)
    }

    fun updateSavedOutfit(){
        savedOutfitsRef.document(closet.savedOutfits[currentSavedOutfitIndex].id).set(closet.savedOutfits[currentSavedOutfitIndex])
    }

    fun addRecentOutfit(fit: Outfit){
        if(recentOutfitIndexToAdd < 10){
            if(closet.recentOutfits.size >= 10) {
                if (closet.recentOutfits[recentOutfitIndexToAdd] != null) {
                    recentOutfitsRef.document(closet.recentOutfits[recentOutfitIndexToAdd]!!.id)
                        .delete()
                }

                closet.recentOutfits[recentOutfitIndexToAdd] = fit
                currentRecentOutfitIndex = recentOutfitIndexToAdd
                recentOutfitIndexToAdd++
            }else{
                closet.recentOutfits.add(fit)
                currentRecentOutfitIndex = recentOutfitIndexToAdd
                recentOutfitIndexToAdd++
            }
        }else{
            recentOutfitIndexToAdd = 0
            recentOutfitsRef.document(closet.recentOutfits[recentOutfitIndexToAdd]!!.id).delete()
            closet.recentOutfits[recentOutfitIndexToAdd] = fit
            currentRecentOutfitIndex = recentOutfitIndexToAdd
            recentOutfitIndexToAdd++
        }

        recentOutfitsRef.add(fit)
        currentOutfit = fit
    }

    fun getTmpFileUri(fragment: Fragment): Uri {
        val storageDir: File = fragment.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val tmpFile = File.createTempFile("JPEG_${timeStamp}_", ".png", storageDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            fragment.requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    fun addPhotoFromUri(fragment: Fragment, uri: Uri?){
        Log.d(Constants.TAG, "Trying to add photo")
        // Check for null uri
        if(uri == null){
            Log.e(Constants.TAG, "Uri is null. Not saving to storage")
            return
        }

        val stream = fragment.requireActivity().contentResolver.openInputStream(uri)

        // Check for null stream
        if(stream == null){
            Log.e(Constants.TAG, "Stream is null. Not saving to storage")
            return
        }

        val imageId = abs(Random.nextLong()).toString()
        storageImagesRef.child(imageId).putStream(stream)
            .continueWithTask { task ->
                if(!task.isSuccessful){
                    task.exception?.let{
                        throw it
                    }
                }
                storageImagesRef.child(imageId).downloadUrl
            }
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    newImageUri = task.result.toString()
                    updateImage()
                    Log.d(Constants.TAG,"Got download uri: $newImageUri")
                    Log.d(Constants.TAG, "${cameraTriggeredFragment}, ${R.id.nav_clothing_edit}")

                    if(cameraTriggeredFragment == R.id.nav_clothing_edit){
                        Log.d(Constants.TAG, "Reassigning image uri to $newImageUri")
                        getCurrentItem().image = newImageUri
                    }
                }else{
                    Log.d(Constants.TAG, "Failed to retrieve download uri")
                    fragment.findNavController().navigate(R.id.nav_closet)
                }
            }

    }

}