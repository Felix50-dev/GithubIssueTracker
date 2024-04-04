package com.example.githubissuetracker.domain

interface IssueClient {

    suspend fun getIssues(query: String): List<SimpleIssue>

}