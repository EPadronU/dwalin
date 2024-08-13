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
 * Describes a reusable interface component that can appear multiple times across one or more pages or components.
 * </p>
 *
 * @param <P> the type of parent to which this component is associated with
 */
public sealed abstract class Component<P extends AbstractionLayer>
    implements AbstractionLayer
    permits GuardedComponent, ElementComponent {

  /**
   * <p>
   * Error message displayed when attempting to create a component with a null parent.
   * </p>
   */
  public static final String PARENT_CANNOT_BE_NULL_MESSAGE = "The parent cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to create a component with a null root element.
   * </p>
   */
  public static final String ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE = "The root element cannot be null";

  private final P parent;

  private final SelenideElement rootElement;

  /**
   * <p>
   * Constructs a new component associated with the specified parent, using the provided element
   * as the root search context.
   * </p>
   *
   * @param parent      the parent to which this component is associated
   * @param rootElement the element used as the root search context for this component
   * @throws NullPointerException if {@code parent} or {@code rootElement} is {@code null}
   */
  public Component(final P parent, final SelenideElement rootElement) {
    this.parent = requireNonNull(parent, PARENT_CANNOT_BE_NULL_MESSAGE);

    this.rootElement = requireNonNull(rootElement, ROOT_ELEMENT_CANNOT_BE_NULL_MESSAGE);
  }

  /**
   * <p>
   * Retrieves the parent to which this component is associated with.
   * </p>
   * <p>
   * It's like ascending in the composition tree.
   * </p>
   *
   * @return the parent associated with this component
   */
  @CheckReturnValue
  @Nonnull
  public P ascend() {
    return parent;
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
   * Creates a new nested component associated with this components as its parent.
   * </p>
   *
   * @param element the root element for the new component
   * @param factory a function to create the new component, typically a constructor
   * @param <Self>  the type of this component
   * @param <C>     the type of component to be created
   * @return a new instance of the specified component type
   */
  @CheckReturnValue
  @Nonnull
  public <Self extends Component<P>, C extends Component<Self>> C asNestedComponent(
      final SelenideElement element, final BiFunction<Self, SelenideElement, C> factory) {
    return asComponent(element, factory);
  }

  /**
   * <p>
   * Creates new nested components for each element in the provided collection, all associated with this
   * components as their parent.
   * </p>
   *
   * @param elements a collection of elements for which new components will be created
   * @param factory  a function to create the new components, typically a constructor
   * @param <Self>   the type of this component
   * @param <C>      the type of component to be created
   * @return an unmodifiable list containing the newly created components
   */
  @CheckReturnValue
  @Nonnull
  public <Self extends Component<P>, C extends Component<Self>> List<C> asNestedComponents(
      final ElementsCollection elements, final BiFunction<Self, SelenideElement, C> factory) {
    return asComponents(elements, factory);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
        .add("parent=" + parent.getClass().getSimpleName())
        .add("rootElement=" + rootElement.describe())
        .toString();
  }
}
