package com.example.foodcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcart.ui.theme.FoodCartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodCartTheme {
                FoodCartScreen()
            }
        }
    }
}

class CartViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val QUANTITY_KEY = "quantity"
    }

    val quantity = savedStateHandle.getStateFlow(QUANTITY_KEY, 1)

    fun incrementQuantity() {
        savedStateHandle[QUANTITY_KEY] = quantity.value + 1
    }

    fun decrementQuantity() {
        if (quantity.value > 1) {
            savedStateHandle[QUANTITY_KEY] = quantity.value - 1
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCartScreen(
    viewModel: CartViewModel = viewModel()
) {
    val quantity by viewModel.quantity.collectAsState()
    val itemName = "Pizza"
    val pricePerItem = 100
    val totalPrice = pricePerItem * quantity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Food Cart") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = itemName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Price: $${pricePerItem}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            QuantitySelector(
                quantity = quantity,
                onIncrement = { viewModel.incrementQuantity() },
                onDecrement = { viewModel.decrementQuantity() }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Total Price",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$${totalPrice}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* Place order logic */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Place Order")
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        IconButton(
            onClick = onDecrement,
            enabled = quantity > 1
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
        }
        
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.headlineMedium
        )
        
        IconButton(
            onClick = onIncrement
        ) {
            Icon(Icons.Default.Add, contentDescription = "Increase quantity")
        }
    }
}
