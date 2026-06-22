package com.saadshakir.aestheticartstore


import com.saadshakir.aestheticartstore.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.*

// 1. DATA MODELS
data class Artwork(
    val title: String,
    val description: String,
    val priceText: String,
    val imageResId: Int,
    val premiumDownloadUrl: String
)

data class StoreItem(
    val productId: String,
    val assetIndexString: String,
    val artwork: Artwork
)

object StoreData {
    fun getItems(): List<StoreItem> {
        return listOf(
            StoreItem("art_asset_1", "1", Artwork(
                "Abstract Sunset Horizon", "A vibrant expressionist blend capturing the sky.", "Rs. 1400", R.drawable.art_1, "https://google.com"
            )),
            StoreItem("art_asset_2", "2", Artwork(
                "Neon Cyber Streetscape", "Calm and serene rhythmic deep blue minimalist stroke visuals.", "Rs. 1400", R.drawable.art_2, "https://google.com"
            )),
            StoreItem("art_asset_3", "3", Artwork(
                "Ethereal Botanical Harmony", "Classic fine art painting highlighting detailed warm organic leaves.", "Rs. 1400", R.drawable.art_3, "https://google.com"
            )),
            StoreItem("art_asset_4", "4", Artwork(
                "Margalla Morning Mist", "Cyberpunk aesthetic contrast featuring rainy street reflection elements.", "Rs. 1400", R.drawable.art_4, "https://google.com"
            )),
            StoreItem("art_asset_5", "5", Artwork(
                "Mystic Indus Geometric", "Soft light peach tones bringing a warm modern aesthetic look.", "Rs. 1400", R.drawable.art_5, "https://google.com"
            )),
            StoreItem("art_asset_6", "6", Artwork(
                "Cobalt Ocean Surge", "Dreamy surreal fine canvas painting featuring soft cream nebula fields.", "Rs. 1400", R.drawable.art_6, "https://google.com"
            )),
            StoreItem("art_asset_7", "7", Artwork(
                "Minimalist Desert Dunes", "Geometric shapes arranged cleanly in mid-century style.", "Rs. 1400", R.drawable.art_7, "https://google.com"
            )),
            StoreItem("art_asset_8", "8", Artwork(
                "Cyberpunk Lahore 2099", "Dark moody fine art capturing deep evergreens under a starry sky.", "Rs. 1400", R.drawable.art_8, "https://google.com"
            )),
            StoreItem("art_asset_9", "9", Artwork(
                "Vintage Pastel Orchards", "Elegant classical sculpture digital drawing on textured dynamic backdrops.", "Rs. 1400", R.drawable.art_9, "https://google.com"
            )),
            StoreItem("art_asset_10", "10", Artwork(
                "Monochrome City Lines", "Japanese watercolor illustration capturing delicate floating petal aesthetics.", "Rs. 1400", R.drawable.art_10, "https://google.com"
            )),
            StoreItem("art_asset_11", "11", Artwork(
                "Emerald Forest Canopy", "High contrast geometric shadows from contemporary urban architectural angles.", "Rs. 1400", R.drawable.art_11, "https://google.com"
            )),
            StoreItem("art_asset_12", "12", Artwork(
                "Celestial Nebula Dust", "Psychedelic warm orange and cream winding ribbon aesthetics.", "Rs. 1400", R.drawable.art_12, "https://google.com"
            )),
            StoreItem("art_asset_13", "13", Artwork(
                "Golden Hour Whispers", "Soft soothing botanical minimal leaf designs perfect for clean backdrops.", "Rs. 1400", R.drawable.art_13, "https://google.com"
            )),
            StoreItem("art_asset_14", "14", Artwork(
                "Retro Vaporwave Dream", "Fantasy cosmic art blending butterfly wings with starlight dust.", "Rs. 1400", R.drawable.art_14, "https://google.com"
            )),
            StoreItem("art_asset_15", "15", Artwork(
                "Zen Ink Balance", "Rich impasto stroke simulation rendering pristine desert dunes.", "Rs. 1400", R.drawable.art_15, "https://google.com"
            ))
        )
    }
}
// 2. MAIN ACTIVITY
class MainActivity : ComponentActivity() {
    private lateinit var billingManager: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Local state trackers for purchased items
        val unlockedItems = mutableStateListOf<String>()

        billingManager = BillingManager(
            activity = this,
            onUnlock = { productId ->
                unlockedItems.add(productId)
                // Instantly open the download link when purchased
                val item = StoreData.getItems().find { it.productId == productId }
                item?.let { openDownloadLink(this, it.artwork.premiumDownloadUrl) }
            },
            onRestore = { productId ->
                unlockedItems.add(productId)
            }
        )

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        // Clean Redirection Header Banner
                        VisitWebGalleryHeader()

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(StoreData.getItems()) { item ->
                                val isUnlocked = unlockedItems.contains(item.productId)
                                ArtworkCard(item = item, isUnlocked = isUnlocked) {
                                    if (isUnlocked) {
                                        openDownloadLink(this@MainActivity, item.artwork.premiumDownloadUrl)
                                    } else {
                                        billingManager.launchPurchaseFlow(item.productId)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
// 3. UI LAYOUT COMPONENTS (COMPOSABLES)
@Composable
fun VisitWebGalleryHeader() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.io"))
                context.startActivity(webIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Visit Web Gallery", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun ArtworkCard(item: StoreItem, isUnlocked: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = item.artwork.imageResId),
                contentDescription = item.artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                // CHANGED: Using .Crop with a capital C solves the unresolved reference instantly!
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = item.artwork.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                Text(
                    text = if (isUnlocked) "🔓 Purchased" else item.artwork.priceText,
                    fontSize = 12.sp,
                    color = if (isUnlocked) Color(0xFF2D6A4F) else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// 4. HELPER UTILS
fun openDownloadLink(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
    }
}

// 5. BILLING MANAGER INFRASTRUCTURE
class BillingManager(
    private val activity: Activity,
    private val onUnlock: (String) -> Unit,
    private val onRestore: (String) -> Unit
) {
    private var billingClient: BillingClient
    @Volatile private var isPurchaseInProgress = false

    init {
        billingClient = BillingClient.newBuilder(activity)
            .setListener { result, purchases ->
                val wasUserInitiated = isPurchaseInProgress
                isPurchaseInProgress = false
                if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        val productId = purchase.products.firstOrNull() ?: continue
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            if (wasUserInitiated) onUnlock(productId) else onRestore(productId)
                            acknowledgePurchase(purchase)
                        }
                    }
                }
            }
            .enablePendingPurchases()
            .build()
        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                startConnection()
            }
        })
    }

    private fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        billingClient.queryPurchasesAsync(params) { _, purchases ->
            for (purchase in purchases) {
                val productId = purchase.products.firstOrNull() ?: continue
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    onRestore(productId)
                }
            }
        }
    }

    fun launchPurchaseFlow(productId: String) {
        isPurchaseInProgress = true
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        billingClient.queryProductDetailsAsync(params) { _, productDetailsList ->
            val productDetails = productDetailsList.firstOrNull()
            if (productDetails != null) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .build()
                    ))
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                isPurchaseInProgress = false
                activity.runOnUiThread {
                    Toast.makeText(activity, "Product not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(params) { }
        }
    }
}
