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
package io.github.epadronu.dwalin.core;
/* ************************************************************************************************/

/* ************************************************************************************************/
import com.codeborne.selenide.Selenide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.page;
import static io.qameta.allure.Allure.getLifecycle;
import static java.util.Objects.requireNonNull;
import static org.openqa.selenium.OutputType.BYTES;
/* ************************************************************************************************/

/**
 * <p>
 * Provides a set of utility methods for navigating to pages that implement the {@code NavigablePage} interface,
 * along with other useful functionality for test automation.
 * </p>
 */
public final class Dwalin {

  /**
   * <p>
   * Error message displayed when attempting to navigate to a page with a null URL supplier.
   * </p>
   */
  public static final String URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE = "The URL supplier cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to navigate to a page with a null URL.
   * </p>
   */
  public static final String URL_CANNOT_BE_NULL_MESSAGE = "The URL cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to attach a screenshot of a null subject.
   * </p>
   */
  public static final String SUBJECT_CANNOT_BE_NULL_MESSAGE = "The subject cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to attach a screenshot associated with a null description.
   * </p>
   */
  public static final String DESCRIPTION_CANNOT_BE_NULL_MESSAGE = "The description cannot be null";

  /**
   * <p>
   * Error message displayed when attempting to navigate to a page with a null "at verification" supplier.
   * </p>
   */
  public static final String AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE = "The at verification supplier cannot be null";

  private static final Logger log = LogManager.getLogger();

  private Dwalin() {

  }

  /**
   * <p>
   * Navigates to the specified page.
   * </p>
   *
   * @param pageObjectClass a {@code Class<P>} representing the type of the page to navigate to
   * @param <P>             the type of the page to navigate to
   * @return a new instance of the specified page
   * @throws NullPointerException if {@code pageObjectClass} is {@code null}
   */
  @CheckReturnValue
  @Nonnull
  public static <P extends NavigablePage> P navigateTo(@Nonnull final Class<P> pageObjectClass) {
    return openAndCheckAtVerification(page(pageObjectClass));
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
  public static <P extends NavigablePage> P navigateTo(final P... reified) {
    return openAndCheckAtVerification(page(reified));
  }

  /**
   * Captures a screenshot from the given {@code TakesScreenshot} subject and attaches it
   * to the Allure report with a default description.
   *
   * @param subject the {@code TakesScreenshot} instance from which the screenshot will be captured
   * @throws NullPointerException if {@code subject} is {@code null}
   */
  @CheckReturnValue
  public static void attachScreenshotToAllureReport(@Nonnull final TakesScreenshot subject) {
    requireNonNull(subject, SUBJECT_CANNOT_BE_NULL_MESSAGE);

    attachScreenshotToAllureReport(subject, subject.toString());
  }

  /**
   * Captures a screenshot from the given {@code TakesScreenshot} subject and attaches it
   * to the Allure report with the specified description.
   *
   * @param subject     the {@code TakesScreenshot} instance from which the screenshot will be captured
   * @param description the description to be used for the screenshot attachment in the Allure report
   * @throws NullPointerException if either {@code subject} or {@code description} are {@code null}
   */
  @CheckReturnValue
  public static void attachScreenshotToAllureReport(@Nonnull final TakesScreenshot subject, @Nonnull final String description) {
    requireNonNull(subject, SUBJECT_CANNOT_BE_NULL_MESSAGE);
    requireNonNull(description, DESCRIPTION_CANNOT_BE_NULL_MESSAGE);

    getLifecycle().getCurrentTestCaseOrStep().ifPresent((_) -> {
      try {
        getLifecycle().addAttachment(
            description, "image/png", "png", subject.getScreenshotAs(BYTES));
      } catch (final WebDriverException exception) {
        log.error("An screenshot couldn't be attached to the Allure report.", exception);
      }
    });
  }

  private static <P extends NavigablePage> P openAndCheckAtVerification(final P page) {
    Selenide.open(requireNonNull(
        requireNonNull(page.urlSupplier(), URL_SUPPLIER_CANNOT_BE_NULL_MESSAGE).get(),
        URL_CANNOT_BE_NULL_MESSAGE));

    requireNonNull(page.atVerificationSupplier(), AT_VERIFICATION_SUPPLIER_CANNOT_BE_NULL_MESSAGE).run();

    return page;
  }
}
