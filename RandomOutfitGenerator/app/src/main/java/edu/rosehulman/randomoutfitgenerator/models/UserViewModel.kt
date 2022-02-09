package edu.rosehulman.randomoutfitgenerator.models

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel(){
    var ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)

    var user: User? = null
    var editUser = false

    fun getOrMakeUser(observer: () -> Unit){
        ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)

        if(user != null){
            //get
            observer()
        }else{
            //make
            ref.get().addOnSuccessListener {
                if(it.exists()){
                    user = it.toObject(User::class.java)
                }else{
                    user = User(name= Firebase.auth.currentUser!!.displayName!!,
                    topsTags= arrayListOf("Long Sleeve", "T-Shirt", "Sweater", "Vest", "Tank Top"),
                    bottomsTags= arrayListOf("Shorts", "Jeans", "Slacks", "Sweat Pants", "Skirt"),
                    accessoriesTags= arrayListOf("Sunglasses", "Hat", "Bracelet", "Necklace", "Ring", "Watch"),
                    shoesTags= arrayListOf("Sneakers", "Boots", "Heels", "Flats", "Sandals", "Crocs"),
                    fullBodyTags= arrayListOf("Suit", "Onesy", "Dress", "Romper", "PJs"),
                    styles= arrayListOf("Casual", "Formal", "Relaxation", "Work", "School") )
                    ref.set(user!!)
                }

                observer()
            }
        }
    }

    fun hasCompletedSetup(): Boolean = user?.hasCompletedSetup ?: false

    fun update(newName: String, newDefaultStyle: String, newHasCompletedSetup: Boolean, newTops: ArrayList<String>,
               newBottoms: ArrayList<String>, newShoes: ArrayList<String>, newAccessoris: ArrayList<String>,
               newFullBody: ArrayList<String>){

        ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)

        if(user!=null){
            with(user!!){
                name = newName
                defaultStyle = newDefaultStyle
                hasCompletedSetup = newHasCompletedSetup
                topsTags = newTops
                bottomsTags = newBottoms
                shoesTags = newShoes
                fullBodyTags = newFullBody
                accessoriesTags = newAccessoris
                ref.set(this)
            }
        }
    }
}