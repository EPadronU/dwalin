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
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
/* ************************************************************************************************/

/**
 * <p>
 * Models a web page, serving as an implementation of a page class following the Page Object Model
 * pattern.
 * </p>
 * <p>
 * This interface provides an API for linking pages and components.
 * </p>
 */
public non-sealed interface Page extends AbstractionLayer {

  /**
   * <p>
   * Error message displayed when attempting to create a component from a null root element.
   * </p>
   */
  String ELEMENT_CANNOT_BE_NULL_MESSAGE = "The element cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to create a collection of components from a null collection of elements.
   * </p>
   */
  String ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE = "The element collection cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to create a component with a null factory.
   * </p>
   */
  String COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE = "The component factory cannot be null";

  /**
   * <p>
   * Creates a new component linked to this page.
   * </p>
   *
   * @param element the root element of the component
   * @param factory a function to create the new component, typically a constructor
   * @param <P>     the type of page this component will be linked to
   * @param <C>     the type of component to be created
   * @return a new instance of the desired component
   */
  @CheckReturnValue
  @Nonnull
  @SuppressWarnings("unchecked")
  default <P extends Page, C extends Component<P>> C asComponent(
      final SelenideElement element, final BiFunction<P, SelenideElement, C> factory) {
    requireNonNull(element, ELEMENT_CANNOT_BE_NULL_MESSAGE);
    requireNonNull(factory, COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE);

    return factory.apply((P) this, element);
  }

  /**
   * <p>
   * Creates new components for all elements in the specified collection and links them to this page.
   * </p>
   *
   * @param elements         the collection of elements for which new components will be created
   * @param componentFactory a function to create the new component, typically a constructor
   * @param <P>              the type of page to which the components will be linked
   * @param <C>              the type of components to be created
   * @return an unmodifiable list containing the newly created components
   */
  @CheckReturnValue
  @Nonnull
  default <P extends Page, C extends Component<P>> List<C> asComponents(
      final ElementsCollection elements, final BiFunction<P, SelenideElement, C> componentFactory) {
    requireNonNull(elements, ELEMENT_COLLECTION_CANNOT_BE_NULL_MESSAGE);
    requireNonNull(componentFactory, COMPONENT_FACTORY_CANNOT_BE_NULL_MESSAGE);

    return StreamSupport.stream(elements.spliterator(), false)
        .map(element -> asComponent(element, componentFactory))
        .toList();
  }
}
