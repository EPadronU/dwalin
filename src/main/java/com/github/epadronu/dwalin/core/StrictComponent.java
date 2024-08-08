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
import java.util.StringJoiner;
/* ************************************************************************************************/

/**
 * <p>
 * Strict version of a component which doesn't allow exposing {@code WebElement}
 * instances or {@code WebElement}-like behavior in pages' public API.
 * </p>
 *
 * @param <P> the type of page this component will be linked to
 * @see StrictFacade
 * @see ElementComponent
 */
public non-sealed abstract class StrictComponent<P extends Page> extends Component<P> {

  private final StrictFacade facade;

  /**
   * <p>
   * Creates a new component linked to the given page and using the provided element as the root search context.
   * </p>
   *
   * @param page        the page this component will be linked to
   * @param rootElement the element used as the root search in the context of this component
   */
  public StrictComponent(final P page, final SelenideElement rootElement) {
    super(page, rootElement);

    this.facade = StrictFacade.strict(rootElement);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade type(final CharSequence charSequence) {
    return facade.type(charSequence);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade setValue(@Nullable final String s) {
    return facade.setValue(s);
  }

  @CheckReturnValue
  @Nonnull
  public String getSearchCriteria() {
    return facade.getSearchCriteria();
  }

  @CheckReturnValue
  @Nonnull
  public File download(final DownloadOptions downloadOptions) throws FileNotDownloadedError {
    return facade.download(downloadOptions);
  }

  public StrictFacade sendKeys(final CharSequence... keysToSend) {
    return facade.sendKeys(keysToSend);
  }

  @CheckReturnValue
  @Nullable
  public String val() {
    return facade.val();
  }

  @CheckReturnValue
  @Nullable
  public String data(final String s) {
    return facade.data(s);
  }

  public StrictFacade click(final ClickOptions clickOptions) {
    return facade.click(clickOptions);
  }

  @CheckReturnValue
  @Nonnull
  public String pseudo(final String s, final String s1) {
    return facade.pseudo(s, s1);
  }

  @CheckReturnValue
  @Nonnull
  public String innerHtml() {
    return facade.innerHtml();
  }

  public WebDriver getWrappedDriver() {
    return facade.getWrappedDriver();
  }

  public Dimension getSize() {
    return facade.getSize();
  }

  public boolean is(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.is(webElementCondition, duration);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade hover() {
    return facade.hover();
  }

  @CheckReturnValue
  @Nullable
  public String name() {
    return facade.name();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade val(@Nullable final String s) {
    return facade.val(s);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade val(final SetValueOptions setValueOptions) {
    return facade.val(setValueOptions);
  }

  @CheckReturnValue
  @Nullable
  public String getAlias() {
    return facade.getAlias();
  }

  @CheckReturnValue
  @Nonnull
  public ElementsCollection getSelectedOptions() {
    return facade.getSelectedOptions();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldNotBe(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.shouldNotBe(webElementCondition, duration);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade setValue(final SetValueOptions setValueOptions) {
    return facade.setValue(setValueOptions);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade type(final TypeOptions typeOptions) {
    return facade.type(typeOptions);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade append(final String s) {
    return facade.append(s);
  }

  public String getAccessibleName() {
    return facade.getAccessibleName();
  }

  @CheckReturnValue
  @Nonnull
  public String pseudo(final String s) {
    return facade.pseudo(s);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade scrollIntoView(final boolean b) {
    return facade.scrollIntoView(b);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade paste() {
    return facade.paste();
  }

  @CheckReturnValue
  @Nonnull
  public String describe() {
    return facade.describe();
  }

  public boolean isSelected() {
    return facade.isSelected();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldNotBe(final WebElementCondition... webElementConditions) {
    return facade.shouldNotBe(webElementConditions);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade unfocus() {
    return facade.unfocus();
  }

  public Rectangle getRect() {
    return facade.getRect();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade should(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.should(webElementCondition, duration);
  }

  @CheckReturnValue
  @Nullable
  public String getSelectedOptionValue() {
    return facade.getSelectedOptionValue();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldNotHave(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.shouldNotHave(webElementCondition, duration);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldBe(final WebElementCondition... webElementConditions) {
    return facade.shouldBe(webElementConditions);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade selectRadio(final String s) {
    return facade.selectRadio(s);
  }

  @CanIgnoreReturnValue
  public StrictFacade clear() {
    return facade.clear();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade setSelected(final boolean b) {
    return facade.setSelected(b);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade contextClick() {
    return facade.contextClick();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade doubleClick(final ClickOptions clickOptions) {
    return facade.doubleClick(clickOptions);
  }

  public String getDomAttribute(final String name) {
    return facade.getDomAttribute(name);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldHave(final WebElementCondition... webElementConditions) {
    return facade.shouldHave(webElementConditions);
  }

  @Nonnull
  public StrictFacade highlight() {
    return facade.highlight();
  }

  @CheckReturnValue
  @Nonnull
  public ElementsCollection getOptions() {
    return facade.getOptions();
  }

  @Nonnull
  public StrictFacade highlight(final HighlightOptions highlightOptions) {
    return facade.highlight(highlightOptions);
  }

  @CheckReturnValue
  public boolean has(final WebElementCondition webElementCondition) {
    return facade.has(webElementCondition);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade scrollTo() {
    return facade.scrollTo();
  }

  public String getTagName() {
    return facade.getTagName();
  }

  public Point getLocation() {
    return facade.getLocation();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade press(final CharSequence... charSequences) {
    return facade.press(charSequences);
  }

  @CheckReturnValue
  public boolean isDisplayed() {
    return facade.isDisplayed();
  }

  public StrictFacade selectOptionByValue(final String s, final String... strings) {
    return facade.selectOptionByValue(s, strings);
  }

  @CheckReturnValue
  @Nonnull
  public File download(final long l) throws FileNotDownloadedError {
    return facade.download(l);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade should(final WebElementCondition... webElementConditions) {
    return facade.should(webElementConditions);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade pressEnter() {
    return facade.pressEnter();
  }

  public String getDomProperty(final String name) {
    return facade.getDomProperty(name);
  }

  @CheckReturnValue
  @Nonnull
  public File download() throws FileNotDownloadedError {
    return facade.download();
  }

  public boolean isImage() {
    return facade.isImage();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldBe(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.shouldBe(webElementCondition, duration);
  }

  @CheckReturnValue
  @Nonnull
  public File download(final long l, final FileFilter fileFilter) throws FileNotDownloadedError {
    return facade.download(l, fileFilter);
  }

  @CheckReturnValue
  @Nullable
  public BufferedImage screenshotAsImage() {
    return facade.screenshotAsImage();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade scrollIntoView(final String s) {
    return facade.scrollIntoView(s);
  }

  @CheckReturnValue
  @Nonnull
  public String getText() {
    return facade.getText();
  }

  @CheckReturnValue
  @Nullable
  public String getSelectedOptionText() {
    return facade.getSelectedOptionText();
  }

  @CheckReturnValue
  @Nonnull
  public StrictFacade getSelectedOption() throws NoSuchElementException {
    return facade.getSelectedOption();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldNot(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.shouldNot(webElementCondition, duration);
  }

  public StrictFacade selectOption(final int i, final int... ints) {
    return facade.selectOption(i, ints);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade dragAndDrop(final DragAndDropOptions dragAndDropOptions) {
    return facade.dragAndDrop(dragAndDropOptions);
  }

  public StrictFacade selectOption(final String s, final String... strings) {
    return facade.selectOption(s, strings);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldNotHave(final WebElementCondition... webElementConditions) {
    return facade.shouldNotHave(webElementConditions);
  }

  @CheckReturnValue
  @Nonnull
  public String innerText() {
    return facade.innerText();
  }

  public String getAriaRole() {
    return facade.getAriaRole();
  }

  @CheckReturnValue
  public boolean is(final WebElementCondition webElementCondition) {
    return facade.is(webElementCondition);
  }

  @CheckReturnValue
  @Nonnull
  public String text() {
    return facade.text();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade pressEscape() {
    return facade.pressEscape();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade pressTab() {
    return facade.pressTab();
  }

  public boolean isEnabled() {
    return facade.isEnabled();
  }

  @CheckReturnValue
  public boolean has(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.has(webElementCondition, duration);
  }

  public StrictFacade selectOptionContainingText(final String s, final String... strings) {
    return facade.selectOptionContainingText(s, strings);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public File uploadFromClasspath(final String... strings) {
    return facade.uploadFromClasspath(strings);
  }

  @CheckReturnValue
  @Nonnull
  public File download(final FileFilter fileFilter) throws FileNotDownloadedError {
    return facade.download(fileFilter);
  }

  public <ReturnType> ReturnType execute(final Command<ReturnType> command, final Duration duration) {
    return facade.execute(command, duration);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public File uploadFile(final File... files) {
    return facade.uploadFile(files);
  }

  @CheckReturnValue
  @Nonnull
  public StrictFacade as(final String s) {
    return facade.as(s);
  }

  @CheckReturnValue
  @Nullable
  public String getValue() {
    return facade.getValue();
  }

  public StrictFacade click() {
    return facade.click();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade hover(final HoverOptions hoverOptions) {
    return facade.hover(hoverOptions);
  }

  @CheckReturnValue
  @Nonnull
  public StrictFacade cached() {
    return facade.cached();
  }

  public SearchContext getShadowRoot() {
    return facade.getShadowRoot();
  }

  public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
    return facade.getScreenshotAs(target);
  }

  public <ReturnType> ReturnType execute(final Command<ReturnType> command) {
    return facade.execute(command);
  }

  @CheckReturnValue
  @Nullable
  public File screenshot() {
    return facade.screenshot();
  }

  @CheckReturnValue
  public boolean exists() {
    return facade.exists();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldNot(final WebElementCondition... webElementConditions) {
    return facade.shouldNot(webElementConditions);
  }

  @Nonnull
  @CheckReturnValue
  public String getCssValue(final String s) {
    return facade.getCssValue(s);
  }

  @CheckReturnValue
  @Nonnull
  public String getOwnText() {
    return facade.getOwnText();
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade shouldHave(final WebElementCondition webElementCondition, final Duration duration) {
    return facade.shouldHave(webElementCondition, duration);
  }

  @Nonnull
  @CanIgnoreReturnValue
  public StrictFacade doubleClick() {
    return facade.doubleClick();
  }

  @Nullable
  @CheckReturnValue
  public String getAttribute(final String s) {
    return facade.getAttribute(s);
  }

  public void submit() {
    facade.submit();
  }

  @CheckReturnValue
  @Nullable
  public String attr(final String s) {
    return facade.attr(s);
  }

  @Override
  @Nonnull
  @CheckReturnValue
  public String toString() {
    return new StringJoiner(", ", StrictComponent.class.getSimpleName() + "[", "]")
        .add("page=" + linkedPage())
        .add("rootElement=" + rootElement())
        .toString();
  }
}
