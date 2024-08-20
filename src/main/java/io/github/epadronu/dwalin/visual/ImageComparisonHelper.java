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
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.ACTUAL;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.EXPECTED;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.RESULT;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.checkIfModeIsEnabledInMask;
import static io.github.epadronu.dwalin.visual.SizeMismatchHandlingMode.THROW_EXCEPTION;
import static java.util.Objects.requireNonNull;
import static org.openqa.selenium.OutputType.FILE;
/* ************************************************************************************************/

/**
 * Helper class for performing image comparisons and managing related reporting activities.
 * <p>
 * The {@code ImageComparisonHelper} class provides methods to compare images and handle differences in their dimensions
 * based on the specified configuration. The results of the comparisons can be attached to an Allure report,
 * depending on the configuration of the report attachment mode.
 * </p>
 * <p>
 * This class supports two primary image comparison workflows:
 * </p>
 * <ul>
 *     <li>Comparing a screenshot taken from a {@link TakesScreenshot} subject against an expected {@link BufferedImage}.</li>
 *     <li>Directly comparing two {@link BufferedImage} objects.</li>
 * </ul>
 * <p>
 * Additionally, the class supports handling size mismatches between images through the {@link SizeMismatchHandlingMode}.
 * </p>
 *
 * @see ReportAttachmentMode
 * @see SizeMismatchHandlingMode
 * @see ImageComparison
 */
public class ImageComparisonHelper {

  private static final Logger log = LogManager.getLogger();

  private final Consumer<ImageComparison> imageComparisonConfigurationContext;

  private final AllureLifecycle lifecycle;

  private final int reportAttachmentMode;

  private final SizeMismatchHandlingMode sizeMismatchHandlingMode;

  private final AtomicReference<String> lastRecordedTestCaseOrStepId = new AtomicReference<>(null);

  private ImageComparisonHelper(
      final Consumer<ImageComparison> imageComparisonConfigurationContext,
      final AllureLifecycle lifecycle,
      final int reportAttachmentMode,
      final SizeMismatchHandlingMode sizeMismatchHandlingMode) {
    this.imageComparisonConfigurationContext = imageComparisonConfigurationContext;
    this.lifecycle = lifecycle;
    this.reportAttachmentMode = reportAttachmentMode;
    this.sizeMismatchHandlingMode = sizeMismatchHandlingMode;
  }

  /**
   * Compares a screenshot taken from a subject against an expected image.
   *
   * @param subject  the source from which the screenshot is taken. Must not be null.
   * @param expected the expected image to compare against. Must not be null.
   * @return the result of the image comparison
   * @throws IllegalArgumentException either if the screenshot cannot be converted to a BufferedImage or
   *                                  if the size mismatch handling mode is {@code THROW_EXCEPTION} and the sizes differ
   */
  public ImageComparisonResult compare(@Nonnull final TakesScreenshot subject, @Nonnull final BufferedImage expected) {
    try {
      return compare(ImageIO.read(requireNonNull(subject).getScreenshotAs(FILE)), requireNonNull(expected));
    } catch (final IOException exception) {
      throw new IllegalArgumentException("Failed to create a BufferedImage from the provided subject screenshot.", exception);
    }
  }

  /**
   * Compares the actual image against the expected image.
   *
   * @param actual   the actual image to compare. Must not be null.
   * @param expected the expected image to compare against. Must not be null.
   * @return the result of the image comparison
   * @throws IllegalArgumentException if the size mismatch handling mode is {@code THROW_EXCEPTION} and the sizes differ
   */
  public ImageComparisonResult compare(@Nonnull final BufferedImage actual, @Nonnull final BufferedImage expected) {
    final Map<String, BufferedImage> images = handleSizeMismatch(requireNonNull(actual), requireNonNull(expected));

    final ImageComparison imageComparison = new ImageComparison(images.get("expected"), images.get("actual"));

    imageComparisonConfigurationContext.accept(imageComparison);

    final ImageComparisonResult imageComparisonResult = imageComparison.compareImages();

    lifecycle.getCurrentTestCaseOrStep().ifPresent((uuid) -> {
      if (!uuid.equals(lastRecordedTestCaseOrStepId.get())) {
        if (checkIfModeIsEnabledInMask(ACTUAL, reportAttachmentMode)) {
          getImageBytes(imageComparisonResult.getActual())
              .ifPresent(bytes -> this.lifecycle.addAttachment("[Image comparison] Actual", "image/png", "png", bytes));
        }

        if (checkIfModeIsEnabledInMask(EXPECTED, reportAttachmentMode)) {
          getImageBytes(imageComparisonResult.getExpected())
              .ifPresent(bytes -> this.lifecycle.addAttachment("[Image comparison] Expected", "image/png", "png", bytes));
        }

        if (checkIfModeIsEnabledInMask(RESULT, reportAttachmentMode)) {
          getImageBytes(imageComparisonResult.getResult())
              .ifPresent(bytes -> this.lifecycle.addAttachment("[Image comparison] Result", "image/png", "png", bytes));
        }
      }

      lastRecordedTestCaseOrStepId.set(uuid);
    });

    return imageComparisonResult;
  }

