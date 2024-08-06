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
package com.github.epadronu.dwalin.qa;
/* ************************************************************************************************/

/* ************************************************************************************************/
import com.github.epadronu.dwalin.utils.selenide.DwalinAllureSelenide;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
/* ************************************************************************************************/

/**
 * <p>
 * Recommended super-class for WebDriver tests based on Dwalin.
 * </p>
 *
 * <p>
 * For now, it only integrates Allure Framework with Selenide.
 * </p>
 */
public class DwalinWebDriverTest extends DwalinTest {

  /**
   * <p>
   * Creates a new instance of {@code DwalinWebDriverTest}.
   * </p>
   */
  public DwalinWebDriverTest() {
  }

  /**
   * <p>
   * Integrates Allure Framework with Selenide.
   * </p>
   */
  @BeforeAll
  static void setupAllureReports() {
    addListener("AllureSelenide", new DwalinAllureSelenide()
        .screenshots(true)
        .savePageSource(true));
  }
}
