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
 * Acts as the foundational layer for the {@code Browser}, {@code Component}, and {@code Page}
 * types, encapsulating their shared API.
 * </p>
 * <p>
 * Implementing this interface ensures that types inherit the common behavior defined here,
 * providing a unified structure and interaction model across various components of the library.
 * </p>
 *
 * @see Browser
 * @see Component
 * @see Page
 */
sealed interface AbstractionLayer permits Browser, Component, Page {

  /**
   * <p>
   * Error message displayed when attempting to open a page with a null URL supplier.
   * </p>
   */
  String URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE = "The URL supplier cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to open a page with a null "at verification" supplier.
   * </p>
   */
  String AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE = "The at verification supplier cannot be null";

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
  default <P extends NavigablePage> P open(final Class<P> pageObjectClass) {
    final P page = page(pageObjectClass);

    Selenide.open(requireNonNull(page.urlSupplier(), URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE).get());

    requireNonNull(page.atVerificationSupplier(), AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE).run();

    return page;
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
  default <P extends NavigablePage> P open(final P... reified) {
    final P page = page(reified);

    Selenide.open(requireNonNull(page.urlSupplier(), URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE).get());

    requireNonNull(page.atVerificationSupplier(), AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE).run();

    return page;
  }
}
