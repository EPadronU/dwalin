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
package io.github.epadronu.dwalin.qa;
/* ************************************************************************************************/

/* ************************************************************************************************/
import io.github.epadronu.dwalin.utils.junit.PascalCaseDisplayNameGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.TestInfo;

import java.util.concurrent.atomic.AtomicReference;
/* ************************************************************************************************/

/**
 * <p>
 * Optional base class for tests based on the Dwalin library.
 * </p>
 * <p>
 * This class primarily logs the start and completion of each test method.
 * </p>
 */
@DisplayNameGeneration(PascalCaseDisplayNameGenerator.class)
public class DwalinTest {

  private static final Logger logger = LogManager.getLogger();

  private final AtomicReference<EntryMessage> atomicEntryMessage = new AtomicReference<>();

  /**
   * <p>
   * Constructs a new {@code DwalinTest} instance.
   * </p>
   */
  public DwalinTest() {
  }

  /**
   * <p>
   * Traces the start of a test method.
   * </p>
   *
   * @param testInfo provides information about the current test
   * @see Logger#traceEntry(Message)
   * @see TestInfo
   */
  @BeforeEach
  @DisplayName("Trace the start of a test method")
  protected void traceTestMethodStart(final TestInfo testInfo) {
    if (testInfo.getDisplayName().startsWith("[") && testInfo.getTestMethod().isPresent()) {
      atomicEntryMessage.set(logger.traceEntry(
          "TEST «{} {}»",
          PascalCaseDisplayNameGenerator.pascalAndCamelCaseToHumanFriendly(testInfo.getTestMethod().get().getName()),
          testInfo.getDisplayName()));
    } else {
      atomicEntryMessage.set(logger.traceEntry("TEST «{}»", testInfo.getDisplayName()));
    }
  }

  /**
   * <p>
   * Traces the end of a test method.
   * </p>
   *
   * @see Logger#traceExit(EntryMessage)
   */
  @AfterEach
  @DisplayName("Trace the end of a test method")
  protected void traceTestMethodEnd() {
    logger.traceExit(atomicEntryMessage.get());
  }
}
