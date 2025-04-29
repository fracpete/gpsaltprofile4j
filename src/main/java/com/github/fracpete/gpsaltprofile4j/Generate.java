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
 * Generate.java
 * Copyright (C) 2018-2025 FracPete
 */

package com.github.fracpete.gpsaltprofile4j;

import com.github.fracpete.gpsaltprofile4j.formats.Format;
import com.github.fracpete.gpsformats4j.core.BaseObject;
import com.github.fracpete.gpsformats4j.core.OptionHandler;
import com.github.fracpete.gpsformats4j.core.OptionUtils;
import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generation class.
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class Generate
  extends BaseObject
  implements OptionHandler {

  public static final String INPUT_FILE = "in_file";

  public static final String INPUT_FORMAT = "in_format";

  public static final String INPUT_OPTIONS = "in_options";

  public static final String OUTPUT_FILE = "out_file";

  public static final String OUTPUT_FORMAT = "out_format";

  public static final String OUTPUT_OPTIONS = "out_options";

  /** the key for the longitude. */
  public final static String KEY_LON = "Longitude";

  /** the key for the latitiude. */
  public final static String KEY_LAT = "Latitude";

  /** the argument parser. */
  protected ArgumentParser m_Parser;

  /** the input file. */
  protected File m_InputFile;

  /** the input format. */
  protected Class m_InputFormat;

  /** the input options. */
  protected String m_InputOptions;

  /** the output file. */
  protected File m_OutputFile;

  /** the output format. */
  protected Class m_OutputFormat;

  /** the output options. */
  protected String m_OutputOptions;

  /** whether help got requested. */
  protected boolean m_HelpRequested;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    String[] 	formatsIn;
    String[] 	formatsOut;
    int		i;

    super.initialize();

    formatsIn = new String[com.github.fracpete.gpsformats4j.formats.Formats.allFormats().length];
    for (i = 0; i < com.github.fracpete.gpsformats4j.formats.Formats.allFormats().length; i++)
      formatsIn[i] = com.github.fracpete.gpsformats4j.formats.Formats.allFormats()[i].getSimpleName();
    formatsOut = new String[com.github.fracpete.gpsaltprofile4j.formats.Formats.allFormats().length];
    for (i = 0; i < com.github.fracpete.gpsaltprofile4j.formats.Formats.allFormats().length; i++)
      formatsOut[i] = com.github.fracpete.gpsaltprofile4j.formats.Formats.allFormats()[i].getSimpleName();

    m_Parser = ArgumentParsers.newFor(getClass().getSimpleName()).build();

    m_Parser.description("Generates altitude profiles from GPS traces with altitude information.");
    m_Parser.defaultHelp(true);

    m_Parser.addArgument("--" + INPUT_FILE)
      .metavar("<file>")
      .dest(INPUT_FILE)
      .required(true)
      .type(String.class)
      .help("The GPS input file to use.");
    m_Parser.addArgument("--" + INPUT_FORMAT)
      .dest(INPUT_FORMAT)
      .required(true)
      .type(String.class)
      .choices(formatsIn)
      .help("The input format.");
    m_Parser.addArgument("--" + INPUT_OPTIONS)
      .metavar("<options>")
      .dest(INPUT_OPTIONS)
      .required(false)
      .type(String.class)
      .setDefault("")
      .help("The options for the input format, if supported. Blank-separated list of key=value pairs.");

    m_Parser.addArgument("--" + OUTPUT_FILE)
      .metavar("<file>")
      .dest(OUTPUT_FILE)
      .required(true)
      .type(String.class)
      .help("The output file to generate.");
    m_Parser.addArgument("--" + OUTPUT_FORMAT)
      .dest(OUTPUT_FORMAT)
      .required(true)
      .type(String.class)
      .choices(formatsOut)
      .help("The output format.");
    m_Parser.addArgument("--" + OUTPUT_OPTIONS)
      .metavar("<options>")
      .dest(OUTPUT_OPTIONS)
      .required(false)
      .type(String.class)
      .setDefault("")
      .help("The options for the output format, if supported. Blank-separated list of key=value pairs.");
  }

  /**
   * Sets the input file.
   * 
   * @param value	the file
   */
  public void setInputFile(File value) {
    m_InputFile = value;
  }

  /**
   * Returns the input file.
   * 
   * @return		the file
   */
  public File getInputFile() {
    return m_InputFile;
  }

  /**
   * Sets the input format.
   * 
   * @param value	the format
   */
  public void setInputFormat(Class value) {
    m_InputFormat = value;
  }

  /**
   * Returns the input format.
   * 
   * @return		the format
   */
  public Class getInputFormat() {
    return m_InputFormat;
  }

  /**
   * Sets the input options.
   *
   * @param value	the options
   */
  public void setInputOptions(String value) {
    m_InputOptions = value;
  }

  /**
   * Returns the input options.
   *
   * @return		the options
   */
  public String getInputOptions() {
    return m_InputOptions;
  }

  /**
   * Sets the output file.
   * 
   * @param value	the file
   */
  public void setOutputFile(File value) {
    m_OutputFile = value;
  }

  /**
   * Returns the output file.
   * 
   * @return		the file
   */
  public File getOutputFile() {
    return m_OutputFile;
  }

  /**
   * Sets the output format.
   * 
   * @param value	the format
   */
  public void setOutputFormat(Class value) {
    m_OutputFormat = value;
  }

  /**
   * Returns the output format.
   * 
   * @return		the format
   */
  public Class getOutputFormat() {
    return m_OutputFormat;
  }

  /**
   * Sets the output options.
   *
   * @param value	the options
   */
  public void setOutputOptions(String value) {
    m_OutputOptions = value;
  }

  /**
   * Returns the output options.
   *
   * @return		the options
   */
  public String getOutputOptions() {
    return m_OutputOptions;
  }

  /**
   * Sets the options.
   *
   * @param options	the options
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    Namespace	ns;

    try {
      ns = m_Parser.parseArgs(options);
    }
    catch (HelpScreenException e) {
      m_HelpRequested = true;
      return;
    }
    catch (Exception e) {
      m_Parser.printHelp();
      m_HelpRequested = false;
      throw e;
    }

    setInputFile(new File(ns.getString(INPUT_FILE)));
    setInputFormat(Class.forName(com.github.fracpete.gpsformats4j.formats.Format.class.getPackage().getName() + "." + ns.getString(INPUT_FORMAT)));
    setInputOptions(ns.getString(INPUT_OPTIONS));
    setOutputFile(new File(ns.getString(OUTPUT_FILE)));
    setOutputFormat(Class.forName(com.github.fracpete.gpsaltprofile4j.formats.Format.class.getPackage().getName() + "." + ns.getString(OUTPUT_FORMAT)));
    setOutputOptions(ns.getString(OUTPUT_OPTIONS));
  }

  /**
   * Returns the help.
   *
   * @return		the help
   */
  @Override
  public String toHelp() {
    return m_Parser.formatHelp();
  }

  /**
   * Turns the GPS trace information into altitude profile information.
   *
   * @param data	the trace
   * @return		the profile
   */
  protected List<CSVRecord> generate(List<CSVRecord> data) {
    List<CSVRecord> 	result;
    String		trackOld;
    String		trackCur;
    String		time;
    String 		lat;
    String 		lon;
    Point 		pointOld;
    Point 		pointCur;
    String		elev;
    double		dist;
    double		distTotal;
    Map<String,Integer>	map;
    String[] 		values;

    result    = new ArrayList<>();
    trackCur  = "";
    pointCur  = null;
    distTotal = 0.0;
    map       = new HashMap<>();
    map.put(Format.KEY_TRACK, 0);
    map.put(Format.KEY_TIME, 1);
    map.put(Format.KEY_DISTANCE, 2);
    map.put(Format.KEY_ELEVATION, 3);
    for (CSVRecord rec: data) {
      trackOld = trackCur;
      trackCur = rec.get(Format.KEY_TRACK);
      time     = rec.get(Format.KEY_TIME);
      elev     = rec.get(Format.KEY_ELEVATION);
      lat = rec.get(KEY_LAT);
      lon = rec.get(KEY_LON);
      pointOld = pointCur;
      pointCur = Point.at(
        Coordinate.fromDegrees(Double.parseDouble(lat)),
	Coordinate.fromDegrees(Double.parseDouble(lon)));
      if (!trackCur.equals(trackOld)) {
        distTotal = 0.0;
        values = new String[]{trackCur, time, "" + distTotal, elev};
        result.add(CSVRecordFactory.newRecord(values, map));
	continue;
      }

      if (pointOld == null)
        continue;

      dist = EarthCalc.gcdDistance(pointOld, pointCur);
      distTotal += dist;
      values = new String[]{trackCur, time, "" + distTotal, elev};
      result.add(CSVRecordFactory.newRecord(values, map));
    }

    return result;
  }

  /**
   * Performs the conversion.
   *
   * @return		null if successful, otherwise error message
   */
  protected String doExecute() {
    com.github.fracpete.gpsformats4j.formats.Format 		formatIn;
    com.github.fracpete.gpsaltprofile4j.formats.Format		formatOut;
    List<CSVRecord>						data;

    if (!m_InputFile.exists())
      return "Input file does not exist: " + m_InputFile;
    if (m_InputFile.isDirectory())
      return "Input file points to a directory: " + m_InputFile;

    try {
      formatIn = (com.github.fracpete.gpsformats4j.formats.Format) m_InputFormat.newInstance();
      m_Logger.info("Input format: " + formatIn.getClass().getName());
      formatOut = (com.github.fracpete.gpsaltprofile4j.formats.Format) m_OutputFormat.newInstance();
      m_Logger.info("Output format: " + formatOut.getClass().getName());
    }
    catch (Exception e) {
      return "Error configuring formats: " + e.toString();
    }

    if (!formatIn.canRead())
      return "Input format does not support reading!";

    if ((formatIn instanceof com.github.fracpete.gpsformats4j.formats.FormatWithOptionHandling) && !m_InputOptions.isEmpty()) {
      try {
	m_Logger.info("Input options: " + m_InputOptions);
	((com.github.fracpete.gpsformats4j.formats.FormatWithOptionHandling) formatIn).setOptions(OptionUtils.split(m_InputOptions));
      }
      catch (Exception e) {
        return "Failed to set options for input format: " + m_InputOptions + "\n" + e;
      }
    }

    if ((formatOut instanceof com.github.fracpete.gpsaltprofile4j.formats.FormatWithOptionHandling) && !m_OutputOptions.isEmpty()) {
      try {
	m_Logger.info("Output options: " + m_OutputOptions);
	((com.github.fracpete.gpsaltprofile4j.formats.FormatWithOptionHandling) formatOut).setOptions(OptionUtils.split(m_OutputOptions));
      }
      catch (Exception e) {
        return "Failed to set options for output format: " + m_OutputOptions + "\n" + e;
      }
    }

    data = formatIn.read(m_InputFile);
    if (data == null)
      return "Failed to read data from: " + m_InputFile;

    data = generate(data);
    if (data == null)
      return "Failed to generate altitude profile from: " + m_InputFile;

    return formatOut.write(data, m_OutputFile);
  }

  /**
   * Performs the conversion.
   *
   * @return		null if successful, otherwise error message
   */
  public String execute() {
    String	result;

    if (m_HelpRequested)
      return null;

    result = doExecute();
    if (result != null)
      m_Logger.error(result);
    else
      m_Logger.info("Successfully converted!");

    return result;
  }

  /**
   * Generates the profile.
   *
   * @param args	the options
   * @throws Exception	if something goes wrong, eg setting the options
   */
  public static void main(String[] args) throws Exception {
    Generate generate = new Generate();
    generate.setOptions(args);
    generate.execute();
  }
}
