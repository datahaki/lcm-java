// code by jph
package lcm.bin;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface OfflineLogListener {
  /** function processes message from log file
   *
   * @param time since begin of log with unit micro seconds 1[us] == 1^-6[s]
   * @param channel
   * @param byteBuffer */
  void event(long utime, String channel, ByteBuffer byteBuffer);
}
