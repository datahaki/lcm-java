// code by jph
package lcm.bin;

/** consumer of a byte array */
@FunctionalInterface
public interface ByteArrayConsumer {
  /** @param data
   * @param length */
  void accept(byte[] data, int length);

  /** @param data */
  default void accept(byte[] data) {
    accept(data, data.length);
  }
}
