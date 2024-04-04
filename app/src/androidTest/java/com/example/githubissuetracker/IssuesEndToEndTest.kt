package com.example.githubissuetracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.githubissuetracker.presentation.IssuesScreen
import com.example.githubissuetracker.presentation.IssuesViewModel
import com.example.githubissuetracker.ui.theme.GithubIssueTrackerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.impl.annotations.MockK
import org.junit.Rule
import org.junit.Test

class IssuesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @MockK
    lateinit var viewModel: IssuesViewModel // Mocked ViewModel

    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            composeRule.setContent {
                GithubIssueTrackerTheme {
                    IssuesScreen(viewModel = viewModel)
                }
            }
        }
    }

    @Test
    fun checkIssueDisplayed() {
        composeRule.onNodeWithContentDescription("Date").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("State").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Title").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Author").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Comments").assertIsDisplayed()
    }

}