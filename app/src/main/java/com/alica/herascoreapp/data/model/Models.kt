package com.alica.herascoreapp.data.model

data class Match(
    val id: String,
    val league: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String?,
    val utcTimestamp: String?,
    val venue: String?,
    val startDate: String?,
    val startTime: String?,
    val homeGoalDetails: List<GoalDetail>,
    val awayGoalDetails: List<GoalDetail>
)

data class GoalDetail(
    val minute: Int?,
    val player: String
)


