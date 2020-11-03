// code by jph
package lcm.bin;

public enum BinaryBlobs {
  ;
  public static BinaryBlob create(int length) {
    BinaryBlob binaryBlob = new BinaryBlob();
    binaryBlob.data_length = length;
    binaryBlob.data = new byte[length];
    return binaryBlob;
  }
}
