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
package com.github.epadronu.dwalin.utils.junit;
/* ************************************************************************************************/

/* ************************************************************************************************/
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
/* ************************************************************************************************/

/**
 * <p>
 * Converts class' and methods' names into human-friendly names.
 * </p>
 */
public final class PascalCaseDisplayNameGenerator implements DisplayNameGenerator {

  /**
   * <p>
   * Creates a new instance of {@code PascalCaseDisplayNameGenerator}.
   * </p>
   */
  public PascalCaseDisplayNameGenerator() {
  }

  /**
   * <p>
   * Generate a display name for the given top-level or {@code static} nested test class.
   * </p>
   *
   * @param aClass the class to generate a name for; never null
   * @return the display name for the class; never null or blank
   */
  @Override
  public String generateDisplayNameForClass(final Class<?> aClass) {
    return pascalAndCamelCaseToHumanFriendly(aClass.getSimpleName());
  }

  /**
   * Generate a display name for the given {@code @Nested} inner test class.
   *
   * @param aClass the class to generate a name for; never null
   * @return the display name for the nested class; never null or blank
   */
  @Override
  public String generateDisplayNameForNestedClass(final Class<?> aClass) {
    return pascalAndCamelCaseToHumanFriendly(aClass.getSimpleName());
  }

  /**
   * <p>
   * Generate a display name for the given method.
   * </p>
   *
   * @param aClass the class the test method is invoked on; never null
   * @param method method to generate a display name for; never null
   * @return the display name for the test; never null or blank
   */
  @Override
  public String generateDisplayNameForMethod(final Class<?> aClass, final Method method) {
    return pascalAndCamelCaseToHumanFriendly(method.getName());
  }

  private static String pascalAndCamelCaseToHumanFriendly(final String name) {
    return StringUtils.capitalize(
        String.join(" ", StringUtils.splitByCharacterTypeCamelCase(name)).toLowerCase());
  }
}
