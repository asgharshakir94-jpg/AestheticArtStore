package com.saadshakir.aestheticartstore

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import coil3.compose.AsyncImage // FIXED: Modern Coil 3.x package mapping path architecture
import coil3.toBitmap
import kotlinx.coroutines.launch
import com.saadshakir.aestheticartstore.R // FIXED: Explicitly maps resource directory pointers to clear drawable errors

data class Artwork(val title: String, val description: String, val priceText: String, val imageUrl: String)

class MainActivity : ComponentActivity() {
    private lateinit var billingClient: BillingClient
    private var isBillingConnected = false
    private val unlockedArtworks = mutableStateListOf<String>()
    private var selectedIndexState by mutableIntStateOf(-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUnlockedArtFromCache()
        setupGoogleBilling()

        val baseUrl = "https://githubusercontent.com"

        val artworkList = listOf(
            Artwork("Abstract Sunset Horizon", "A vibrant expressionist blend capturing the sky.", "Price: Rs. 1,400", "${baseUrl}asset_1.jpg"),
            Artwork("Neon Cyber Streetscape", "Cyberpunk aesthetic contrast featuring rainy street reflection elements.", "Price: Rs. 1,400", "${baseUrl}asset_2.jpg"),
            Artwork("Ethereal Botanical Harmony", "Classic fine art painting highlighting detailed warm organic leaves.", "Price: Rs. 1,400", "${baseUrl}asset_3.jpg"),
            Artwork("Margalla Morning Mist", "Soft soothing atmospheric minimal leaf designs perfect for clean backdrops.", "Price: Rs. 1,400", "${baseUrl}asset_4.jpg"),
            Artwork("Mystic Indus Geometric", "Geometric shapes arranged cleanly in a mid-century style layout.", "Price: Rs. 1,400", "${baseUrl}asset_5.jpg"),
            Artwork("Cobalt Ocean Surge", "Calm and serene rhythmic deep blue minimalist stroke visuals.", "Price: Rs. 1,400", "${baseUrl}asset_6.jpg"),
            Artwork("Minimalist Desert Dunes", "Rich impasto stroke simulation rendering pristine desert dunes.", "Price: Rs. 1,400", "${baseUrl}asset_7.jpg"),
            Artwork("Cyberpunk Lahore 2099", "Futuristic neon visual elements blended with historic cultural structures.", "Price: Rs. 1,400", "${baseUrl}asset_8.jpg"),
            Artwork("Vintage Pastel Orchards", "Elegant classical composition digital drawing on textured dynamic backdrops.", "Price: Rs. 1,400", "${baseUrl}asset_9.jpg"),
            Artwork("Monochrome City Lines", "High contrast geometric shadows from contemporary urban architectural angles.", "Price: Rs. 1,400", "${baseUrl}asset_10.jpg"),
            Artwork("Emerald Forest Canopy", "Dark moody fine art capturing deep evergreens under a starry sky.", "Price: Rs. 1,400", "${baseUrl}asset_11.jpg"),
            Artwork("Celestial Nebula Dust", "Dreamy surreal fine canvas painting featuring soft cream nebula fields.", "Price: Rs. 1,400", "${baseUrl}asset_12.jpg"),
            Artwork("Golden Hour Whispers", "Soft light peach tones bringing a warm modern aesthetic look.", "Price: Rs. 1,400", "${baseUrl}asset_13.jpg"),
            Artwork("Retro Vaporwave Dream", "Psychedelic warm orange and cream winding ribbon aesthetics.", "Price: Rs. 1,400", "${baseUrl}asset_14.jpg"),
            Artwork("Zen Ink Balance", "Japanese watercolor illustration capturing delicate floating petal aesthetics.", "Price: Rs. 1,400", "${baseUrl}asset_15.jpg")
        )

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var screenState by remember { mutableStateOf("gallery") }

                    if (screenState == "gallery") {
                        ArtStoreGallery(artworkList = artworkList, onArtClick = { index: Int ->
                            selectedIndexState = index
                            screenState = "purchase"
                        })
                    } else if (screenState == "purchase" && selectedIndexState != -1) {
                        val currentProductID = "art_asset_${selectedIndexState + 1}"
                        val isProductUnlocked = unlockedArtworks.contains(currentProductID)

                        PurchaseScreen(
                            artIndex = selectedIndexState,
                            artwork = artworkList[selectedIndexState],
                            isUnlocked = isProductUnlocked,
                            onBackClick = { screenState = "gallery" },
                            onBuyClick = {
                                launchPurchaseFlow(currentProductID)
                            }
                        )
                    }
                }
            }
        }
    }
    private fun loadUnlockedArtFromCache() {
        val prefs = getSharedPreferences("ArtStorePrefs", Context.MODE_PRIVATE)
        for (i in 1..15) {
            val productID = "art_asset_$i"
            if (prefs.getBoolean(productID, false)) {
                unlockedArtworks.add(productID)
            }
        }
    }

    private fun setupGoogleBilling() {
        billingClient = BillingClient.newBuilder(this)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            val productBought = "art_asset_${selectedIndexState + 1}"

                            getSharedPreferences("ArtStorePrefs", Context.MODE_PRIVATE).edit().apply {
                                putBoolean(productBought, true)
                                apply()
                            }

                            if (!unlockedArtworks.contains(productBought)) {
                                unlockedArtworks.add(productBought)
                            }

                            Toast.makeText(this, "High-Res Unlock Granted!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isBillingConnected = true
                }
            }
            override fun onBillingServiceDisconnected() {
                isBillingConnected = false
            }
        })
    }

    private fun launchPurchaseFlow(productId: String) {
        if (!isBillingConnected) {
            Toast.makeText(this, "Billing system not ready. Try again.", Toast.LENGTH_SHORT).show()
            return
        }

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList.find { it.productId == productId }

                if (productDetails != null) {
                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                            listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .build()
                            )
                        ).build()

                    runOnUiThread {
                        billingClient.launchBillingFlow(this, billingFlowParams)
                    }
                }
            }
        }
    }
} // This specific brace seals the main Activity class container safely
@Composable
fun ArtStoreGallery(artworkList: List<Artwork>, onArtClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(artworkList) { index, artwork ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onArtClick(index) }
            ) {
                Column {
                    AsyncImage(
                        model = artwork.imageUrl,
                        contentDescription = artwork.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = artwork.title, style = MaterialTheme.typography.labelLarge, maxLines = 2)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = artwork.priceText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseScreen(
    artIndex: Int,
    artwork: Artwork,
    isUnlocked: Boolean,
    onBackClick: () -> Unit,
    onBuyClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = artwork.imageUrl,
            contentDescription = artwork.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 20.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )

        Text(text = artwork.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = artwork.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isUnlocked) "Status: Unlocked (Purchased)" else "Status: Locked",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = onBackClick) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (!isUnlocked) {
                Button(onClick = onBuyClick) {
                    Text("Buy Setup")
                }
            } else {
                Button(
                    onClick = {
                        Toast.makeText(context, "Downloading High-Res from GitHub...", Toast.LENGTH_SHORT).show()
                        coroutineScope.launch {
                            downloadAndSaveFromGitHub(context, artwork.imageUrl, artwork.title)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Download High-Res")
                }
            }
        }
    }
}
suspend fun downloadAndSaveFromGitHub(context: android.content.Context, urlString: String, title: String) {
    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            val loader = coil3.ImageLoader(context)
            val request = coil3.request.ImageRequest.Builder(context)
                .data(urlString)
                .build()

            val result = (loader.execute(request) as? coil3.request.SuccessResult)?.image
            val bitmap = result?.toBitmap()

            if (bitmap != null) {
                val filename = "${title.replace(" ", "_")}_HighRes.jpg"
                val contentValues = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_PICTURES + "/AestheticArtStore")
                        put(android.provider.MediaStore.MediaColumns.IS_PENDING, 1)
                    }
                }

                val resolver = context.contentResolver
                val imageUri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                if (imageUri != null) {
                    val outputStream = resolver.openOutputStream(imageUri)
                    if (outputStream != null) {
                        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(android.provider.MediaStore.MediaColumns.IS_PENDING, 0)
                        resolver.update(imageUri, contentValues, null, null)
                    }

                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        Toast.makeText(context, "Saved to Gallery under Pictures/AestheticArtStore!", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    Toast.makeText(context, "Failed to download image data.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                Toast.makeText(context, "Download Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
