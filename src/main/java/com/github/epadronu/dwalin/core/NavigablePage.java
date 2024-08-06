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
import java.util.function.Supplier;
/* ************************************************************************************************/

/**
 * <p>
 * Models a page that can be navigated to by the browser.
 * </p>
 *
 * <p>
 * It provides methods to supply the URL for navigation and a way to verify that the desired page has been reached.
 * </p>
 */
public interface NavigablePage extends Page {

  /**
   * Supplies the URL that the browser will navigate to.
   *
   * @return a {@code Supplier<String>} providing the URL.
   */
  @CheckReturnValue
  Supplier<String> urlSupplier();

  /**
   * Supplies a way to verify that the desired page has been navigated to.
   *
   * @return a {@code Runnable} for verification.
   */
  @CheckReturnValue
  Runnable atVerificationSupplier();
}
