/*
 * ORAEditorKit.java
 * A simple extension of the HTMLEditor kit that uses a verbose
 * ViewFactory.
 */

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.Serializable;
import java.net.*;

public class ORAEditorKit extends HTMLEditorKit {
  // We're going to add a custom ORA tag and hide any enclosed text
  // from the end user.
  public static HTML.Tag ORA = new HTML.UnknownTag("ora");

  // We will also keep track of the current <a> tag, in an
  // admittedly non-thread-friendly manner.
  public static AttributeSet currentAnchor;

  public void install(JEditorPane paneEditor)
  {
    LinkController linkController = new LinkController() ;
    
    // Use our patched link controller for both mouse clicks and motions
    paneEditor.addMouseListener(linkController) ;
    paneEditor.addMouseMotionListener(linkController) ;

    // Manually set our H1 tags to use a 48-pt font.
    StyleSheet ss = getStyleSheet();
    Style s = ss.getStyle("h1");
    StyleConstants.setFontSize(s, 48);
  }

  // the new patched link controller
  public static class LinkController extends HTMLEditorKit.LinkController
    implements Serializable
  {
    public void mouseClicked(MouseEvent me) {
      JEditorPane jep = (JEditorPane)me.getSource();
      Document doc = jep.getDocument();
      if (doc instanceof HTMLDocument) {
	HTMLDocument hdoc = (HTMLDocument) doc;
	int pos = jep.viewToModel(me.getPoint());
	Element e = hdoc.getCharacterElement(pos);
	AttributeSet a = e.getAttributes();

	// all that work gets us the attribute set associated with
	// the current <a> tag, which we now store:
	currentAnchor = (AttributeSet) a.getAttribute(HTML.Tag.A);
      }
      super.mouseClicked(me);
    }

    // and ditto for the mouseMoved method as we pass over links...
    public void mouseMoved(MouseEvent me) {
      JEditorPane jep = (JEditorPane)me.getSource();
      Document doc = jep.getDocument();
      if (doc instanceof HTMLDocument) {
	HTMLDocument hdoc = (HTMLDocument) doc;
	int pos = jep.viewToModel(me.getPoint());
	Element e = hdoc.getCharacterElement(pos);
	AttributeSet a = e.getAttributes();
	currentAnchor = (AttributeSet) a.getAttribute(HTML.Tag.A);
      }
      super.mouseMoved(me);
    }
  }

  // We're overriding some of the view information, so supply our
  // own version of the factory.
  public ViewFactory getViewFactory() {
    return new VerboseViewFactory();
  }

  public static class VerboseViewFactory extends HTMLEditorKit.HTMLFactory {
    public boolean hideText = false;
    
    // This view factory spits out lots of debugging information and also
    // hides any content between <ora> and </ora> tags.
    public View create(Element elem) {
      System.out.print("Element: " + elem.getName());
      Object o=elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
      HTML.Tag kind = (HTML.Tag) o;
      System.out.println(" view as: " + o);
      dumpElementAttributes(elem);
      if (kind.equals(ORA)) {
	hideText = !hideText;
	System.out.println("Found ORA tag, hiding: " + hideText);
      }
      return hideText ? new NoView(elem) : super.create(elem);
    }

    private void dumpElementAttributes(Element elem) {
      AttributeSet attrs = elem.getAttributes();
      java.util.Enumeration names = attrs.getAttributeNames();
      while (names.hasMoreElements()) {
	Object key = names.nextElement();
	System.out.println("  " + key + " : " + attrs.getAttribute(key));
      }
      try {
	System.out.println("  " + 
			   elem.getDocument().getText(elem.getStartOffset(), 
						      elem.getEndOffset()));
      } catch (Exception e) {
      }
    }
  }

  // Our own custom "no view" class to hide any content found inside
  // <ora> and </ora> tags.  Basically, we return a 0x0 bounding box
  // and do nothing in the paint() method.

  public static class NoView extends View {
    public NoView(Element elem) {
      super(elem);
      setSize(0.0f, 0.0f);
    }
   
    public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
      return 0;
    }
    public Shape modelToView(int pos, Shape a, Position.Bias b)
      throws BadLocationException 
    {
      return new Rectangle(0, 0);
    }
    public float getPreferredSpan(int axis) {
      return 0.0f;
    }
    public void paint(Graphics g, Shape allocation) {
    }
  }
}
