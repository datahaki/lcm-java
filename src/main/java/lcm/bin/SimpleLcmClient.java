// code by jph
package lcm.bin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SimpleLcmClient<L> extends BinaryLcmClient {
  protected final List<L> listeners = new CopyOnWriteArrayList<>();

  public SimpleLcmClient(String channel) {
    super(channel);
  }

  public final void addListener(L listener) {
    listeners.add(listener);
  }
}
