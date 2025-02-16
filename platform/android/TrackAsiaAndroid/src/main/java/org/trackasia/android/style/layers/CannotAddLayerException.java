package org.trackasia.android.style.layers;

import androidx.annotation.Keep;

/**
 * Thrown when adding a layer to a map twice
 */
@Keep
public class CannotAddLayerException extends RuntimeException {

  public CannotAddLayerException(String message) {
    super(message);
  }

}
