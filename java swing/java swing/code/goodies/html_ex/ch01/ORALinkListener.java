/*
 * ORALinkListener.java
 * A hyperlink listener for use with JEditorPane.  This
 * listener will change the cursor over hotspots based on enter/exit
 * events and also load a new page when a valid hyperlink is clicked.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class ORALinkListener implements HyperlinkListener {

  private JEditorPane pane;       // The pane we're using to display HTML

  private JTextField  urlField;   // An optional textfield for showing
                                  // the current URL being displayed

  private JLabel statusBar;       // An option label for showing where
                                  // a link would take you
  private HashMap localKeys = new HashMap();

  public ORALinkListener(JEditorPane jep, JTextField jtf, JLabel jl) {
    pane = jep;
    urlField = jtf;
    statusBar = jl;
  }

  public ORALinkListener(JEditorPane jep) {
    this(jep, null, null);
  }

  public void hyperlinkUpdate(HyperlinkEvent he) {
    HyperlinkEvent.EventType type = he.getEventType();
    // Ok.  Decide which event we got...
    if (he instanceof HTMLFrameHyperlinkEvent) {
      JOptionPane.showMessageDialog(null, "Frame Event");
    }
    if (type == HyperlinkEvent.EventType.ENTERED) {
      // Enter event.  Go the the "hand" cursor and fill in the status bar
      AttributeSet anchor = ORAEditorKit.currentAnchor;
      Object att = getLocalAttributeKey("onmouseover", anchor);
      if (att != null) {
	// Ok, at least there's one onmouseover event...
	String request = (String)anchor.getAttribute(att);
	if (request != null) {
	  handleORARequest(request);
	}
      }
      if (statusBar != null) {
	statusBar.setText(he.getURL().toString());
      }
    }
    else if (type == HyperlinkEvent.EventType.EXITED) {
      // Exit event.  Go back to the default cursor and clear the status bar
      if (statusBar != null) {
	statusBar.setText(" "); // must be a space or it disappears
      }
    }
    else {
      // Jump event.  Get the url, and if it's not null, switch to that
      // page in the main editor pane and update the "site url" label.
      if (he instanceof HTMLFrameHyperlinkEvent) {
	HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)he;
	HTMLDocument doc = (HTMLDocument)pane.getDocument();
	doc.processHTMLFrameHyperlinkEvent(evt);
      } else {
	try {
	  pane.setPage(he.getURL());
	  if (urlField != null) {
	    urlField.setText(he.getURL().toString());
	  }
	}
	catch (FileNotFoundException fnfe) {
	  pane.setText("Could not open file: <tt>" + he.getURL() + 
		       "</tt>.<hr>");
	}
	catch (Exception e) {
	  e.printStackTrace();
	}
      }
    }
  }

  private Object getLocalAttributeKey(String key, AttributeSet as) {
    key = key.toLowerCase();
    if (localKeys.containsKey(key)) {
      return localKeys.get(key);
    }
    else if (as == null) {
      return null;
    }
    else {
      Enumeration enum = as.getAttributeNames();
      while (enum.hasMoreElements()) {
	Object o = enum.nextElement();
	if (o.toString().equals(key)) {
	  localKeys.put(key, o);
	  return o;
	}
      }
    }
    return null;
  }

  // Just a simple helper method to display "alert" boxes.
  // We understand the following request:
  //  <a ... onmouseover="orascript:alert('Message');" ... >
  private void handleORARequest(String request) {
    String req = request.toLowerCase();
    int start = req.indexOf("orascript:alert");
    if (start != -1) {
      // Hooray! Something we can deal with.
      int end = request.lastIndexOf("'");
      String msg = request.substring(start + 17, end);
      JOptionPane.showMessageDialog(null, msg);
    }
    else {
      JOptionPane.showMessageDialog(null, "Unknown ORAScript:\n" + request);
    }
  }
}
