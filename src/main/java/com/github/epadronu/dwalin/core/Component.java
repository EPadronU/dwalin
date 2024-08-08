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
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
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
public sealed abstract class Component<P extends Page> implements AbstractionLayer permits StrictComponent, ElementComponent {

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
