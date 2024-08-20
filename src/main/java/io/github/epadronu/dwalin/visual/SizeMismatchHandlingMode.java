/***************************************************************************************************
 * Copyright 2024 Edinson E. Padrón Urdaneta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************************************/

/* ************************************************************************************************/
package io.github.epadronu.dwalin.visual;
/* ************************************************************************************************/

/**
 * Enum representing the different modes for handling size mismatches between the actual and expected images
 * during image comparison.
 * <p>
 * This enum provides two strategies for dealing with images of differing dimensions:
 * </p>
 * <ul>
 *     <li>{@link #CROP}: Crops the larger image to match the dimensions of the smaller image.</li>
 *     <li>{@link #THROW_EXCEPTION}: Throws an exception if the dimensions of the actual and expected images do not match.</li>
 * </ul>
 */
public enum SizeMismatchHandlingMode {

  /**
   * Crops the larger image to fit the dimensions of the smaller image.
   */
  CROP,

  /**
   * Throws an exception when the sizes of the actual and expected images do not match.
   */
  THROW_EXCEPTION,
}
