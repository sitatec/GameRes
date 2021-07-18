package dev.berete.gameres.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.screens.shared.components.PlatformLogos
import dev.berete.gameres.ui.utils.buildFakeGameList

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResult by viewModel.searchResult.observeAsState(emptyList())
    val focusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Card(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
        ) {
            TextField(
                value = searchQuery, { searchQuery = it },
                leadingIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }
                },
                placeholder = {
                    Text("Enter a game name")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.search(searchQuery)
                        keyboardController?.hide()
                    },
                ),
                modifier = Modifier.focusRequester(focusRequester = focusRequest)
            )
        }

        LaunchedEffect(Unit) {
            focusRequest.requestFocus()
        }

        if (viewModel.notFound.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Game Not Found",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface.copy(0.6f),
                )
            }
        } else {
            LazyColumn {
                item {
                    Spacer(Modifier.height(8.dp))
                }

                if (viewModel.isSearching.value) {
                    items(10) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Box(
                                    Modifier
                                        .padding(end = 8.dp)
                                        .size(68.dp, 75.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                        )
                                )
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(
                                        Modifier
                                            .placeholder(
                                                visible = true,
                                                highlight = PlaceholderHighlight.shimmer(),
                                            )
                                            .height(15.dp)
                                            .fillMaxWidth(0.65f)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Box(
                                        Modifier
                                            .placeholder(
                                                visible = true,
                                                highlight = PlaceholderHighlight.shimmer(),
                                            )
                                            .height(15.dp)
                                            .fillMaxWidth(0.35f)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                } else {
                    items(searchResult) { game ->
                        Card(
                            modifier = Modifier
                                .height(75.dp)
                                .fillMaxWidth()
                                .clickable { navController.navigate(Routes.gameDetails(game.id)) },
                        ) {
                            Row {
                                Image(
                                    painter = rememberCoilPainter(game.coverUrl),
                                    contentDescription = game.name,
                                    Modifier
                                        .padding(end = 8.dp)
                                        .fillMaxHeight()
                                )
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(game.name)
                                    // TODO Refactor
                                    PlatformLogos(platformTypeList = game.platformList.groupBy { it.platformType }.keys.toList())
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}