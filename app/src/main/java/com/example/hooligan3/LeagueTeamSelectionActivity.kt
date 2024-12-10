package com.example.hooligan3

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.hooligan3.R

// Define the Team class
data class Team(val name: String, val league: String, val latitude: Double, val longitude: Double)

class LeagueTeamSelectionActivity : Activity() {

    private lateinit var leagueSpinner: Spinner
    private lateinit var teamSpinner: Spinner
    private val teams = mutableListOf<Team>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.hooligan3.R.layout.activity_league_team_selection)

        // Initialize league spinner
        leagueSpinner = findViewById(com.example.hooligan3.R.id.league_spinner)
        val leagues = arrayOf("NFL", "MLB", "NBA", "NHL", "English Premier League")
        val leagueAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leagues)
        leagueSpinner.adapter = leagueAdapter

        // Initialize team spinner
        teamSpinner = findViewById(com.example.hooligan3.R.id.team_spinner)
        // Populate team spinner based on selected league
        leagueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLeague = leagues[position]
                // Filter teams by selected league and populate team spinner
                teams.clear()
                teams.addAll(getTeamsByLeague(selectedLeague))
                val teamAdapter = ArrayAdapter(this@LeagueTeamSelectionActivity, android.R.layout.simple_spinner_item, teams.map { it.name })
                teamSpinner.adapter = teamAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun getTeamsByLeague(league: String): List<Team> {
        // Return a list of teams for the selected league
        // This can be a database query or a hardcoded list
        // For example:
        return when (league) {
            "English Premier League" -> listOf(
                Team("Liverpool", league, 53.4308, -2.9606),
                Team("Manchester City", league, 53.4839, -2.2008),
                // ...
            )
            else -> listOf() // Return teams for other leagues
        }
    }
}