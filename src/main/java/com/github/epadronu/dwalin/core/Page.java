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
/* ************************************************************************************************/

/**
 * <p>
 * Models a web page, being an implementation of a page class under the Page Object Model pattern.
 * </p>
 *
 * <p>
 * This interface exposed an API for linking pages and components.
 * </p>
 */
public non-sealed interface Page extends AbstractionLayer {

  /**
   * <p>
   * Creates a new component linked to this page.
   * </p>
   *
   * @param element          the root element for this component
   * @param componentFactory method capable of creating the new component (usually a constructor)
   * @param <P>              the type of page the component will be linked to
   * @param <C>              the type of component to be created
   * @return a new instant of the desired component
   */
  @CheckReturnValue
  @Nonnull
  default <P extends Page, C extends Component<P>> C asComponent(
      final SelenideElement element, final BiFunction<P, SelenideElement, C> componentFactory) {
    return componentFactory.apply((P) this, element);
  }

  /**
   * <p>
   * Creates new components for all elements contained in the collection and that will be linked to this page.
   * </p>
   *
   * @param elements         the elements for which new components will be created
   * @param componentFactory method capable of creating the new components (usually a constructor)
   * @param <P>              the type of page the components will be linked to
   * @param <C>              the type of component to be created
   * @return an unmodifiable list with the new components
   */
  @CheckReturnValue
  @Nonnull
  default <P extends Page, C extends Component<P>> List<C> asComponents(
      final ElementsCollection elements, final BiFunction<P, SelenideElement, C> componentFactory) {
    return StreamSupport.stream(elements.spliterator(), false)
        .map(element -> asComponent(element, componentFactory))
        .toList();
  }
}
