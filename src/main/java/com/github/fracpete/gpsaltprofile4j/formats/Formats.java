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
 * Formats.java
 * Copyright (C) 2018 FracPete
 */

package com.github.fracpete.gpsaltprofile4j.formats;

/**
 * Helper class for all formats.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class Formats {

  /**
   * Returns all available formats.
   *
   * @return		the formats
   */
  public static Class[] allFormats() {
    return new Class[]{
      CSV.class,
      PNG.class,
    };
  }
}
