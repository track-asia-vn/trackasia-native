package com.trackasia.android.maps;


import androidx.annotation.NonNull;

import com.trackasia.android.annotations.Polygon;
import com.trackasia.android.annotations.PolygonOptions;

import java.util.List;

/**
 * Interface that defines convenient methods for working with a {@link Polygon}'s collection.
 */
interface Polygons {
  Polygon addBy(@NonNull PolygonOptions polygonOptions, @NonNull TrackasiaMap mapboxMap);

  List<Polygon> addBy(@NonNull List<PolygonOptions> polygonOptionsList, @NonNull TrackasiaMap mapboxMap);

  void update(Polygon polygon);

  List<Polygon> obtainAll();
}
