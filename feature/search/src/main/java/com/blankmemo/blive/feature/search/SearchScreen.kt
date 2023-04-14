/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.blankmemo.blive.feature.search

import android.webkit.URLUtil
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState

@Composable
internal fun SearchRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val feedState by viewModel.feedUiState.collectAsStateWithLifecycle()
    SearchScreen(
        feedState = feedState,
        removeFromBookmarks = viewModel::removeFromSavedResources,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun SearchScreen(
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
//    val searchResult by remember { mutableStateOf("") }
//    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Search",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = "searchQuery",
            onValueChange = { newValue ->
//                searchQuery = newValue
//                viewModel.onSearchTextChanged(newValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = com.google.samples.apps.nowinandroid.core.designsystem.R.drawable.ic_bookmarks),
                        contentDescription = "Search Icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    innerTextField()
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        val clickableText = remember("searchResult") {
            buildAnnotatedString {
                append("searchResult")
                val regex = Regex("""\b(https?://\S+\b)/?""")
                regex.findAll("searchResult").forEach { matchResult ->
                    val url = matchResult.value
                    val startIndex = matchResult.range.first
                    val endIndex = matchResult.range.last + 1
                    addStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline), startIndex, endIndex)
                    addStringAnnotation("URL", url, startIndex, endIndex)
                }
            }
        }
        ClickableText(
            text = clickableText,
            onClick = { offset ->
                clickableText.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()
                    ?.let { annotation ->
                        if (URLUtil.isValidUrl(annotation.item)) {
//                            navController.navigate(Uri.parse(annotation.item).toString())
                        }
                    }
            }
        )
    }
}
