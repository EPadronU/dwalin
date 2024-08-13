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
import java.util.function.Supplier;
/* ************************************************************************************************/

/**
 * <p>
 * Describes a web page that can be navigated to within a browser.
 * </p>
 * <p>
 * Provides methods to supply the URL for navigation and to verify that the correct page has been reached.
 * </p>
 */
public interface NavigablePage extends Page {

  /**
   * <p>
   * Returns a {@code Supplier} that provides the URL for the browser to navigate to.
   * </p>
   *
   * @return a {@code Supplier<String>} that provides the URL
   */
  @CheckReturnValue
  @Nonnull
  Supplier<String> urlSupplier();

  /**
   * Returns a {@code Runnable} that performs verification to ensure the browser has navigated to the expected page.
   *
   * @return a {@code Runnable} for verifying that the correct page has been reached
   */
  @CheckReturnValue
  @Nonnull
  Runnable atVerificationSupplier();
}
