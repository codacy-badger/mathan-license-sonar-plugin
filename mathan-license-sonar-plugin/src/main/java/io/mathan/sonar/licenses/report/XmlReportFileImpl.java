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

package io.mathan.sonar.licenses.report;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.mathan.sonar.licenses.Constants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Configuration;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class XmlReportFileImpl implements XmlReportFile {

  private static final Logger LOGGER = Loggers.get(XmlReportFileImpl.class);

  private final File report;

  private XmlReportFileImpl(File report) {
    this.report = report;
  }

  public InputStream getInputStream() throws IOException {
    return Files.newInputStream(this.report.toPath());
  }

  @CheckForNull
  private static File checkReport(@Nullable File report) {
    if (report != null) {
      if (!report.exists()) {
        LOGGER.info("Licenses {} report does not exists. Please check property {}:{}", "XML", Constants.CONFIG_REPORT_PATH_PROPERTY, report.getAbsolutePath());
        return null;
      }
      if (!report.isFile()) {
        LOGGER.info("Licenses {} report is not a normal file", "XML");
        return null;
      }
      if (!report.canRead()) {
        LOGGER.info("Licenses {} report is not readable", "XML");
        return null;
      }
    }
    return report;
  }

  /**
   * Returns a {@link XmlReportFile} for a dependency-updates-report found.
   *
   * @throws FileNotFoundException If no report could be found.
   */
  public static XmlReportFile getReport(Configuration config, FileSystem fileSystem, PathResolver pathResolver) throws FileNotFoundException {
    String path = config.get(Constants.CONFIG_REPORT_PATH_PROPERTY).orElse(Constants.CONFIG_REPORT_PATH_DEFAULT);
    File report = pathResolver.relativeFile(fileSystem.baseDir(), path);
    report = checkReport(report);
    if (report == null) {
      throw new FileNotFoundException("licenses.xml report does not exist.");
    }
    return new XmlReportFileImpl(report);
  }

  public static List<XmlReportFile> getReports(FileSystem fileSystem) {
    return FileUtils.listFiles(fileSystem.baseDir(), new NameFileFilter("licenses.xml"), DirectoryFileFilter.DIRECTORY).stream().map(file -> new XmlReportFileImpl(file)).collect(
        Collectors.toList());
  }

}
