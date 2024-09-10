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
import com.codeborne.selenide.ex.UIAssertionError;
import io.github.epadronu.dwalin.core.Dwalin;
import io.github.epadronu.dwalin.core.ElementGuard;
import io.github.epadronu.dwalin.core.NavigablePage;
import io.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import io.qameta.allure.model.Attachment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junitpioneer.jupiter.RetryingTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.github.epadronu.dwalin.core.Dwalin.attachScreenshotToAllureReport;
import static io.github.epadronu.dwalin.core.ElementGuard.guard;
import static io.qameta.allure.Allure.getLifecycle;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

/**
 * Tests validating the functionality of the Dwalin utility class, as well as the NavigablePage interface.
 *
 * @see Dwalin
 * @see NavigablePage
 */
@Tag("core")
@DisplayName("Dwalin's tests")
public final class DwalinTests extends DwalinWebDriverTest {

  private static final String PAGE_URL = "https://searxng.ch/";

  private static final String EXPECTED_TITLE = "SearXNG";

  private static final String WRONG_EXPECTED_TITLE = "Wrong title";

  private static class SearXNGHomePage implements NavigablePage {

    private static final By title = By.className("title");

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> PAGE_URL;
    }

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title(EXPECTED_TITLE));
    }

    public ElementGuard pageTitle() {
      return guard($(title).shouldBe(visible));
    }
  }

  private static final class WrongUrlPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> "http://bad-url";
    }
  }

  private static final class WrongAtVerificationPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title(WRONG_EXPECTED_TITLE));
    }
  }

  private static final class NullUrlSupplierPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return null;
    }
  }

  private static final class NullUrlPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> null;
    }
  }

  private static final class NullAtVerificationPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return null;
    }
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenTheNavigateToStaticMethodIsCalledWithAClassParameter() {
    assertThatCode(() -> Dwalin.navigateTo(SearXNGHomePage.class))
        .describedAs("Navigation did not proceed as expected; no exceptions should have been thrown.")
        .doesNotThrowAnyException();

    assertSoftly(softly -> {
      softly.assertThat(getWebDriver().getCurrentUrl())
          .describedAs("The current URL does not match the expected page URL.")
          .isEqualTo(PAGE_URL);

      softly.assertThat(getWebDriver().getTitle())
          .describedAs("The current page title does not match the one expected by the page.")
          .isEqualTo(EXPECTED_TITLE);
    });
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenTheNavigateToStaticMethodIsCalledWithAClassParameterAndThePageUrlIsWrong() {
    assertThatCode(() -> Dwalin.navigateTo(WrongUrlPage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("unknown error: net::ERR_NAME_NOT_RESOLVED")
        .doesNotThrowAnyExceptionExcept(WebDriverException.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenTheNavigateToStaticMethodIsCalledWithAClassParameterAndTheAtVerificationFails() {
    assertThatCode(() -> Dwalin.navigateTo(WrongAtVerificationPage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("Page should have title " + WRONG_EXPECTED_TITLE)
        .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenTheNavigateToStaticMethodIsCalledWithAReifiedGeneric() {
    assertThatCode(() -> Dwalin.<SearXNGHomePage>navigateTo())
        .describedAs("Navigation did not proceed as expected; no exceptions should have been thrown.")
        .doesNotThrowAnyException();

    assertSoftly(softly -> {
      softly.assertThat(getWebDriver().getCurrentUrl())
          .describedAs("The current URL does not match the expected page URL.")
          .isEqualTo(PAGE_URL);

      softly.assertThat(getWebDriver().getTitle())
          .describedAs("The current page title does not match the one expected by the page.")
          .isEqualTo(EXPECTED_TITLE);
    });
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenTheNavigateToStaticMethodIsCalledWithAReifiedGenericAndThePageUrlIsWrong() {
    assertThatCode(() -> Dwalin.<WrongUrlPage>navigateTo())
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("unknown error: net::ERR_NAME_NOT_RESOLVED")
        .doesNotThrowAnyExceptionExcept(WebDriverException.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenTheNavigateToStaticMethodIsCalledWithAReifiedGenericAndTheAtVerificationFails() {
    assertThatCode(() -> Dwalin.<WrongAtVerificationPage>navigateTo())
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("Page should have title " + WRONG_EXPECTED_TITLE)
        .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenAttemptingToNavigateToAPageWithANullUrlSupplier() {
    assertThatCode(() -> Dwalin.navigateTo(NullUrlSupplierPage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessage(Dwalin.URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenAttemptingToNavigateToAPageWithANullUrl() {
    assertThatCode(() -> Dwalin.navigateTo(NullUrlPage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessage(Dwalin.URL_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenAttemptingToNavigateToAPageWithANullAtVerificationSupplier() {
    assertThatCode(() -> Dwalin.<NullAtVerificationPage>navigateTo())
        .describedAs("Navigation should have thrown an error.")
        .hasMessage(Dwalin.AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("happy-path")
  void shouldWorkWhenAttachingAScreenshotToTheAllureReportWithTheDefaultDescription() {
    assertThatCode(() -> attachScreenshotToAllureReport(Dwalin.<SearXNGHomePage>navigateTo().pageTitle()))
        .describedAs("Navigation did not proceed as expected; no exceptions should have been thrown.")
        .doesNotThrowAnyException();

    assertScreenshotAttachment("ElementGuard[element=<div class=\"title\"></div>]");
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("happy-path")
  void shouldWorkWhenAttachingAScreenshotToTheAllureReportWithTheSpecifiedDescription() {
    assertThatCode(() -> attachScreenshotToAllureReport(Dwalin.<SearXNGHomePage>navigateTo().pageTitle(), "Title"))
        .describedAs("Navigation did not proceed as expected; no exceptions should have been thrown.")
        .doesNotThrowAnyException();

    assertScreenshotAttachment("Title");
  }

  @RetryingTest(maxAttempts = 3, name = "[Attempt {index}]")
  @Tag("sad-path")
  void shouldFailWhenAttemptingToAttachAScreenshotToTheAllureReportWithNullSubjectOrDescription() {
    final SearXNGHomePage page = Dwalin.navigateTo();

    assertSoftly(softly -> {
      softly.assertThatCode(() -> attachScreenshotToAllureReport(null))
          .describedAs("Navigation did not proceed as expected; no exceptions should have been thrown.")
          .hasMessage(Dwalin.SUBJECT_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> attachScreenshotToAllureReport(page.pageTitle(), null))
          .describedAs("Navigation did not proceed as expected; no exceptions should have been thrown.")
          .hasMessage(Dwalin.DESCRIPTION_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }

  private static void assertScreenshotAttachment(final String description) {
    getLifecycle().updateTestCase(testResult -> {
      final Attachment screenshot = testResult.getAttachments()
          .stream()
          .findFirst()
          .orElseThrow(() -> new AssertionError("No attachment was found during the operation."));

      assertSoftly(softly -> {
        softly.assertThat(screenshot.getName())
            .describedAs("The attachment's name did not match the expected value.")
            .isNotBlank()
            .isEqualTo(description);

        softly.assertThat(screenshot.getType())
            .describedAs("The attachment was not a PNG image as expected.")
            .isNotBlank()
            .isEqualTo("image/png");

        softly.assertThat(screenshot.getSource())
            .describedAs("The attachment's source must have the correct file extension.")
            .isNotBlank()
            .endsWith(".png");
      });
    });
  }
}
