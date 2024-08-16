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
package io.github.epadronu.dwalin.core;
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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.Coordinates;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;
/* ************************************************************************************************/

/**
 * <p>
 * A {@code SelenideElement} facade designed for developers who prefer not to expose
 * {@code WebElement} instances or {@code WebElement}-like behavior in the public API of their pages.
 * </p>
 *
 * @see SelenideElement
 * @see GuardedComponent
 */
public final class ElementGuard implements TakesScreenshot {

  /**
   * <p>
   * Error message displayed when attempting to create a new instance with a null element.
   * </p>
   */
  public static final String ELEMENT_CANNOT_BE_NULL_MESSAGE = "The element cannot be null";

  private final SelenideElement element;

  private ElementGuard(final SelenideElement element) {
    this.element = element;
  }

  /**
   * <p>
   * Encapsulates the provided element within a new {@code ElementGuard} instance.
   * </p>
   *
   * @param element the element to be encapsulated
   * @return a new {@code ElementGuard} that wraps the provided element
   * @throws NullPointerException if {@code element} is {@code null}
   */
  public static ElementGuard guard(final SelenideElement element) {
    return new ElementGuard(requireNonNull(element, ELEMENT_CANNOT_BE_NULL_MESSAGE));
  }

  /**
   * Append text to this element and trigger a "change" event.
   *
   * @param text the text to append.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  public ElementGuard append(final String text) {
    return guard(element.append(text));
  }

  /**
   * Assigns a human-readable name (alias) to the element for easier identification in reports.
   *
   * @param alias the alias to set
   * @return the updated instance of the facade for chaining
   */
  @CheckReturnValue
  @Nonnull
  public ElementGuard as(final String alias) {
    return guard(element.as(alias));
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
  public String attr(final String name) {
    return element.attr(name);
  }

  /**
   * Caches the current state of the web element.
   * This can be used to avoid redundant lookups of the same element.
   *
   * @return the updated instance of the facade for chaining
   */
  @CheckReturnValue
  public ElementGuard cached() {
    return guard(element.cached());
  }

  /**
   * Clears the input field.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  public ElementGuard clear() {
    element.clear();

    return this;
  }

  /**
   * Clicks on the element.
   *
   * @return the updated instance of the facade for chaining
   */
  public ElementGuard click() {
    element.click();

    return this;
  }

  /**
   * Click this element using the provided click options.
   *
   * @param options the options for the click action.
   * @return the current instance of the facade for method chaining.
   */
  public ElementGuard click(final ClickOptions options) {
    return guard(element.click(options));
  }

  /**
   * Performs a right-click on the element.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard contextClick() {
    return guard(element.contextClick());
  }

  /**
   * Get the value of a data attribute from this element.
   *
   * @param name the name of the data attribute.
   * @return the value of the data attribute, or {@code null} if not set.
   */
  @CheckReturnValue
  @Nullable
  public String data(final String name) {
    return element.data(name);
  }

  /**
   * Displays element in human-readable format. Useful for logging and debugging.
   * Not recommended to use for test verifications. May work relatively slowly because
   * it fetches actual element information from browser.
   *
   * @return a description of the element.
   */
  @Nonnull
  @CheckReturnValue
  public String describe() {
    return element.describe();
  }

  /**
   * Double-clicks on the element.
   *
   * @return the updated instance of the facade for chaining
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard doubleClick() {
    return guard(element.doubleClick());
  }

  /**
   * Performs a double click on the element using the specified click options.
   *
   * @param options the click options to use
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard doubleClick(final ClickOptions options) {
    return guard(element.doubleClick(options));
  }

  /**
   * Downloads a file by clicking the element with the default timeout.
   *
   * @return the downloaded file
   * @throws FileNotDownloadedError if the file could not be downloaded
   */
  @Nonnull
  @CheckReturnValue
  public File download() throws FileNotDownloadedError {
    return element.download();
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
  public File download(final long timeout) throws FileNotDownloadedError {
    return element.download(timeout);
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
  public File download(final long timeout, final FileFilter filter) throws FileNotDownloadedError {
    return element.download(timeout, filter);
  }

  /**
   * Download a file by clicking this element.
   *
   * @param options options for downloading the file.
   * @return the downloaded file.
   * @throws FileNotDownloadedError if the file could not be downloaded.
   */
  @Nonnull
  @CheckReturnValue
  public File download(final DownloadOptions options) throws FileNotDownloadedError {
    return element.download(options);
  }

  /**
   * Downloads a file by clicking on the element, filtered by the given file filter.
   *
   * @param filter the filter to apply for the file download
   * @return the downloaded file
   * @throws FileNotDownloadedError if the file could not be downloaded
   */
  @Nonnull
  @CheckReturnValue
  public File download(final FileFilter filter) throws FileNotDownloadedError {
    return element.download(filter);
  }

  /**
   * Drag and drop this element to the target
   *
   * @param options drag and drop options to define target and which way it will be executed
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard dragAndDrop(final DragAndDropOptions options) {
    return guard(element.dragAndDrop(options));
  }

  /**
   * Executes a custom command.
   * The command will be executed with no timeout.
   *
   * @param command      the custom command to execute
   * @param <ReturnType> the return type of the command
   * @return the result of the command execution
   */
  public <ReturnType> ReturnType execute(final Command<ReturnType> command) {
    return element.execute(command);
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
  public <ReturnType> ReturnType execute(final Command<ReturnType> command, final Duration timeout) {
    return element.execute(command, timeout);
  }

  /**
   * Checks if the element exists on the current page.
   *
   * @return true if the element exists, false otherwise
   */
  @CheckReturnValue
  public boolean exists() {
    return element.exists();
  }

  /**
   * Element alias, which can be set with {@link ElementGuard#as(String)}. Usually you should
   * not need this method, unless you are writing a custom reporting engine like Allure Reports.
   *
   * @return the alias name, or {@code null} if not set.
   * @see ElementGuard#as(String)
   */
  @Nullable
  @CheckReturnValue
  public String getAlias() {
    return element.getAlias();
  }

  /**
   * Retrieves the value of the specified attribute of the element.
   *
   * @param name the name of the attribute
   * @return the value of the attribute, or null if it does not exist
   */
  @Nullable
  @CheckReturnValue
  public String getAttribute(final String name) {
    return element.getAttribute(name);
  }

  /**
   * Retrieves the value of the specified CSS property of the element.
   *
   * @param propertyName the name of the CSS property
   * @return the value of the CSS property
   */
  @Nonnull
  @CheckReturnValue
  public String getCssValue(final String propertyName) {
    return element.getCssValue(propertyName);
  }

  /**
   * Retrieves all options from a select field.
   *
   * @return an {@link ElementsCollection} containing all options
   */
  @Nonnull
  @CheckReturnValue
  public ElementsCollection getOptions() {
    return element.getOptions();
  }

  /**
   * Gets the text of the element excluding its children.
   *
   * @return the text content of the element
   */
  @Nonnull
  @CheckReturnValue
  public String getOwnText() {
    return element.getOwnText();
  }

  /**
   * Return the criteria by which this element is located.
   *
   * @return the search criteria.
   */
  @Nonnull
  @CheckReturnValue
  public String getSearchCriteria() {
    return element.getSearchCriteria();
  }

  /**
   * Retrieves the selected option from a select field.
   *
   * @return the selected option as a {@link ElementGuard}
   * @throws NoSuchElementException if no option is selected
   */
  @Nonnull
  @CheckReturnValue
  public ElementGuard getSelectedOption() throws NoSuchElementException {
    return guard(element.getSelectedOption());
  }

  /**
   * Get all selected options from this select field.
   *
   * @return a collection of selected options.
   */
  @Nonnull
  @CheckReturnValue
  public ElementsCollection getSelectedOptions() {
    return element.getSelectedOptions();
  }

  /**
   * Retrieves the text of the selected option from a select field.
   *
   * @return the text of the selected option, or null if no option is selected
   */
  @Nullable
  @CheckReturnValue
  public String getSelectedOptionText() {
    return element.getSelectedOptionText();
  }

  /**
   * Get the value of the selected option from this select field.
   *
   * @return the value of the selected option, or {@code null} if not selected.
   */
  @Nullable
  @CheckReturnValue
  public String getSelectedOptionValue() {
    return element.getSelectedOptionValue();
  }

  /**
   * Retrieves the visible text of the element, including sub-elements, with leading and trailing whitespace trimmed.
   *
   * @return the visible text of the element
   */
  @Nonnull
  @CheckReturnValue
  public String getText() {
    return element.getText();
  }

  /**
   * Gets the "value" attribute of the element.
   *
   * @return the value of the "value" attribute, or null if it does not exist
   */
  @Nullable
  @CheckReturnValue
  public String getValue() {
    return element.getValue();
  }

  /**
   * Checks if the element matches the specified condition immediately without waiting.
   *
   * @param condition the condition to check
   * @return true if the element matches the condition, false otherwise
   */
  @CheckReturnValue
  public boolean has(final WebElementCondition condition) {
    return element.has(condition);
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
  public boolean has(final WebElementCondition condition, final Duration timeout) {
    return element.has(condition, timeout);
  }

  /**
   * Highlights the element with default options.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @Nonnull
  public ElementGuard highlight() {
    return guard(element.highlight());
  }

  /**
   * Highlights the element with the specified highlight options.
   *
   * @param options the highlight options to use
   * @return the current instance of {@link ElementGuard}
   */
  @Nonnull
  public ElementGuard highlight(final HighlightOptions options) {
    return guard(element.highlight(options));
  }

  /**
   * Emulate a mouse hover event over this element.
   *
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard hover() {
    return guard(element.hover());
  }

  /**
   * Emulates a "mouseOver" event on the element with the specified hover options.
   *
   * @param options the options for the hover action
   * @return the updated instance of the facade for chaining
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard hover(final HoverOptions options) {
    return guard(element.hover(options));
  }

  /**
   * Get the HTML content of this element including its children.
   *
   * @return the inner HTML of the element.
   */
  @Nonnull
  @CheckReturnValue
  public String innerHtml() {
    return element.innerHtml();
  }

  /**
   * Get the text code of the element with children.
   *
   * @return the text code of the element with children.
   */
  @Nonnull
  @CheckReturnValue
  public String innerText() {
    return element.innerText();
  }

  /**
   * Check if this element matches the given condition within the default timeout.
   *
   * @param condition the condition to check.
   * @return {@code true} if the condition is met within the timeout, otherwise {@code false}.
   */
  @CheckReturnValue
  public boolean is(final WebElementCondition condition) {
    return element.is(condition);
  }

  /**
   * Check if this element matches the given condition within the specified timeout.
   *
   * @param condition the condition to check.
   * @param timeout   the timeout duration.
   * @return {@code true} if the condition is met within the timeout, otherwise {@code false}.
   */
  public boolean is(final WebElementCondition condition, final Duration timeout) {
    return element.is(condition, timeout);
  }

  /**
   * Checks if the element is displayed on the page.
   *
   * @return true if the element is displayed, false otherwise
   */
  @CheckReturnValue
  public boolean isDisplayed() {
    return element.isDisplayed();
  }

  /**
   * Checks if the element is an image and if it is properly loaded.
   *
   * @return true if the element is an image and is loaded, false otherwise
   */
  public boolean isImage() {
    return element.isImage();
  }

  /**
   * Get the "name" attribute of this element.
   *
   * @return the value of the "name" attribute, or {@code null} if not set.
   */
  @Nullable
  @CheckReturnValue
  public String name() {
    return element.name();
  }

  /**
   * Append text from the clipboard into this element and trigger a "change" event.
   *
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard paste() {
    return guard(element.paste());
  }

  /**
   * Simulates pressing the specified key(s) on the keyboard.
   *
   * @param charSequences the key(s) to press
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard press(final CharSequence... charSequences) {
    return guard(element.press(charSequences));
  }

  /**
   * Simulates pressing the ENTER key.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard pressEnter() {
    return guard(element.pressEnter());
  }

  /**
   * Simulates pressing the ESCAPE key.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard pressEscape() {
    return guard(element.pressEscape());
  }

  /**
   * Simulates pressing the TAB key.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard pressTab() {
    return guard(element.pressTab());
  }

  /**
   * Get the content of a pseudo-element's specified property.
   *
   * @param pseudoElementName the name of the pseudo-element.
   * @return the property value of the pseudo-element.
   */
  @CheckReturnValue
  @Nonnull
  public String pseudo(final String pseudoElementName) {
    return element.pseudo(pseudoElementName);
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
  public String pseudo(final String pseudoElementName, final String propertyName) {
    return element.pseudo(pseudoElementName, propertyName);
  }

  /**
   * Takes a screenshot of the element.
   *
   * @return the screenshot file, or null if the screenshot could not be taken
   */
  @Nullable
  @CheckReturnValue
  public File screenshot() {
    return element.screenshot();
  }

  /**
   * Takes a screenshot of the element and returns it as a {@link BufferedImage}.
   *
   * @return the screenshot as a {@link BufferedImage}, or null if the screenshot could not be taken
   */
  @Nullable
  @CheckReturnValue
  public BufferedImage screenshotAsImage() {
    return element.screenshotAsImage();
  }

  /**
   * Scroll this element into view, optionally aligning it to the top.
   *
   * @param alignToTop if {@code true}, align to the top; otherwise, align to the bottom.
   * @return the current instance of the facade for method chaining.
   */
  @Nonnull
  @CanIgnoreReturnValue
  public ElementGuard scrollIntoView(final boolean alignToTop) {
    return guard(element.scrollIntoView(alignToTop));
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
   * @return the current instance of {@link ElementGuard}
   */
  @Nonnull
  @CanIgnoreReturnValue
  public ElementGuard scrollIntoView(final String scrollIntoViewOptions) {
    return guard(element.scrollIntoView(scrollIntoViewOptions));
  }

  /**
   * Scrolls the browser view to the element.
   *
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard scrollTo() {
    return guard(element.scrollTo());
  }

  /**
   * Select an option from dropdown list (by index)
   *
   * @param index        0..N (0 means first option)
   * @param otherIndexes other indexes (if you need to select multiple options)
   * @return the current instance of {@link ElementGuard}
   */
  public ElementGuard selectOption(final int index, final int... otherIndexes) {
    element.selectOption(index, otherIndexes);

    return this;
  }

  /***
   * Select an option from dropdown list (by text)
   * @param text visible text of option
   * @param otherText other texts (if you need to select multiple options)
   * @return the current instance of {@link ElementGuard}
   */
  public ElementGuard selectOption(final String text, final String... otherText) {
    element.selectOption(text, otherText);

    return this;
  }

  /**
   * Selects an option from a dropdown list by its value.
   *
   * @param value       the value of the option to select
   * @param otherValues additional values of options to select
   * @return the current instance of {@link ElementGuard}
   */
  public ElementGuard selectOptionByValue(final String value, final String... otherValues) {
    element.selectOptionByValue(value, otherValues);

    return this;
  }

  /**
   * Selects an option from a dropdown list that contains the specified text.
   * This method will select options matching any of the provided texts.
   *
   * @param text       the text to match in the options
   * @param otherTexts additional texts to match in the options
   * @return the updated instance of the facade for chaining
   */
  public ElementGuard selectOptionContainingText(final String text, final String... otherTexts) {
    element.selectOptionContainingText(text, otherTexts);

    return this;
  }

  /**
   * Selects the radio button with the specified value.
   *
   * @param value the value of the radio button to select
   * @return the current instance of {@link ElementGuard}
   */
  @Nonnull
  @CanIgnoreReturnValue
  public ElementGuard selectRadio(final String value) {
    return guard(element.selectRadio(value));
  }

  /**
   * Sets the checkbox state to checked or unchecked.
   *
   * @param selected true to check the checkbox, false to uncheck it
   * @return the current instance of {@link ElementGuard}
   */
  @Nonnull
  @CanIgnoreReturnValue
  public ElementGuard setSelected(final boolean selected) {
    return guard(element.setSelected(selected));
  }

  /**
   * Set the value of an input element using the provided options.
   *
   * @param options options for setting the value.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard setValue(final SetValueOptions options) {
    return guard(element.setValue(options));
  }

  /**
   * Set the value of an input element.
   *
   * @param text the value to set.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard setValue(@Nullable final String text) {
    return guard(element.setValue(text));
  }

  /**
   * Ensures that the element meets all the specified conditions.
   *
   * @param conditions the conditions that the element should meet
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard should(final WebElementCondition... conditions) {
    return guard(element.should(conditions));
  }

  /**
   * Check that this element meets the specified condition within the given timeout.
   *
   * @param condition the condition to check.
   * @param timeout   the timeout duration.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard should(final WebElementCondition condition, final Duration timeout) {
    return guard(element.should(condition, timeout));
  }

  /**
   * Ensures that the element meets all the specified conditions.
   *
   * @param conditions the conditions that the element should meet
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldBe(final WebElementCondition... conditions) {
    return guard(element.shouldBe(conditions));
  }

  /**
   * Ensures that the element meets the specified condition within the given timeout.
   *
   * @param condition the condition that the element should meet
   * @param timeout   the timeout duration
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldBe(final WebElementCondition condition, final Duration timeout) {
    return guard(element.shouldBe(condition, timeout));
  }

  /**
   * Ensures that the element meets all the specified conditions.
   *
   * @param conditions the conditions that the element should meet
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldHave(final WebElementCondition... conditions) {
    return guard(element.shouldHave(conditions));
  }

  /**
   * Asserts that the element meets the specified condition within the given duration.
   * This method waits until the element meets the condition.
   *
   * @param condition the condition to check
   * @param timeout   the timeout duration for checking the condition
   * @return the updated instance of the facade for chaining
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldHave(final WebElementCondition condition, final Duration timeout) {
    return guard(element.shouldHave(condition, timeout));
  }

  /**
   * Asserts that the element does not meet any of the specified conditions.
   * This method waits until the element does not meet any of the conditions.
   *
   * @param conditions the conditions that the element should not meet
   * @return the updated instance of the facade for chaining
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldNot(final WebElementCondition... conditions) {
    return guard(element.shouldNot(conditions));
  }

  /**
   * Check that this element doesn't meet the specified condition within the given timeout.
   *
   * @param condition the condition that the element should meet
   * @param timeout   the timeout duration
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldNot(final WebElementCondition condition, final Duration timeout) {
    return guard(element.shouldNot(condition, timeout));
  }

  /**
   * Check that this element does not meet any of the specified conditions.
   *
   * @param conditions the conditions to check.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldNotBe(final WebElementCondition... conditions) {
    return guard(element.shouldNotBe(conditions));
  }

  /**
   * Check that this element does not meet the specified condition within the given timeout.
   *
   * @param condition the condition to check.
   * @param timeout   the timeout duration.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldNotBe(final WebElementCondition condition, final Duration timeout) {
    return guard(element.shouldNotBe(condition, timeout));
  }

  /**
   * Ensures that the element does not have the specified conditions.
   *
   * @param conditions the conditions that the element should not have
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldNotHave(final WebElementCondition... conditions) {
    return guard(element.shouldNotHave(conditions));
  }

  /**
   * Ensures that the element does not have the specified condition within the given timeout.
   *
   * @param condition the condition that the element should not have
   * @param timeout   the timeout duration
   * @return the current instance of {@link ElementGuard}
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard shouldNotHave(final WebElementCondition condition, final Duration timeout) {
    return guard(element.shouldNotHave(condition, timeout));
  }

  /**
   * Short form of {@link ElementGuard#getText()}.
   *
   * @return the visible text of the element
   * @see ElementGuard#getText()
   */
  @Nonnull
  @CheckReturnValue
  public String text() {
    return element.text();
  }

  /**
   * Mimic how a real user would type in a text field.
   * Useful for working with autosuggestion dropdowns.
   *
   * @param text the text to type.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard type(final CharSequence text) {
    return guard(element.type(text));
  }

  /**
   * Mimic typing in a text field using the provided options.
   *
   * @param options options for typing.
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard type(final TypeOptions options) {
    return guard(element.type(options));
  }

  /**
   * Remove focus from this element.
   *
   * @return the current instance of the facade for method chaining.
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard unfocus() {
    return guard(element.unfocus());
  }

  /**
   * Uploads one or more files into a file upload field.
   *
   * @param files the files to upload
   * @return the uploaded file
   */
  @CanIgnoreReturnValue
  @Nonnull
  public File uploadFile(final File... files) {
    return element.uploadFile(files);
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
  public File uploadFromClasspath(final String... fileName) {
    return element.uploadFromClasspath(fileName);
  }

  /**
   * Get the value attribute of this element.
   *
   * @return the value attribute, or {@code null} if not set.
   */
  @Nullable
  @CheckReturnValue
  public String val() {
    return element.val();
  }

  /**
   * Same as {@link ElementGuard#setValue(SetValueOptions)}
   *
   * @param options options for setting the value.
   * @return the current instance of the facade for method chaining.
   * @see ElementGuard#setValue(SetValueOptions)
   */
  @CanIgnoreReturnValue
  @Nonnull
  public ElementGuard val(final SetValueOptions options) {
    return guard(element.val(options));
  }

  /**
   * Same as {@link ElementGuard#setValue(String)}.
   *
   * @param text the value to set.
   * @return the current instance of the facade for method chaining.
   * @see ElementGuard#setValue(String)
   */
  @Nonnull
  @CanIgnoreReturnValue
  public ElementGuard val(@Nullable final String text) {
    return guard(element.val(text));
  }

  /**
   * Provides coordinates of an element for advanced interactions.
   *
   * @return the coordinates of an element for advanced interactions.
   */
  public Coordinates getCoordinates() {
    return element.getCoordinates();
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
    return element.getScreenshotAs(target);
  }

  /**
   * Get the accessible name of this element.
   *
   * @return the accessible name of the element.
   */
  public String getAccessibleName() {
    return element.getAccessibleName();
  }

  /**
   * Gets result of computing the WAI-ARIA role of element.
   *
   * @return the WAI-ARIA role of the element.
   */
  public String getAriaRole() {
    return element.getAriaRole();
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
  public String getDomAttribute(final String name) {
    return element.getDomAttribute(name);
  }

  /**
   * Retrieves the value of the specified DOM property of the element.
   *
   * @param name the name of the property
   * @return the value of the property
   */
  public String getDomProperty(final String name) {
    return element.getDomProperty(name);
  }

  /**
   * Retrieves the location of the element on the page.
   *
   * @return a {@link Point} representing the location of the element
   */
  public Point getLocation() {
    return element.getLocation();
  }

  /**
   * The location and size of the rendered element
   *
   * @return the rectangle representing the element's position and size.
   */
  public Rectangle getRect() {
    return element.getRect();
  }

  /**
   * Retrieves a representation of an element's shadow root for accessing the shadow DOM of a web component.
   *
   * @return a representation of an element's shadow root for accessing the shadow DOM of a web component.
   */
  public SearchContext getShadowRoot() {
    return element.getShadowRoot();
  }

  /**
   * Get the size of this element.
   *
   * @return the size of the element.
   */
  public Dimension getSize() {
    return element.getSize();
  }

  /**
   * Retrieves the tag name of the element.
   *
   * @return the tag name of the element
   */
  public String getTagName() {
    return element.getTagName();
  }

  /**
   * Check if this element is enabled.
   *
   * @return {@code true} if the element is enabled, otherwise {@code false}.
   */
  public boolean isEnabled() {
    return element.isEnabled();
  }

  /**
   * Check if this element is selected.
   *
   * @return {@code true} if the element is selected, otherwise {@code false}.
   */
  public boolean isSelected() {
    return element.isSelected();
  }

  /**
   * Send keystrokes to this element.
   *
   * @param keysToSend the keys to send.
   * @return the current instance of the facade for method chaining.
   */
  public ElementGuard sendKeys(final CharSequence... keysToSend) {
    element.sendKeys(keysToSend);

    return this;
  }

  /**
   * If this current element is a form, or an element within a form, then this will
   * be submitted to the remote server. If this causes the current page to change, then
   * this method will block until the new page is loaded.
   */
  public void submit() {
    element.submit();
  }

  @Nonnull
  @CheckReturnValue
  @Override
  public String toString() {
    return new StringJoiner(", ", ElementGuard.class.getSimpleName() + "[", "]")
        .add("element=" + element.describe())
        .toString();
  }
}