  /**
   * Creates a new {@link ImageComparisonCondition} that checks if a {@link WebElement} visually matches the given expected image.
   * <p>
   * This method facilitates the creation of a condition to verify that a web element's appearance matches the specified expected image,
   * considering visual differences. The condition will use the {@link ImageComparisonHelper} associated with the current instance to
   * perform the image comparison.
   * </p>
   *
   * @param expectedImage the {@link BufferedImage} that represents the expected visual appearance of the web element.
   *                      This image should be a reference image that the web element's screenshot will be compared against.
   *                      Must not be null.
   * @return a new {@link ImageComparisonCondition} instance configured to compare the web element's screenshot with the given expected image.
   * @throws NullPointerException if the provided {@code expectedImage} is null.
   */
  public ImageComparisonCondition visuallyMatch(@Nonnull final BufferedImage expectedImage) {
    // Validate that the provided image is not null
    requireNonNull(expectedImage, "The expected image must not be null.");

    // Create and return a new ImageComparisonCondition instance with a descriptive name and the current instance's image comparison helper
    return new ImageComparisonCondition("visually match the expected image", this, expectedImage);
  }

  /**
   * Creates and returns a new {@link Builder} instance for constructing an {@link ImageComparisonHelper}.
   * <p>
   * This method provides a convenient way to start configuring an {@link ImageComparisonHelper} using the builder pattern.
   * </p>
   *
   * @return a new {@link Builder} instance for building an {@link ImageComparisonHelper}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Handles size mismatches between the actual and expected images according to the configured mode.
   *
   * @param actual   the actual image. Must not be null.
   * @param expected the expected image. Must not be null.
   * @return a map containing the processed actual and expected images
   * @throws IllegalArgumentException if the size mismatch handling mode is {@code THROW_EXCEPTION} and the sizes differ
   */
  private Map<String, BufferedImage> handleSizeMismatch(final BufferedImage actual, final BufferedImage expected) {
    switch (sizeMismatchHandlingMode) {
      case THROW_EXCEPTION:
        if ((actual.getWidth() != expected.getWidth()) || (actual.getHeight() != expected.getHeight())) {
          throw new IllegalArgumentException("Image size mismatch: Actual (%dx%d) vs Expected (%dx%d)"
              .formatted(actual.getWidth(), actual.getHeight(), expected.getWidth(), expected.getHeight()));
        } else {
          return Map.of("actual", actual, "expected", expected);
        }
      case CROP:
        final int minWidth = Math.min(actual.getWidth(), expected.getWidth());

        final int minHeight = Math.min(actual.getHeight(), expected.getHeight());

        return Map.of(
            "actual", cropIfNeeded(actual, minWidth, minHeight),
            "expected", cropIfNeeded(expected, minWidth, minHeight));
      default:
        throw new IllegalStateException("Unexpected value: " + sizeMismatchHandlingMode);
    }
  }

  /**
   * Crops the given image if its dimensions are larger than the specified width and height.
   *
   * @param image     the image to crop
   * @param maxWidth  the maximum allowed width
   * @param maxHeight the maximum allowed height
   * @return the cropped image or the original image if resizing is not needed
   */
  private BufferedImage cropIfNeeded(final BufferedImage image, final int maxWidth, final int maxHeight) {
    if ((image.getWidth() > maxWidth) || (image.getHeight() > maxHeight)) {
      return image.getSubimage(0, 0, maxWidth, maxHeight);
    } else {
      return image;
    }
  }

