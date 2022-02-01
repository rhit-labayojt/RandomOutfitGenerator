package edu.rosehulman.randomoutfitgenerator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentSettingsBinding
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.lang.Runnable
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private var counter = 0;
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

//        lifecycleScope.launch(Dispatchers.Main){
//            val millis = TimeUnit.MILLISECONDS.toMillis(1000)
//
//            if(millis % timeInterval == 0)
//        }

        return binding.root
    }



}