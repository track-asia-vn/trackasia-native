package org.trackasia.android.testapp.activity.style

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.maps.*
import org.trackasia.android.style.layers.*
import org.trackasia.android.style.sources.GeoJsonSource
import org.trackasia.android.testapp.R
import timber.log.Timber
import java.net.URI
import java.net.URISyntaxException

/**
 * Test activity showcasing using realtime GeoJSON to move a symbol on your map
 *
 *
 * TrackAsia Native equivalent of https://track-asia.com/trackasia-gl-js-docs/example/live-geojson/
 *
 */
class RealTimeGeoJsonActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: TrackAsiaMap) {
        trackasiaMap = map
        trackasiaMap.setStyle(Style.getPredefinedStyle("Streets")) { style -> // add source
            try {
                style.addSource(GeoJsonSource(ID_GEOJSON_SOURCE, URI(URL_GEOJSON_SOURCE)))
            } catch (malformedUriException: URISyntaxException) {
                Timber.e(malformedUriException, "Invalid URL")
            }

            // add layer
            val layer = SymbolLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
            layer.setProperties(PropertyFactory.iconImage("rocket_15"))
            style.addLayer(layer)

            // loop refresh geojson
            handler = Handler(Looper.getMainLooper())
            runnable = RefreshGeoJsonRunnable(trackasiaMap!!, handler!!)
            runnable?.let {
                handler!!.postDelayed(it, 2000)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        runnable?.let {
            handler!!.removeCallbacks(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private class RefreshGeoJsonRunnable(
        private val trackasiaMap: TrackAsiaMap,
        private val handler: Handler
    ) : Runnable {
        override fun run() {
            val geoJsonSource = trackasiaMap.style!!.getSource(ID_GEOJSON_SOURCE) as GeoJsonSource
            geoJsonSource.setUri(URL_GEOJSON_SOURCE)
            handler.postDelayed(this, 2000)
        }
    }

    companion object {
        private const val ID_GEOJSON_LAYER = "border"
        private const val ID_GEOJSON_SOURCE = ID_GEOJSON_LAYER
        private const val URL_GEOJSON_SOURCE = "https://m6rgfvqjp34nnwqcdm4cmmy3cm0dtupu.lambda-url.us-east-1.on.aws/"
    }
}
