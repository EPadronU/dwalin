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
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

@Tag("core")
@DisplayName("Browser's tests")
@Feature("Tests covering the Browser interface's functionality")
public final class BrowserTests extends DwalinWebDriverTest {

  private static final String pageUrl = "https://searxng.ch/";

  private static final String expectedTitle = "SearXNG";

  private static final String wrongExpectedTitle = "Wrong title";

  static class SearXNGHomePage implements NavigablePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> pageUrl;
    }

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title(expectedTitle));
    }
  }

  static final class WrongSearXNGHomePage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title(wrongExpectedTitle));
    }
  }

  static final class NullUrlPage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return null;
    }
  }

  static final class NullTitlePage extends SearXNGHomePage {

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return null;
    }
  }

  @Test
  @Tag("happy-path")
  void theNavigateStaticMethodShouldSucessfullyInjectABrowserInstance() {
    Browser.navigate(browser -> {
      assertThat(browser)
          .describedAs("The browser injected into the navigation context must not be null")
          .isNotNull();
    });
  }

  @Test
  @Tag("happy-path")
  void theOpenMethodWithAClassParameterInheritedFromTheAbstractionLayerInterfaceMustWorkAsExpected() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(SearXNGHomePage.class))
          .describedAs("Navigation should be performed as expected with no exceptions being thrown")
          .doesNotThrowAnyException();

      assertSoftly(softly -> {
        softly.assertThat(getWebDriver().getCurrentUrl())
            .describedAs("The current URL must match the page's")
            .isEqualTo(pageUrl);

        softly.assertThat(getWebDriver().getTitle())
            .describedAs("The current title must match the one expected by the page")
            .isEqualTo(expectedTitle);
      });
    });
  }

  @Test
  @Tag("sad-path")
  void theOpenMethodWithAClassParameterInheritedFromTheAbstractionLayerInterfaceMustFailWhenTheAtConditionFails() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(WrongSearXNGHomePage.class))
          .describedAs("Navigation should threw an error")
          .hasMessageContaining("Page should have title " + wrongExpectedTitle)
          .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
    });
  }

  @Test
  @Tag("happy-path")
  void theOpenMethodWithAReifiedParameterInheritedFromTheAbstractionLayerInterfaceMustWorkAsExpected() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<SearXNGHomePage>open())
          .describedAs("Navigation should be performed as expected with no exceptions being thrown")
          .doesNotThrowAnyException();

      assertSoftly(softly -> {
        softly.assertThat(getWebDriver().getCurrentUrl())
            .describedAs("The current URL must match the page's")
            .isEqualTo(pageUrl);

        softly.assertThat(getWebDriver().getTitle())
            .describedAs("The current title must match the one expected by the page")
            .isEqualTo(expectedTitle);
      });
    });
  }

  @Test
  @Tag("sad-path")
  void theOpenMethodWithAReifiedParameterInheritedFromTheAbstractionLayerInterfaceMustFailWhenTheAtConditionFails() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<WrongSearXNGHomePage>open())
          .describedAs("Navigation should threw an error")
          .hasMessageContaining("Page should have title " + wrongExpectedTitle)
          .doesNotThrowAnyExceptionExcept(UIAssertionError.class);
    });
  }

  @Test
  @Tag("sad-path")
  void openingAPageWithANullUrlSupplierMustFail() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.open(NullUrlPage.class))
          .describedAs("Navigation should threw an error")
          .hasMessage(Browser.urlSupplierNotNullMessage)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }

  @Test
  @Tag("sad-path")
  void openingAPageWithANullAtVerificationMustFail() {
    Browser.navigate(browser -> {
      assertThatCode(() -> browser.<NullTitlePage>open())
          .describedAs("Navigation should threw an error")
          .hasMessage(Browser.atVerificationNotNullMessage)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }
}
