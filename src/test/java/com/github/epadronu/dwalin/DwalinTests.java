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
import com.github.epadronu.dwalin.core.Dwalin;
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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

/**
 * Tests validating the functionality of the Dwalin utility class, as well as the AbstractionLayer
 * and NavigablePage interfaces.
 */
@Tag("core")
@DisplayName("Dwalin's tests")
public final class DwalinTests extends DwalinWebDriverTest {

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
  void shouldWorkAsExpectedWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAClassParameter() {
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

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAClassParameterAndTheUrlIsWrong() {
    assertThatCode(() -> Dwalin.navigateTo(WrongUrlPage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("unknown error: net::ERR_NAME_NOT_RESOLVED")
        .doesNotThrowAnyExceptionExcept(WebDriverException.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAClassParameterAndTheAtVerificationFails() {
    assertThatCode(() -> Dwalin.navigateTo(WrongTitlePage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("Page should have title " + WRONG_EXPECTED_TITLE)
        .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
  }

  @Test
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAReifiedGeneric() {
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

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAReifiedGenericAndTheUrlIsWrong() {
    assertThatCode(() -> Dwalin.<WrongUrlPage>navigateTo())
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("unknown error: net::ERR_NAME_NOT_RESOLVED")
        .doesNotThrowAnyExceptionExcept(WebDriverException.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenTheOpenMethodInheritedFromTheAbstractionLayerInterfaceIsCalledWithAReifiedGenericAndTheAtVerificationFails() {
    assertThatCode(() -> Dwalin.<WrongTitlePage>navigateTo())
        .describedAs("Navigation should have thrown an error.")
        .hasMessageContaining("Page should have title " + WRONG_EXPECTED_TITLE)
        .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenAttemptingToOpenAPageWithANullUrlSupplier() {
    assertThatCode(() -> Dwalin.navigateTo(NullUrlPage.class))
        .describedAs("Navigation should have thrown an error.")
        .hasMessage(Dwalin.URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenAttemptingToOpenAPageWithANullAtVerificationSupplier() {
    assertThatCode(() -> Dwalin.<NullAtVerificationPage>navigateTo())
        .describedAs("Navigation should have thrown an error.")
        .hasMessage(Dwalin.AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }
}
