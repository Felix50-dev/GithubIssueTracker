package com.example.githubissuetracker

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.githubissuetracker.presentation.IssuesScreen
import com.example.githubissuetracker.presentation.IssuesViewModel
import com.example.githubissuetracker.ui.theme.GithubIssueTrackerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule

class IssuesScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @MockK
    lateinit var viewModel: IssuesViewModel // Mocked ViewModel

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            GithubIssueTrackerTheme {
                IssuesScreen(viewModel = viewModel)
            }
        }
    }

}