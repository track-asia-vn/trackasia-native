package org.trackasia.android.testapp.activity.snapshot

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.trackasia.android.camera.CameraPosition
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.geometry.LatLngBounds
import org.trackasia.android.maps.Style
import org.trackasia.android.snapshotter.MapSnapshot
import org.trackasia.android.snapshotter.MapSnapshotter
import org.trackasia.android.testapp.R
import java.util.*

/**
 * Test activity showing how to use a the [MapSnapshotter]
 */
class MapSnapshotterReuseActivity : AppCompatActivity(), MapSnapshotter.SnapshotReadyCallback {
    private var mapSnapshotter: MapSnapshotter? = null
    private lateinit var fab: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_snapshotter_reuse)
        fab = findViewById(R.id.fab)
        fab.setVisibility(View.INVISIBLE)
        fab.setOnClickListener(
            View.OnClickListener { v: View? ->
                fab.setVisibility(View.INVISIBLE)
                mapSnapshotter!!.setStyleUrl(randomStyle)
                if (random.nextInt(2) == 0) {
                    mapSnapshotter!!.setCameraPosition(randomCameraPosition)
                } else {
                    mapSnapshotter!!.setRegion(randomBounds)
                }
                if (random.nextInt(2) == 0) {
                    mapSnapshotter!!.setSize(512, 512)
                } else {
                    mapSnapshotter!!.setSize(256, 256)
                }
                mapSnapshotter!!.start(this@MapSnapshotterReuseActivity)
            }
        )
        mapSnapshotter = MapSnapshotter(
            applicationContext,
            MapSnapshotter.Options(512, 512).withStyleBuilder(
                Style.Builder().fromUri(
                    randomStyle
                )
            )
        )
        mapSnapshotter!!.start(this@MapSnapshotterReuseActivity)
    }

    override fun onSnapshotReady(snapshot: MapSnapshot) {
        fab!!.visibility = View.VISIBLE
        val imageView = findViewById<ImageView>(R.id.snapshot_image)
        imageView.setImageBitmap(snapshot.bitmap)
    }

    private val randomBounds: LatLngBounds
        private get() = LatLngBounds.Builder()
            .include(
                LatLng(
                    randomInRange(5f, 10f).toDouble(),
                    randomInRange(-5f, 5f)
                        .toDouble()
                )
            )
            .include(
                LatLng(
                    randomInRange(-5f, 5f).toDouble(),
                    randomInRange(5f, 10f)
                        .toDouble()
                )
            )
            .build()
    private val randomCameraPosition: CameraPosition
        private get() = CameraPosition.Builder()
            .target(
                LatLng(
                    randomInRange(-80f, 80f).toDouble(),
                    randomInRange(-160f, 160f)
                        .toDouble()
                )
            )
            .zoom(randomInRange(2f, 10f).toDouble())
            .bearing(randomInRange(0f, 90f).toDouble())
            .build()
    val randomStyle: String
        get() = when (random.nextInt(5)) {
            0 -> Style.getPredefinedStyle("Pastel")
            1 -> Style.getPredefinedStyle("Bright")
            2 -> Style.getPredefinedStyle("Streets")
            3 -> Style.getPredefinedStyle("Outdoor")
            4 -> Style.getPredefinedStyle("Satellite Hybrid")
            else -> Style.getPredefinedStyle("Streets")
        }

    companion object {
        private val random = Random()
        fun randomInRange(min: Float, max: Float): Float {
            return random.nextFloat() * (max - min) + min
        }
    }
}
