package org.trackasia.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.camera.CameraUpdateFactory
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.MapView
import org.trackasia.android.maps.TrackAsiaMap
import org.trackasia.android.maps.OnMapReadyCallback
import org.trackasia.android.maps.Style
import org.trackasia.android.style.expressions.Expression
import org.trackasia.android.style.layers.FillLayer
import org.trackasia.android.style.layers.PropertyFactory
import org.trackasia.android.style.sources.GeoJsonSource
import org.trackasia.android.style.sources.Source
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.utils.IdleZoomListener
import org.trackasia.android.testapp.utils.ResourceUtils
import timber.log.Timber
import java.io.IOException

/**
 * Test activity showcasing the data driven runtime style API.
 */
class DataDrivenStyleActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var trackasiaMap: TrackAsiaMap
    private var idleListener: IdleZoomListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_driven_style)

        // Initialize map as normal
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { map: TrackAsiaMap? ->
                // Store for later
                if (map != null) {
                    trackasiaMap = map
                }
                trackasiaMap.setStyle(Style.getPredefinedStyle("Streets")) { style: Style? ->
                    // Add a parks layer
                    addParksLayer()

                    // Add debug overlay
                    setupDebugZoomView()
                }

                // Center and Zoom (Amsterdam, zoomed to streets)
                trackasiaMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(52.379189, 4.899431),
                        14.0
                    )
                )
            }
        )
    }

    private fun setupDebugZoomView() {
        val textView = findViewById<View>(R.id.textZoom) as TextView
        trackasiaMap.addOnCameraIdleListener(
            IdleZoomListener(
                trackasiaMap,
                textView
            ).also { idleListener = it }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_data_driven_style, menu)
        return true
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
        if (trackasiaMap != null && idleListener != null) {
            trackasiaMap.removeOnCameraIdleListener(idleListener!!)
        }
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_exponential_zoom_function -> {
                addExponentialZoomFunction()
                true
            }
            R.id.action_add_interval_zoom_function -> {
                addIntervalZoomFunction()
                true
            }
            R.id.action_add_categorical_source_function -> {
                addCategoricalSourceFunction()
                true
            }
            R.id.action_add_exponential_source_function -> {
                addExponentialSourceFunction()
                true
            }
            R.id.action_add_identity_source_function -> {
                addIdentitySourceFunction()
                true
            }
            R.id.action_add_interval_source_function -> {
                addIntervalSourceFunction()
                true
            }
            R.id.action_add_composite_categorical_function -> {
                addCompositeCategoricalFunction()
                true
            }
            R.id.action_add_composite_exponential_function -> {
                addCompositeExponentialFunction()
                true
            }
            R.id.action_add_composite_interval_function -> {
                addCompositeIntervalFunction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addExponentialZoomFunction() {
        Timber.i("Add exponential zoom function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>("water")!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.interpolate(
                    Expression.exponential(0.5f),
                    Expression.zoom(),
                    Expression.stop(1, Expression.color(Color.RED)),
                    Expression.stop(5, Expression.color(Color.BLUE)),
                    Expression.stop(10, Expression.color(Color.GREEN))
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addIntervalZoomFunction() {
        Timber.i("Add interval zoom function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>("water")!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.step(
                    Expression.zoom(),
                    Expression.rgba(0.0f, 255.0f, 255.0f, 1.0f),
                    Expression.stop(1, Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f)),
                    Expression.stop(5, Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f)),
                    Expression.stop(10, Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f))
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addExponentialSourceFunction() {
        Timber.i("Add exponential source function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.interpolate(
                    Expression.exponential(0.5f),
                    Expression.get("stroke-width"),
                    Expression.stop(1f, Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f)),
                    Expression.stop(5f, Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f)),
                    Expression.stop(10f, Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f))
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addCategoricalSourceFunction() {
        Timber.i("Add categorical source function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.match(
                    Expression.get("name"),
                    Expression.literal("Westerpark"),
                    Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                    Expression.literal("Jordaan"),
                    Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                    Expression.literal("Prinseneiland"),
                    Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f),
                    Expression.rgba(0.0f, 255.0f, 255.0f, 1.0f)
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addIdentitySourceFunction() {
        Timber.i("Add identity source function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillOpacity(
                Expression.get("fill-opacity")
            )
        )
        Timber.i("Fill opacity: %s", layer.fillOpacity)
    }

    private fun addIntervalSourceFunction() {
        Timber.i("Add interval source function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.step(
                    Expression.get("stroke-width"),
                    Expression.rgba(0.0f, 255.0f, 255.0f, 1.0f),
                    Expression.stop(1f, Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f)),
                    Expression.stop(2f, Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f)),
                    Expression.stop(3f, Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f))
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addCompositeExponentialFunction() {
        Timber.i("Add composite exponential function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.interpolate(
                    Expression.exponential(1f),
                    Expression.zoom(),
                    Expression.stop(
                        12,
                        Expression.step(
                            Expression.get("stroke-width"),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                            Expression.stop(1f, Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f)),
                            Expression.stop(2f, Expression.rgba(0.0f, 0.0f, 0.0f, 1.0f)),
                            Expression.stop(3f, Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f))
                        )
                    ),
                    Expression.stop(
                        15,
                        Expression.step(
                            Expression.get("stroke-width"),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                            Expression.stop(1f, Expression.rgba(255.0f, 255.0f, 0.0f, 1.0f)),
                            Expression.stop(2f, Expression.rgba(211.0f, 211.0f, 211.0f, 1.0f)),
                            Expression.stop(3f, Expression.rgba(0.0f, 255.0f, 255.0f, 1.0f))
                        )
                    ),
                    Expression.stop(
                        18,
                        Expression.step(
                            Expression.get("stroke-width"),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                            Expression.stop(1f, Expression.rgba(0.0f, 0.0f, 0.0f, 1.0f)),
                            Expression.stop(2f, Expression.rgba(128.0f, 128.0f, 128.0f, 1.0f)),
                            Expression.stop(3f, Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f))
                        )
                    )
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addCompositeIntervalFunction() {
        Timber.i("Add composite interval function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.interpolate(
                    Expression.linear(),
                    Expression.zoom(),
                    Expression.stop(
                        12,
                        Expression.step(
                            Expression.get("stroke-width"),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                            Expression.stop(1f, Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f)),
                            Expression.stop(2f, Expression.rgba(0.0f, 0.0f, 0.0f, 1.0f)),
                            Expression.stop(3f, Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f))
                        )
                    ),
                    Expression.stop(
                        15,
                        Expression.step(
                            Expression.get("stroke-width"),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                            Expression.stop(1f, Expression.rgba(255.0f, 255.0f, 0.0f, 1.0f)),
                            Expression.stop(2f, Expression.rgba(211.0f, 211.0f, 211.0f, 1.0f)),
                            Expression.stop(3f, Expression.rgba(0.0f, 255.0f, 255.0f, 1.0f))
                        )
                    ),
                    Expression.stop(
                        18,
                        Expression.step(
                            Expression.get("stroke-width"),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                            Expression.stop(1f, Expression.rgba(0.0f, 0.0f, 0.0f, 1.0f)),
                            Expression.stop(2f, Expression.rgba(128.0f, 128.0f, 128.0f, 1.0f)),
                            Expression.stop(3f, Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f))
                        )
                    )
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addCompositeCategoricalFunction() {
        Timber.i("Add composite categorical function")
        val layer = trackasiaMap.style!!.getLayerAs<FillLayer>(AMSTERDAM_PARKS_LAYER)!!
        layer.setProperties(
            PropertyFactory.fillColor(
                Expression.step(
                    Expression.zoom(),
                    Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f),
                    Expression.stop(
                        7f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        8f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        9f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        10f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        11f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        12f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        13f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        14f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.literal("Jordaan"),
                            Expression.rgba(0.0f, 255.0f, 0.0f, 1.0f),
                            Expression.literal("PrinsenEiland"),
                            Expression.rgba(0.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        15f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        16f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        17f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        18f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.literal("Jordaan"),
                            Expression.rgba(0.0f, 255.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        19f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        20f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        21f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(255.0f, 0.0f, 0.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    ),
                    Expression.stop(
                        22f,
                        Expression.match(
                            Expression.get("name"),
                            Expression.literal("Westerpark"),
                            Expression.rgba(0.0f, 0.0f, 255.0f, 1.0f),
                            Expression.rgba(255.0f, 255.0f, 255.0f, 1.0f)
                        )
                    )
                )
            )
        )
        Timber.i("Fill color: %s", layer.fillColor)
    }

    private fun addParksLayer() {
        // Add a source
        val source: Source
        try {
            source = GeoJsonSource(
                "amsterdam-parks-source",
                ResourceUtils.readRawResource(this, R.raw.amsterdam)
            )
            trackasiaMap.style!!.addSource(source)
        } catch (ioException: IOException) {
            Toast.makeText(
                this@DataDrivenStyleActivity,
                "Couldn't add source: " + ioException.message,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Add a fill layer
        trackasiaMap.style!!
            .addLayer(
                FillLayer(AMSTERDAM_PARKS_LAYER, source.id)
                    .withProperties(
                        PropertyFactory.fillColor(Expression.color(Color.GREEN)),
                        PropertyFactory.fillOutlineColor(Expression.rgb(0, 0, 255)),
                        PropertyFactory.fillAntialias(true)
                    )
            )
    }

    companion object {
        const val AMSTERDAM_PARKS_LAYER = "amsterdam-parks-layer"
    }
}
