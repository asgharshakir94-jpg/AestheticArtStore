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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.*

// The clean data models mapping local resources and cloud links
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
                "Abstract Sunset Horizon",
                "A vibrant expressionist blend capturing the sky.",
                "Rs. 1400",
                R.drawable.art_1,
                "https://drive.google.com/uc?id=1T4hsNAFr6G9lzg8tcPFcI2_IL_ILafwc"
            )),
            StoreItem("art_asset_2", "2", Artwork(
                "Neon Cyber Streetscape",
                "Calm and serene rhythmic deep blue minimalist stroke visuals.",
                "Rs. 1400",
                R.drawable.art_2,
                "https://drive.google.com/uc?id=1ZECC3rJRslYOpJcglV54NWbIL8Bwix1i"
            )),
            StoreItem("art_asset_3", "3", Artwork(
                "Ethereal Botanical Harmony",
                "Classic fine art painting highlighting detailed warm organic leaves.",
                "Rs. 1400",
                R.drawable.art_3,
                "https://drive.google.com/uc?id=14tzDWHXIKPF5GXmeMUkTuq7Gt_HDsTuE"
            )),
            StoreItem("art_asset_4", "4", Artwork(
                "Margalla Morning Mist",
                "Cyberpunk aesthetic contrast featuring rainy street reflection elements.",
                "Rs. 1400",
                R.drawable.art_4,
                "https://drive.google.com/uc?id=1ahxm-oJDvFDhUHGWFvXF2v0VguXn5IIq"
            )),
            StoreItem("art_asset_5", "5", Artwork(
                "Mystic Indus Geometric",
                "Soft light peach tones bringing a warm modern aesthetic look.",
                "Rs. 1400",
                R.drawable.art_5,
                "https://drive.google.com/uc?id=1ny9sVxmc5ZBK2KCsIcGLDEK7cBb_ExIz"
            )),
            StoreItem("art_asset_6", "6", Artwork(
                "Cobalt Ocean Surge",
                "Dreamy surreal fine canvas painting featuring soft cream nebula fields.",
                "Rs. 1400",
                R.drawable.art_6,
                "https://drive.google.com/uc?id=1HrOJgHmMA3J-XDky76VGCzhcw8TgaMN4"
            )),
            StoreItem("art_asset_7", "7", Artwork(
                "Minimalist Desert Dunes",
                "Geometric shapes arranged cleanly in mid-century style.",
                "Rs. 1400",
                R.drawable.art_7,
                "https://drive.google.com/uc?id=1zCSlgbYv-cz4G3wyKCNVcO-W3ILShfDx"
            )),
            StoreItem("art_asset_8", "8", Artwork(
                "Cyberpunk Lahore 2099",
                "Dark moody fine art capturing deep evergreens under a starry sky.",
                "Rs. 1400",
                R.drawable.art_8,
                "https://drive.google.com/uc?id=1Sa6q_NZItUytsSorO-UWqYWljMHy1rk5"
            )),
            StoreItem("art_asset_9", "9", Artwork(
                "Vintage Pastel Orchards",
                "Elegant classical sculpture digital drawing on textured dynamic backdrops.",
                "Rs. 1400",
                R.drawable.art_9,
                "https://drive.google.com/uc?id=1hBSxHs8RuyhlJy-Hthoxo_AZjBH5WgzC"
            )),
            StoreItem("art_asset_10", "10", Artwork(
                "Monochrome City Lines",
                "Japanese watercolor illustration capturing delicate floating petal aesthetics.",
                "Rs. 1400",
                R.drawable.art_10,
                "https://drive.google.com/uc?id=1heRbetzCZv3ey8kpSqD2cJ8duaVfqPHe"
            )),
            StoreItem("art_asset_11", "11", Artwork(
                "Emerald Forest Canopy",
                "High contrast geometric shadows from contemporary urban architectural angles.",
                "Rs. 1400",
                R.drawable.art_11,
                "https://drive.google.com/uc?id=1adM644PIm4cJQ0Y0D5rAtpzALrJWrOtc"
            )),
            StoreItem("art_asset_12", "12", Artwork(
                "Celestial Nebula Dust",
                "Psychedelic warm orange and cream winding ribbon aesthetics.",
                "Rs. 1400",
                R.drawable.art_12,
                "https://drive.google.com/uc?id=1VASX838TH4y6mhrXXslCqEvPvuzP1qJv"
            )),
            StoreItem("art_asset_13", "13", Artwork(
                "Golden Hour Whispers",
                "Soft soothing botanical minimal leaf designs perfect for clean backdrops.",
                "Rs. 1400",
                R.drawable.art_13,
                "https://drive.google.com/uc?id=1xtRaDMeejx0kqTVGDVsXzyYK6fU5RF_D"
            )),
            StoreItem("art_asset_14", "14", Artwork(
                "Retro Vaporwave Dream",
                "Fantasy cosmic art blending butterfly wings with starlight dust.",
                "Rs. 1400",
                R.drawable.art_14,
                "https://drive.google.com/uc?id=1yjgkVRhaSVhWNwk8ZBPsJZJrOWUqSqPC"
            )),
            StoreItem("art_asset_15", "15", Artwork(
                "Zen Ink Balance",
                "Rich impasto stroke simulation rendering pristine desert dunes.",
                "Rs. 1400",
                R.drawable.art_15,
                "https://drive.google.com/uc?id=1yN-XY_LDBz11URByX9W6vWyK8FmBE0Xn"
            ))
        )
    }
}
class BillingManager(
    private val activity: Activity,
    private val onUnlock: (String) -> Unit,
    private val onRestore: (String) -> Unit
) {
    private val billingClient: BillingClient
    private val productDetailsCache = mutableMapOf<String, ProductDetails>()

    // Only true while the user has an active purchase flow open (between launchPurchase
    // and the result coming back). Play's setListener can redeliver a backlog of old,
    // unacknowledged purchases on connection/reconnect — without this flag those would
    // also call onUnlock and re-open every download link at once.
    @Volatile
    private var isPurchaseInProgress = false

    init {
        billingClient = BillingClient.newBuilder(activity)
            .setListener { result, purchases ->
                val wasUserInitiated = isPurchaseInProgress
                isPurchaseInProgress = false

                if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        val productId = purchase.products.firstOrNull() ?: continue
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            if (wasUserInitiated) {
                                onUnlock(productId)
                            } else {
                                // Backlog/redelivered purchase, not a fresh buy — restore silently
                                onRestore(productId)
                            }

                            // Permanent unlock: acknowledge only, never consume.
                            // Consuming would let the same artwork be "bought" again later
                            // instead of Play Billing recognizing it as already owned.
                            if (!purchase.isAcknowledged) {
                                val ack = AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.purchaseToken)
                                    .build()
                                billingClient.acknowledgePurchase(ack) {}
                            }
                        }
                    }
                } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // user backed out of the Play purchase sheet; nothing to do
                } else {
                    Toast.makeText(activity, "Purchase failed: ${result.debugMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
            .build()

        startConnection()
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails()
                    restorePurchases()
                } else {
                    Toast.makeText(activity, "Billing unavailable: ${billingResult.debugMessage}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Play will call startConnection again on the next purchase attempt if needed
            }
        })
    }

    private fun queryProductDetails() {
        val productList = StoreData.getItems().map { item ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(item.productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        billingClient.queryProductDetailsAsync(
            params,
            object : ProductDetailsResponseListener {
                override fun onProductDetailsResponse(
                    billingResult: BillingResult,
                    productDetailsList: MutableList<ProductDetails>
                ) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        for (details in productDetailsList) {
                            productDetailsCache[details.productId] = details
                        }
                    }
                }
            }
        )
    }

    // Restores already-owned unlocks (e.g. after reinstall) so they don't have to be rebought
    private fun restorePurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(
            params,
            object : PurchasesResponseListener {
                override fun onQueryPurchasesResponse(
                    billingResult: BillingResult,
                    purchases: MutableList<Purchase>
                ) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        for (purchase in purchases) {
                            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                purchase.products.firstOrNull()?.let { onRestore(it) }
                            }
                        }
                    }
                }
            }
        )
    }

    fun launchPurchase(productId: String) {
        val details = productDetailsCache[productId]
        if (details == null) {
            Toast.makeText(activity, "Item not ready yet, please try again", Toast.LENGTH_SHORT).show()
            return
        }

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        )

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        isPurchaseInProgress = true
        billingClient.launchBillingFlow(activity, flowParams)
    }
}

