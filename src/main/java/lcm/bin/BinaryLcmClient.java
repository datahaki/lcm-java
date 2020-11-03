// code by jph
package lcm.bin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import lcm.lcm.LCM;
import lcm.lcm.LCMDataInputStream;
import lcm.lcm.LCMSubscriber;
import lcm.lcm.SubscriptionRecord;

public abstract class BinaryLcmClient implements LcmClientInterface, LCMSubscriber {
  protected final String channel;
  private SubscriptionRecord subscriptionRecord = null;

  public BinaryLcmClient(String channel) {
    this.channel = channel;
  }

  @Override // from LcmClientInterface
  public final void startSubscriptions() {
    if (Objects.isNull(subscriptionRecord))
      subscriptionRecord = LCM.getSingleton().subscribe(channel, this);
    else
      System.err.println("already started subscription");
  }

  @Override // from LcmClientInterface
  public final void stopSubscriptions() {
    if (Objects.nonNull(subscriptionRecord)) {
      LCM.getSingleton().unsubscribe(subscriptionRecord);
      subscriptionRecord = null;
    }
  }

  @Override // from LCMSubscriber
  public final void messageReceived(LCM lcm, String channel, LCMDataInputStream ins) {
    try {
      BinaryBlob binaryBlob = new BinaryBlob(ins);
      ByteBuffer byteBuffer = ByteBuffer.wrap(binaryBlob.data);
      byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
      messageReceived(byteBuffer);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /** callback function
   * 
   * @param byteBuffer with content of lcm message; encoding set to little endian */
  protected abstract void messageReceived(ByteBuffer byteBuffer);
}
