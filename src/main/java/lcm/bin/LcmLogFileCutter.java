// code by jph
package lcm.bin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import lcm.logging.Log;
import lcm.logging.Log.Event;
import lcm.logging.LogEventWriter;

// TODO JPH class design: the actions should not happen in the constructor
public abstract class LcmLogFileCutter {
  /** lcm self test contains the string "lcm self test" as bytes */
  private static final String LCM_SELF_TEST = "LCM_SELF_TEST";
  // ---
  private final List<File> list = new LinkedList<>();

  /** @param src
   * @param navigableMap with entries that encode start and end
   * of segments that should be extracted to separate files
   * @throws IOException */
  public LcmLogFileCutter(File src, NavigableMap<Integer, Integer> navigableMap) throws IOException {
    try (Log log = new Log(src.toString(), "r")) {
      int index = 0;
      for (Entry<Integer, Integer> entry : navigableMap.entrySet()) {
        /** break point lo */
        final int lo = entry.getKey() - 1;
        /** break point hi */
        final int hi = entry.getValue();
        File dst = filename(index);
        if (dst.exists()) {
          System.out.println("deleting: " + dst);
          dst.delete();
        }
        LogEventWriter logEventWriter = new LogEventWriter(dst);
        {
          while (true) {
            Event event = log.readNext();
            if (lo <= event.eventNumber)
              break;
          }
          while (true) {
            Event event = log.readNext();
            if (!event.channel.equals(LCM_SELF_TEST))
              try {
                new BinaryBlob(event.data);
                logEventWriter.write(event);
              } catch (Exception exception) {
                System.err.println("problem in channel:\n" + event.channel);
                exception.printStackTrace();
              }
            if (hi <= event.eventNumber)
              break;
          }
        }
        logEventWriter.close();
        list.add(dst);
        ++index;
      }
    }
  }

  /** @return */
  public final List<File> files() {
    return Collections.unmodifiableList(list);
  }

  /** @param index
   * @return file to which extract entry with given index */
  public abstract File filename(int index);
}
