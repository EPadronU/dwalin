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
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase;
/* ************************************************************************************************/

/**
 * <p>
 * Converts class and method names into human-readable format.
 * </p>
 */
public final class PascalCaseDisplayNameGenerator implements DisplayNameGenerator {

  /**
   * <p>
   * Constructs a new {@code PascalCaseDisplayNameGenerator} instance.
   * </p>
   */
  public PascalCaseDisplayNameGenerator() {
  }

  /**
   * <p>
   * Generates a human-friendly display name for the specified top-level or {@code static} nested test class.
   * </p>
   *
   * @param aClass the class for which to generate a display name; never null
   * @return the generated display name; never null or blank
   */
  @Override
  public String generateDisplayNameForClass(final Class<?> aClass) {
    return pascalAndCamelCaseToHumanFriendly(aClass.getSimpleName());
  }

  /**
   * <p>
   * Generates a human-friendly display name for the specified {@code @Nested} inner test class.
   * </p>
   *
   * @param aClass the nested class for which to generate a display name; never null
   * @return the generated display name; never null or blank
   */
  @Override
  public String generateDisplayNameForNestedClass(final Class<?> aClass) {
    return pascalAndCamelCaseToHumanFriendly(aClass.getSimpleName());
  }

  /**
   * <p>
   * Generates a human-friendly display name for the specified test method.
   * </p>
   *
   * @param aClass the class containing the test method; never null
   * @param method the method for which to generate a display name; never null
   * @return the generated display name; never null or blank
   */
  @Override
  public String generateDisplayNameForMethod(final Class<?> aClass, final Method method) {
    return pascalAndCamelCaseToHumanFriendly(method.getName());
  }

  /**
   * <p>
   * Converts a given PascalCase or camelCase string into a human-readable format.
   * </p>
   * <p>
   * This method splits the input string based on camel case boundaries and converts it to a lowercase string
   * with the first word capitalized. The resulting string is formatted with spaces between words.
   * </p>
   *
   * @param name the PascalCase or camelCase string to convert; never null
   * @return a human-readable version of the input string; never null
   */
  private static String pascalAndCamelCaseToHumanFriendly(final String name) {
    return capitalize(join(" ", splitByCharacterTypeCamelCase(name)).toLowerCase());
  }
}
