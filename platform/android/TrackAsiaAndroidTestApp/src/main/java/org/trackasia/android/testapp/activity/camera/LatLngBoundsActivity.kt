package org.trackasia.android.testapp.activity.camera

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.trackasia.geojson.FeatureCollection
import org.trackasia.geojson.FeatureCollection.fromJson
import org.trackasia.geojson.Point
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.geometry.LatLngBounds
import org.trackasia.android.maps.TrackAsiaMap
import org.trackasia.android.maps.Style
import org.trackasia.android.style.layers.Property.ICON_ANCHOR_CENTER
import org.trackasia.android.style.layers.PropertyFactory.*
import org.trackasia.android.style.layers.SymbolLayer
import org.trackasia.android.style.sources.GeoJsonSource
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.databinding.ActivityLatlngboundsBinding
import org.trackasia.android.testapp.utils.GeoParseUtil
import org.trackasia.android.utils.BitmapUtils
import java.net.URISyntaxException

/** Test activity showcasing using the LatLngBounds camera API. */
class LatLngBoundsActivity : AppCompatActivity() {

    private lateinit var trackasiaMap: TrackAsiaMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bounds: LatLngBounds
    private lateinit var binding: ActivityLatlngboundsBinding

    private val peekHeight by lazy {
        375.toPx(this) // 375dp
    }

    private val additionalPadding by lazy {
        32.toPx(this) // 32dp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatlngboundsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initMapView(savedInstanceState)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            trackasiaMap = map

            val featureCollection: FeatureCollection =
                fromJson(GeoParseUtil.loadStringFromAssets(this, "points-sf.geojson"))
            bounds = createBounds(featureCollection)

            map.getCameraForLatLngBounds(bounds, createPadding(peekHeight))?.let {
                map.cameraPosition = it
            }

            try {
                loadStyle(featureCollection)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }

    private fun loadStyle(featureCollection: FeatureCollection) {
        trackasiaMap.setStyle(
            Style.Builder()
                .fromUri(Style.getPredefinedStyle("Streets"))
                .withLayer(
                    SymbolLayer("symbol", "symbol")
                        .withProperties(
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconImage("icon"),
                            iconAnchor(ICON_ANCHOR_CENTER)
                        )
                )
                .withSource(GeoJsonSource("symbol", featureCollection))
                .withImage(
                    "icon",
                    BitmapUtils.getDrawableFromRes(
                        this@LatLngBoundsActivity,
                        R.drawable.ic_android
                    )!!
                )
        ) {
            initBottomSheet()
            binding.fab.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val offset = convertSlideOffset(slideOffset)
                    val bottomPadding = (peekHeight * offset).toInt()

                    trackasiaMap.getCameraForLatLngBounds(bounds, createPadding(bottomPadding))
                        ?.let { trackasiaMap.cameraPosition = it }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // no-op
                }
            }
        )
    }

    // slideOffset ranges from NaN to -1.0, range from 1.0 to 0 instead
    fun convertSlideOffset(slideOffset: Float): Float {
        return if (slideOffset.equals(Float.NaN)) {
            1.0f
        } else {
            1 + slideOffset
        }
    }

    fun createPadding(bottomPadding: Int): IntArray {
        return intArrayOf(additionalPadding, additionalPadding, additionalPadding, bottomPadding)
    }

    private fun createBounds(featureCollection: FeatureCollection): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
        featureCollection.features()?.let {
            for (feature in it) {
                val point = feature.geometry() as Point
                boundsBuilder.include(LatLng(point.latitude(), point.longitude()))
            }
        }
        return boundsBuilder.build()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}

fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
