package com.example.githubissuetracker.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubissuetracker.domain.SimpleIssue
import com.example.githubissuetracker.ui.theme.GithubIssueTrackerTheme
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun IssuesScreen(
    viewModel: IssuesViewModel = hiltViewModel<IssuesViewModel>()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Scaffold(
                topBar = {
                    //IssuesTrackerAppBar(date = Date())
                }
            ) {
                FilterSection(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueItem(
    simpleIssue: SimpleIssue
) {
    var selected by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Opened ${simpleIssue.createdAt}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            FilterChip(
                selected = selected,
                onClick = { selected = !selected },
                label = { Text(text = simpleIssue.state) },
                shape = RoundedCornerShape(4.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = simpleIssue.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
            Text(
                text = simpleIssue.author.login,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null
            )
            Text(
                text = "${simpleIssue.comments.totalCount} comments",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    Divider()
}

@Composable
fun IssuesTrackerAppBar(
    date: Date
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Settings,
                modifier = Modifier.clickable { },
                contentDescription = null
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterSection(
    viewModel: IssuesViewModel
) {
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val state by viewModel.state.collectAsState()
    var isDatePickerVisible by remember {
        mutableStateOf(false)
    }
    var isDialogVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Date",
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        isDatePickerVisible = true
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Filter By",
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        isDialogVisible = true
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "iOS",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Xcode",
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (isDatePickerVisible) {
            CustomDatePicker(
                viewModel = viewModel,
                onDismiss = { isDatePickerVisible = false }
            )
        }

        if (isDialogVisible) {
            IssuesStateDialog(
                viewModel = viewModel,
                onDismiss = { isDialogVisible = false }
            )

        }

        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn {
                itemsIndexed(state.issues) { _, issue ->
                    IssueItem(
                        simpleIssue = issue
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(
    viewModel: IssuesViewModel,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp),
                dateFormatter = DatePickerFormatter()
            )
            val date = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it)
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = date?.atZone(ZoneOffset.UTC)?.format(formatter)
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = {
                    if (formattedDate != null) {
                        viewModel.setStartDate( formattedDate.toString())
                    }
                    onDismiss()
                }
            ) {
                Text(text = "OK")
            }
        }
    }
}

@Composable
fun IssuesStateDialog(
    viewModel: IssuesViewModel,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Open",
                modifier = Modifier.clickable {
                    viewModel.setIssuesState("Open")
                }
            )
            Text(
                text = "Closed",
                modifier = Modifier.clickable {
                    viewModel.setIssuesState("Closed")
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun IssuesScreenPreview() {
    GithubIssueTrackerTheme {
        IssuesScreen()
    }
}