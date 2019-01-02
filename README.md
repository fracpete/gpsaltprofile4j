# gpsaltprofile4j

Little Java library for turning GPS traces with altitude information into altitude profiles. 

Uses *Spherical law of cosines* for calculating distances between points of the trace,
using the [geocalc](https://github.com/grumlimited/geocalc) library.

## Supported formats

Currently the following input formats are supported (using [gpsformats4j](https://github.com/fracpete/gpsformats4j)):

* CSV (requires columns in this order: track, time, lat, lon, elevation)
* [GPX](https://en.wikipedia.org/wiki/GPS_Exchange_Format)
* [TCX](https://en.wikipedia.org/wiki/Training_Center_XML)
* [KML](https://en.wikipedia.org/wiki/Keyhole_Markup_Language) (only `LineString` tag)

Current supported output formats:

* CSV (columns: track, time, distance, elevation)
* PNG (options: width, height)

## Example usage

Using it with the provided `generate.sh`/`generate.bat` scripts (custom dimensions for image):

```bash
./generate.sh --in_file test.gpx --in_format GPX --out_file out.csv --out_format PNG --out_options "width=2000 height=400"
```

Calling the `Generate` class directly:

```bash
java -cp "./lib/*" com.github.fracpete.gpsaltprofile4j.Generate \
  --in_file test.gpx --in_format GPX \
  --out_file out.png --out_format PNG --out_options "width=2000 height=400"
```

Using Java code:

```java
import com.github.fracpete.gpsaltprofile4j.Generate;
import java.io.File;
...
Genreate generate = new Generate();
generate.setInputFile(new File("test.gpx"));
generate.setInputFormat("GPX");
generate.setOutputFile(new File("out.png"));
generate.setOutputFormat("PNG");
generate.setOutputOptions("width=2000 height=400");
String msg = generate.execute();
// successful if null returned:
if (msg != null)
  System.err.println(msg);
```

## Maven

Use the following dependency to include the library in your Maven project:
```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>gpsaltprofile4j</artifactId>
      <version>0.0.1</version>
    </dependency>
```
