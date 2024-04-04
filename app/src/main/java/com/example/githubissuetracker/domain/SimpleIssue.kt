package com.example.githubissuetracker.domain

import com.example.githubissuetracker.GetIssuesQuery
import com.example.githubissuetracker.type.IssueState

data class SimpleIssue(
    val title:String,
    val url: String,
    val state: String,
    val createdAt: String,
    val author: GetIssuesQuery.Author,
    val comments: GetIssuesQuery.Comments
)
