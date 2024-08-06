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
package com.github.epadronu.dwalin.utils.selenide;
/* ************************************************************************************************/

/* ************************************************************************************************/
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.codeborne.selenide.logevents.SelenideLog;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.selenide.LogType;
import io.qameta.allure.util.ResultsUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
/* ************************************************************************************************/

/**
 * <p>
 * Based on {@link io.qameta.allure.selenide.AllureSelenide}, this class extends the original functionality by
 * attaching screenshots on successful steps for failed tests.
 * </p>
 *
 * @see io.qameta.allure.selenide.AllureSelenide
 */
@ParametersAreNonnullByDefault
public class DwalinAllureSelenide implements LogEventListener {

  private static final Logger logger = LogManager.getLogger();

  private boolean saveScreenshots;

  private boolean savePageHtml;

  private boolean includeSelenideLocatorsSteps;

  private final Map<LogType, Level> logTypesToSave;

  private final AllureLifecycle lifecycle;

  /**
   * <p>
   * Create a new {@code DwalinAllureSelenide} instance getting the required {@code AllureLifecycle} invoking
   * {@code Allure.getLifecycle()}.
   * </p>
   *
   * @see Allure#getLifecycle()
   */
  public DwalinAllureSelenide() {
    this(Allure.getLifecycle());
  }

  /**
   * <p>
   * Create a new {@code DwalinAllureSelenide} instance with the given {@code AllureLifecycle}.
   * </p>
   *
   * @param lifecycle Allure's lifecycle to attach to
   */
  public DwalinAllureSelenide(final AllureLifecycle lifecycle) {
    this.saveScreenshots = true;
    this.savePageHtml = true;
    this.includeSelenideLocatorsSteps = true;
    this.logTypesToSave = new EnumMap<>(LogType.class);
    this.lifecycle = lifecycle;
  }

  public DwalinAllureSelenide screenshots(final boolean saveScreenshots) {
    this.saveScreenshots = saveScreenshots;
    return this;
  }

  public DwalinAllureSelenide savePageSource(final boolean savePageHtml) {
    this.savePageHtml = savePageHtml;
    return this;
  }

  public DwalinAllureSelenide includeSelenideSteps(final boolean includeSelenideSteps) {
    this.includeSelenideLocatorsSteps = includeSelenideSteps;
    return this;
  }

  public DwalinAllureSelenide enableLogs(final LogType logType, final Level logLevel) {
    this.logTypesToSave.put(logType, logLevel);
    return this;
  }

  public DwalinAllureSelenide disableLogs(final LogType logType) {
    this.logTypesToSave.remove(logType);
    return this;
  }

  @Override
  public void beforeEvent(final LogEvent event) {
    if (this.stepsShouldBeLogged(event)) {
      this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
        final String uuid = UUID.randomUUID().toString();

        this.lifecycle.startStep(parentUuid, uuid, (new StepResult()).setName(event.toString()));
      });
    }
  }

  @Override
  public void afterEvent(final LogEvent event) {
    if (event.getStatus().equals(LogEvent.EventStatus.PASS)) {
      this.lifecycle.getCurrentTestCaseOrStep().ifPresent((_) -> {
        if (this.saveScreenshots) {
          getScreenshotBytes().ifPresent(bytes -> {
            this.lifecycle.addAttachment("Screenshot", "image/png", "png", bytes);
          });
        }
      });
    }

    if (event.getStatus().equals(LogEvent.EventStatus.FAIL)) {
      this.lifecycle.getCurrentTestCaseOrStep().ifPresent((_) -> {
        if (this.saveScreenshots) {
          getScreenshotBytes().ifPresent(bytes -> {
            this.lifecycle.addAttachment("Screenshot", "image/png", "png", bytes);
          });
        }

        if (this.savePageHtml) {
          getPageSourceBytes().ifPresent(bytes -> {
            this.lifecycle.addAttachment("Page source", "text/html", "html", bytes);
          });
        }

        if (!this.logTypesToSave.isEmpty()) {
          this.logTypesToSave.forEach((logType, level) -> {
            final byte[] content = getBrowserLogs(logType, level).getBytes(StandardCharsets.UTF_8);

            this.lifecycle.addAttachment("Logs from: " + logType, "application/json", ".txt", content);
          });
        }
      });
    }

    if (this.stepsShouldBeLogged(event)) {
      this.lifecycle.getCurrentTestCaseOrStep().ifPresent((_) -> {
        switch (event.getStatus()) {
          case PASS -> this.lifecycle.updateStep((step) -> {
            step.setStatus(Status.PASSED);
          });
          case FAIL -> this.lifecycle.updateStep((stepResult) -> {
            stepResult.setStatus(ResultsUtils.getStatus(event.getError()).orElse(Status.BROKEN));
            stepResult.setStatusDetails(ResultsUtils.getStatusDetails(event.getError()).orElse(new StatusDetails()));
          });
          default -> logger.warn("Step finished with unsupported status {}", event.getStatus());
        }

        this.lifecycle.stopStep();
      });
    }
  }

  private static Optional<byte[]> getScreenshotBytes() {
    try {
      return WebDriverRunner.hasWebDriverStarted()
          ? Optional.of(((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES))
          : Optional.empty();
    } catch (final WebDriverException exception) {
      logger.warn("Could not get screen shot", exception);

      return Optional.empty();
    }
  }

  private static Optional<byte[]> getPageSourceBytes() {
    try {
      return WebDriverRunner.hasWebDriverStarted()
          ? Optional.of(WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8))
          : Optional.empty();
    } catch (final WebDriverException exception) {
      logger.warn("Could not get page source", exception);

      return Optional.empty();
    }
  }

  private static String getBrowserLogs(final LogType logType, final Level level) {
    return String.join("\n\n", Selenide.getWebDriverLogs(logType.toString(), level));
  }

  private boolean stepsShouldBeLogged(final LogEvent event) {
    return this.includeSelenideLocatorsSteps || !(event instanceof SelenideLog);
  }
}
