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

/* ************************************************************************************************/
import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;

import static java.util.Objects.requireNonNull;
/* ************************************************************************************************/

/**
 * A condition that verifies if a screenshot of a {@link WebElement} matches a given expected image using image comparison.
 * <p>
 * This condition is typically used in test automation to ensure that the appearance of a web element on a page matches
 * a predefined image, taking into account possible differences such as slight shifts or variations in rendering.
 * </p>
 */
public final class ImageComparisonCondition extends WebElementCondition {

  private final ImageComparisonHelper imageComparisonHelper;

  private final BufferedImage expectedImage;

  /**
   * Constructs an {@code ImageComparisonCondition} with the specified name, comparison helper, and expected image.
   *
   * @param name                  the name of the condition, used for logging and identification purposes. Must not be null.
   * @param imageComparisonHelper the helper responsible for performing the image comparison. Must not be null.
   * @param expectedImage         the expected {@link BufferedImage} to compare against the screenshot of the {@link WebElement}. Must not be null.
   * @throws NullPointerException if any of the parameters are null
   */
  public ImageComparisonCondition(
      final String name,
      final ImageComparisonHelper imageComparisonHelper,
      final BufferedImage expectedImage) {
    super(requireNonNull(name, "Condition name must not be null."));

    this.imageComparisonHelper = requireNonNull(imageComparisonHelper, "ImageComparisonHelper must not be null.");
    this.expectedImage = requireNonNull(expectedImage, "Expected image must not be null.");
  }

  /**
   * Checks whether the screenshot of the given {@link WebElement} matches the expected image.
   * <p>
   * This method captures a screenshot of the specified {@link WebElement} and compares it against the expected image
   * using the {@link ImageComparisonHelper}. The result of this comparison is used to determine if the condition is met.
   * </p>
   *
   * @param driver     the {@link Driver} instance used to interact with the web page
   * @param webElement the {@link WebElement} to capture and compare
   * @return a {@link CheckResult} indicating whether the images match, including a descriptive message if they do not
   * @throws IllegalArgumentException if an error occurs during image comparison, such as a size mismatch or failure to capture the screenshot
   */
  @Nonnull
  @Override
  public CheckResult check(final Driver driver, final WebElement webElement) {
    final ImageComparisonResult imageComparisonResult = imageComparisonHelper.compare(webElement, expectedImage);

    // Check if the images match based on the comparison result
    final boolean imagesMatch = ImageComparisonState.MATCH.equals(imageComparisonResult.getImageComparisonState());

    // Log a detailed message about the comparison result
    final String message = imagesMatch
        ? "The images match."
        : "The images mismatch by a difference of %.2f%%".formatted(imageComparisonResult.getDifferencePercent());

    return new CheckResult(imagesMatch, message);
  }
}
