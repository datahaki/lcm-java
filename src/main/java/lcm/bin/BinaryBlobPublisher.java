// code by jph
package lcm.bin;

import lcm.lcm.LCM;

/** universal implementation that publishes the lcm type {@link BinaryBlob}
 * 
 * known use cases: Hdl32e, Vlp16, Urg04lxug01 */
public final class BinaryBlobPublisher implements ByteArrayConsumer {
  private final LCM lcm = LCM.getSingleton();
  private final String channel;

  public BinaryBlobPublisher(String channel) {
    this.channel = channel;
  }

  @Override // from ByteArrayConsumer
  public void accept(byte[] data, int data_length) {
    BinaryBlob binaryBlob = new BinaryBlob();
    binaryBlob.data_length = data_length;
    binaryBlob.data = data;
    accept(binaryBlob);
  }

  public void accept(BinaryBlob binaryBlob) {
    lcm.publish(channel, binaryBlob);
  }
}