class MainActivity : ComponentActivity() {

    // Tracks which productIds are unlocked. Backed by SharedPreferences so unlocks
    // survive process death (Play Billing restore in BillingManager also re-feeds this).
    private lateinit var unlockedState: MutableState<Set<String>>
    private lateinit var prefs: android.content.SharedPreferences
    private lateinit var billingManager: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("unlocked_artworks", Context.MODE_PRIVATE)
        val initiallyUnlocked = prefs.getStringSet("unlocked_ids", emptySet()) ?: emptySet()

        setContent {
            unlockedState = remember { mutableStateOf(initiallyUnlocked) }
            val unlockedIds by unlockedState

            billingManager = remember {
                BillingManager(
                    activity = this,
                    onUnlock = { productId -> markUnlocked(productId) },
                    onRestore = { productId -> markRestored(productId) }
                )
            }

            var selectedItem by remember { mutableStateOf<StoreItem?>(null) }

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(StoreData.getItems()) { item ->
                            ArtworkCard(
                                item = item,
                                isUnlocked = unlockedIds.contains(item.productId),
                                onClick = { selectedItem = item }
                            )
                        }
                    }

                    selectedItem?.let { item ->
                        ArtworkDialog(
                            item = item,
                            isUnlocked = unlockedIds.contains(item.productId),
                            onDismiss = { selectedItem = null },
                            onBuy = { billingManager.launchPurchase(item.productId) },
                            onDownload = { openDownloadLink(item.artwork.premiumDownloadUrl) }
                        )
                    }
                }
            }
        }
    }

    private fun markUnlocked(productId: String) {
        // Guard against duplicate PURCHASED callbacks for the same item re-opening the link
        if (unlockedState.value.contains(productId)) return

        val updated = unlockedState.value + productId
        unlockedState.value = updated
        prefs.edit().putStringSet("unlocked_ids", updated).apply()

        // Auto-open the download link in the browser — only for a purchase that just happened
        StoreData.getItems().firstOrNull { it.productId == productId }?.let {
            openDownloadLink(it.artwork.premiumDownloadUrl)
        }
    }

    // Silently restores ownership state on app launch. Never opens the download link —
    // that would re-trigger a browser download every time the app starts.
    private fun markRestored(productId: String) {
        if (unlockedState.value.contains(productId)) return
        val updated = unlockedState.value + productId
        unlockedState.value = updated
        prefs.edit().putStringSet("unlocked_ids", updated).apply()
    }

    private fun openDownloadLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Couldn't open download link", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ArtworkCard(item: StoreItem, isUnlocked: Boolean, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
    ) {
        Column {
            Image(
                painter = painterResource(id = item.artwork.imageResId),
                contentDescription = item.artwork.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.artwork.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isUnlocked) "Owned" else item.artwork.priceText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ArtworkDialog(
    item: StoreItem,
    isUnlocked: Boolean,
    onDismiss: () -> Unit,
    onBuy: () -> Unit,
    onDownload: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(item.artwork.title) },
        text = {
            Column {
                Image(
                    painter = painterResource(id = item.artwork.imageResId),
                    contentDescription = item.artwork.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(item.artwork.description)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isUnlocked) "You own this artwork" else "Unlock for ${item.artwork.priceText}",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (isUnlocked) onDownload() else onBuy()
                if (isUnlocked) onDismiss()
            }) {
                Text(if (isUnlocked) "Download" else "Buy & Download")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}