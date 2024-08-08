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
import org.openqa.selenium.NoSuchElementException;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;
/* ************************************************************************************************/

/**
 * <p>
 * Models a re-usable interface component that can be present multiple times in one or more pages.
 * </p>
 *
 * @param <P> the type of page this component will be linked to
 */
public non-sealed abstract class Component<P extends Page> implements AbstractionLayer, SelenideElement {

  /**
   * <p>
   * Error message to be shown when trying to create a component with a null page.
   * </p>
   */
  public static final String PAGE_CANNOT_BE_NULL_MESSAGE = "The page cannot be null";

  /**
   * <p>
   * Error message to be shown when trying to create a component with a null root element.
   * </p>
   */
  public static final String ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE = "The root element cannot be null";

  private final P page;

  private final SelenideElement rootElement;

  /**
   * <p>
   * Creates a new component linked to the given page and using the provided element as the root search context.
   * </p>
   *
   * @param page        the page this component will be linked to
   * @param rootElement the element used as the root search in the context of this component
   */
  public Component(final P page, final SelenideElement rootElement) {
    this.page = requireNonNull(page, PAGE_CANNOT_BE_NULL_MESSAGE);

    this.rootElement = requireNonNull(rootElement, ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE);
  }

  /**
   * <p>
   * Provides the element used as the root search in the context of this component.
   * </p>
   *
   * @return the element used as the root search in the context of this component
   */
  @CheckReturnValue
  @Nonnull
  protected SelenideElement rootElement() {
    return rootElement;
  }

  /**
   * <p>
   * Provides the page this component is linked to.
   * </p>
   *
   * @return the page this component is linked to
   */
  @CheckReturnValue
  @Nonnull
  public P linkedPage() {
    return page;
  }

  /**
   * <p>
   * Creates a new component linked to the page this component is linked to.
   * </p>
   *
   * @param element          the root element for this component
   * @param componentFactory method capable of creating the new component (usually a constructor)
   * @param <C>              the type of component to be created
   * @return a new instant of the desired component
   */
  @CheckReturnValue
  @Nonnull
  public <C extends Component<P>> C asComponent(
      final SelenideElement element, final BiFunction<P, SelenideElement, C> componentFactory) {
    return page.asComponent(element, componentFactory);
  }

  /**
   * <p>
   * Creates new components for all elements contained in the collection and that will be linked
   * to the same page this component is linked to.
   * </p>
   *
   * @param elements         the elements for which new components will be created
   * @param componentFactory method capable of creating the new components (usually a constructor)
   * @param <C>              the type of component to be created
   * @return an unmodifiable list with the new components
   */
  @CheckReturnValue
  @Nonnull
  public <C extends Component<P>> List<C> asComponents(
      final ElementsCollection elements, final BiFunction<P, SelenideElement, C> componentFactory) {
    return page.asComponents(elements, componentFactory);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement setValue(@Nullable final String s) {
    return rootElement.setValue(s);
  }

  @Nullable
  @CheckReturnValue
  public String val() {
    return rootElement.val();
  }

  @Nonnull
  public SelenideElement highlight() {
    return rootElement.highlight();
  }

  @CheckReturnValue
  @Nonnull
  public String getCssValue(final String s) {
    return rootElement.getCssValue(s);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldNot(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.shouldNot(webElementCondition, duration);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement type(final TypeOptions typeOptions) {
    return rootElement.type(typeOptions);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement should(final WebElementCondition... webElementConditions) {
    return rootElement.should(webElementConditions);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement scrollIntoView(final boolean b) {
    return rootElement.scrollIntoView(b);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement hover() {
    return rootElement.hover();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldHave(final WebElementCondition... webElementConditions) {
    return rootElement.shouldHave(webElementConditions);
  }

  public WebElement findElement(final By by) {
    return rootElement.findElement(by);
  }

  public WebDriver getWrappedDriver() {
    return rootElement.getWrappedDriver();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement doubleClick() {
    return rootElement.doubleClick();
  }

  public void selectOption(final String s, final String... strings) {
    rootElement.selectOption(s, strings);
  }

  @Nullable
  @CheckReturnValue
  public BufferedImage screenshotAsImage() {
    return rootElement.screenshotAsImage();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement val(@Nullable final String s) {
    return rootElement.val(s);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement find(final String s, final int i) {
    return rootElement.find(s, i);
  }

  public Point getLocation() {
    return rootElement.getLocation();
  }

  @Nonnull
  @CheckReturnValue
  public String getOwnText() {
    return rootElement.getOwnText();
  }

  @Nullable
  @CheckReturnValue
  public String getSelectedOptionValue() {
    return rootElement.getSelectedOptionValue();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldNotHave(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.shouldNotHave(webElementCondition, duration);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement lastChild() {
    return rootElement.lastChild();
  }

  public String getAriaRole() {
    return rootElement.getAriaRole();
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection $$(final By by) {
    return rootElement.$$(by);
  }

  public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
    return rootElement.getScreenshotAs(target);
  }

  @CheckReturnValue
  @Nullable
  public String getAttribute(final String s) {
    return rootElement.getAttribute(s);
  }

  @Nonnull
  @CheckReturnValue
  public WebElement getWrappedElement() {
    return rootElement.getWrappedElement();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement dragAndDrop(final DragAndDropOptions dragAndDropOptions) {
    return rootElement.dragAndDrop(dragAndDropOptions);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldNotBe(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.shouldNotBe(webElementCondition, duration);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldBe(final WebElementCondition... webElementConditions) {
    return rootElement.shouldBe(webElementConditions);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement ancestor(final String s, final int i) {
    return rootElement.ancestor(s, i);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement $(final String s, final int i) {
    return rootElement.$(s, i);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement pressEscape() {
    return rootElement.pressEscape();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldNot(final WebElementCondition... webElementConditions) {
    return rootElement.shouldNot(webElementConditions);
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection $$(final String s) {
    return rootElement.$$(s);
  }

  public Coordinates getCoordinates() {
    return rootElement.getCoordinates();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement type(final CharSequence charSequence) {
    return rootElement.type(charSequence);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement setSelected(final boolean b) {
    return rootElement.setSelected(b);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement hover(final HoverOptions hoverOptions) {
    return rootElement.hover(hoverOptions);
  }

  public String getAccessibleName() {
    return rootElement.getAccessibleName();
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement find(final By by, final int i) {
    return rootElement.find(by, i);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldNotHave(final WebElementCondition... webElementConditions) {
    return rootElement.shouldNotHave(webElementConditions);
  }

  public void selectOption(final int i, final int... ints) {
    rootElement.selectOption(i, ints);
  }

  @Nonnull
  @CheckReturnValue
  public String getText() {
    return rootElement.getText();
  }

  @Nullable
  @CheckReturnValue
  public String getValue() {
    return rootElement.getValue();
  }

  public List<WebElement> findElements(final By by) {
    return rootElement.findElements(by);
  }

  @CheckReturnValue
  public boolean isDisplayed() {
    return rootElement.isDisplayed();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement doubleClick(final ClickOptions clickOptions) {
    return rootElement.doubleClick(clickOptions);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldNotBe(final WebElementCondition... webElementConditions) {
    return rootElement.shouldNotBe(webElementConditions);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement pressEnter() {
    return rootElement.pressEnter();
  }

  @Nonnull
  @CheckReturnValue
  public String innerText() {
    return rootElement.innerText();
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement parent() {
    return rootElement.parent();
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement find(final String s) {
    return rootElement.find(s);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement $x(final String s, final int i) {
    return rootElement.$x(s, i);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public File uploadFile(final File... files) {
    return rootElement.uploadFile(files);
  }

  @Nonnull
  @CheckReturnValue
  public File download(final long l, final FileFilter fileFilter) throws FileNotDownloadedError {
    return rootElement.download(l, fileFilter);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement setValue(final SetValueOptions setValueOptions) {
    return rootElement.setValue(setValueOptions);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement find(final By by) {
    return rootElement.find(by);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement $(final By by, final int i) {
    return rootElement.$(by, i);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement getSelectedOption() throws NoSuchElementException {
    return rootElement.getSelectedOption();
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement preceding(final int i) {
    return rootElement.preceding(i);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement cached() {
    return rootElement.cached();
  }

  public Dimension getSize() {
    return rootElement.getSize();
  }

  @Nullable
  @CheckReturnValue
  public String data(final String s) {
    return rootElement.data(s);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement press(final CharSequence... charSequences) {
    return rootElement.press(charSequences);
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection findAll(final By by) {
    return rootElement.findAll(by);
  }

  @Nonnull
  @CheckReturnValue
  public String getSearchCriteria() {
    return rootElement.getSearchCriteria();
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement ancestor(final String s) {
    return rootElement.ancestor(s);
  }

  public boolean isSelected() {
    return rootElement.isSelected();
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement $(final By by) {
    return rootElement.$(by);
  }

  @Nullable
  @CheckReturnValue
  public String getAlias() {
    return rootElement.getAlias();
  }

  @Nonnull
  @CheckReturnValue
  public String pseudo(final String s, final String s1) {
    return rootElement.pseudo(s, s1);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement as(final String s) {
    return rootElement.as(s);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement closest(final String s) {
    return rootElement.closest(s);
  }

  @Nullable
  @CheckReturnValue
  public String getSelectedOptionText() {
    return rootElement.getSelectedOptionText();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement paste() {
    return rootElement.paste();
  }

  @Nonnull
  public SelenideElement highlight(final HighlightOptions highlightOptions) {
    return rootElement.highlight(highlightOptions);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement $(final String s) {
    return rootElement.$(s);
  }

  public void submit() {
    rootElement.submit();
  }

  public <ReturnType> ReturnType execute(final Command<ReturnType> command, final Duration duration) {
    return rootElement.execute(command, duration);
  }

  @CheckReturnValue
  public boolean has(final WebElementCondition webElementCondition) {
    return rootElement.has(webElementCondition);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement sibling(final int i) {
    return rootElement.sibling(i);
  }

  @Nonnull
  @CheckReturnValue
  public SelenideElement $x(final String s) {
    return rootElement.$x(s);
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection getSelectedOptions() {
    return rootElement.getSelectedOptions();
  }

  public String getDomAttribute(final String name) {
    return rootElement.getDomAttribute(name);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public File uploadFromClasspath(final String... strings) {
    return rootElement.uploadFromClasspath(strings);
  }

  @Nonnull
  @CheckReturnValue
  public File download(final FileFilter fileFilter) throws FileNotDownloadedError {
    return rootElement.download(fileFilter);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement selectRadio(final String s) {
    return rootElement.selectRadio(s);
  }

  public SearchContext getShadowRoot() {
    return rootElement.getShadowRoot();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement contextClick() {
    return rootElement.contextClick();
  }

  public String getTagName() {
    return rootElement.getTagName();
  }

  public void selectOptionByValue(final String s, final String... strings) {
    rootElement.selectOptionByValue(s, strings);
  }

  @Nonnull
  @CheckReturnValue
  public File download() throws FileNotDownloadedError {
    return rootElement.download();
  }

  public Rectangle getRect() {
    return rootElement.getRect();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement val(final SetValueOptions setValueOptions) {
    return rootElement.val(setValueOptions);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement pressTab() {
    return rootElement.pressTab();
  }

  @CheckReturnValue
  public boolean has(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.has(webElementCondition, duration);
  }

  public SelenideElement click(final ClickOptions clickOptions) {
    return rootElement.click(clickOptions);
  }

  @Nonnull
  @CheckReturnValue
  public String innerHtml() {
    return rootElement.innerHtml();
  }

  public boolean isEnabled() {
    return rootElement.isEnabled();
  }

  @Nonnull
  @CheckReturnValue
  public String describe() {
    return rootElement.describe();
  }

  public void selectOptionContainingText(final String s, final String... strings) {
    rootElement.selectOptionContainingText(s, strings);
  }

  @Nullable
  @CheckReturnValue
  public String name() {
    return rootElement.name();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement scrollIntoView(final String s) {
    return rootElement.scrollIntoView(s);
  }

  @Nonnull
  @CheckReturnValue
  public WebElement toWebElement() {
    return rootElement.toWebElement();
  }

  @Nullable
  @CheckReturnValue
  public File screenshot() {
    return rootElement.screenshot();
  }

  @Nonnull
  @CheckReturnValue
  public String text() {
    return rootElement.text();
  }

  @CheckReturnValue
  public boolean is(final WebElementCondition webElementCondition) {
    return rootElement.is(webElementCondition);
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection findAll(final String s) {
    return rootElement.findAll(s);
  }

  @CheckReturnValue
  public boolean exists() {
    return rootElement.exists();
  }

  public void sendKeys(final CharSequence... keysToSend) {
    rootElement.sendKeys(keysToSend);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement append(final String s) {
    return rootElement.append(s);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement should(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.should(webElementCondition, duration);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement scrollTo() {
    return rootElement.scrollTo();
  }

  @Nonnull
  @CheckReturnValue
  public File download(final DownloadOptions downloadOptions) throws FileNotDownloadedError {
    return rootElement.download(downloadOptions);
  }

  public void click() {
    rootElement.click();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldHave(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.shouldHave(webElementCondition, duration);
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection getOptions() {
    return rootElement.getOptions();
  }

  public boolean isImage() {
    return rootElement.isImage();
  }

  public String getDomProperty(final String name) {
    return rootElement.getDomProperty(name);
  }

  @Nonnull
  @CheckReturnValue
  public String pseudo(final String s) {
    return rootElement.pseudo(s);
  }

  public boolean is(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.is(webElementCondition, duration);
  }

  @CanIgnoreReturnValue
  public void clear() {
    rootElement.clear();
  }

  @Nullable
  @CheckReturnValue
  public String attr(final String s) {
    return rootElement.attr(s);
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement unfocus() {
    return rootElement.unfocus();
  }

  @CanIgnoreReturnValue
  @Nonnull
  public SelenideElement shouldBe(final WebElementCondition webElementCondition, final Duration duration) {
    return rootElement.shouldBe(webElementCondition, duration);
  }

  @Nonnull
  @CheckReturnValue
  public ElementsCollection $$x(final String s) {
    return rootElement.$$x(s);
  }

  @Nonnull
  @CheckReturnValue
  public File download(final long l) throws FileNotDownloadedError {
    return rootElement.download(l);
  }

  public <ReturnType> ReturnType execute(final Command<ReturnType> command) {
    return rootElement.execute(command);
  }

  @Override
  @Nonnull
  @CheckReturnValue
  public String toString() {
    return new StringJoiner(", ", Component.class.getSimpleName() + "[", "]")
        .add("page=" + page)
        .add("rootElement=" + rootElement)
        .toString();
  }
}
