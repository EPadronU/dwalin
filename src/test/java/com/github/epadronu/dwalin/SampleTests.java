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
import com.github.epadronu.dwalin.core.Dwalin;
import com.github.epadronu.dwalin.core.ElementGuard;
import com.github.epadronu.dwalin.core.GuardedComponent;
import com.github.epadronu.dwalin.core.NavigablePage;
import com.github.epadronu.dwalin.core.Page;
import com.github.epadronu.dwalin.qa.DwalinWebDriverTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.github.epadronu.dwalin.core.ElementGuard.guard;
import static io.qameta.allure.Allure.getLifecycle;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.openqa.selenium.OutputType.BYTES;
/* ************************************************************************************************/

/**
 * <p>
 * This class demonstrates how to implement and navigate through pages using the Dwalin library.
 * </p>
 */
@DisplayName("Sample tests")
public final class SampleTests extends DwalinWebDriverTest {

  private static final Logger log = LogManager.getLogger(SampleTests.class);

  /**
   * Represents the DuckDuckGo home page.
   * <p>
   * This page can be navigated to and contains a search form component.
   */
  private static final class DuckDuckGoHomePage implements NavigablePage {

    private static final By searchForm = By.cssSelector("[aria-label='Searchbox']");

    /**
     * Provides the URL of the DuckDuckGo home page.
     *
     * @return a {@code Supplier<String>} that supplies the DuckDuckGo URL
     */
    @Nonnull
    @Override
    public Supplier<String> urlSupplier() {
      return () -> "https://duckduckgo.com/";
    }

    /**
     * Verifies that the DuckDuckGo home page has been reached as expected.
     *
     * @return a {@code Runnable} that checks the page title
     */
    @Nonnull
    @Override
    public Runnable atVerificationSupplier() {
      return () -> webdriver().shouldHave(title("DuckDuckGo — Privacy, simplified."));
    }

    /**
     * Provides access to the search box component on the DuckDuckGo home page.
     *
     * @return a {@code SearchBox<DuckDuckGoHomePage>} representing the search box
     */
    public SearchBox<DuckDuckGoHomePage> searchBox() {
      return asComponent($(searchForm).shouldBe(visible), SearchBox::new);
    }
  }

  /**
   * Represents the search box component on the DuckDuckGo home page.
   *
   * @param <P> the type of the parent page
   */
  private static final class SearchBox<P extends Page> extends GuardedComponent<P> {

    private static final By searchInput = By.name("q");

    /**
     * Constructs a {@code SearchBox} component.
     *
     * @param page        the parent page containing this component
     * @param rootElement the root element of the search box
     */
    public SearchBox(final P page, final SelenideElement rootElement) {
      super(page, rootElement);
    }

    /**
     * Performs a search using the specified query and returns the search result page.
     *
     * @param query the search query to be entered
     * @return the {@code DuckDuckGoSearchResultPage} displaying the search results
     */
    public DuckDuckGoSearchResultPage search(final String query) {
      $(searchInput).shouldBe(enabled).sendKeys(query, Keys.ENTER);

      return page(DuckDuckGoSearchResultPage.class);
    }
  }

  /**
   * Represents the DuckDuckGo search results page.
   * <p>
   * This page contains a list of search results
   */
  private static final class DuckDuckGoSearchResultPage implements Page {

    private static final By resultItem = By.cssSelector("li[data-layout='organic']");

    /**
     * Retrieves the search results displayed on the page.
     *
     * @return a list of {@code SearchResult<DuckDuckGoSearchResultPage>} representing the search results
     */
    public List<SearchResult<DuckDuckGoSearchResultPage>> results() {
      return asComponents($$(resultItem).should(sizeGreaterThanOrEqual(1), ofSeconds(20L)), SearchResult::new);
    }
  }

  /**
   * Represents an individual search result on the DuckDuckGo search results page.
   *
   * @param <P> the type of the parent page
   */
  private static final class SearchResult<P extends Page> extends GuardedComponent<P> {

    private static final By h2 = By.tagName("h2");

    private static final By siteLinks = By.cssSelector("[id^='sl']");

    /**
     * Constructs a {@code SearchResult} component.
     *
     * @param page        the parent page containing this component
     * @param rootElement the root element of the search result
     */
    public SearchResult(final P page, final SelenideElement rootElement) {
      super(page, rootElement);
    }

    /**
     * Retrieves the title of the search result.
     *
     * @return a {@code ElementGuard} representing the title element
     */
    public ElementGuard title() {
      return guard($(h2)).shouldBe(visible);
    }

