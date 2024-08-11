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
package com.github.epadronu.dwalin.core;
/* ************************************************************************************************/

/* ************************************************************************************************/
import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Command;
import com.codeborne.selenide.DownloadOptions;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.HighlightOptions;
import com.codeborne.selenide.HoverOptions;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import com.codeborne.selenide.TypeOptions;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.ex.FileNotDownloadedError;
import com.codeborne.selenide.files.FileFilter;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
/* ************************************************************************************************/

/**
 * <p>
 * Element-like version of a component that mimics the {@code SelenideElement} behavior.
 * </p>
 *
 * @param <P> the type of page this component will be linked to
 * @see GuardedComponent
 * @see SelenideElement
 */
@ParametersAreNonnullByDefault
public non-sealed abstract class ElementComponent<P extends Page> extends Component<P> implements SelenideElement {

  /**
   * <p>
   * Creates a new component linked to the given page and using the provided element as the root search context.
   * </p>
   *
   * @param page        the page this component will be linked to
   * @param rootElement the element used as the root search in the context of this component
   */
  public ElementComponent(final P page, final SelenideElement rootElement) {
    super(page, rootElement);
  }

  /**
   * Locates the first element matching the given CSS selector within the root element.
   *
   * @param cssSelector CSS selector to locate the element
   * @return the first element that matches the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement $(final String cssSelector) {
    return rootElement().$(cssSelector);
  }

  /**
   * Locates the Nth element matching the given CSS selector within the root element.
   *
   * @param cssSelector CSS selector to locate the element
   * @param index       0...N index of the element to locate
   * @return the Nth element that matches the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement $(final String cssSelector, final int index) {
    return rootElement().$(cssSelector, index);
  }

  /**
   * Locates the first element matching the given {@link By} selector within the root element.
   *
   * @param selector {@link By} selector to locate the element
   * @return the first element that matches the given {@link By} selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement $(final By selector) {
    return rootElement().$(selector);
  }

  /**
   * Locates the Nth element matching the given {@link By} selector within the root element.
   *
   * @param selector {@link By} selector to locate the element
   * @param index    0...N index of the element to locate
   * @return the Nth element that matches the given {@link By} selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement $(final By selector, final int index) {
    return rootElement().$(selector, index);
  }

  /**
   * Locates all elements matching the given CSS selector within the root element.
   *
   * @param cssSelector CSS selector to locate the elements
   * @return an {@link ElementsCollection} of all elements that match the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public ElementsCollection $$(final String cssSelector) {
    return rootElement().$$(cssSelector);
  }

  /**
   * Locates all elements matching the given {@link By} selector within the root element.
   *
   * @param selector {@link By} selector to locate the elements
   * @return an {@link ElementsCollection} of all elements that match the given {@link By} selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public ElementsCollection $$(final By selector) {
    return rootElement().$$(selector);
  }

  /**
   * Locates the first element matching the given XPath expression within the root element.
   *
   * @param xpath XPath expression to locate the element
   * @return the first element that matches the given XPath expression
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement $x(final String xpath) {
    return rootElement().$x(xpath);
  }

  /**
   * Locates the Nth element matching the given XPath expression within the root element.
   *
   * @param xpath XPath expression to locate the element
   * @param index 0...N index of the element to locate
   * @return the Nth element that matches the given XPath expression
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement $x(final String xpath, final int index) {
    return rootElement().$x(xpath, index);
  }

  /**
   * Locates all elements matching the given XPath expression within the root element.
   *
   * @param xpath XPath expression to locate the elements
   * @return an {@link ElementsCollection} of all elements that match the given XPath expression
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public ElementsCollection $$x(final String xpath) {
    return rootElement().$$x(xpath);
  }

  /**
   * Locates the closest ancestor element matching the given CSS selector for the root element.
   *
   * @param selector CSS selector to locate the ancestor element
   * @return the closest ancestor element that matches the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement ancestor(final String selector) {
    return rootElement().ancestor(selector);
  }

  /**
   * Locates the Nth ancestor element matching the given CSS selector for the root element.
   *
   * @param selector CSS selector to locate the ancestor element
   * @param index    0...N index of the ancestor element to locate
   * @return the Nth ancestor element that matches the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement ancestor(final String selector, final int index) {
    return rootElement().ancestor(selector, index);
  }

  /**
   * Same as {@link ElementComponent#ancestor(String)}.
   *
   * @param selector CSS selector to locate the ancestor element
   * @return the closest ancestor element that matches the given CSS selector
   * @see ElementComponent#ancestor(String)
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement closest(final String selector) {
    return rootElement().closest(selector);
  }

  /**
   * Locates the first element matching the given CSS selector within the root element.
   *
   * @param cssSelector CSS selector to locate the element
   * @return the first element that matches the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement find(final String cssSelector) {
    return rootElement().find(cssSelector);
  }

  /**
   * Locates the Nth element matching the given CSS selector within the root element.
   *
   * @param cssSelector CSS selector to locate the element
   * @param index       0...N index of the element to locate
   * @return the Nth element that matches the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement find(final String cssSelector, final int index) {
    return rootElement().find(cssSelector, index);
  }

  /**
   * Locates the first element matching the given {@link By} selector within the root element.
   *
   * @param selector {@link By} selector to locate the element
   * @return the first element that matches the given {@link By} selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement find(final By selector) {
    return rootElement().find(selector);
  }

  /**
   * Locates the Nth element matching the given {@link By} selector within the root element.
   *
   * @param selector {@link By} selector to locate the element
   * @param index    0...N index of the element to locate
   * @return the Nth element that matches the given {@link By} selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public SelenideElement find(final By selector, final int index) {
    return rootElement().find(selector, index);
  }

  /**
   * Locates all elements matching the given CSS selector within the root element.
   *
   * @param cssSelector CSS selector to locate the elements
   * @return an {@link ElementsCollection} of all elements that match the given CSS selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public ElementsCollection findAll(final String cssSelector) {
    return rootElement().findAll(cssSelector);
  }

  /**
   * Locates all elements matching the given {@link By} selector within the root element.
   *
   * @param selector {@link By} selector to locate the elements
   * @return an {@link ElementsCollection} of all elements that match the given {@link By} selector
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public ElementsCollection findAll(final By selector) {
    return rootElement().findAll(selector);
  }

  /**
   * Get the driver that contains this element.
   *
   * @return the driver that contains this element.
   */
  @Override
  public WebDriver getWrappedDriver() {
    return rootElement().getWrappedDriver();
  }

  /**
   * Get the underlying {@code WebElement}.
   *
   * @return the underlying {@code WebElement}
   * @throws com.codeborne.selenide.ex.ElementNotFound if element does not exist (after waiting for N seconds)
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public WebElement getWrappedElement() {
    return rootElement().getWrappedElement();
  }

  /**
   * Get last child element of this element.
   * For example, $("tr").lastChild(); could give the last "td".
   *
   * @return the last child element of this element
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement lastChild() {
    return rootElement().lastChild();
  }

  /**
   * Get parent element of this element (lazy evaluation).
   * For example, $("td").parent() could give some "tr".
   *
   * @return the parent element of this element
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement parent() {
    return rootElement().parent();
  }

  /**
   * Get the preceding sibling element of this element.
   * For example, $("td").preceding(0) will give the first preceding sibling element of "td"
   *
   * @param index the index of sibling element
   * @return the preceding sibling element of this element.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement preceding(final int index) {
    return rootElement().preceding(index);
  }

  /**
   * Get the following sibling element of this element.
   * For example, $("td").sibling(0) will give the first following sibling element of "td" *
   *
   * @param index the index of sibling element
   * @return the preceding sibling element of this element.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement sibling(final int index) {
    return rootElement().preceding(index);
  }

  /**
   * Get the original Selenium {@code WebElement} wrapped by this object.
   *
   * @return the original Selenium {@code WebElement} wrapped by this object
   * @throws java.util.NoSuchElementException if element does not exist (without waiting for the element)
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public WebElement toWebElement() {
    return rootElement().toWebElement();
  }

  /**
   * Find the first element using the given method.
   *
   * @param selector the locating mechanism to use
   * @return the first matching element on the current context
   * @throws java.util.NoSuchElementException if element does not exist (without waiting for the element)
   */
  @Override
  public WebElement findElement(final By selector) {
    return rootElement().findElement(selector);
  }

  /**
   * Find all elements within the current context using the given mechanism.
   *
   * @param selector the locating mechanism to use
   * @return a list of all WebElements, or an empty list if nothing matches
   */
  @Override
  public List<WebElement> findElements(final By selector) {
    return rootElement().findElements(selector);
  }

  /**
   * Append text to this element and trigger a "change" event.
   *
   * @param text the text to append.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement append(final String text) {
    return rootElement().append(text);
  }

  /**
   * Assigns a human-readable name (alias) to the element for easier identification in reports.
   *
   * @param alias the alias to set
   * @return the updated instance of the facade for chaining
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement as(final String alias) {
    return rootElement().as(alias);
  }

  /**
   * Retrieves the value of the specified attribute of the element.
   * This is an alias for {@link #getAttribute(String)}.
   *
   * @param name the name of the attribute
   * @return the value of the attribute, or null if it does not exist
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String attr(final String name) {
    return rootElement().attr(name);
  }

  /**
   * Caches the current state of the web element.
   * This can be used to avoid redundant lookups of the same element.
   *
   * @return the updated instance of the facade for chaining
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement cached() {
    return rootElement().cached();
  }

  /**
   * Clears the input field.
   */
  @CanIgnoreReturnValue
  @Override
  public void clear() {
    rootElement().clear();
  }

  /**
   * Clicks on the element.
   */
  @Override
  public void click() {
    rootElement().click();
  }

  /**
   * Click this element using the provided click options.
   *
   * @param options the options for the click action.
   * @return the current instance of the facade for method chaining.
   */
  @Override
  public SelenideElement click(final ClickOptions options) {
    return rootElement().click(options);
  }

  /**
   * Performs a right-click on the element.
   *
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement contextClick() {
    return rootElement().contextClick();
  }

  /**
   * Get the value of a data attribute from this element.
   *
   * @param name the name of the data attribute.
   * @return the value of the data attribute, or {@code null} if not set.
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String data(final String name) {
    return rootElement().data(name);
  }

  /**
   * Displays element in human-readable format. Useful for logging and debugging.
   * Not recommended to use for test verifications. May work relatively slowly because
   * it fetches actual element information from browser.
   *
   * @return a description of the element.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String describe() {
    return rootElement().describe();
  }

  /**
   * Double-clicks on the element.
   *
   * @return the updated instance of the facade for chaining
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement doubleClick() {
    return rootElement().doubleClick();
  }

  /**
   * Performs a double click on the element using the specified click options.
   *
   * @param options the click options to use
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement doubleClick(final ClickOptions options) {
    return rootElement().doubleClick(options);
  }

  /**
   * Downloads a file by clicking the element with the default timeout.
   *
   * @return the downloaded file
   * @throws FileNotDownloadedError if the file could not be downloaded
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public File download() throws FileNotDownloadedError {
    return rootElement().download();
  }

  /**
   * Downloads a file by clicking the element with the specified timeout.
   *
   * @param timeout the timeout duration in milliseconds
   * @return the downloaded file
   * @throws FileNotDownloadedError if the file could not be downloaded
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public File download(final long timeout) throws FileNotDownloadedError {
    return rootElement().download(timeout);
  }

  /**
   * Downloads a file by clicking the element with the specified timeout and file filter.
   *
   * @param timeout the timeout duration in milliseconds
   * @param filter  the filter to apply for the downloaded file
   * @return the downloaded file
   * @throws FileNotDownloadedError if the file could not be downloaded
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public File download(final long timeout, final FileFilter filter) throws FileNotDownloadedError {
    return rootElement().download(timeout, filter);
  }

  /**
   * Download a file by clicking this element.
   *
   * @param options options for downloading the file.
   * @return the downloaded file.
   * @throws FileNotDownloadedError if the file could not be downloaded.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public File download(final DownloadOptions options) throws FileNotDownloadedError {
    return rootElement().download(options);
  }

  /**
   * Downloads a file by clicking on the element, filtered by the given file filter.
   *
   * @param filter the filter to apply for the file download
   * @return the downloaded file
   * @throws FileNotDownloadedError if the file could not be downloaded
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public File download(final FileFilter filter) throws FileNotDownloadedError {
    return rootElement().download(filter);
  }

  /**
   * Drag and drop this element to the target
   *
   * @param options drag and drop options to define target and which way it will be executed
   * @return the current instance of {@link SelenideElement}
   */
  @CanIgnoreReturnValue
  @Nonnull
  @Override
  public SelenideElement dragAndDrop(final DragAndDropOptions options) {
    return rootElement().dragAndDrop(options);
  }

  /**
   * Executes a custom command.
   * The command will be executed with no timeout.
   *
   * @param command      the custom command to execute
   * @param <ReturnType> the return type of the command
   * @return the result of the command execution
   */
  @Override
  public <ReturnType> ReturnType execute(final Command<ReturnType> command) {
    return rootElement().execute(command);
  }

  /**
   * Executes a custom command with a specified timeout.
   * The command will be executed with the given timeout.
   *
   * @param command      the custom command to execute
   * @param timeout      the timeout for executing the command
   * @param <ReturnType> the return type of the command
   * @return the result of the command execution
   */
  @Override
  public <ReturnType> ReturnType execute(final Command<ReturnType> command, final Duration timeout) {
    return rootElement().execute(command, timeout);
  }

  /**
   * Checks if the element exists on the current page.
   *
   * @return true if the element exists, false otherwise
   */
  @CheckReturnValue
  @Override
  public boolean exists() {
    return rootElement().exists();
  }

  /**
   * Element alias, which can be set with {@link ElementComponent#as(String)}. Usually you should
   * not need this method, unless you are writing a custom reporting engine like Allure Reports.
   *
   * @return the alias name, or {@code null} if not set.
   * @see ElementComponent#as(String)
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String getAlias() {
    return rootElement().getAlias();
  }

  /**
   * Retrieves the value of the specified attribute of the element.
   *
   * @param name the name of the attribute
   * @return the value of the attribute, or null if it does not exist
   */
  @Nullable
  @CheckReturnValue
  @Override
  public String getAttribute(final String name) {
    return rootElement().getAttribute(name);
  }

  /**
   * Retrieves the value of the specified CSS property of the element.
   *
   * @param propertyName the name of the CSS property
   * @return the value of the CSS property
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public String getCssValue(final String propertyName) {
    return rootElement().getCssValue(propertyName);
  }

  /**
   * Retrieves all options from a select field.
   *
   * @return an {@link ElementsCollection} containing all options
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public ElementsCollection getOptions() {
    return rootElement().getOptions();
  }

  /**
   * Gets the text of the element excluding its children.
   *
   * @return the text content of the element
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String getOwnText() {
    return rootElement().getOwnText();
  }

  /**
   * Return the criteria by which this element is located.
   *
   * @return the search criteria.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String getSearchCriteria() {
    return rootElement().getSearchCriteria();
  }

  /**
   * Retrieves the selected option from a select field.
   *
   * @return the selected option as a {@link SelenideElement}
   * @throws java.util.NoSuchElementException if no option is selected
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public SelenideElement getSelectedOption() throws NoSuchElementException {
    return rootElement().getSelectedOption();
  }

  /**
   * Get all selected options from this select field.
   *
   * @return a collection of selected options.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public ElementsCollection getSelectedOptions() {
    return rootElement().getSelectedOptions();
  }

  /**
   * Retrieves the text of the selected option from a select field.
   *
   * @return the text of the selected option, or null if no option is selected
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String getSelectedOptionText() {
    return rootElement().getSelectedOptionText();
  }

  /**
   * Get the value of the selected option from this select field.
   *
   * @return the value of the selected option, or {@code null} if not selected.
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String getSelectedOptionValue() {
    return rootElement().getSelectedOptionValue();
  }

  /**
   * Gets the "value" attribute of the element.
   *
   * @return the value of the "value" attribute, or null if it does not exist
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String getValue() {
    return rootElement().getValue();
  }

  /**
   * Retrieves the visible text of the element, including sub-elements, with leading and trailing whitespace trimmed.
   *
   * @return the visible text of the element
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String getText() {
    return rootElement().getText();
  }

  /**
   * Checks if the element matches the specified condition immediately without waiting.
   *
   * @param condition the condition to check
   * @return true if the element matches the condition, false otherwise
   */
  @CheckReturnValue
  @Override
  public boolean has(final WebElementCondition condition) {
    return rootElement().has(condition);
  }

  /**
   * Checks if the element meets the specified condition within the given duration.
   * This method immediately returns true if the condition is met; otherwise, it waits up to the given duration.
   *
   * @param condition the condition to be checked
   * @param timeout   the maximum time to wait for the condition to be met
   * @return true if the element meets the condition within the specified duration, false otherwise
   */
  @CheckReturnValue
  @Override
  public boolean has(final WebElementCondition condition, final Duration timeout) {
    return rootElement().has(condition, timeout);
  }

  /**
   * Highlights the element with default options.
   *
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @Override
  public SelenideElement highlight() {
    return rootElement().highlight();
  }

  /**
   * Highlights the element with the specified highlight options.
   *
   * @param options the highlight options to use
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @Override
  public SelenideElement highlight(final HighlightOptions options) {
    return rootElement().highlight(options);
  }

  /**
   * Emulate a mouse hover event over this element.
   *
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement hover() {
    return rootElement().hover();
  }

  /**
   * Emulates a "mouseOver" event on the element with the specified hover options.
   *
   * @param options the options for the hover action
   * @return the updated instance of the facade for chaining
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement hover(final HoverOptions options) {
    return rootElement().hover(options);
  }

  /**
   * Get the HTML content of this element including its children.
   *
   * @return the inner HTML of the element.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String innerHtml() {
    return rootElement().innerHtml();
  }

  /**
   * Get the text code of the element with children.
   *
   * @return the text code of the element with children.
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public String innerText() {
    return rootElement().innerText();
  }

  /**
   * Check if this element matches the given condition within the default timeout.
   *
   * @param condition the condition to check.
   * @return {@code true} if the condition is met within the timeout, otherwise {@code false}.
   */
  @Override
  public boolean is(final WebElementCondition condition) {
    return rootElement().is(condition);
  }

  /**
   * Check if this element matches the given condition within the specified timeout.
   *
   * @param condition the condition to check.
   * @param timeout   the timeout duration.
   * @return {@code true} if the condition is met within the timeout, otherwise {@code false}.
   */
  @Override
  public boolean is(final WebElementCondition condition, final Duration timeout) {
    return rootElement().is(condition, timeout);
  }

  /**
   * Checks if the element is displayed on the page.
   *
   * @return true if the element is displayed, false otherwise
   */
  @CheckReturnValue
  @Override
  public boolean isDisplayed() {
    return rootElement().isDisplayed();
  }

  /**
   * Checks if the element is an image and if it is properly loaded.
   *
   * @return true if the element is an image and is loaded, false otherwise
   */
  @Override
  public boolean isImage() {
    return rootElement().isImage();
  }

  /**
   * Get the "name" attribute of this element.
   *
   * @return the value of the "name" attribute, or {@code null} if not set.
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String name() {
    return rootElement().name();
  }

  /**
   * Append text from the clipboard into this element and trigger a "change" event.
   *
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement paste() {
    return rootElement().paste();
  }

  /**
   * Simulates pressing the specified key(s) on the keyboard.
   *
   * @param charSequences the key(s) to press
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement press(final CharSequence... charSequences) {
    return rootElement().press(charSequences);
  }

  /**
   * Simulates pressing the ENTER key.
   *
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement pressEnter() {
    return rootElement().pressEnter();
  }

  /**
   * Simulates pressing the ESCAPE key.
   *
   * @return the current instance of {@link SelenideElement}
   */
  @CanIgnoreReturnValue
  @Nonnull
  @Override
  public SelenideElement pressEscape() {
    return rootElement().pressEscape();
  }

  /**
   * Simulates pressing the TAB key.
   *
   * @return the current instance of {@link SelenideElement}
   */
  @CanIgnoreReturnValue
  @Nonnull
  @Override
  public SelenideElement pressTab() {
    return rootElement().pressTab();
  }

  /**
   * Get the content of a pseudo-element.
   *
   * @param pseudoElementName the name of the pseudo-element.
   * @return the content of the pseudo-element.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String pseudo(final String pseudoElementName) {
    return rootElement().pseudo(pseudoElementName);
  }

  /**
   * Get the content of a pseudo-element's specified property.
   *
   * @param pseudoElementName the name of the pseudo-element.
   * @param propertyName      the name of the property.
   * @return the property value of the pseudo-element.
   */
  @CheckReturnValue
  @Nonnull
  @Override
  public String pseudo(final String pseudoElementName, final String propertyName) {
    return rootElement().pseudo(pseudoElementName, propertyName);
  }

  /**
   * Takes a screenshot of the element.
   *
   * @return the screenshot file, or null if the screenshot could not be taken
   */
  @CheckReturnValue
  @Nullable
  @Override
  public File screenshot() {
    return rootElement().screenshot();
  }

  /**
   * Scroll this element into view, optionally aligning it to the top.
   *
   * @param alignToTop if {@code true}, align to the top; otherwise, align to the bottom.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement scrollIntoView(final boolean alignToTop) {
    return rootElement().scrollIntoView(alignToTop);
  }

  /**
   * Takes a screenshot of the element and returns it as a {@link BufferedImage}.
   *
   * @return the screenshot as a {@link BufferedImage}, or null if the screenshot could not be taken
   */
  @CheckReturnValue
  @Nullable
  @Override
  public BufferedImage screenshotAsImage() {
    return rootElement().screenshotAsImage();
  }

  /**
   * Scrolls the browser view to the element with the specified scroll options.
   *
   * <p>
   * scrollIntoViewOptions:
   * <ul>
   *   <li><b>behavior (optional)</b> - Defines the transition animation:
   *     <ol>
   *       <li><b>auto</b> (default) - The browser will determine the scrolling behavior.</li>
   *       <li><b>instant</b> - Scrolls the element into view instantly, without any smooth animation.</li>
   *       <li><b>smooth</b> - Scrolls the element into view with a smooth animation.</li>
   *     </ol>
   *   </li>
   *   <li><b>block (optional)</b> - Defines vertical alignment:
   *     <ol>
   *       <li><b>start</b> (default) - Aligns the element to the top of the scrolling area.</li>
   *       <li><b>center</b> - Aligns the element to the center of the scrolling area.</li>
   *       <li><b>end</b> - Aligns the element to the bottom of the scrolling area.</li>
   *       <li><b>nearest</b> - Aligns the element to the nearest edge of the scrolling area.</li>
   *     </ol>
   *   </li>
   *   <li><b>inline (optional)</b> - Defines horizontal alignment:
   *     <ol>
   *       <li><b>start</b> - Aligns the element to the left edge of the scrolling area.</li>
   *       <li><b>center</b> - Aligns the element to the center of the scrolling area.</li>
   *       <li><b>end</b> - Aligns the element to the right edge of the scrolling area.</li>
   *       <li><b>nearest</b> (default) - Aligns the element to the nearest edge of the scrolling area.</li>
   *     </ol>
   *   </li>
   * </ul>
   *
   * <p>
   * <b>Usage:</b>
   * <p>
   *    {@code element.scrollIntoView("{block: \"end\"}");}
   * <p>
   *    {@code element.scrollIntoView("{behavior: \"instant\", block: \"end\", inline: \"nearest\"}");}
   *
   * @param scrollIntoViewOptions the scroll options to use
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement scrollIntoView(final String scrollIntoViewOptions) {
    return rootElement().scrollIntoView(scrollIntoViewOptions);
  }

  /**
   * Scrolls the browser view to the element.
   *
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement scrollTo() {
    return rootElement().scrollTo();
  }

  /**
   * Select an option from dropdown list (by index)
   *
   * @param index        0..N (0 means first option)
   * @param otherIndexes other indexes (if you need to select multiple options)
   */
  @Override
  public void selectOption(final int index, final int... otherIndexes) {
    rootElement().selectOption(index, otherIndexes);
  }

  /***
   * Select an option from dropdown list (by text)
   * @param text visible text of option
   * @param otherText other texts (if you need to select multiple options)
   */
  @Override
  public void selectOption(final String text, final String... otherText) {
    rootElement().selectOption(text, otherText);
  }

  /**
   * Selects an option from a dropdown list by its value.
   *
   * @param value       the value of the option to select
   * @param otherValues additional values of options to select
   */
  @Override
  public void selectOptionByValue(final String value, final String... otherValues) {
    rootElement().selectOptionByValue(value, otherValues);
  }

  /**
   * Selects an option from a dropdown list that contains the specified text.
   * This method will select options matching any of the provided texts.
   *
   * @param text       the text to match in the options
   * @param otherTexts additional texts to match in the options
   */
  @Override
  public void selectOptionContainingText(final String text, final String... otherTexts) {
    rootElement().selectOptionContainingText(text, otherTexts);
  }

  /**
   * Selects the radio button with the specified value.
   *
   * @param value the value of the radio button to select
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement selectRadio(final String value) {
    return rootElement().selectRadio(value);
  }

  /**
   * Sets the checkbox state to checked or unchecked.
   *
   * @param selected true to check the checkbox, false to uncheck it
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement setSelected(final boolean selected) {
    return rootElement().setSelected(selected);
  }

  /**
   * Set the value of an input element using the provided options.
   *
   * @param options options for setting the value.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement setValue(final SetValueOptions options) {
    return rootElement().setValue(options);
  }

  /**
   * Set the value of an input element.
   *
   * @param text the value to set.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement setValue(@Nullable final String text) {
    return rootElement().setValue(text);
  }

  /**
   * Ensures that the element meets all the specified conditions.
   *
   * @param conditions the conditions that the element should meet
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement should(final WebElementCondition... conditions) {
    return rootElement().should(conditions);
  }

  /**
   * Check that this element meets the specified condition within the given timeout.
   *
   * @param condition the condition to check.
   * @param timeout   the timeout duration.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement should(final WebElementCondition condition, final Duration timeout) {
    return rootElement().should(condition, timeout);
  }

  /**
   * Ensures that the element meets all the specified conditions.
   *
   * @param conditions the conditions that the element should meet
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldBe(final WebElementCondition... conditions) {
    return rootElement().shouldBe(conditions);
  }

  /**
   * Ensures that the element meets the specified condition within the given timeout.
   *
   * @param condition the condition that the element should meet
   * @param timeout   the timeout duration
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldBe(final WebElementCondition condition, final Duration timeout) {
    return rootElement().shouldBe(condition, timeout);
  }

  /**
   * Ensures that the element meets all the specified conditions.
   *
   * @param conditions the conditions that the element should meet
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldHave(final WebElementCondition... conditions) {
    return rootElement().shouldHave(conditions);
  }

  /**
   * Asserts that the element meets the specified condition within the given duration.
   * This method waits until the element meets the condition.
   *
   * @param condition the condition to check
   * @param timeout   the timeout duration for checking the condition
   * @return the updated instance of the facade for chaining
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldHave(final WebElementCondition condition, final Duration timeout) {
    return rootElement().shouldHave(condition, timeout);
  }

  /**
   * Asserts that the element does not meet any of the specified conditions.
   * This method waits until the element does not meet any of the conditions.
   *
   * @param conditions the conditions that the element should not meet
   * @return the updated instance of the facade for chaining
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldNot(final WebElementCondition... conditions) {
    return rootElement().shouldNot(conditions);
  }

  /**
   * Check that this element doesn't meet the specified condition within the given timeout.
   *
   * @param condition the condition that the element should meet
   * @param timeout   the timeout duration
   * @return the current instance of {@link SelenideElement}
   */
  @CanIgnoreReturnValue
  @Nonnull
  @Override
  public SelenideElement shouldNot(final WebElementCondition condition, final Duration timeout) {
    return rootElement().shouldNot(condition, timeout);
  }

  /**
   * Check that this element does not meet any of the specified conditions.
   *
   * @param conditions the conditions to check.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldNotBe(final WebElementCondition... conditions) {
    return rootElement().shouldNotBe(conditions);
  }

  /**
   * Check that this element does not meet the specified condition within the given timeout.
   *
   * @param condition the condition to check.
   * @param timeout   the timeout duration.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldNotBe(final WebElementCondition condition, final Duration timeout) {
    return rootElement().shouldNotBe(condition, timeout);
  }

  /**
   * Ensures that the element does not have the specified conditions.
   *
   * @param conditions the conditions that the element should not have
   * @return the current instance of {@link SelenideElement}
   */
  @CanIgnoreReturnValue
  @Nonnull
  @Override
  public SelenideElement shouldNotHave(final WebElementCondition... conditions) {
    return rootElement().shouldNotHave(conditions);
  }

  /**
   * Ensures that the element does not have the specified condition within the given timeout.
   *
   * @param condition the condition that the element should not have
   * @param timeout   the timeout duration
   * @return the current instance of {@link SelenideElement}
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement shouldNotHave(final WebElementCondition condition, final Duration timeout) {
    return rootElement().shouldNotHave(condition, timeout);
  }

  /**
   * Short form of {@link ElementComponent#getText()}.
   *
   * @return the visible text of the element
   * @see ElementComponent#getText()
   */
  @Nonnull
  @CheckReturnValue
  @Override
  public String text() {
    return rootElement().text();
  }

  /**
   * Mimic how a real user would type in a text field.
   * Useful for working with autosuggestion dropdowns.
   *
   * @param text the text to type.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement type(final CharSequence text) {
    return rootElement().type(text);
  }

  /**
   * Mimic typing in a text field using the provided options.
   *
   * @param options options for typing.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement type(final TypeOptions options) {
    return rootElement().type(options);
  }

  /**
   * Remove focus from this element.
   *
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement unfocus() {
    return rootElement().unfocus();
  }

  /**
   * Uploads one or more files into a file upload field.
   *
   * @param files the files to upload
   * @return the uploaded file
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public File uploadFile(final File... files) {
    return rootElement().uploadFile(files);
  }

  /**
   * Uploads a file into a file upload field from the classpath.
   * The file path should be relative to the classpath of the application.
   *
   * @param fileName the names of the files to upload
   * @return the uploaded file
   * @throws IllegalArgumentException if any of the files cannot be found in the classpath
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public File uploadFromClasspath(final String... fileName) {
    return rootElement().uploadFromClasspath(fileName);
  }

  /**
   * Get the value attribute of this element.
   *
   * @return the value attribute, or {@code null} if not set.
   */
  @CheckReturnValue
  @Nullable
  @Override
  public String val() {
    return rootElement().val();
  }

  /**
   * Same as {@link ElementComponent#setValue(SetValueOptions)}
   *
   * @param options options for setting the value.
   * @return the current instance of the facade for method chaining.
   * @see ElementComponent#setValue(SetValueOptions)
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement val(final SetValueOptions options) {
    return rootElement().val(options);
  }

  /**
   * Same as {@link ElementComponent#setValue(String)}.
   *
   * @param text the value to set.
   * @return the current instance of the facade for method chaining.
   * @see ElementComponent#setValue(String)
   */
  @Nonnull
  @CanIgnoreReturnValue
  @Override
  public SelenideElement val(@Nullable final String text) {
    return rootElement().val(text);
  }

  /**
   * Provides coordinates of an element for advanced interactions.
   *
   * @return the coordinates of an element for advanced interactions.
   */
  @Override
  public Coordinates getCoordinates() {
    return rootElement().getCoordinates();
  }

  /**
   * Takes a screenshot of the element and returns it in the specified output format.
   *
   * @param target the output type of the screenshot
   * @param <X>    the type of the output
   * @return the screenshot in the specified output format
   * @throws WebDriverException if an error occurs while taking the screenshot
   */
  @Override
  public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
    return rootElement().getScreenshotAs(target);
  }

  /**
   * Get the accessible name of this element.
   *
   * @return the accessible name of the element.
   */
  @Override
  public String getAccessibleName() {
    return rootElement().getAccessibleName();
  }

  /**
   * Gets result of computing the WAI-ARIA role of element.
   *
   * @return the WAI-ARIA role of the element.
   */
  @Override
  public String getAriaRole() {
    return rootElement().getAriaRole();
  }

  /**
   * Get the value of the given attribute of the element.
   * <p>
   * This method, unlike getAttribute(String), returns the value of the attribute with the given name
   * but not the property with the same name.
   * <p>
   * The following are deemed to be "boolean" attributes, and will return either "true" or null:
   * <p>
   * async, autofocus, autoplay, checked, compact, complete, controls, declare, defaultchecked,
   * defaultselected, defer, disabled, draggable, ended, formnovalidate, hidden, indeterminate,
   * iscontenteditable, ismap, itemscope, loop, multiple, muted, nohref, noresize, noshade, novalidate,
   * nowrap, open, paused, pubdate, readonly, required, reversed, scoped, seamless, seeking, selected,
   * truespeed, willvalidate
   *
   * @param name the name of the attribute
   * @return the value of the attribute
   */
  @Override
  public String getDomAttribute(final String name) {
    return rootElement().getDomAttribute(name);
  }

  /**
   * Retrieves the value of the specified DOM property of the element.
   *
   * @param name the name of the property
   * @return the value of the property
   */
  @Override
  public String getDomProperty(final String name) {
    return rootElement().getDomProperty(name);
  }

  /**
   * Retrieves the location of the element on the page.
   *
   * @return a {@link Point} representing the location of the element
   */
  @Override
  public Point getLocation() {
    return rootElement().getLocation();
  }

  /**
   * The location and size of the rendered element
   *
   * @return the rectangle representing the element's position and size.
   */
  @Override
  public Rectangle getRect() {
    return rootElement().getRect();
  }

  /**
   * Retrieves a representation of an element's shadow root for accessing the shadow DOM of a web component.
   *
   * @return a representation of an element's shadow root for accessing the shadow DOM of a web component.
   */
  @Override
  public SearchContext getShadowRoot() {
    return rootElement().getShadowRoot();
  }

  /**
   * Get the size of this element.
   *
   * @return the size of the element.
   */
  @Override
  public Dimension getSize() {
    return rootElement().getSize();
  }

  /**
   * Retrieves the tag name of the element.
   *
   * @return the tag name of the element
   */
  @Override
  public String getTagName() {
    return rootElement().getTagName();
  }

  /**
   * Check if this element is enabled.
   *
   * @return {@code true} if the element is enabled, otherwise {@code false}.
   */
  @Override
  public boolean isEnabled() {
    return rootElement().isEnabled();
  }

  /**
   * Check if this element is selected.
   *
   * @return {@code true} if the element is selected, otherwise {@code false}.
   */
  @Override
  public boolean isSelected() {
    return rootElement().isSelected();
  }

  /**
   * Send keystrokes to this element.
   *
   * @param keysToSend the keys to send.
   */
  @Override
  public void sendKeys(final CharSequence... keysToSend) {
    rootElement().sendKeys(keysToSend);
  }

  /**
   * If this current element is a form, or an element within a form, then this will
   * be submitted to the remote server. If this causes the current page to change, then
   * this method will block until the new page is loaded.
   */
  @Override
  public void submit() {
    rootElement().submit();
  }

  @Nonnull
  @CheckReturnValue
  @Override
  public String toString() {
    return new StringJoiner(", ", GuardedComponent.class.getSimpleName() + "[", "]")
        .add("page=" + linkedPage().getClass().getSimpleName())
        .add("rootElement=" + rootElement().describe())
        .toString();
  }
}
