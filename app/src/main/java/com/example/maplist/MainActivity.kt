package com.example.maplist

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class MainActivity : AppCompatActivity() {


    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var placesClient: PlacesClient
    private lateinit var predictionsAdapter: ArrayAdapter<String>
    private var predictionsList: List<AutocompletePrediction> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        autoCompleteTextView = findViewById(R.id.etLocation)

        // Initialize Places SDK with your key
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }

        placesClient = Places.createClient(this)

        predictionsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            ArrayList()
        )

        autoCompleteTextView.setAdapter(predictionsAdapter)

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val prediction = predictionsList[position]
            fetchPlaceDetails(prediction.placeId)
        }

        autoCompleteTextView.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                if (!query.isNullOrEmpty()) {
                    fetchPlacePredictions(query.toString())
                }
            }
        })
    }

    private fun fetchPlacePredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                predictionsList = response.autocompletePredictions
                val suggestionList = predictionsList.map { it.getFullText(null).toString() }

                predictionsAdapter.clear()
                predictionsAdapter.addAll(suggestionList)
                predictionsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val info = "Selected: ${place.name}, ${place.address}\nLatLng: ${place.latLng}"
                Toast.makeText(this, info, Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch place details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}