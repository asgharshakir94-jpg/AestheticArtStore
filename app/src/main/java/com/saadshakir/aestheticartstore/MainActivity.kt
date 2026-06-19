package com.saadshakir.aestheticartstore

import android.app.Activity
import android.content.Context
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    private lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("ArtStorePrefs", Context.MODE_PRIVATE)
        val appIcon = R.drawable.app_icon

        val storeItems = mapOf(
            "art_asset_1" to Pair("Abstract Sunset Horizon", R.drawable.art_1),
            "art_asset_2" to Pair("Neon Cyber Streetscape", R.drawable.art_2),
            "art_asset_3" to Pair("Ethereal Botanical Harmony", R.drawable.art_3),
            "art_asset_4" to Pair("Margalla Morning Mist", R.drawable.art_4),
            "art_asset_5" to Pair("Mystic Indus Geometric", R.drawable.art_5),
            "art_asset_6" to Pair("Cobalt Ocean Surge", R.drawable.art_6),
            "art_asset_7" to Pair("Minimalist Desert Dunes", R.drawable.art_7),
            "art_asset_8" to Pair("Cyberpunk Lahore 2099", R.drawable.art_8),
            "art_asset_9" to Pair("Vintage Pastel Orchards", R.drawable.art_9),
            "art_asset_10" to Pair("Monochrome City Lines", R.drawable.art_10),
            "art_asset_11" to Pair("Emerald Forest Canopy", R.drawable.art_11),
            "art_asset_12" to Pair("Celestial Nebula Dust", R.drawable.art_12),
            "art_asset_13" to Pair("Golden Hour Whispers", R.drawable.art_13),
            "art_asset_14" to Pair("Retro Vaporwave Dream", R.drawable.art_14),
            "art_asset_15" to Pair("Zen Ink Balance", R.drawable.art_15)
        )

        billingClient = BillingClient.newBuilder(this)
            .setListener { result, purchases ->

                if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {

                    for (purchase in purchases) {

                        val productId = purchase.products.firstOrNull() ?: continue

                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

                            prefs.edit().putBoolean(productId, true).apply()

                            if (!purchase.isAcknowledged) {
                                val ack = AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.purchaseToken)
                                    .build()

                                billingClient.acknowledgePurchase(ack) {}
                            }

                            runOnUiThread {
                                Toast.makeText(
                                    this,
                                    "Purchase successful! HD unlocked",
                                    Toast.LENGTH_LONG
                                ).show()

                                recreate()
                            }
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
            override fun onBillingSetupFinished(result: BillingResult) {}
            override fun onBillingServiceDisconnected() {}
        })

        setContent {

            var screen by remember { mutableStateOf("home") }
            var selected by remember { mutableStateOf<String?>(null) }

            val context = this
            val activity = this

            MaterialTheme {

                // ✅ FIXED ROOT SURFACE (this removes wallpaper issue)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F0F0F) // clean dark background
                ) {

                    when (screen) {

                        "home" -> {

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Image(
                                    painter = painterResource(appIcon),
                                    contentDescription = null,
                                    modifier = Modifier.size(110.dp)
                                )

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    "Aesthetic Art Store",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(Modifier.height(30.dp))

                                Button(onClick = { screen = "gallery" }) {
                                    Text("Enter Gallery")
                                }
                            }
                        }

                        "gallery" -> {

                            LazyVerticalGrid(columns = GridCells.Fixed(2)) {

                                items(storeItems.entries.toList()) { item ->

                                    val productId = item.key
                                    val art = item.value

                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .height(280.dp)
                                            .clickable {
                                                selected = productId
                                                screen = "purchase"
                                            }
                                    ) {

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                            Image(
                                                painter = painterResource(art.second),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(220.dp),
                                                contentScale = ContentScale.Crop
                                            )

                                            Text(art.first)
                                        }
                                    }
                                }
                            }
                        }

                        "purchase" -> {

                            val id = selected ?: return@Surface
                            val art = storeItems[id] ?: return@Surface

                            val unlocked = prefs.getBoolean(id, false)

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    art.first,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(Modifier.height(10.dp))

                                Image(
                                    painter = painterResource(art.second),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(260.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.height(10.dp))

                                if (unlocked) {

                                    Button(onClick = {

                                        Thread {

                                            try {

                                                val assetName =
                                                    id.replace("art_asset_", "asset_") + ".jpg"

                                                val input = assets.open(assetName)

                                                val file = File(
                                                    getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS),
                                                    assetName
                                                )

                                                val output = FileOutputStream(file)

                                                input.copyTo(output)

                                                input.close()
                                                output.close()

                                                runOnUiThread {
                                                    Toast.makeText(
                                                        context,
                                                        "HD saved successfully",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                            } catch (e: Exception) {
                                                runOnUiThread {
                                                    Toast.makeText(
                                                        context,
                                                        "Download failed",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }

                                        }.start()

                                    }) {
                                        Text("Download HD")
                                    }

                                } else {

                                    Button(onClick = {
                                        launchPurchase(id)
                                    }) {
                                        Text("Unlock")
                                    }
                                }

                                Spacer(Modifier.height(10.dp))

                                Button(onClick = { screen = "home" }) {
                                    Text("Back Home")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun launchPurchase(productId: String) {

        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, list ->

            if (result.responseCode != BillingClient.BillingResponseCode.OK || list.isEmpty()) return@queryProductDetailsAsync

            val flow = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(list[0])
                            .build()
                    )
                )
                .build()

            billingClient.launchBillingFlow(this, flow)
        }
    }
}