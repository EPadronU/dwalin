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
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
/* ************************************************************************************************/

/**
 * <p>
 * Acts as the foundational layer for the {@code Component},and {@code Page} types, encapsulating
 * their shared API.
 * </p>
 * <p>
 * Implementing this interface ensures that types inherit the common behavior defined here,
 * providing a unified structure and interaction model across various components of the library.
 * </p>
 *
 * @see Component
 * @see Page
 */
sealed interface AbstractionLayer permits Component, Page {

  /**
   * <p>
   * Navigates to the specified page.
   * </p>
   *
   * @param pageObjectClass a {@code Class<P>} representing the type of the page to navigate to
   * @param <P>             the type of the page to navigate to
   * @return a new instance of the specified page
   */
  @CheckReturnValue
  @Nonnull
  default <P extends NavigablePage> P navigateTo(final Class<P> pageObjectClass) {
    return Dwalin.navigateTo(pageObjectClass);
  }

  /**
   * <p>
   * Navigates to the specified page.
   * </p>
   *
   * @param reified a {@code P[]} representing the type of the page to navigate to, using a reified generic. No values should be passed here.
   * @param <P>     the type of the page to navigate to
   * @return a new instance of the specified page
   */
  @CheckReturnValue
  @Nonnull
  @SuppressWarnings("unchecked")
  default <P extends NavigablePage> P navigateTo(final P... reified) {
    return Dwalin.navigateTo(reified);
  }
}
