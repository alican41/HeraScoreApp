package com.alica.herascoreapp.data

import com.alica.herascoreapp.data.model.GoalDetail
import com.alica.herascoreapp.data.model.Match
import com.alica.herascoreapp.data.remote.EventItemDto
import com.alica.herascoreapp.data.remote.TheSportsDbApi

class MatchRepository(
    private val api: TheSportsDbApi,
    private val apiKey: String
) {
    suspend fun getMatchesForDate(dateIso: String): List<Match> {
        // Start with today's events
        val today = runCatching { api.getEventsOfDay(apiKey, dateIso, "Soccer").events.orEmpty() }.getOrDefault(emptyList())

        // If dataset is small, also fetch previous and next day to increase count
        val extraDates = listOf(-1, 1)
        val extra = extraDates.flatMap { offset ->
            val d = try {
                java.time.LocalDate.parse(dateIso).plusDays(offset.toLong()).toString()
            } catch (t: Throwable) { dateIso }
            runCatching { api.getEventsOfDay(apiKey, d, "Soccer").events.orEmpty() }.getOrDefault(emptyList())
        }

        val dayEvents = (today + extra)
        val dedupDay = dayEvents.distinctBy { it.idEvent }

        val liveEvents = runCatching { api.getLiveScores(apiKey, "Soccer").events.orEmpty() }.getOrDefault(emptyList())
        val liveById = liveEvents.associateBy { it.idEvent }

        val merged = dedupDay.map { evt ->
            val live = liveById[evt.idEvent]
            val base = evt
            (live ?: base).toDomain()
        }
        val dayIds = dedupDay.mapNotNull { it.idEvent }.toSet()
        val liveOnly = liveEvents.filter { it.idEvent !in dayIds }.map { it.toDomain() }
        val all = (merged + liveOnly)
            .distinctBy { it.id }
            .sortedBy { it.startTime ?: it.utcTimestamp ?: it.id }
            .take(200)
        return all
    }

    suspend fun getMatchDetails(eventId: String): Match? {
        val resp = api.lookupEvent(apiKey, eventId).events.orEmpty().firstOrNull()
        return resp?.toDomain()
    }
}

private fun EventItemDto.toDomain(): Match {
    return Match(
        id = idEvent.orEmpty(),
        league = strLeague.orEmpty(),
        homeTeam = strHomeTeam.orEmpty(),
        awayTeam = strAwayTeam.orEmpty(),
        homeScore = intHomeScore?.toIntOrNull(),
        awayScore = intAwayScore?.toIntOrNull(),
        status = strStatus,
        utcTimestamp = strTimestamp,
        venue = strVenue,
        startDate = dateEvent,
        startTime = strTime,
        homeGoalDetails = parseGoals(strHomeGoalDetails),
        awayGoalDetails = parseGoals(strAwayGoalDetails)
    )
}

private fun parseGoals(raw: String?): List<GoalDetail> {
    if (raw.isNullOrBlank()) return emptyList()
    return raw.split(";")
        .mapNotNull { token ->
            val parts = token.trim().split(":", limit = 2)
            if (parts.size == 2) {
                val minute = parts[0].replace("'", "").trim().toIntOrNull()
                val player = parts[1].trim()
                GoalDetail(minute = minute, player = player)
            } else null
        }
}


