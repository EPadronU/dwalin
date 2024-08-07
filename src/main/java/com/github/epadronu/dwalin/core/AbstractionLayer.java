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
import com.codeborne.selenide.Selenide;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.page;
import static java.util.Objects.requireNonNull;
/* ************************************************************************************************/

/**
 * <p>
 * Serves as the foundation layer for the {@code Browser}, {@code Component}, and
 * {@code Page} types and encapsulates the common API shared among them.
 * </p>
 * <p>
 * Types implementing this interface will inherit the common behavior defined here,
 * providing a consistent structure and interaction model across different components of the library.
 * </p>
 *
 * @see Browser
 * @see Component
 * @see Page
 */
public sealed interface AbstractionLayer permits Browser, Component, Page {

  /**
   * <p>
   * Error message to be shown when trying to open a page which URL supplier is null.
   * </p>
   */
  String urlSupplierNotNullMessage = "The URL supplier cannot be null";

  /**
   * <p>
   * Error message to be shown when trying to open a page which "at verification" is null.
   * </p>
   */
  String atVerificationNotNullMessage = "The at verification cannot be null";

  /**
   * <p>
   * Navigate to the desired page.
   * </p>
   *
   * @param pageObjectClass a {@code Class<P>} providing the type of the page to navigate to
   * @param <P>             the type of the page to navigate to
   * @return a new instance of the page
   */
  @CheckReturnValue
  @Nonnull
  default <P extends NavigablePage> P open(final Class<P> pageObjectClass) {
    final P page = page(pageObjectClass);

    Selenide.open(requireNonNull(page.urlSupplier(), urlSupplierNotNullMessage).get());

    requireNonNull(page.atVerificationSupplier(), atVerificationNotNullMessage).run();

    return page;
  }

  /**
   * <p>
   * Navigate to the desired page.
   * </p>
   *
   * @param reified a {@code P[]} providing the type of the page to navigate to through a reified generic. Don't pass any values here.
   * @param <P>     the type of the page to navigate to
   * @return a new instance of the page
   */
  @CheckReturnValue
  @Nonnull
  @SuppressWarnings("unchecked")
  default <P extends NavigablePage> P open(final P... reified) {
    final P page = page(reified);

    Selenide.open(requireNonNull(page.urlSupplier(), urlSupplierNotNullMessage).get());

    requireNonNull(page.atVerificationSupplier(), atVerificationNotNullMessage).run();

    return page;
  }
}
