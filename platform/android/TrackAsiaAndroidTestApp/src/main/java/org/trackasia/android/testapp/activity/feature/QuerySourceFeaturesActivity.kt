package org.trackasia.android.testapp.activity.feature

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import org.trackasia.geojson.Feature
import org.trackasia.geojson.FeatureCollection
import org.trackasia.geojson.Point
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.MapView
import org.trackasia.android.maps.TrackAsiaMap
import org.trackasia.android.maps.Style
import org.trackasia.android.style.expressions.Expression
import org.trackasia.android.style.layers.CircleLayer
import org.trackasia.android.style.sources.GeoJsonSource
import org.trackasia.android.testapp.R

/**
 * Test activity showcasing using the query source features API to query feature counts
 */
class QuerySourceFeaturesActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_source_features)

        // Initialize map as normal
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map: TrackAsiaMap? ->
            if (map != null) {
                trackasiaMap = map
            }
            trackasiaMap.getStyle { style: Style -> initStyle(style) }
            trackasiaMap.setStyle(Style.getPredefinedStyle("Streets"))
        }
    }

    private fun initStyle(style: Style) {
        val properties = JsonObject()
        properties.addProperty("key1", "value1")
        val source = GeoJsonSource(
            "test-source",
            FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(Point.fromLngLat(17.1, 51.0), properties),
                    Feature.fromGeometry(Point.fromLngLat(17.2, 51.0), properties),
                    Feature.fromGeometry(Point.fromLngLat(17.3, 51.0), properties),
                    Feature.fromGeometry(Point.fromLngLat(17.4, 51.0), properties)
                )
            )
        )
        style.addSource(source)
        val visible = Expression.eq(Expression.get("key1"), Expression.literal("value1"))
        val invisible = Expression.neq(Expression.get("key1"), Expression.literal("value1"))
        val layer = CircleLayer("test-layer", source.id)
            .withFilter(visible)
        style.addLayer(layer)

        // Add a click listener
        trackasiaMap.addOnMapClickListener { point: LatLng? ->
            // Query
            val features = source.querySourceFeatures(
                Expression.eq(
                    Expression.get("key1"),
                    Expression.literal("value1")
                )
            )
            Toast.makeText(
                this@QuerySourceFeaturesActivity,
                String.format(
                    "Found %s features",
                    features.size
                ),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setColorFilter(ContextCompat.getColor(this, R.color.primary))
        fab.setOnClickListener { view: View? ->
            val visibility = layer.filter
            if (visibility != null && visibility == visible) {
                layer.setFilter(invisible)
                fab.setImageResource(R.drawable.ic_layers_clear)
            } else {
                layer.setFilter(visible)
                fab.setImageResource(R.drawable.ic_layers)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
