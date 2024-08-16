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
import com.codeborne.selenide.SelenideElement;
import io.github.epadronu.dwalin.core.AbstractionLayer;
import io.github.epadronu.dwalin.core.Component;
import io.github.epadronu.dwalin.core.ElementGuard;
import io.github.epadronu.dwalin.core.GuardedComponent;
import io.github.epadronu.dwalin.core.Page;
import io.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static io.github.epadronu.dwalin.core.AbstractionLayer.COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE;
import static io.github.epadronu.dwalin.core.AbstractionLayer.ELEMENT_CANNOT_BE_NULL_MESSAGE;
import static io.github.epadronu.dwalin.core.AbstractionLayer.ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE;
import static io.github.epadronu.dwalin.core.Component.PARENT_CANNOT_BE_NULL_MESSAGE;
import static io.github.epadronu.dwalin.core.Component.ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE;
import static io.github.epadronu.dwalin.core.ElementGuard.guard;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
/* ************************************************************************************************/

/**
 * Tests validating the functionality of the AbstractionLayer and Page interfaces, as well as the
 * ElementGuard, Component and GuardedComponent classes.
 *
 * @see AbstractionLayer
 * @see Page
 * @see ElementGuard
 * @see Component
 * @see GuardedComponent
 */
@Tag("core")
@DisplayName("GuardedComponent's tests")
public final class GuardedComponentTests extends DwalinWebDriverTest {

  private static final String PAGE_URL = "https://duckduckgo.com/";

  private static final class DuckDuckGoHomePage implements Page {

    private static final By searchForm = By.cssSelector("[aria-label='Searchbox']");

    public SearchBox<DuckDuckGoHomePage> searchBox() {
      return asComponent($(searchForm).shouldBe(visible), SearchBox::new);
    }
  }

  private static final class SearchBox<P extends Page> extends GuardedComponent<P> {

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

    private static final By resultItem = By.cssSelector("li[data-layout='organic']");

    public List<SearchResult<DuckDuckGoSearchResultPage>> results() {
      return asComponents($$(resultItem).should(sizeGreaterThanOrEqual(1), ofSeconds(20L)), SearchResult::new);
    }
  }

  private static final class SearchResult<P extends Page> extends GuardedComponent<P> {

    private static final By h2 = By.tagName("h2");

    private static final By siteLinks = By.cssSelector("[id^='sl']");

    public SearchResult(final P page, final SelenideElement rootElement) {
      super(page, rootElement);
    }

    public ElementGuard title() {
      return guard($(h2)).shouldBe(visible);
    }

    public String url() {
      return $(h2).$("a").attr("href");
    }

    public List<SiteLink<SearchResult<P>>> siteLinks() {
      return asNestedComponents($$(siteLinks).shouldHave(sizeGreaterThanOrEqual(6)), SiteLink::new);
    }
  }

  private static final class SiteLink<P extends SearchResult<?>> extends GuardedComponent<P> {

    private static final By title = By.tagName("h3");

    private static final By description = By.tagName("p");

    public SiteLink(final P parent, final SelenideElement rootElement) {
      super(parent, rootElement);
    }

    public ElementGuard title() {
      return guard($(title)).shouldBe(visible);
    }

    public ElementGuard description() {
      return guard($(description)).shouldBe(visible);
    }
  }

  private static final class NullsForAsComponentMethodsPage implements Page {

    public SearchBox<NullsForAsComponentMethodsPage> asComponentWithNullElement() {
      return asComponent(null, SearchBox::new);
    }

    public SearchBox<NullsForAsComponentMethodsPage> asComponentWithNullFactory() {
      return asComponent($("html"), null);
    }

    public List<SearchResult<NullsForAsComponentMethodsPage>> asComponentsWithNullElementCollection() {
      return asComponents(null, SearchResult::new);
    }

    public List<SearchResult<NullsForAsComponentMethodsPage>> asComponentsWithNullFactory() {
      return asComponents($$("html"), null);
    }
  }

  private static final class NullsForAsNestedComponentsMethodsComponent<P extends Page> extends GuardedComponent<P> {

    public NullsForAsNestedComponentsMethodsComponent(final P parent, final SelenideElement rootElement) {
      super(parent, rootElement);
    }

    public SiteLink<SearchResult<P>> asNestedComponentWithNullElement() {
      return asNestedComponent(null, SiteLink::new);
    }

    public SiteLink<SearchResult<P>> asNestedComponentWithNullFactory() {
      return asNestedComponent($("html"), null);
    }

    public List<SiteLink<SearchResult<P>>> asNestedComponentsWithNullElementCollection() {
      return asNestedComponents(null, SiteLink::new);
    }

    public List<SiteLink<SearchResult<P>>> asNestedComponentsWithNullFactory() {
      return asNestedComponents($$("html"), null);
    }
  }

