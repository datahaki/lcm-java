// code by lcm
package lcm.spy;

import javax.swing.Action;
import javax.swing.JDesktopPane;

/** A class which can provide a visualization of a data type. **/
public interface SpyPlugin {
  /** return true if you can do something intelligent with data types matching
   * cls. **/
  boolean canHandle(long fingerprint);

  /** Return an action suitable for adding to a popup menu that will display a
   * viewer for the given channel. **/
  Action getAction(JDesktopPane jdp, ChannelData cd);
}
