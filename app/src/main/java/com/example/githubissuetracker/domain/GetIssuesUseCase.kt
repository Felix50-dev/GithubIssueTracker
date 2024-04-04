package com.example.githubissuetracker.domain

import android.util.Log
import com.example.githubissuetracker.data.REPO_NAME
import com.example.githubissuetracker.data.REPO_OWNER

class GetIssuesUseCase(
    private val issuesClient: IssueClient
) {

    suspend fun execute(): List<SimpleIssue> {
        return issuesClient
            .getIssues("")
            .sortedBy { it.title }
    }

    fun getQuery(startDate: String, endDate: String, state: String, searchText: String): String {
        val query: StringBuilder = StringBuilder()
        query.append("repo:")
        query.append(REPO_OWNER)
        query.append("/")
        query.append(REPO_NAME)
        if (startDate.isNotEmpty()) {//only add if it is a date query
            query.append(" created:$startDate..$endDate")
        }
        if (state.isNotEmpty()) {//only add if it is a state filter
            query.append(" state:$state")
        }
        if (searchText.isNotEmpty()) {
            query.append(" in:title $searchText")
        }
        query.append(" type:issue")
        Log.e("getQueryForDate: ", query.toString())
        return query.toString()
    }
}