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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;

import static java.util.Objects.requireNonNull;
/* ************************************************************************************************/

/**
 * <p>
 * A {@code SelenideElement} facade for those of us that don't want to expose {@code WebElement}
 * instances or {@code WebElement}-like behavior in our pages' public API.
 * </p>
 *
 * @see SelenideElement
 */
public final class StrictFacade {

  private final SelenideElement element;

  /**
   * <p>
   * Error message to be shown when trying to create a component from a null element.
   * </p>
   */
  public static final String ELEMENT_CANNOT_BE_NULL_MESSAGE = "The element cannot be null";

  /**
   * <p>
   * Wraps the given element inside a new {@code StrictFacade} instance.
   * </p>
   *
   * @param element the element to be wrapped
   * @return a new {@code StrictFacade} with the given element wrapped inside
   */
  public static StrictFacade strict(final SelenideElement element) {
    return new StrictFacade(requireNonNull(element, ELEMENT_CANNOT_BE_NULL_MESSAGE));
  }

  private StrictFacade(final SelenideElement element) {
    this.element = element;
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade setValue(@Nullable final String s) {
    return strict(element.setValue(s));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade val(@Nullable final String s) {
    return strict(element.val(s));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade val(final SetValueOptions setValueOptions) {
    return strict(element.val(setValueOptions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade setValue(final SetValueOptions setValueOptions) {
    return strict(element.setValue(setValueOptions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade type(final CharSequence charSequence) {
    return strict(element.type(charSequence));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade type(final TypeOptions typeOptions) {
    return strict(element.type(typeOptions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade append(final String s) {
    return strict(element.append(s));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade paste() {
    return strict(element.paste());
  }

  @CanIgnoreReturnValue
  public StrictFacade clear() {
    element.clear();

    return this;
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade pressEnter() {
    return strict(element.pressEnter());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade pressTab() {
    return strict(element.pressTab());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade unfocus() {
    return strict(element.unfocus());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade pressEscape() {
    return strict(element.pressEscape());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade press(final CharSequence... charSequences) {
    return strict(element.press(charSequences));
  }

  @Nonnull
  @CheckReturnValue
  public String getText() {
    return element.getText();
  }

  @Nullable
  @CheckReturnValue
  public String getAlias() {
    return element.getAlias();
  }

  @Nonnull
  @CheckReturnValue
  public String text() {
    return element.text();
  }

  @Nonnull
  @CheckReturnValue
  public String getOwnText() {
    return element.getOwnText();
  }

  @Nonnull
  @CheckReturnValue
  public String innerText() {
    return element.innerText();
  }

  @Nonnull
  @CheckReturnValue
  public String innerHtml() {
    return element.innerHtml();
  }

  @Nullable
  @CheckReturnValue
  public String attr(final String s) {
    return element.attr(s);
  }

  @Nullable
  @CheckReturnValue
  public String name() {
    return element.name();
  }

  @Nullable
  @CheckReturnValue
  public String val() {
    return element.val();
  }

  @Nullable
  @CheckReturnValue
  public String getValue() {
    return element.getValue();
  }

  @Nonnull
  @CheckReturnValue
  public String pseudo(final String s, final String s1) {
    return element.pseudo(s, s1);
  }

  @Nonnull
  @CheckReturnValue
  public String pseudo(final String s) {
    return element.pseudo(s);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade selectRadio(final String s) {
    return strict(element.selectRadio(s));
  }

  @Nullable
  @CheckReturnValue
  public String data(final String s) {
    return element.data(s);
  }

  @CheckReturnValue
  @Nullable
  public String getAttribute(final String s) {
    return element.getAttribute(s);
  }

  @CheckReturnValue
  @Nonnull
  public String getCssValue(final String s) {
    return element.getCssValue(s);
  }

  @CheckReturnValue
  public boolean exists() {
    return element.exists();
  }

  @CheckReturnValue
  public boolean isDisplayed() {
    return element.isDisplayed();
  }

  @CheckReturnValue
  public boolean is(final WebElementCondition webElementCondition) {
    return element.is(webElementCondition);
  }

  public boolean is(final WebElementCondition webElementCondition, final Duration duration) {
    return element.is(webElementCondition, duration);
  }

  @CheckReturnValue
  public boolean has(final WebElementCondition webElementCondition) {
    return element.has(webElementCondition);
  }

  @CheckReturnValue
  public boolean has(final WebElementCondition webElementCondition, final Duration duration) {
    return element.has(webElementCondition, duration);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade setSelected(final boolean b) {
    return strict(element.setSelected(b));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade should(final WebElementCondition... webElementConditions) {
    return strict(element.should(webElementConditions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade should(final WebElementCondition webElementCondition, final Duration duration) {
    return strict(element.should(webElementCondition, duration));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldHave(final WebElementCondition... webElementConditions) {
    return strict(element.shouldHave(webElementConditions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldHave(final WebElementCondition webElementCondition, final Duration duration) {
    return strict(element.shouldHave(webElementCondition, duration));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldBe(final WebElementCondition... webElementConditions) {
    return strict(element.shouldBe(webElementConditions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldBe(final WebElementCondition webElementCondition, final Duration duration) {
    return strict(element.shouldBe(webElementCondition, duration));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldNot(final WebElementCondition... webElementConditions) {
    return strict(element.shouldNot(webElementConditions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldNot(final WebElementCondition webElementCondition, final Duration duration) {
    return strict(element.shouldNot(webElementCondition, duration));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldNotHave(final WebElementCondition... webElementConditions) {
    return strict(element.shouldNotHave(webElementConditions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldNotHave(final WebElementCondition webElementCondition, final Duration duration) {
    return strict(element.shouldNotHave(webElementCondition, duration));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldNotBe(final WebElementCondition... webElementConditions) {
    return strict(element.shouldNotBe(webElementConditions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade shouldNotBe(final WebElementCondition webElementCondition, final Duration duration) {
    return strict(element.shouldNotBe(webElementCondition, duration));
  }

  @Nonnull
  @CheckReturnValue
  public String describe() {
    return element.describe();
  }

  @Nonnull
  public StrictFacade highlight() {
    return strict(element.highlight());
  }

  @Nonnull
  public StrictFacade highlight(final HighlightOptions highlightOptions) {
    return strict(element.highlight(highlightOptions));
  }

  @Nonnull
  @CheckReturnValue
  public StrictFacade as(final String s) {
    return strict(element.as(s));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public File uploadFromClasspath(final String... strings) {
    return element.uploadFromClasspath(strings);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public File uploadFile(final File... files) {
    return element.uploadFile(files);
  }

  public StrictFacade selectOption(final int i, final int... ints) {
    element.selectOption(i, ints);

    return this;
  }

  public StrictFacade selectOption(final String s, final String... strings) {
    element.selectOption(s, strings);

    return this;
  }

  public StrictFacade selectOptionContainingText(final String s, final String... strings) {
    element.selectOptionContainingText(s, strings);

    return this;
  }

  public StrictFacade selectOptionByValue(final String s, final String... strings) {
    element.selectOptionByValue(s, strings);

    return this;
  }

  @Nonnull
  @CheckReturnValue
  public StrictFacade getSelectedOption() throws NoSuchElementException {
    return strict(element.getSelectedOption());
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection getSelectedOptions() {
    return element.getSelectedOptions();
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection getOptions() {
    return element.getOptions();
  }

  @Nullable
  @CheckReturnValue
  public String getSelectedOptionValue() {
    return element.getSelectedOptionValue();
  }

  @Nullable
  @CheckReturnValue
  public String getSelectedOptionText() {
    return element.getSelectedOptionText();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade scrollTo() {
    return strict(element.scrollTo());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade scrollIntoView(final boolean b) {
    return strict(element.scrollIntoView(b));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade scrollIntoView(final String s) {
    return strict(element.scrollIntoView(s));
  }

  @Nonnull
  @CheckReturnValue
  public File download() throws FileNotDownloadedError {
    return element.download();
  }

  @Nonnull
  @CheckReturnValue
  public File download(final long l) throws FileNotDownloadedError {
    return element.download(l);
  }

  @Nonnull
  @CheckReturnValue
  public File download(final FileFilter fileFilter) throws FileNotDownloadedError {
    return element.download(fileFilter);
  }

  @Nonnull
  @CheckReturnValue
  public File download(final long l, final FileFilter fileFilter) throws FileNotDownloadedError {
    return element.download(l, fileFilter);
  }

  @Nonnull
  @CheckReturnValue
  public File download(final DownloadOptions downloadOptions) throws FileNotDownloadedError {
    return element.download(downloadOptions);
  }

  @Nonnull
  @CheckReturnValue
  public String getSearchCriteria() {
    return element.getSearchCriteria();
  }

  @Nonnull
  @CheckReturnValue
  public StrictFacade cached() {
    return strict(element.cached());
  }

  public StrictFacade click(final ClickOptions clickOptions) {
    return strict(element.click(clickOptions));
  }

  public StrictFacade click() {
    element.click();

    return this;
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade contextClick() {
    return strict(element.contextClick());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade doubleClick() {
    return strict(element.doubleClick());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade doubleClick(final ClickOptions clickOptions) {
    return strict(element.doubleClick(clickOptions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade hover() {
    return strict(element.hover());
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade hover(final HoverOptions hoverOptions) {
    return strict(element.hover(hoverOptions));
  }

  @CanIgnoreReturnValue
  @Nonnull
  public StrictFacade dragAndDrop(final DragAndDropOptions dragAndDropOptions) {
    return strict(element.dragAndDrop(dragAndDropOptions));
  }

  public <ReturnType> ReturnType execute(final Command<ReturnType> command) {
    return element.execute(command);
  }

  public <ReturnType> ReturnType execute(final Command<ReturnType> command, final Duration duration) {
    return element.execute(command, duration);
  }

  public boolean isImage() {
    return element.isImage();
  }

  @Nullable
  @CheckReturnValue
  public File screenshot() {
    return element.screenshot();
  }

  @Nullable
  @CheckReturnValue
  public BufferedImage screenshotAsImage() {
    return element.screenshotAsImage();
  }

  public void submit() {
    element.submit();
  }

  public WebDriver getWrappedDriver() {
    return element.getWrappedDriver();
  }

  public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
    return element.getScreenshotAs(target);
  }

  public Rectangle getRect() {
    return element.getRect();
  }

  public Dimension getSize() {
    return element.getSize();
  }

  public Point getLocation() {
    return element.getLocation();
  }

  public SearchContext getShadowRoot() {
    return element.getShadowRoot();
  }

  public boolean isEnabled() {
    return element.isEnabled();
  }

  public boolean isSelected() {
    return element.isSelected();
  }

  public String getAccessibleName() {
    return element.getAccessibleName();
  }

  public String getAriaRole() {
    return element.getAriaRole();
  }

  public String getDomAttribute(final String name) {
    return element.getDomAttribute(name);
  }

  public String getTagName() {
    return element.getTagName();
  }

  public String getDomProperty(final String name) {
    return element.getDomProperty(name);
  }

  public StrictFacade sendKeys(final CharSequence... keysToSend) {
    element.sendKeys(keysToSend);

    return this;
  }

  @Override
  @Nonnull
  @CheckReturnValue
  public String toString() {
    return element.toString();
  }
}
