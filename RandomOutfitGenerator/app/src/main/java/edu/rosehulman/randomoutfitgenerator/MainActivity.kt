package edu.rosehulman.randomoutfitgenerator

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import edu.rosehulman.randomoutfitgenerator.databinding.ActivityMainBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.User
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var model: ClosetViewModel
    private var myUser: User? = null
    private var curTheme: Int = 0

    var callbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(Constants.TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(Constants.TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(Constants.TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                var storedVerificationId = verificationId
                var resendToken = token
//                val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            }
        }

    private val signinLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){/*empty since the auth listener already responds */}

    override fun onStart(){
        super.onStart()
        Firebase.auth.addAuthStateListener(authListener)
    }

    override fun setTheme(themeId: Int){
        super.setTheme(themeId)
        curTheme = themeId
    }

    override fun onStop(){
        super.onStop()
        Firebase.auth.removeAuthStateListener(authListener)
    }

//    override fun recreate(){
//        finish();
//        overridePendingTransition(R.anim.anime_fade_in,
//            R.anim.anime_fade_out);
//        startActivity(getIntent());
//        overridePendingTransition(R.anim.anime_fade_in,
//            R.anim.anime_fade_out);
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeAuthListener()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        model = ViewModelProvider(this).get(ClosetViewModel::class.java)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_closet,
                R.id.nav_saved_outfits,
                R.id.nav_settings
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if(myUser != null){
            setTheme(myUser!!.theme)
            if(curTheme != myUser!!.theme){
                recreate()
            }
        }else{
            setTheme(R.style.Theme_RandomOutfitGenerator)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initializeAuthListener(){
        authListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser

            if(user == null){
                setupAuthUI()
                if(Firebase.auth.currentUser!!.phoneNumber != null) {

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(Firebase.auth.currentUser!!.phoneNumber!!)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(options)

                }
            }else{
                with(user){
                    val userModel = ViewModelProvider(this@MainActivity).get(UserViewModel::class.java)
                    userModel.getOrMakeUser{
                        if(userModel.hasCompletedSetup()){
                            val id = findNavController(
                                R.id.nav_host_fragment_content_main).currentDestination!!.id

                            myUser = userModel.user
                            if(id == R.id.nav_splash){

                                        var username = findViewById<TextView>(R.id.nav_drawer_name)
                                        var login = findViewById<TextView>(R.id.nav_drawer_login_info)

                                        username.setText(userModel.user!!.name)

                                        if(Firebase.auth.currentUser!!.email != null){
                                            login.setText("Email: ${Firebase.auth.currentUser!!.email}")
                                        }else{
                                            login.setText("Phone: ${Firebase.auth.currentUser!!.phoneNumber}")
                                        }

                                findNavController(R.id.nav_host_fragment_content_main)
                                    .navigate(R.id.nav_home)
                            }
                        }else{
                            navController.navigate(R.id.nav_user_edit)
                        }
                    }
                }
            }
        }
    }

    private fun setupAuthUI(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.Theme_RandomOutfitGenerator)
            .build()
        signinLauncher.launch(signinIntent)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(Constants.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}