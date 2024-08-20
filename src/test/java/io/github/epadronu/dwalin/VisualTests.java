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
package io.github.epadronu.dwalin;
/* ************************************************************************************************/

/* ************************************************************************************************/
import com.codeborne.selenide.ex.ElementShould;
import io.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import io.github.epadronu.dwalin.visual.ImageComparisonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junitpioneer.jupiter.RetryingTest;
import org.openqa.selenium.Keys;

import java.awt.image.BufferedImage;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.github.romankh3.image.comparison.ImageComparisonUtil.readImageFromResources;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.ACTUAL;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.EXPECTED;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.NONE;
import static io.github.epadronu.dwalin.visual.ReportAttachmentMode.RESULT;
import static io.github.epadronu.dwalin.visual.SizeMismatchHandlingMode.CROP;
import static io.github.epadronu.dwalin.visual.SizeMismatchHandlingMode.THROW_EXCEPTION;
import static io.qameta.allure.Allure.getLifecycle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

/**
 * <p>
 * This class contains tests that verify the functionality of the following components:
 * {@code ImageComparisonCondition}, {@code ImageComparisonHelper}, {@code ReportAttachmentMode},
 * and the {@code SizeMismatchHandlingMode} enum. These tests ensure that the image comparison
 * features work as expected and handle various scenarios, such as size mismatches and reporting.
 * </p>
 *
 * @see io.github.epadronu.dwalin.visual.ImageComparisonCondition
 * @see io.github.epadronu.dwalin.visual.ImageComparisonHelper
 * @see io.github.epadronu.dwalin.visual.ReportAttachmentMode
 * @see io.github.epadronu.dwalin.visual.SizeMismatchHandlingMode
 */
@Tag("core")
@DisplayName("Visual tests")
public final class VisualTests extends DwalinWebDriverTest {

  private static final String PAGE_URL = "https://searxng.ch/";

  private static final BufferedImage firstResultExpectedImage = readImageFromResources(
      "images/image-comparison-first-result.png");

  private static final BufferedImage firstResultCutoffExpectedImage = readImageFromResources(
      "images/cutoff-image-comparison-first-result.png");

  @BeforeEach
  void openSearxngAndDoASearch() {
    open(PAGE_URL);

    $("#q").sendKeys("romankh3 image-comparison", Keys.ENTER);
  }

  @RetryingTest(maxAttempts = 3, name = "{displayName} [Attempt {index}]")
  @Tag("happy-path")
  void shouldImageComparisonWithTheDefaultConfigurationWorkAsExpected() {
    final ImageComparisonHelper helper = ImageComparisonHelper.builder().build();

    assertThatCode(() -> $$(".result")
        .shouldHave(sizeGreaterThanOrEqual(1))
        .first()
        .should(helper.visuallyMatch(firstResultExpectedImage)))
        .describedAs("The visual comparison did not match the expected outcome.")
        .doesNotThrowAnyException();
  }

  @RetryingTest(maxAttempts = 3, name = "{displayName} [Attempt {index}]")
  @Tag("sad-path")
  void shouldImageComparisonWithTheWrongElementFail() {
    final ImageComparisonHelper helper = ImageComparisonHelper.builder()
        .reportAttachmentMode(EXPECTED | RESULT)
        .build();

    assertThatCode(() -> $$(".result")
        .shouldHave(sizeGreaterThanOrEqual(2))
        .get(1)
        .should(helper.visuallyMatch(firstResultExpectedImage)))
        .describedAs("An exception was expected, but none was thrown.")
        .hasMessageContaining("Element should visually match the expected image")
        .hasMessageContaining("Actual value: The images mismatch by a difference of ")
        .doesNotThrowAnyExceptionExcept(ElementShould.class);
  }

