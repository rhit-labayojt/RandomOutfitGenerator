package edu.rosehulman.randomoutfitgenerator

import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import edu.rosehulman.randomoutfitgenerator.databinding.ActivityMainBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import edu.rosehulman.randomoutfitgenerator.models.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var model: ClosetViewModel
//    private lateinit var myUser: UserViewModel

    private val signinLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){/*empty since the auth listener already responds */}

    override fun onStart(){
        super.onStart()
        Firebase.auth.addAuthStateListener(authListener)
    }

    override fun onStop(){
        super.onStop()
        Firebase.auth.removeAuthStateListener(authListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        model = ViewModelProvider(this).get(ClosetViewModel::class.java)

        setContentView(binding.root)

        initializeAuthListener()

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
            }else{
                with(user){
                    val userModel = ViewModelProvider(this@MainActivity).get(UserViewModel::class.java)
                    userModel.getOrMakeUser{
                        if(userModel.hasCompletedSetup()){
                            val id = findNavController(
                                R.id.nav_host_fragment_content_main).currentDestination!!.id

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
}