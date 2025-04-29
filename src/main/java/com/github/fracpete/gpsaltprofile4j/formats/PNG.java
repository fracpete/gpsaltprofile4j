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
 * PNG.java
 * Copyright (C) 2018-2025 FracPete
 */

package com.github.fracpete.gpsaltprofile4j.formats;

import com.github.fracpete.gpsformats4j.core.OptionUtils;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * PNG format. Additional options:
 * <ol>
 *   <li>width</li>
 *   <li>height</li>
 * </ol>
 *
 * @author FracPete (fracpete at gmail dot com)
 */
public class PNG
  extends AbstractFormat
  implements FormatWithOptionHandling {

  public static final int DEFAULT_WIDTH = 1000;

  public static final int DEFAULT_HEIGHT = 200;

  /** the argument parser. */
  protected ArgumentParser m_Parser;

  /** the width. */
  protected int m_Width;

  /** the height. */
  protected int m_Height;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_Width  = DEFAULT_WIDTH;
    m_Height = DEFAULT_HEIGHT;
    
    m_Parser = ArgumentParsers.newArgumentParser(getClass().getSimpleName());

    m_Parser.description("Options for the PNG format.");
    m_Parser.defaultHelp(true);

    m_Parser.addArgument("--width")
      .metavar("<pixels>")
      .dest("width")
      .required(false)
      .type(Integer.class)
      .setDefault(DEFAULT_WIDTH)
      .help("The width of the image.");
    m_Parser.addArgument("--height")
      .metavar("<pixels>")
      .dest("height")
      .required(false)
      .type(Integer.class)
      .setDefault(DEFAULT_HEIGHT)
      .help("The height of the image.");
  }

  /**
   * Sets the width.
   * 
   * @param value	the width
   */
  public void setWidth(int value) {
    if (value > 0)
      m_Width = value;
  }

  /**
   * Returns the width.
   * 
   * @return		the width
   */
  public int getWidth() {
    return m_Width;
  }

  /**
   * Sets the height.
   * 
   * @param value	the height
   */
  public void setHeight(int value) {
    if (value > 0)
      m_Height = value;
  }

  /**
   * Returns the height.
   * 
   * @return		the height
   */
  public int getHeight() {
    return m_Height;
  }

  /**
   * For setting options.
   *
   * @param options	the options
   */
  public void setOptions(String[] options) throws Exception {
    Namespace ns;

    try {
      ns = m_Parser.parseArgs(options);
    }
    catch (HelpScreenException e) {
      // ignored
      return;
    }
    catch (Exception e) {
      throw new Exception("Failed to parse options: " + OptionUtils.flatten(options), e);
    }

    setWidth(ns.getInt("width"));
    setHeight(ns.getInt("height"));
  }

  /**
   * Returns the help string.
   *
   * @return		the help
   */
  public String toHelp() {
    return "Supported options:\n"
      + "  width: width in pixels for the output, default: " + DEFAULT_WIDTH + "\n"
      + "  height: height in pixels for the output, default: " + DEFAULT_HEIGHT + "\n";
  }

  /**
   * Writes to a file.
   *
   * @param data	the data to write
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  @Override
  public String write(List<CSVRecord> data, File output) {
    DefaultXYDataset	dataset;
    TDoubleList 	dist;
    TDoubleList 	elev;
    JFreeChart		jfreechart;
    XYPlot		plot;
    BufferedImage	image;

    dataset = new DefaultXYDataset();
    dist = new TDoubleArrayList();
    elev = new TDoubleArrayList();
    for (CSVRecord rec: data) {
      dist.add(Double.parseDouble("" +rec.get(KEY_DISTANCE)));
      elev.add(Double.parseDouble("" +rec.get(KEY_ELEVATION)));
    }
    dataset.addSeries("Elevation", new double[][]{elev.toArray(), dist.toArray()});

    jfreechart = ChartFactory.createXYLineChart(
      "", "Elevation", "Distance", dataset, PlotOrientation.HORIZONTAL, false, false, false);
    plot = (XYPlot) jfreechart.getPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setDomainGridlinesVisible(true);
    plot.setDomainGridlinePaint(Color.GRAY);
    plot.setRangeGridlinesVisible(true);
    plot.setRangeGridlinePaint(Color.GRAY);
    image = jfreechart.createBufferedImage(m_Width, m_Height);
    try {
      ImageIO.write(image, "png", output.getAbsoluteFile());
    }
    catch (Exception e) {
      return "Failed to write generated chart to: " + output + "\n" + e;
    }
    return null;
  }
}