  @RetryingTest(maxAttempts = 3, name = "{displayName} [Attempt {index}]")
  @Tag("happy-path")
  void shouldImageComparisonWithCutoffExpectedImageAndCutAndFitModeWorkAsExpected() {
    final ImageComparisonHelper helper = ImageComparisonHelper.builder()
        .sizeMismatchHandlingMode(CROP)
        .build();

    assertThatCode(() -> $$(".result")
        .shouldHave(sizeGreaterThanOrEqual(1))
        .first()
        .should(helper.visuallyMatch(firstResultCutoffExpectedImage)))
        .describedAs("The visual comparison did not match the expected outcome.")
        .doesNotThrowAnyException();
  }

  @RetryingTest(maxAttempts = 3, name = "{displayName} [Attempt {index}]")
  @Tag("sad-path")
  void shouldImageComparisonWithCutoffExpectedImageAndThrowExceptionModeFailAsExpected() {
    final ImageComparisonHelper helper = ImageComparisonHelper.builder()
        .sizeMismatchHandlingMode(THROW_EXCEPTION)
        .build();

    assertThatCode(() -> $$(".result")
        .shouldHave(sizeGreaterThanOrEqual(1))
        .first()
        .should(helper.visuallyMatch(firstResultCutoffExpectedImage)))
        .describedAs("An exception was expected, but none was thrown.")
        .hasMessageContaining("Image size mismatch: Actual (648x144) vs Expected (529x142)")
        .doesNotThrowAnyExceptionExcept(IllegalArgumentException.class);
  }

  @RetryingTest(maxAttempts = 3, name = "{displayName} [Attempt {index}]")
  @Tag("happy-path")
  void shouldImageComparisonWithNoneAsReportAttachmentModeNotAttachAnImage() {
    final ImageComparisonHelper helper = ImageComparisonHelper.builder()
        .reportAttachmentMode(NONE)
        .build();

    $$(".result")
        .shouldHave(sizeGreaterThanOrEqual(1))
        .first()
        .should(helper.visuallyMatch(firstResultExpectedImage));

    getLifecycle().updateTestCase(testResult -> {
      final long imageComparisonAttachmentCount = testResult.getAttachments()
          .stream()
          .filter(it -> it.getName().startsWith("[Image comparison]"))
          .count();

      assertThat(imageComparisonAttachmentCount)
          .describedAs("An unexpected attachment was found.")
          .isEqualTo(0L);
    });
  }

  @RetryingTest(maxAttempts = 3, name = "{displayName} [Attempt {index}]")
  @Tag("happy-path")
  void shouldImageComparisonWithActualExpectedAndResultAsReportAttachmentModeAttachAllImages() {
    final ImageComparisonHelper helper = ImageComparisonHelper.builder()
        .reportAttachmentMode(ACTUAL | EXPECTED | RESULT)
        .build();

    $$(".result")
        .shouldHave(sizeGreaterThanOrEqual(1))
        .first()
        .should(helper.visuallyMatch(firstResultExpectedImage));

    getLifecycle().updateStep(stepResult -> {
      final long imageComparisonAttachmentCount = stepResult.getAttachments()
          .stream()
          .filter(it -> it.getName().startsWith("[Image comparison]"))
          .count();

      assertThat(imageComparisonAttachmentCount)
          .describedAs("The expected number of attachments was not found.")
          .isEqualTo(3L);

      assertSoftly(softly -> stepResult.getAttachments()
          .stream()
          .filter(it -> it.getName().startsWith("[Image comparison]"))
          .forEach(image -> {
            softly.assertThat(image.getName())
                .describedAs("The attachment's name did not match the expected value.")
                .isIn("[Image comparison] Actual", "[Image comparison] Expected", "[Image comparison] Result");

            softly.assertThat(image.getType())
                .describedAs("The attachment was not a PNG image as expected.")
                .isNotBlank()
                .isEqualTo("image/png");

            softly.assertThat(image.getSource())
                .describedAs("The attachment's source must have the correct file extension.")
                .isNotBlank()
                .endsWith(".png");
          }));
    });
  }
}
