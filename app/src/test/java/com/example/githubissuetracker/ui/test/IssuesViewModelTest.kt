package com.example.githubissuetracker.ui.test

import com.example.githubissuetracker.domain.IssueClient
import com.example.githubissuetracker.domain.SimpleIssue
import com.example.githubissuetracker.presentation.IssuesViewModel
import io.mockk.coEvery
import io.mockk.Mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class IssuesViewModelTest {

    private lateinit var viewModel: IssuesViewModel
    private val mockIssueClient: IssueClient = mockk()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        viewModel = IssuesViewModel(mockIssueClient)
    }

    @After
    fun cleanup() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test setIssuesState`() {
        viewModel.setIssuesState("Open")
        assert(viewModel.stateIssues.value == "Open")
    }

    @Test
    fun `test setStartDate`() {
        viewModel.setStartDate("2024-01-01")
        assert(viewModel.startDate.value == "2024-01-01")
    }

    @Test
    fun `test setEndDate`() {
        viewModel.setEndDate("2024-12-31")
        assert(viewModel.endDate.value == "2024-12-31")
    }

    @Test
    fun `test onSearchTextChange`() {
        viewModel.onSearchTextChange("test")
        assert(viewModel.searchText.value == "test")
    }

    @Test
    fun `test execute`() = testScope.runBlockingTest {
        // Mock the response from the IssueClient
        val expectedIssues = listOf(
            SimpleIssue(title = "Issue 1"),
            SimpleIssue(title = "Issue 2")
        )
        coEvery { mockIssueClient.getIssues(any()) } returns expectedIssues

        // Call the execute function and verify the result
        val result = viewModel.execute("query")
        assert(result == expectedIssues)
    }

    @Test
    fun `test stateFlow`() = testScope.runBlockingTest {
        // Mock the response from the IssueClient
        val expectedIssues = listOf(
            SimpleIssue(title = "Issue 1"),
            SimpleIssue(title = "Issue 2")
        )
        coEvery { mockIssueClient.getIssues(any()) } returns expectedIssues

        // Set up the ViewModel with initial state
        viewModel.setIssuesState("Open")
        viewModel.setStartDate("2024-01-01")
        viewModel.setEndDate("2024-12-31")
        viewModel.onSearchTextChange("test")

        // Verify the state flow
        val state = viewModel.state.first()
        assert(state.issues == expectedIssues)
        assert(state.isLoading == false)
    }
}
