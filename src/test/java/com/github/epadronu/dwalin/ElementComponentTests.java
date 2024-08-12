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
import com.github.epadronu.dwalin.core.ElementComponent;
import com.github.epadronu.dwalin.core.Page;
import com.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static com.github.epadronu.dwalin.core.AbstractionLayer.COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE;
import static com.github.epadronu.dwalin.core.AbstractionLayer.ELEMENT_CANNOT_BE_NULL_MESSAGE;
import static com.github.epadronu.dwalin.core.AbstractionLayer.ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE;
import static com.github.epadronu.dwalin.core.Component.PARENT_CANNOT_BE_NULL_MESSAGE;
import static com.github.epadronu.dwalin.core.Component.ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

/**
 * Tests validating the functionality of the Page interface as well as the ElementComponent abstract class.
 */
@Tag("core")
@DisplayName("ElementComponent's tests")
public final class ElementComponentTests extends DwalinWebDriverTest {

  private static final String PAGE_URL = "https://duckduckgo.com/";

  private static final class DuckDuckGoHomePage implements Page {

    private static final By searchForm = By.cssSelector("[aria-label='Searchbox']");

    public SearchBox<DuckDuckGoHomePage> searchBox() {
      return asComponent($(searchForm).shouldBe(visible), SearchBox::new);
    }
  }

  private static final class SearchBox<P extends Page> extends ElementComponent<P> {

    private static final By searchInput = By.name("q");

    public SearchBox(final P page, final SelenideElement rootElement) {
      super(page, rootElement);
    }

    public DuckDuckGoSearchResultPage search(final String query) {
      $(searchInput).shouldBe(enabled).sendKeys(query, Keys.ENTER);

      return page(DuckDuckGoSearchResultPage.class);
    }
  }

  private static final class DuckDuckGoSearchResultPage implements Page {

    private static final By searchForm = By.name("x");

    private static final By resultItem = By.cssSelector("li[data-layout='organic']");

    public List<SearchResult<DuckDuckGoSearchResultPage>> results() {
      return asComponents($$(resultItem).should(sizeGreaterThanOrEqual(1), ofSeconds(10L)), SearchResult::new);
    }

    public SearchBox<DuckDuckGoHomePage> searchBox() {
      return asComponent($(searchForm).shouldBe(visible), SearchBox::new);
    }
  }

  private static final class SearchResult<P extends Page> extends ElementComponent<P> {

    private static final By h2 = By.tagName("h2");

    public SearchResult(final P page, final SelenideElement rootElement) {
      super(page, rootElement);
    }

    public SelenideElement title() {
      return $(h2).shouldBe(visible);
    }
  }

  private static final class PageWithNullsForComponentTransformation implements Page {

    public SearchBox<PageWithNullsForComponentTransformation> componentWithNullElement() {
      return asComponent(null, SearchBox::new);
    }

    public SearchBox<PageWithNullsForComponentTransformation> componentWithNullFactory() {
      return asComponent($("html"), null);
    }

    public List<SearchResult<PageWithNullsForComponentTransformation>> componentsWithNullElementCollection() {
      return asComponents(null, SearchResult::new);
    }

    public List<SearchResult<PageWithNullsForComponentTransformation>> componentsWithNullFactory() {
      return asComponents($$("html"), null);
    }
  }

  @Test
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenInteractingWithAPageThroughTheUseOfElementComponents() {
    assertThatCode(() -> open(PAGE_URL, DuckDuckGoHomePage.class)
        .searchBox()
        .search("selenide")
        .results()
        .getFirst()
        .title()
        .shouldBe(text("Selenide: concise UI tests in Java")))
        .describedAs("Component interaction did not behave as expected.")
        .doesNotThrowAnyException();
  }

  @Test
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenUsingElementComponentsAsSelenideElements() {
    assertThatCode(() -> {
      open(PAGE_URL, DuckDuckGoHomePage.class)
          .searchBox()
          .$(SearchBox.searchInput)
          .shouldBe(enabled)
          .sendKeys("selenide", Keys.ENTER);

      page(DuckDuckGoSearchResultPage.class)
          .results()
          .getFirst()
          .$(SearchResult.h2)
          .shouldBe(visible)
          .shouldBe(text("Selenide: concise UI tests in Java"));
    })
        .describedAs("Components used as Selenide elements did not function as expected.")
        .doesNotThrowAnyException();
  }

  @Test
  @Tag("sad-path")
  void shouldFailToCreateAElementComponentWithANullPage() {
    assertThatCode(() -> {
      open(PAGE_URL);

      new SearchBox<>(null, $("html"));
    })
        .describedAs("Creating a component with a null page did not throw the expected exception.")
        .hasMessage(PARENT_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailToCreateAElementComponentWithANullRootElement() {
    assertThatCode(() -> {
      new SearchBox<>(open(PAGE_URL, DuckDuckGoHomePage.class), null);
    })
        .describedAs("Creating a component with a null root element did not throw the expected exception.")
        .hasMessage(ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenUsingNullInTheAsComponentAndAsElementComponentsMethodsForElementComponents() {
    final PageWithNullsForComponentTransformation page = page();

    assertSoftly(softly -> {
      softly.assertThatCode(() -> page.componentWithNullElement())
          .describedAs("Using a null element for `asComponent` did not throw the expected exception")
          .hasMessage(ELEMENT_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> page.componentWithNullFactory())
          .describedAs("Using a null component factory for `asComponent` did not throw the expected exception")
          .hasMessage(COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> page.componentsWithNullElementCollection())
          .describedAs("Using a null element collection for `asComponents` did not throw the expected exception")
          .hasMessage(ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> page.componentsWithNullFactory())
          .describedAs("Using a null component factory for `asComponents` did not throw the expected exception")
          .hasMessage(COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }
}
