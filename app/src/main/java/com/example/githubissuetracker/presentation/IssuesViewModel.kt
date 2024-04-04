package com.example.githubissuetracker.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubissuetracker.data.REPO_NAME
import com.example.githubissuetracker.data.REPO_OWNER
import com.example.githubissuetracker.domain.IssueClient
import com.example.githubissuetracker.domain.SimpleIssue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IssuesViewModel @Inject constructor(
    private val issuesClient: IssueClient
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _startDate = MutableStateFlow("")
    val startDate = _startDate.asStateFlow()

    private val _endDate =  MutableStateFlow("")
    val endDate = _endDate.asStateFlow()

    private val _stateIssues = MutableStateFlow("")
    val stateIssues = _stateIssues.asStateFlow()

    private val _state = MutableStateFlow(IssuesState())
    @OptIn(FlowPreview::class)
    val state = combine(
        searchText.debounce(1000L)
            .onEach {
                _isSearching.update { true }
            },
        _state,
        _startDate,
        _endDate,
        _stateIssues
    ) { text, issuesState, startDate, endDate, stateIssues ->
        val filtered = if (text.isBlank() && startDate.isBlank() && endDate.isBlank() && stateIssues.isBlank()) {
            issuesState.issues
        }
        else if (startDate.isNotBlank()) {
            issuesState.issues.filter {
                it.createdAt.contains(startDate, ignoreCase = true)
            }
        }
        else if (stateIssues.isNotBlank()) {
            issuesState.issues.filter {
                it.state.contains(stateIssues, ignoreCase = true)
            }
        }
        else {
            delay(2000L)
            issuesState.issues.filter {
                it.title.contains(text, ignoreCase = true)
            }
        }
        issuesState.copy(issues = filtered)
    }.onEach {
        _isSearching.update { false }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )


    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            _state.update {
                it.copy(
                    issues = execute(getQuery("", "", "", "")),
                    isLoading = false
                )
            }
        }
    }

    private suspend fun execute(query: String): List<SimpleIssue> {
        return issuesClient
            .getIssues(query)
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

    fun setIssuesState(text: String) {
        _stateIssues.value = text
    }

    fun setStartDate(date: String) {
        _startDate.value = date
    }

    fun setEndDate(date: String) {
        _endDate.value = date
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    data class IssuesState(
        val issues: List<SimpleIssue> = emptyList(),
        val isLoading: Boolean = false
    )
}