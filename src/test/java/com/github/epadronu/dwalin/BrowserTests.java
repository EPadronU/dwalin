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
package com.github.epadronu.dwalin;
/* ************************************************************************************************/

/* ************************************************************************************************/
import com.codeborne.selenide.ex.UIAssertionError;
import com.github.epadronu.dwalin.core.Browser;
import com.github.epadronu.dwalin.core.NavigablePage;
import com.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

/**
 * Tests validating the functionality of the AbstractionLayer, Browser, and NavigablePage interfaces.
 */
@Tag("core")
@DisplayName("Browser's tests")
public final class BrowserTests extends DwalinWebDriverTest {

  private static final String PAGE_URL = "https://searxng.ch/";

  private static final String EXPECTED_TITLE = "SearXNG";

  private static final String WRONG_EXPECTED_TITLE = "Wrong title";

  private static class SearXNGHomePage implements NavigablePage {

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
  }

  private static final class WrongUrlPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> "http://bad-url";
    }
  }

  private static final class WrongTitlePage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title(WRONG_EXPECTED_TITLE));
    }
  }

  private static final class NullUrlPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return null;
    }
  }

  private static final class NullAtVerificationPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return null;
    }
  }

  @Test
  @Tag("happy-path")
  void shouldInjectABrowserInstanceSuccessfullyWhenTheNavigateStaticMethodIsCalled() {
    Browser.navigate(browser -> {
      assertThat(browser)
          .describedAs("Expected a non-null browser instance in the navigation context, but it was null.")
          .isNotNull();
    });
  }

  @Test
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAClassParameter() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(SearXNGHomePage.class))
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
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAClassParameterAndTheUrlIsWrong() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(WrongUrlPage.class))
          .describedAs("Navigation should have thrown an error.")
          .hasMessageContaining("unknown error: net::ERR_NAME_NOT_RESOLVED")
          .doesNotThrowAnyExceptionExcept(WebDriverException.class);
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAClassParameterAndTheAtVerificationFails() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(WrongTitlePage.class))
          .describedAs("Navigation should have thrown an error.")
          .hasMessageContaining("Page should have title " + WRONG_EXPECTED_TITLE)
          .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
    });
  }

  @Test
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAReifiedGeneric() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<SearXNGHomePage>open())
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
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAReifiedGenericAndTheUrlIsWrong() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<WrongUrlPage>open())
          .describedAs("Navigation should have thrown an error.")
          .hasMessageContaining("unknown error: net::ERR_NAME_NOT_RESOLVED")
          .doesNotThrowAnyExceptionExcept(WebDriverException.class);
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAReifiedGenericAndTheAtVerificationFails() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<WrongTitlePage>open())
          .describedAs("Navigation should have thrown an error.")
          .hasMessageContaining("Page should have title " + WRONG_EXPECTED_TITLE)
          .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenAttemptingToOpenAPageWithANullUrlSupplier() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(NullUrlPage.class))
          .describedAs("Navigation should have thrown an error.")
          .hasMessage(Browser.URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenAttemptingToOpenAPageWithANullAtVerificationSupplier() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<NullAtVerificationPage>open())
          .describedAs("Navigation should have thrown an error.")
          .hasMessage(Browser.AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }
}
