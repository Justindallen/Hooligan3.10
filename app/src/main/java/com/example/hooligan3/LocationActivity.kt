package com.example.hooligan3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LocationActivity : AppCompatActivity() {

    private lateinit var zipCodeEditText: EditText
    private lateinit var locationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        zipCodeEditText = findViewById(R.id.zip_code_edit_text)
        locationButton = findViewById(R.id.location_button)

        locationButton.setOnClickListener {
            // Get user's current location or zip code
            val zipCode = zipCodeEditText.text.toString()
            // Use Google Maps API to get location from zip code
            // ...
        }
    }
}