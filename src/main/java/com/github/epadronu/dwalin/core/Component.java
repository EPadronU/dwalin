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
 * Models a reusable interface component that can appear multiple times across one or more pages.
 * </p>
 *
 * @param <P> the type of page to which this component is associated
 */
public sealed abstract class Component<P extends Page> implements AbstractionLayer permits GuardedComponent, ElementComponent {

  /**
   * <p>
   * Error message displayed when attempting to create a component with a null page.
   * </p>
   */
  public static final String PAGE_CANNOT_BE_NULL_MESSAGE = "The page cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to create a component with a null root element.
   * </p>
   */
  public static final String ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE = "The root element cannot be null";

  private final P page;

  private final SelenideElement rootElement;

  /**
   * <p>
   * Constructs a new component linked to the specified page, using the provided element as the root search context.
   * </p>
   *
   * @param page        the page to which this component is associated
   * @param rootElement the element used as the root search context for this component
   * @throws NullPointerException if {@code page} or {@code rootElement} is {@code null}
   */
  public Component(final P page, final SelenideElement rootElement) {
    this.page = requireNonNull(page, PAGE_CANNOT_BE_NULL_MESSAGE);

    this.rootElement = requireNonNull(rootElement, ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE);
  }

  /**
   * <p>
   * Retrieves the element used as the root search context for this component.
   * </p>
   *
   * @return the root element used for this component
   */
  @CheckReturnValue
  @Nonnull
  protected SelenideElement rootElement() {
    return rootElement;
  }

  /**
   * <p>
   * Retrieves the page to which this component is associated.
   * </p>
   *
   * @return the page associated with this component
   */
  @CheckReturnValue
  @Nonnull
  public P linkedPage() {
    return page;
  }

  /**
   * <p>
   * Creates a new component linked to the same page as this component.
   * </p>
   *
   * @param element the root element for the new component
   * @param factory a function to create the new component, typically a constructor
   * @param <C>     the type of component to be created
   * @return a new instance of the specified component type
   */
  @CheckReturnValue
  @Nonnull
  public <C extends Component<P>> C asComponent(
      final SelenideElement element, final BiFunction<P, SelenideElement, C> factory) {
    return page.asComponent(element, factory);
  }

  /**
   * <p>
   * Creates new components for each element in the provided collection, all linked to the same page as this component.
   * </p>
   *
   * @param elements a collection of elements for which new components will be created
   * @param factory  a function to create the new components, typically a constructor
   * @param <C>      the type of component to be created
   * @return an unmodifiable list containing the newly created components
   */
  @CheckReturnValue
  @Nonnull
  public <C extends Component<P>> List<C> asComponents(
      final ElementsCollection elements, final BiFunction<P, SelenideElement, C> factory) {
    return page.asComponents(elements, factory);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Component.class.getSimpleName() + "[", "]")
        .add("page=" + page)
        .add("rootElement=" + rootElement)
        .toString();
  }
}
