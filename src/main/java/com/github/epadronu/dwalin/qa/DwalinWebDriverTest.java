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
import org.junit.jupiter.api.DisplayName;

import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
/* ************************************************************************************************/

/**
 * <p>
 * A recommended base class for WebDriver tests based on the Dwalin library.
 * </p>
 * <p>
 * This class currently provides integration between the Allure Framework and Selenide.
 * </p>
 */
public class DwalinWebDriverTest extends DwalinTest {

  /**
   * <p>
   * Constructs a new {@code DwalinWebDriverTest} instance.
   * </p>
   */
  public DwalinWebDriverTest() {
  }

  /**
   * <p>
   * Configures the integration between Allure Framework and Selenide.
   * </p>
   */
  @BeforeAll
  @DisplayName("Configure the integration between Allure Framework and Selenide")
  static void setupAllureReports() {
    addListener("AllureSelenide", new DwalinAllureSelenide()
        .screenshots(true)
        .screenshotsForSteps(true)
        .savePageSource(true));
  }
}