  @Test
  @Tag("happy-path")
  void shouldWorkAsExpectedWhenInteractingWithAPageThroughTheUseOfGuardedComponents() {
    assertThatCode(() -> {
      final var firstResult = open(PAGE_URL, DuckDuckGoHomePage.class)
          .searchBox()
          .search("java")
          .results()
          .getFirst();

      firstResult.title().shouldHave(text("Java | Oracle"));

      final var firstSiteLink = firstResult.siteLinks().getFirst();

      firstSiteLink.title().shouldHave(text("Download"));

      firstSiteLink.description().shouldHave(partialText("Important Oracle Java License"));
    })
        .describedAs("Component interaction did not behave as expected.")
        .doesNotThrowAnyException();
  }

  @Test
  @Tag("happy-path")
  void shouldTheParentsForGuardedComponentsAndNesterGuardedComponentsBeSetAsExpected() {
    final var resultPage = open(PAGE_URL, DuckDuckGoHomePage.class)
        .searchBox()
        .search("java");

    final var firstResult = resultPage.results().getFirst();

    assertSoftly(softly -> {
      softly.assertThat(firstResult.ascend())
          .describedAs("The parent for the component was not set as expected.")
          .isNotNull()
          .isEqualTo(resultPage);

      softly.assertThat(firstResult.siteLinks().getFirst().ascend())
          .describedAs("The parent for the nested component was not set as expected.")
          .isNotNull()
          .isEqualTo(firstResult);
    });
  }

  @Test
  @Tag("happy-path")
  void shouldTheToStringMethodForGuardedComponentsWorkAsExpected() {
    final var resultPage = open(PAGE_URL, DuckDuckGoHomePage.class)
        .searchBox()
        .search("java");

    final var firstResult = resultPage.results().getFirst();

    assertSoftly(softly -> {
      softly.assertThat(firstResult.toString())
          .describedAs("The `toString` method for the component didn't produce the expected string.")
          .isNotBlank()
          .startsWith("SearchResult[parent=DuckDuckGoSearchResultPage, rootElement=<li");

      softly.assertThat(firstResult.siteLinks().getFirst().toString())
          .describedAs("The `toString` method for the nested component didn't produce the expected string.")
          .isNotNull()
          .startsWith("SiteLink[parent=SearchResult, rootElement=<li");
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailToConstructAGuardedComponentWithANullPage() {
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
  void shouldFailToConstructAGuardedComponentWithANullRootElement() {
    assertThatCode(() -> {
      new SearchBox<>(open(PAGE_URL, DuckDuckGoHomePage.class), null);
    })
        .describedAs("Creating a component with a null root element did not throw the expected exception.")
        .hasMessage(ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE)
        .doesNotThrowAnyExceptionExcept(NullPointerException.class);
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenUsingNullsInTheAsComponentAndAsComponentsMethodsForGuardedComponents() {
    final NullsForAsComponentMethodsPage page = page();

    assertSoftly(softly -> {
      softly.assertThatCode(() -> page.asComponentWithNullElement())
          .describedAs("Using a null element for `asComponent` did not throw the expected exception")
          .hasMessage(ELEMENT_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> page.asComponentWithNullFactory())
          .describedAs("Using a null component factory for `asComponent` did not throw the expected exception")
          .hasMessage(COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> page.asComponentsWithNullElementCollection())
          .describedAs("Using a null element collection for `asComponents` did not throw the expected exception")
          .hasMessage(ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> page.asComponentsWithNullFactory())
          .describedAs("Using a null component factory for `asComponents` did not throw the expected exception")
          .hasMessage(COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }

  @Test
  @Tag("sad-path")
  void shouldFailWhenUsingNullsInTheAsNestedComponentAndAsNestedComponentsMethodsForGuardedComponents() {
    final NullsForAsComponentMethodsPage page = page();

    final var component = new NullsForAsNestedComponentsMethodsComponent<>(page, $("html"));

    assertSoftly(softly -> {
      softly.assertThatCode(() -> component.asNestedComponentWithNullElement())
          .describedAs("Using a null element for `asNestedComponent` did not throw the expected exception")
          .hasMessage(ELEMENT_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> component.asNestedComponentWithNullFactory())
          .describedAs("Using a null component factory for `asNestedComponent` did not throw the expected exception")
          .hasMessage(COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> component.asNestedComponentsWithNullElementCollection())
          .describedAs("Using a null element collection for `asNestedComponents` did not throw the expected exception")
          .hasMessage(ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);

      softly.assertThatCode(() -> component.asNestedComponentsWithNullFactory())
          .describedAs("Using a null component factory for `asNestedComponents` did not throw the expected exception")
          .hasMessage(COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE)
          .doesNotThrowAnyExceptionExcept(NullPointerException.class);
    });
  }
}
