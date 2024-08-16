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
package io.github.epadronu.dwalin.utils.selenide;
/* ************************************************************************************************/

/* ************************************************************************************************/
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

import static com.codeborne.selenide.Selenide.getWebDriverLogs;
import static java.lang.String.join;
/* ************************************************************************************************/

/**
 * <p>
 * Extends the functionality of {@link io.qameta.allure.selenide.AllureSelenide} by automatically
 * attaching screenshots for every step (if enabled).
 * </p>
 *
 * @see io.qameta.allure.selenide.AllureSelenide
 */
@ParametersAreNonnullByDefault
public final class DwalinAllureSelenide implements LogEventListener {

  private static final Logger logger = LogManager.getLogger();

  private boolean saveScreenshots;

  private boolean saveScreenshotsForEveryStep;

  private boolean savePageHtml;

  private boolean includeSelenideLocatorsSteps;

  private final Map<LogType, Level> logTypesToSave;

  private final AllureLifecycle lifecycle;

  /**
   * <p>
   * Creates a new {@code DwalinAllureSelenide} instance and internally initializes
   * the {@link AllureLifecycle} by invoking {@link Allure#getLifecycle()}.
   * </p>
   *
   * @see Allure#getLifecycle()
   */
  public DwalinAllureSelenide() {
    this(Allure.getLifecycle());
  }

  /**
   * <p>
   * Creates a new {@code DwalinAllureSelenide} instance with the specified {@code AllureLifecycle}.
   * </p>
   *
   * @param lifecycle the Allure lifecycle to be used for attaching screenshots, logs, and HTML sources
   */
  public DwalinAllureSelenide(final AllureLifecycle lifecycle) {
    this.saveScreenshots = true;
    this.saveScreenshotsForEveryStep = false;
    this.savePageHtml = true;
    this.includeSelenideLocatorsSteps = true;
    this.logTypesToSave = new EnumMap<>(LogType.class);
    this.lifecycle = lifecycle;
  }

  /**
   * <p>
   * Enables or disables the automatic saving of screenshots for failed tests.
   * </p>
   *
   * @param saveScreenshots whether to save screenshots for failed tests
   * @return the current instance of {@code DwalinAllureSelenide} for method chaining
   */
  public DwalinAllureSelenide screenshots(final boolean saveScreenshots) {
    this.saveScreenshots = saveScreenshots;
    return this;
  }

  /**
   * <p>
   * Enables or disables the automatic saving of screenshots for every step.
   * </p>
   *
   * @param saveScreenshotsForEveryStep whether to save screenshots for every step
   * @return the current instance of {@code DwalinAllureSelenide} for method chaining
   */
  public DwalinAllureSelenide screenshotsForSteps(final boolean saveScreenshotsForEveryStep) {
    this.saveScreenshotsForEveryStep = saveScreenshotsForEveryStep;
    return this;
  }

  /**
   * <p>
   * Enables or disables the saving of the page source for failed tests.
   * </p>
   *
   * @param savePageHtml whether to save the page source for failed tests
   * @return the current instance of {@code DwalinAllureSelenide} for method chaining
   */
  public DwalinAllureSelenide savePageSource(final boolean savePageHtml) {
    this.savePageHtml = savePageHtml;
    return this;
  }

  /**
   * <p>
   * Configures whether to include Selenide locator steps in the Allure report.
   * </p>
   *
   * @param includeSelenideSteps whether to include Selenide locator steps
   * @return the current instance of {@code DwalinAllureSelenide} for method chaining
   */
  public DwalinAllureSelenide includeSelenideSteps(final boolean includeSelenideSteps) {
    this.includeSelenideLocatorsSteps = includeSelenideSteps;
    return this;
  }

  /**
   * <p>
   * Enables logging of browser console logs for the specified log type and level.
   * </p>
   *
   * @param logType  the type of browser logs to save
   * @param logLevel the level of logs to capture
   * @return the current instance of {@code DwalinAllureSelenide} for method chaining
   */
  public DwalinAllureSelenide enableLogs(final LogType logType, final Level logLevel) {
    this.logTypesToSave.put(logType, logLevel);
    return this;
  }

  /**
   * <p>
   * Disables logging of browser console logs for the specified log type.
   * </p>
   *
   * @param logType the type of browser logs to disable
   * @return the current instance of {@code DwalinAllureSelenide} for method chaining
   */
  public DwalinAllureSelenide disableLogs(final LogType logType) {
    this.logTypesToSave.remove(logType);
    return this;
  }

  /**
   * <p>
   * Executes actions before a Selenide event, logging the step if required.
   * </p>
   *
   * @param event the event that triggers this action
   */
  @Override
  public void beforeEvent(final LogEvent event) {
    if (this.stepsShouldBeLogged(event)) {
      this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
        final String uuid = UUID.randomUUID().toString();

        this.lifecycle.startStep(parentUuid, uuid, (new StepResult()).setName(event.toString()));
      });
    }
  }

  /**
   * <p>
   * Executes actions after a Selenide event, such as attaching screenshots, page sources, and logs.
   * </p>
   *
   * @param event the event that triggers this action
   */
  @Override
  public void afterEvent(final LogEvent event) {
    if (!event.getStatus().equals(LogEvent.EventStatus.FAIL)) {
      this.lifecycle.getCurrentTestCaseOrStep().ifPresent((_) -> {
        if (this.saveScreenshotsForEveryStep) {
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
          case PASS -> this.lifecycle.updateStep((stepResult) -> {
            stepResult.setStatus(Status.PASSED);
            stepResult.setStatusDetails(new StatusDetails());
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

  /**
   * <p>
   * Captures a screenshot as a byte array, if the WebDriver is running.
   * </p>
   *
   * @return an {@code Optional} containing the screenshot bytes, or an empty {@code Optional} if capturing fails
   */
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

  /**
   * <p>
   * Captures the page source as a byte array, if the WebDriver is running.
   * </p>
   *
   * @return an {@code Optional} containing the page source bytes, or an empty {@code Optional} if capturing fails
   */
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

  /**
   * <p>
   * Retrieves browser logs as a string for the specified log type and log level.
   * </p>
   *
   * @param logType the type of browser logs to retrieve
   * @param level   the log level to filter by
   * @return the filtered browser logs as a string
   */
  private static String getBrowserLogs(final LogType logType, final Level level) {
    return join("\n\n", getWebDriverLogs(logType.toString(), level));
  }

  /**
   * <p>
   * Determines whether a Selenide step should be logged.
   * </p>
   *
   * @param event the event to evaluate
   * @return {@code true} if the step should be logged, otherwise {@code false}
   */
  private boolean stepsShouldBeLogged(final LogEvent event) {
    return this.includeSelenideLocatorsSteps || !(event instanceof SelenideLog);
  }
}