    /**
     * Retrieves the url of the search result.
     *
     * @return a {@code String} representing the URL
     */
    public String url() {
      return $(h2).$("a").attr("href");
    }

    /**
     * Retrieves the site links associated with the search result.
     *
     * @return a list of {@code SiteLink<P>} representing the site links
     */
    public List<SiteLink<P>> siteLinks() {
      return asComponents($$(siteLinks).shouldHave(sizeGreaterThanOrEqual(6)), SiteLink::new);
    }
  }

  /**
   * Represents a site link associated with a search result.
   *
   * @param <P> the type of the parent page
   */
  private static final class SiteLink<P extends Page> extends GuardedComponent<P> {

    private static final By title = By.tagName("h3");

    private static final By description = By.tagName("p");

    /**
     * Constructs a {@code SiteLink} component.
     *
     * @param page        the parent page containing this component
     * @param rootElement the root element of the site link
     */
    public SiteLink(P page, SelenideElement rootElement) {
      super(page, rootElement);
    }

    /**
     * Retrieves the title of the site link.
     *
     * @return a {@code ElementGuard} representing the title element
     */
    public ElementGuard title() {
      return guard($(title)).shouldBe(visible);
    }

    /**
     * Retrieves the description of the site link.
     *
     * @return a {@code ElementGuard} representing the title element
     */
    public ElementGuard description() {
      return guard($(description)).shouldBe(visible);
    }
  }

  @Test
  void shouldFirstResultBeOraclesAndHaveSiteLinksWhenSearchingForJava() {
    final SearchResult<DuckDuckGoSearchResultPage> firstResult = Dwalin.navigateTo(DuckDuckGoHomePage.class)
        .searchBox()
        .search("java")
        .results()
        .getFirst();

    // Because we can but it may be useful
    attachScreenshotToReport(firstResult);

    assertSoftly(softly -> {
      softly.assertThat(firstResult.title().text())
          .describedAs("The text of the first result did not match the expected value.")
          .isEqualTo("Java | Oracle");

      softly.assertThat(firstResult.siteLinks())
          .describedAs("No enough site links were found for the first result.")
          .hasSizeGreaterThan(4);

      final SiteLink<DuckDuckGoSearchResultPage> siteLink = firstResult.siteLinks().get(3);

      softly.assertThat(siteLink.title().text())
          .describedAs("The text of the 4th site link in the first result did not match the expected value.")
          .isEqualTo("What is Java");

      softly.assertThat(siteLink.description().text())
          .describedAs("The text of the 4th site link in the first result did not match the expected value.")
          .startsWith("What is Java technology and why do I need it?");
    });
  }

  @Test
  void shouldFirstResultBeTheOfficialOneAndHaveSiteLinksWhenSearchingForSelenium() {
    final SearchResult<DuckDuckGoSearchResultPage> firstResult = Dwalin.navigateTo(DuckDuckGoHomePage.class)
        .searchBox()
        .search("selenium")
        .results()
        .getFirst();

    // Because we can but it may be useful
    attachScreenshotToReport(firstResult);

    assertSoftly(softly -> {
      softly.assertThat(firstResult.title().text())
          .describedAs("The text of the first result did not match the expected value.")
          .isEqualTo("Selenium");

      softly.assertThat(firstResult.url())
          .describedAs("The URL of the first result did not match the expected value.")
          .isEqualTo("https://www.selenium.dev/");

      softly.assertThat(firstResult.siteLinks())
          .describedAs("No enough site links were found for the first result.")
          .hasSizeGreaterThan(3);

      final SiteLink<DuckDuckGoSearchResultPage> siteLink = firstResult.siteLinks().get(2);

      softly.assertThat(siteLink.title().text())
          .describedAs("The text of the 3rd site link in the first result did not match the expected value.")
          .isEqualTo("About");

      softly.assertThat(siteLink.description().text())
          .describedAs("The text of the 3rd site link in the first result did not match the expected value.")
          .startsWith("About Selenium Selenium is a suite of tools for automating web browsers.");
    });
  }

  private static void attachScreenshotToReport(final TakesScreenshot subject) {
    getLifecycle().getCurrentTestCaseOrStep().ifPresent((_) -> {
      try {
        getLifecycle().addAttachment(
            subject.toString(),
            "image/png",
            "png",
            subject.getScreenshotAs(BYTES));
      } catch (final WebDriverException exception) {
        log.error("An screenshot couldn't be taken.", exception);
      }
    });
  }
}