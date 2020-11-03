// code by jph
package lcm.bin;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lcm.logging.Log;
import lcm.logging.Log.Event;

/** Remark:
 * the timestamps of the lcm event sequence as recorded are NOT guaranteed to be monotonous.
 * occasionally, the event that follows another may have a smaller timestamp.
 * Observed "step backs" are 1[us], 18[us], or 70[us]. */
public enum OfflineLogPlayer {
  ;
  /** lcm self test contains the string "lcm self test" as bytes */
  private static final String LCM_SELF_TEST = "LCM_SELF_TEST"; // TODO redundant
  private static final String END_OF_FILE = "EOF";

  public static void process(File file, OfflineLogListener... offlineLogListeners) throws IOException {
    process(file, Arrays.asList(offlineLogListeners));
  }

  public static void process(File file, Collection<? extends OfflineLogListener> offlineLogListeners) throws IOException {
    Set<String> set = new HashSet<>();
    set.add(LCM_SELF_TEST);
    try (Log log = new Log(file.toString(), "r")) {
      Long tic = null;
      // int count = 0;
      while (true) {
        Event event = log.readNext();
        if (Objects.isNull(tic))
          tic = event.utime;
        BinaryBlob binaryBlob = null;
        try {
          binaryBlob = new BinaryBlob(event.data);
        } catch (Exception exception) {
          if (set.add(event.channel))
            System.err.println("not a binary blob: " + event.channel);
        }
        if (Objects.nonNull(binaryBlob)) {
          long utime = event.utime - tic;
          // Scalar time = UnitSystem.SI().apply(Quantity.of(event.utime - tic, NonSI.MICRO_SECOND));
          for (OfflineLogListener offlineLogListener : offlineLogListeners)
            offlineLogListener.event(utime, event.channel, ByteBuffer.wrap(binaryBlob.data).order(ByteOrder.LITTLE_ENDIAN));
          // ++count;
          // if (count%10_000==0)
          // System.out.println(count);
        }
      }
    } catch (Exception exception) {
      if (!END_OF_FILE.equals(exception.getMessage()))
        throw exception;
    }
  }

  public static RuntimeException endOfFile() {
    return new RuntimeException(END_OF_FILE);
  }
}
