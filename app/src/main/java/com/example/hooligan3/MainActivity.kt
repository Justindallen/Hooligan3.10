package com.example.hooligan3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import android.widget.AdapterView
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.location.GnssStatus
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.SphericalUtil
import android.widget.ListView
import android.util.Log


class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var binding: ViewDataBinding
    private lateinit var leagueSpinner: Spinner
    private lateinit var teamSpinner: Spinner
    private lateinit var map: GoogleMap
    private lateinit var mainViewModel: MainViewModel

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        1f,
                        locationListener
                    )
                }
            }
        }
    }


    // Team name arrays
    private val mlbTeams = arrayOf("Arizona Diamondbacks",
        "Atlanta Braves",
        "Baltimore Orioles",
        "Boston Red Sox",
        "Chicago Cubs",
        "Chicago White Sox",
        "Cincinnati Reds",
        "Cleveland Guardians",
        "Colorado Rockies",
        "Detroit Tigers",
        "Houston Astros",
        "Kansas City Royals",
        "Los Angeles Angels",
        "Los Angeles Dodgers",
        "Miami Marlins",
        "Milwaukee Brewers",
        "Minnesota Twins",
        "New York Mets",
        "New York Yankees",
        "Oakland Athletics",
        "Philadelphia Phillies",
        "Pittsburgh Pirates",
        "San Diego Padres",
        "San Francisco Giants",
        "Seattle Mariners",
        "St. Louis Cardinals",
        "Tampa Bay Rays",
        "Texas Rangers",
        "Toronto Blue Jays",
        "Washington Nationals"
    )

    private val nbaTeams = arrayOf("Atlanta Hawks",
        "Boston Celtics",
        "Brooklyn Nets",
        "Charlotte Hornets",
        "Chicago Bulls",
        "Cleveland Cavaliers",
        "Dallas Mavericks",
        "Denver Nuggets",
        "Detroit Pistons",
        "Golden State Warriors",
        "Houston Rockets",
        "Indiana Pacers",
        "Los Angeles Clippers",
        "Los Angeles Lakers",
        "Memphis Grizzlies",
        "Miami Heat",
        "Milwaukee Bucks",
        "Minnesota Timberwolves",
        "New Orleans Pelicans",
        "New York Knicks",
        "Oklahoma City Thunder",
        "Orlando Magic",
        "Philadelphia 76ers",
        "Phoenix Suns",
        "Portland Trail Blazers",
        "Sacramento Kings",
        "San Antonio Spurs",
        "Toronto Raptors",
        "Utah Jazz",
        "Washington Wizards"
    )

    private val nflTeams = arrayOf( "Arizona Cardinals",
        "Atlanta Falcons",
        "Baltimore Ravens",
        "Buffalo Bills",
        "Carolina Panthers",
        "Chicago Bears",
        "Cincinnati Bengals",
        "Cleveland Browns",
        "Dallas Cowboys",
        "Denver Broncos",
        "Detroit Lions",
        "Green Bay Packers",
        "Houston Texans",
        "Indianapolis Colts",
        "Jacksonville Jaguars",
        "Kansas City Chiefs",
        "Las Vegas Raiders",
        "Los Angeles Chargers",
        "Los Angeles Rams",
        "Miami Dolphins",
        "Minnesota Vikings",
        "New England Patriots",
        "New Orleans Saints",
        "New York Giants",
        "New York Jets",
        "Philadelphia Eagles",
        "Pittsburgh Steelers",
        "San Francisco 49ers",
        "Seattle Seahawks",
        "Tampa Bay Buccaneers",
        "Tennessee Titans",
        "Washington Commanders"
    )

    private val nhlTeams = arrayOf( "Anaheim Ducks",
        "Arizona Coyotes",
        "Boston Bruins",
        "Buffalo Sabres",
        "Calgary Flames",
        "Carolina Hurricanes",
        "Chicago Blackhawks",
        "Colorado Avalanche",
        "Columbus Blue Jackets",
        "Dallas Stars",
        "Detroit Red Wings",
        "Edmonton Oilers",
        "Florida Panthers",
        "Los Angeles Kings",
        "Minnesota Wild",
        "Montreal Canadiens",
        "Nashville Predators",
        "New Jersey Devils",
        "New York Islanders",
        "New York Rangers",
        "Ottawa Senators",
        "Philadelphia Flyers",
        "Pittsburgh Penguins",
        "San Jose Sharks",
        "Seattle Kraken",
        "St. Louis Blues",
        "Tampa Bay Lightning",
        "Toronto Maple Leafs",
        "Vancouver Canucks",
        "Vegas Golden Knights",
        "Washington Capitals",
        "Winnipeg Jets"
    )

    private val eplTeams = arrayOf( "Arsenal",
        "Aston Villa",
        "Bournemouth",
        "Brentford",
        "Brighton & Hove Albion",
        "Chelsea",
        "Crystal Palace",
        "Everton",
        "Fulham",
        "Leeds United",
        "Leicester City",
        "Liverpool",
        "Manchester City",
        "Manchester United",
        "Newcastle United",
        "Nottingham Forest",
        "Southampton",
        "Tottenham Hotspur",
        "West Ham United",
        "Wolverhampton Wanderers"
    )

    private var barToLocations = hashMapOf(
        "Iron Abbey" to LatLng(40.1743, -75.3953), // Horsham, PA
        "Liverpool Bar" to LatLng(40.7128, -74.0060), // New York, NY
        "Misconduct Tavern" to LatLng(39.9473, -75.1633), // Philadelphia, PA
        "O'Neals Pub" to LatLng(39.9383, -75.1533), // Philadelphia, PA
        "The Black Sheep" to LatLng(39.9493, -75.1723), // Philadelphia, PA
        "Tir Na Nog" to LatLng(39.9553, -75.1633), // Philadelphia, PA
        "Fado" to LatLng(39.9473, -75.1633), // Philadelphia, PA
        "Cavanaugh's Headhouse Square" to LatLng(39.9403, -75.1473), // Philadelphia, PA
        "Founding Fathers" to LatLng(39.9393, -75.1633), // Philadelphia, PA
        "Big Charlie's Saloon" to LatLng(39.9496, -75.1634), // Philadelphia, PA
        "Jack's NYB" to LatLng(39.9242, -75.1739), // Philadelphia, PA
        "Wooly Mammoth" to LatLng(39.9526, -75.1652), // Philadelphia, PA
        "Milkboy South" to LatLng(39.9473, -75.1633), // Philadelphia, PA
        "Scoreboard Bar & Grill" to LatLng(39.9526, -75.1652), // Philadelphia, PA
        "Chickies & Pete's" to LatLng(39.9537, -75.1639), // Philadelphia, PA
        // Add more bars and locations here
    )

    private var barToTeams = hashMapOf(
        "Iron Abbey" to arrayOf("Liverpool"),
        "Misconduct Tavern" to arrayOf("Arsenal"),
        "O'Neals Pub" to arrayOf("Everton"),
        "The Black Sheep" to arrayOf("Manchester United"),
        "Tir Na Nog" to arrayOf("Buffalo Bills", "Chelsea", "Manchester City"),
        "Fado" to arrayOf("Crystal Palace"),
        "Cavanaugh's Headhouse Square" to arrayOf("Newcastle"),
        "Founding Fathers" to arrayOf("Tottenham"),
        "Big Charlie's Saloon" to arrayOf("Kansas City Chiefs"),
        "Jack's NYB" to arrayOf("St. Louis Blues"),
        "Liverpool Bar" to arrayOf("Liverpool"),
        "Wooly Mammoth" to arrayOf("Washington Commanders"),
        "Milkboy South" to arrayOf("Baltimore Ravens"),
        "Scoreboard Bar & Grill" to arrayOf("New York Jets"),
        "Chickies & Pete's" to arrayOf("Philadelphia Eagles", "Philadelphia Phillies", "Philadelphia Flyers", "Philadelphia 76ers"),
        // Add more bars and teams here
    )
    private fun displayBars(sortedBars: List<String>) {
        val barListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sortedBars)
        findViewById<ListView>(R.id.bar_list).adapter = barListAdapter
    }


    fun calculateDistance(from: LatLng, to: LatLng): Double {
        return SphericalUtil.computeDistanceBetween(from, to)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        teamSpinner = findViewById<Spinner>(R.id.team_spinner)

        // Populate the barToTeams map
        barToTeams = barToTeams.toMap() as HashMap<String, Array<String>>

// Populate the barToLocations map
        barToLocations = barToLocations.toMap() as HashMap<String, LatLng>

        Log.d("MainActivity", "barToTeams: $barToTeams")
        Log.d("MainActivity", "barToLocations: $barToLocations")
        Log.d("MainActivity", "barToTeams size: ${barToTeams.size}")
        Log.d("MainActivity", "barToLocations size: ${barToLocations.size}")
        Log.d("MainActivity", "barToTeams keys: ${barToTeams.keys}")
        Log.d("MainActivity", "barToTeams values: ${barToTeams.values}")
        Log.d("MainActivity", "barToLocations keys: ${barToLocations.keys}")
        Log.d("MainActivity", "barToLocations values: ${barToLocations.values}")

// Define teams
        val teams: List<String> = barToTeams.values.flatMap { it.toList() }.distinct()
// Set up the team spinner
        teamSpinner = findViewById<Spinner>(R.id.team_spinner)
        teamSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, teams)
        teamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedTeam = teamSpinner.selectedItem.toString()
                // Get the affiliated bars for the selected team
                val affiliatedBars = barToTeams.filter { it.value.contains(selectedTeam) }.keys
                // ...

                // Sort the affiliated bars by distance from the user's current location
                val sortedBars = affiliatedBars.sortedBy { bar ->
                    if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                            val latLng = LatLng(location.latitude, location.longitude)
                            calculateDistance(latLng, barToLocations[bar]!!)
                        } ?: 0.0.toDouble() // <--- Add .toDouble() here
                    } else {
                        0.0
                    }
                }

                // Clear the map of previous markers
                map.clear()

                // Add the markers for the affiliated bars
                for (bar in sortedBars) {
                    val location = barToLocations[bar]
                    if (location != null) {
                        // Create a marker for the bar
                        val marker = MarkerOptions()
                            .position(location)
                            .title(bar)
                        // Add the marker to the map
                        map.addMarker(marker)
                    }
                }

                displayBars(sortedBars)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Initialize the map object
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap

            fun displayBars(bars: List<String>) {
                val barList = findViewById<ListView>(R.id.bar_list)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bars)
                barList.adapter = adapter
            }

            // Move the teamSpinner.onItemSelectedListener code inside the getMapAsync callback
            teamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Toast.makeText(this@MainActivity, "Team selected", Toast.LENGTH_SHORT).show()
                    val selectedTeam = teamSpinner.selectedItem.toString()
                    // Get the affiliated bars for the selected team
                    val affiliatedBars = barToTeams.filter { it.value.contains(selectedTeam) }.keys

                    // Sort the affiliated bars by distance from the user's current location
                    val sortedBars = affiliatedBars.sortedBy { bar ->
                        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                                val latLng = LatLng(location.latitude, location.longitude)
                                calculateDistance(latLng, barToLocations[bar]!!)
                            } ?: 0.0.toDouble() // <--- Add .toDouble() here
                        } else {
                            0.0
                        }
                    }

                    // Clear the map of previous markers
                    map.clear()

                    // Add the markers for the affiliated bars
                    for (bar in sortedBars) {
                        val location = barToLocations[bar]
                        if (location != null) {
                            // Create a marker for the bar
                            val marker = MarkerOptions()
                                .position(location)
                                .title(bar)
                            // Add the marker to the map
                            map.addMarker(marker)
                        }
                    }

                    displayBars(sortedBars)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(this@MainActivity, "Nothing selected", Toast.LENGTH_SHORT).show()
                }
            }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                // Update location logic here
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // Update status logic here
            }
        }

