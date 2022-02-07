package edu.rosehulman.randomoutfitgenerator.models

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
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
import androidx.lifecycle.lifecycleScope
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

    private var currentItemIndex = 0
    private var currentSavedOutfitIndex = 0
    private var currentRecentOutfitIndex = 0
    private var recentOutfitIndexToAdd = 0

    private var storageImagesRef = Firebase.storage
        .reference
        .child("images")

    fun getCurrentItem() = closet.clothing.get(currentItemIndex)
    fun getCurrentSavedOutfit() = closet.savedOutfits.get(currentSavedOutfitIndex)
    fun getCurrentRecentOutfit() = closet.recentOutfits.get(currentRecentOutfitIndex)

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

    fun updateCurrentItem(pos: Int){
        currentItemIndex = pos

    }

    fun updateClothing(){
        var currentItem = getCurrentItem()
        clothingRef.document(currentItem.id).set(currentItem)
    }

    fun deleteCurrentClothing(){
        clothingRef.document(getCurrentItem().id).delete()
    }

    fun updateCurrentSavedOutfit(pos: Int){
        currentSavedOutfitIndex = pos
    }

    fun deleteCurrentSavedOutfit(){
        savedOutfitsRef.document(getCurrentItem().id).delete()
    }

    fun updateCurrentRecentOutfit(pos: Int){
        currentRecentOutfitIndex = pos
    }

    fun addRecentOutfit(fit: Outfit){
        if(recentOutfitIndexToAdd < 10){
            closet.recentOutfits[recentOutfitIndexToAdd] = fit
            currentRecentOutfitIndex = recentOutfitIndexToAdd
            recentOutfitIndexToAdd++
        }else{
            recentOutfitIndexToAdd = 0
            closet.recentOutfits[recentOutfitIndexToAdd] = fit
            currentRecentOutfitIndex = recentOutfitIndexToAdd
            recentOutfitIndexToAdd++
        }
    }

    fun takePhoto(fragment: Fragment){
        var latestTmpUri: Uri? = null

        val takeImageResult =
            fragment.registerForActivityResult(ActivityResultContracts.TakePicture()){isSuccess ->
                if(isSuccess){
                    latestTmpUri?.let{uri ->
                        addPhotoFromUri(fragment, uri)
                    }
                }
            }

        fragment.lifecycleScope.launchWhenStarted{
            getTmpFileUri(fragment).let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(fragment: Fragment): Uri {
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

    private fun addPhotoFromUri(fragment: Fragment, uri: Uri?){
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
                    Log.d(Constants.TAG,"Got download uri: $newImageUri")
                    fragment.findNavController().navigate(R.id.nav_clothing_edit)
                }else{
                    Log.d(Constants.TAG, "Failed to retrieve download uri")
                    fragment.findNavController().navigate(R.id.nav_home)
                }
            }

    }

}