  /**
   * Converts a BufferedImage to a byte array.
   *
   * @param image the image to convert. Must not be null.
   * @return an {@code Optional} containing the byte array if successful, or an empty Optional if an error occurs
   */
  private static Optional<byte[]> getImageBytes(@Nonnull final BufferedImage image) {
    try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      ImageIO.write(image, "png", outputStream);

      return Optional.of(outputStream.toByteArray());
    } catch (final IOException exception) {
      log.warn("Failed to convert BufferedImage to byte array.", exception);
    }

    return Optional.empty();
  }

  /**
   * Builder class for constructing instances of {@link ImageComparisonHelper}.
   * <p>
   * The {@code Builder} class provides a fluent API for configuring and creating instances of {@link ImageComparisonHelper}.
   * It allows setting various parameters, such as the image comparison configuration context, the report attachment mode,
   * and how to handle size mismatches between images.
   * </p>
   * <p>
   * Example usage:
   * </p>
   * <pre>{@code
   * final ImageComparisonHelper helper = ImageComparisonHelper.builder()
   *     .imageComparisonConfigurationContext(config -> config.setThreshold(0.1))
   *     .reportAttachmentMode(ReportAttachmentMode.EXPECTED | ReportAttachmentMode.ACTUAL)
   *     .sizeMismatchHandlingMode(SizeMismatchHandlingMode.CUT_AND_FIT)
   *     .build();
   * }</pre>
   */
  public static final class Builder {

    private Consumer<ImageComparison> imageComparisonConfigurationContext = (_) -> {
    };

    private AllureLifecycle lifecycle = Allure.getLifecycle();

    private int reportAttachmentMode = RESULT;

    private SizeMismatchHandlingMode sizeMismatchHandlingMode = THROW_EXCEPTION;

    private Builder() {
      // Prevent instantiation outside the ImageComparisonHelper class.
    }

    /**
     * Sets the image comparison configuration context.
     *
     * @param imageComparisonConfigurationContext the configuration context to set. Must not be null.
     * @return the Builder instance for method chaining
     */
    public Builder imageComparisonConfigurationContext(@Nonnull final Consumer<ImageComparison> imageComparisonConfigurationContext) {
      this.imageComparisonConfigurationContext = requireNonNull(imageComparisonConfigurationContext);

      return this;
    }

    /**
     * Sets the Allure lifecycle instance to use for reporting.
     *
     * @param lifecycle the Allure lifecycle instance. Must not be null.
     * @return the Builder instance for method chaining
     */
    public Builder lifecycle(@Nonnull final AllureLifecycle lifecycle) {
      this.lifecycle = requireNonNull(lifecycle);

      return this;
    }

    /**
     * Sets the report attachment mode.
     *
     * @param reportAttachmentMode the mode indicating which images to attach to the report
     * @return the Builder instance for method chaining
     */
    public Builder reportAttachmentMode(final int reportAttachmentMode) {
      this.reportAttachmentMode = reportAttachmentMode;

      return this;
    }

    /**
     * Sets the size mismatch handling mode.
     *
     * @param sizeMismatchHandlingMode the mode indicating how to handle size mismatches between the actual and expected images
     * @return the Builder instance for method chaining
     */
    public Builder sizeMismatchHandlingMode(@Nonnull final SizeMismatchHandlingMode sizeMismatchHandlingMode) {
      this.sizeMismatchHandlingMode = requireNonNull(sizeMismatchHandlingMode);

      return this;
    }

    /**
     * Constructs and returns a new {@link ImageComparisonHelper} instance based on the current configuration of the {@link Builder}.
     * <p>
     * This method finalizes the configuration and creates an {@link ImageComparisonHelper} object that can be used to perform
     * image comparisons according to the specified settings.
     * </p>
     *
     * @return a new {@link ImageComparisonHelper} instance configured with the parameters set in the {@link Builder}.
     */
    public ImageComparisonHelper build() {
      return new ImageComparisonHelper(imageComparisonConfigurationContext, lifecycle, reportAttachmentMode, sizeMismatchHandlingMode);
    }
  }
}
