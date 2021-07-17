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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.screens.shared.components.PlatformLogos

@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResult by viewModel.searchResult.observeAsState(emptyList())
    val focusRequest = remember{ FocusRequester() }

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
                keyboardActions = KeyboardActions(onSearch = { viewModel.search(searchQuery) }),
                modifier = Modifier.focusRequester(focusRequester = focusRequest)
            )
        }

        LaunchedEffect(Unit){
            focusRequest.requestFocus()
        }

        LazyColumn() {
            item{
                Spacer(Modifier.height(8.dp))
            }
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