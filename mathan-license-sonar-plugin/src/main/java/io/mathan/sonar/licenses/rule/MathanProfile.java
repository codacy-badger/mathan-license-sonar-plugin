/*
 * mathan-license-sonar-plugin
 * Copyright (c) 2020 Matthias Hanisch
 * matthias@mathan.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mathan.sonar.licenses.rule;

import io.mathan.sonar.licenses.Constants;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

public class MathanProfile implements BuiltInQualityProfilesDefinition {

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile dependencyCheckWay = context.createBuiltInQualityProfile("mathanlicense", Constants.LANGUAGE_KEY);
    dependencyCheckWay.activateRule(Constants.REPOSITORY_KEY, Constants.RULE_KEY);
    dependencyCheckWay.done();
  }
}