// Add these two methods to handle provider changes
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.registerGnssStatusCallback(object : GnssStatus.Callback() {
                override fun onFirstFix(ttff: Int) {
                    // Handle first fix event
                }

                override fun onSatelliteStatusChanged(status: GnssStatus) {
                    // Handle satellite status change event
                }
            })
        } else {
            // Handle permission denied
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }

// ...
// Initialize the spinners
        val leagueSpinner = findViewById<Spinner>(R.id.league_spinner)
        val teamSpinner = findViewById<Spinner>(R.id.team_spinner)

// ...

// Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
            }
        }

// Create an adapter for the league spinner
        val leagueAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.leagues))
        leagueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        leagueSpinner.adapter = leagueAdapter

        leagueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedLeague = leagueSpinner.selectedItem.toString()
                when (selectedLeague) {
                    "MLB" -> {
                        val teamAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, mlbTeams)
                        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        teamSpinner.adapter = teamAdapter
                    }
                    "NBA" -> {
                        val teamAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, nbaTeams)
                        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        teamSpinner.adapter = teamAdapter
                    }
                    "NFL" -> {
                        val teamAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, nflTeams)
                        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        teamSpinner.adapter = teamAdapter
                    }
                    "NHL" -> {
                        val teamAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, nhlTeams)
                        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        teamSpinner.adapter = teamAdapter
                    }
                    "EPL" -> {
                        val teamAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, eplTeams)
                        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        teamSpinner.adapter = teamAdapter
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Update nothing selected logic here
            }
        }

        // Define the calculateDistance function outside of the onItemSelected method
        fun calculateDistance(location1: LatLng, location2: LatLng): Double {
            // Calculate the distance using the Haversine formula or any other method
            // Return the calculated distance in meters
            val lat1 = location1.latitude * Math.PI / 180
            val lon1 = location1.longitude * Math.PI / 180
            val lat2 = location2.latitude * Math.PI / 180
            val lon2 = location2.longitude * Math.PI / 180
            val dlat = lat2 - lat1
            val dlon = lon2 - lon1
            val a = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            val R = 6371 // Radius of the Earth in kilometers
            return R * c * 1000 // Return the distance in meters
        }

            // Request location updates
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
                }
            }
        }


        }}
