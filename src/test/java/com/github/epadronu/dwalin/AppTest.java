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
import com.codeborne.selenide.SelenideElement;
import com.github.epadronu.dwalin.core.Browser;
import com.github.epadronu.dwalin.core.ElementGuard;
import com.github.epadronu.dwalin.core.GuardedComponent;
import com.github.epadronu.dwalin.core.NavigablePage;
import com.github.epadronu.dwalin.core.Page;
import com.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.github.epadronu.dwalin.core.ElementGuard.guard;
/* ************************************************************************************************/

/**
 * <p>
 * Temporal <abbr title="Proof of concept">PoC</abbr> of a test class.
 * </p>
 *
 * <p>
 * It will be removed in the future in favor of a test suit for the library as well as a couple of examples.
 * </p>
 */
public final class AppTest extends DwalinWebDriverTest {

  private static class GoogleHomePage implements NavigablePage {

    private final By searchInput = By.name("q");

    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> "https://google.com";
    }

    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title("Google"));
    }

    public SearchBox<GoogleHomePage> searchBox() {
      return asComponent($(searchInput), SearchBox::new);
    }
  }

  private static class GoogleResultPage implements Page {

    private final By resultEntries = By.xpath("//*[@id='rso']/div");

    public List<ResultEntry<GoogleResultPage>> getResultEntries() {
      return asComponents($$(resultEntries).should(sizeGreaterThanOrEqual(1)), ResultEntry::new);
    }
  }

  private static class SearchBox<P extends Page> extends GuardedComponent<P> {

    SearchBox(final P page, final SelenideElement element) {
      super(page, element);
    }

    public GoogleResultPage search(final String criteria) {
      sendKeys(criteria, Keys.ENTER);

      return page(GoogleResultPage.class);
    }
  }

  private static class ResultEntry<P extends Page> extends GuardedComponent<P> {

    private final By title = By.tagName("h3");

    ResultEntry(final P page, final SelenideElement element) {
      super(page, element);
    }

    public ElementGuard title() {
      return guard($(title).shouldBe(visible));
    }
  }

  @Test
  void aBasicSearchShouldSucceed() {
    Browser.navigate(browser -> {
      final GoogleHomePage page = browser.open(GoogleHomePage.class);

      page.searchBox()
          .search("gitlab epadronu")
          .getResultEntries()
          .getFirst()
          .title()
          .shouldHave(exactText("Edinson E. Padrón Urdaneta"));
    });
  }

  @Test
  void aBasicSearchThatFailsOnPurpose() {
    Browser.navigate(browser -> {
      final GoogleHomePage page = browser.open();

      page.searchBox()
          .search("gitlab epadronu")
          .getResultEntries()
          .getFirst()
          .title()
          .shouldHave(exactText("Edinson Padrón Urdaneta"));
    });
  }
}
