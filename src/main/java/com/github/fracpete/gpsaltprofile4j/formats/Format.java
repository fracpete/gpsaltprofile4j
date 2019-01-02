/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Format.java
 * Copyright (C) 2018 FracPete
 */
package com.github.fracpete.gpsaltprofile4j.formats;

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.List;

/**
 * Interface for formats.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public interface Format {

  /** the key for the track. */
  public final static String KEY_TRACK = "Track";

  /** the key for the time. */
  public final static String KEY_TIME = "Time";

  /** the key for the distance. */
  public final static String KEY_DISTANCE = "Distance";

  /** the key for the elevation. */
  public final static String KEY_ELEVATION = "Elevation";

  /**
   * Writes to a file.
   *
   * @param data	the data to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  public String write(List<CSVRecord> data, File output);
}
