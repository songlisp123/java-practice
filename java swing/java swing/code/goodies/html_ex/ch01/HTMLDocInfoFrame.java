/*
 * HTMLDocInfoFrame.java
 * A display tool for the structure of an HTMLDocument.
 * Using iterators, you can display any tags you like.
 */

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.BorderLayout;
import java.util.Enumeration;

public class HTMLDocInfoFrame extends JFrame {

  public HTMLDocInfoFrame(HTMLDocument doc) {
    super("Page Info");
    setSize(400,300);

    JEditorPane jep = new JEditorPane();
    getContentPane().add(new JScrollPane(jep), BorderLayout.CENTER);
    jep.setEditable(false);

    // Now fill the text area
    String results = "<html><body><h1>Page Structure</h1><hr><br>\n";
    results += "Links:<br>";
    HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
    while (it.isValid()) {
      String href = it.getAttributes()
	.getAttribute(HTML.Attribute.HREF).toString();
      results += ("<a href=\"" + href + "\">" + href + "</a><br>\n");
      it.next();
    }

    results += "<br>Images:<br>";
    TagIterator it2 = new TagIterator(HTML.Tag.IMG, doc);
    while (it2.isValid()) {
      AttributeSet a = it2.getAttributes();
      String src = a.getAttribute(HTML.Attribute.SRC).toString();
      results += ( "<a href=\"" + src + "\">Image: " + src + "</a><br>" );
      it2.next();
    }

    results += "<br><hr><br>Unknowns:<br>";
    it2 = new TagIterator(new HTML.UnknownTag("ora"), doc);
    while (it2.isValid()) {
      AttributeSet a = it2.getAttributes();
      Enumeration enum = a.getAttributeNames();
      results += "<ul>";
      while (enum.hasMoreElements()) {
	Object o = enum.nextElement();
	results += ( "<li> " + o + " (" + o.getClass() + ") : " + a.getAttribute(o));
      }
      results += "</ul><br>";
      it2.next();
    }

    jep.setContentType("text/html");
    jep.setText(results);
    HTMLDocument newDoc = (HTMLDocument)jep.getDocument();
    newDoc.setBase(doc.getBase());
System.err.println("Done...");

    setVisible(true);
  }

  // Our own version of an Iterator that allows us to look at non-leaf
  // tags as well.
  public static class TagIterator {

    private HTML.Tag tag;
    private ElementIterator pos;

    TagIterator(HTML.Tag t, Document doc) {
      tag = t;
      pos = new ElementIterator(doc);
      next();
    }

    /**
     * Fetch the attributes for this tag.
     */
    public AttributeSet getAttributes() {
      Element elem = pos.current();
      if (elem != null) {
	AttributeSet a = (AttributeSet)elem.getAttributes().getAttribute(tag);
	if (a == null) {
	  return elem.getAttributes();
	}
	return a;
      }
      return null;
    }

    /**
     * Move the iterator forward to the next occurence
     * of the tag it represents.
     */
    public void next() {
      for (pos.next(); isValid(); pos.next()) {
	Element elem = pos.current();
	if (elem.getName().equals(tag.toString())) {
	  break;
	}
	AttributeSet a = pos.current().getAttributes();
	if (a.isDefined(tag)) {
	  // we found the next one
	  break;
	}
      }
    }

    /**
     * Type of tag this iterator represents.
     */
    public HTML.Tag getTag() {
      return tag;
    }

    public boolean isValid() {
      return (pos.current() != null);
    }
  }
}
