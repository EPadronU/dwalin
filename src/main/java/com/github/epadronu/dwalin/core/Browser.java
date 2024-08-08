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
import java.util.function.Consumer;
/* ************************************************************************************************/

/**
 * <p>
 * Models a web browser within the test automation library.
 * </p>
 */
public sealed interface Browser extends AbstractionLayer permits BrowserImpl {

  /**
   * Defines a navigation context for navigating/browsing the web.
   *
   * @param context a {@code Consumer<Browser>} representing the navigation context.
   */
  static void navigate(final Consumer<Browser> context) {
    context.accept(new BrowserImpl());
  }
}

/**
 * <p>
 * Internal and single implementation of the {@code Browser} interface.
 * </p>
 *
 * @see Browser
 */
final class BrowserImpl implements Browser {

}
