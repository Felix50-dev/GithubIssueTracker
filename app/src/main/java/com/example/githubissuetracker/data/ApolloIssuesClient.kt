package com.example.githubissuetracker.data

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.example.githubissuetracker.GetIssuesQuery
import com.example.githubissuetracker.domain.IssueClient
import com.example.githubissuetracker.domain.SimpleIssue

private const val TAG = "ApolloIssuesClient"
class ApolloIssuesClient(
    private val apolloClient: ApolloClient
) : IssueClient {

    override suspend fun getIssues(query: String): List<SimpleIssue> {
        return apolloClient
            .query(GetIssuesQuery(query))
            .execute()
            .data
            ?.search?.nodes
            ?.mapNotNull { node ->
                when (val onIssue = node?.onIssue) {
                    is GetIssuesQuery.OnIssue -> onIssue.author?.let {
                        SimpleIssue(
                            title = onIssue.title,
                            url = onIssue.url.toString(), // Assuming url is a String
                            state = onIssue.state.name,
                            createdAt = onIssue.createdAt.toString(), // Assuming createdAt is a String
                            comments = onIssue.comments,
                            author = it,
                        )
                    }
                    else -> null
                }
            } ?: emptyList()
    }
}