package edu.rosehulman.randomoutfitgenerator.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.randomoutfitgenerator.Constants
import edu.rosehulman.randomoutfitgenerator.R
import edu.rosehulman.randomoutfitgenerator.adapters.OutfitDisplayAdapter
import edu.rosehulman.randomoutfitgenerator.databinding.FragmentHomeBinding
import edu.rosehulman.randomoutfitgenerator.models.ClosetViewModel
import java.lang.Math.abs

class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var model: ClosetViewModel
    private lateinit var sensorManager: SensorManager
    private var temperature: Sensor? = null
    private var currentTemp: Float = 0.0F

    companion object{
        const val fragmentName = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ClosetViewModel::class.java)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        binding.randomOutfitFab.visibility = View.GONE
        binding.weatherWidget.visibility = View.GONE
        binding.outfitCarousel.visibility = View.GONE

        model.addRecentOutfitsListener(fragmentName){
            binding.randomOutfitFab.visibility = View.VISIBLE
            binding.weatherWidget.visibility = View.VISIBLE
            binding.outfitCarousel.visibility = View.VISIBLE

            binding.outfitCarousel.adapter = OutfitDisplayAdapter(this, model.closet.recentOutfits)
        }

        binding.outfitCarousel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.outfitCarousel.setHasFixedSize(true)
        binding.outfitCarousel.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

        binding.randomOutfitFab.setOnClickListener {
            findNavController().navigate(R.id.nav_randomization)
//            sensorManager.requestTriggerSensor()
        }

        binding.weatherWidget.setText("Current Temperature: $currentTemp defF")

        return binding.root
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val tempReading = event.values[0]
        var farenheit = tempReading * (9/5) + 32

//        if(abs(farenheit - currentTemp) > 5){
//            currentTemp = farenheit
//        }
        currentTemp = farenheit
        Log.d(Constants.TAG, "$currentTemp")
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroyView(){
        super.onDestroyView()
        model.removeListener(fragmentName)
    }


}