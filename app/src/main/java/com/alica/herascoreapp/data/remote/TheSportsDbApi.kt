package com.alica.herascoreapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheSportsDbApi {
    // Example: https://www.thesportsdb.com/api/v1/json/3/eventsday.php?d=2025-10-28&s=Soccer
    @GET("api/v1/json/{key}/eventsday.php")
    suspend fun getEventsOfDay(
        @Path("key") apiKey: String,
        @Query("d") date: String,
        @Query("s") sport: String = "Soccer"
    ): EventsDayResponse

    @GET("api/v1/json/{key}/livescore.php")
    suspend fun getLiveScores(
        @Path("key") apiKey: String,
        @Query("s") sport: String = "Soccer"
    ): EventsDayResponse

    @GET("api/v1/json/{key}/lookupevent.php")
    suspend fun lookupEvent(
        @Path("key") apiKey: String,
        @Query("id") eventId: String
    ): EventsDayResponse

    // List all soccer leagues
    @GET("api/v1/json/{key}/search_all_leagues.php")
    suspend fun searchAllLeagues(
        @Path("key") apiKey: String,
        @Query("s") sport: String = "Soccer"
    ): LeaguesResponse

    // Events by league + date
    @GET("api/v1/json/{key}/eventsday.php")
    suspend fun getEventsOfDayForLeague(
        @Path("key") apiKey: String,
        @Query("l") leagueName: String,
        @Query("d") date: String
    ): EventsDayResponse
}

data class EventsDayResponse(
    val events: List<EventItemDto>?
)

data class EventItemDto(
    val idEvent: String?,
    val strEvent: String?,
    val strLeague: String?,
    val strHomeTeam: String?,
    val strAwayTeam: String?,
    val intHomeScore: String?,
    val intAwayScore: String?,
    val strStatus: String?, // e.g., Not Started, Match Finished, 1H, 2H
    val strTimestamp: String?, // UTC timestamp
    val strVenue: String?,
    val dateEvent: String?,
    val strTime: String?,
    val strHomeGoalDetails: String?, // "23':Player A;45':Player B"
    val strAwayGoalDetails: String?
)

data class LeaguesResponse(
    val countries: List<LeagueItemDto>?
)

data class LeagueItemDto(
    val idLeague: String?,
    val strLeague: String?,
    val strSport: String?
)



