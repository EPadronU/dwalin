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
 * Utility class that defines various modes for attaching images to a report.
 * <p>
 * The modes are represented as bit flags, allowing combinations of different modes to be used together.
 * These modes determine which images (if any) should be attached to the test report.
 * </p>
 * <p>
 * This class cannot be instantiated.
 * </p>
 */
public final class ReportAttachmentMode {

  /**
   * No attachments will be added to the report.
   */
  public static int NONE = 0b000;

  /**
   * Attach the result image to the report.
   */
  public static int RESULT = 0b001;

  /**
   * Attach the actual image to the report.
   */
  public static int ACTUAL = 0b010;

  /**
   * Attach the expected image to the report.
   */
  public static int EXPECTED = 0b100;

  private ReportAttachmentMode() {
    // Prevent instantiation
  }

  /**
   * Checks if a specific mode is enabled in the provided bitmask.
   *
   * @param mode the mode to check (e.g., {@link ReportAttachmentMode#RESULT})
   * @param mask the bitmask in which the mode is being checked
   * @return true if the mode is enabled in the mask, false otherwise
   */
  public static boolean checkIfModeIsEnabledInMask(final int mode, final int mask) {
    return (mode & mask) == mode;
  }
}
