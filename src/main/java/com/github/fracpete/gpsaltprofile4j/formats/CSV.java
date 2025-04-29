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
 * CSV.java
 * Copyright (C) 2018 FracPete
 */

package com.github.fracpete.gpsaltprofile4j.formats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordFactory;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV format. Requires the following columns in this order:
 * <ol>
 *   <li>track</li>
 *   <li>time</li>
 *   <li>distance</li>
 *   <li>elevation</li>
 * </ol>
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class CSV
  extends AbstractFormat {

  /**
   * Writes to a file.
   *
   * @param data	the data to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  @Override
  public String write(List<CSVRecord> data, File output) {
    CSVPrinter		printer;
    FileWriter		writer;
    boolean		first;
    CSVRecord		header;
    List<String>	values;
    Map<String,Integer>	map;

    writer  = null;
    printer = null;
    try {
      m_Logger.info("Writing: " + output);
      writer  = new FileWriter(output);
      printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
      first   = true;
      for (CSVRecord rec: data) {
	if (first) {
	  map = new HashMap<>();
	  map.put(KEY_TRACK, 0);
	  map.put(KEY_TIME, 1);
	  map.put(KEY_DISTANCE, 2);
	  map.put(KEY_ELEVATION, 3);
	  values = new ArrayList<>();
	  values.add(KEY_TRACK);
	  values.add(KEY_TIME);
	  values.add(KEY_DISTANCE);
	  values.add(KEY_ELEVATION);
	  header = CSVRecordFactory.newRecord(values.toArray(new String[0]), map);
	  printer.printRecord(header);
	}
	printer.printRecord(rec);
	first = false;
      }
      printer.flush();
      printer.close();
    }
    catch (Exception e) {
      m_Logger.error("Failed to write: " + output, e);
      return "Failed to write: " + output + "\n" + e;
    }
    finally {
      IOUtils.closeQuietly(writer);
      IOUtils.closeQuietly(printer);
    }

    return null;
  }
}
