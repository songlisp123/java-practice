/*
 * ORABrowser.java
 * A *very* simple application that will display an HTML page.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class ORABrowser extends JFrame {

  JEditorPane htmlPane;

  public ORABrowser() {
    this("http://www.oreilly.com/");
  }

  public ORABrowser(String url) {
    super("ORABrowser 1.0");
    setSize(400,500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    if (!url.startsWith("http:")) {
      try {
	url = (new java.io.File(url)).toURL().toString();
      } catch (java.net.MalformedURLException mfe) {
	System.err.println("Invalid url: " + url);
	System.err.println("Using default of www.oreilly.com");
	url = "http://www.oreilly.com/";
      }
    }

    try {
      htmlPane = new JEditorPane();
      htmlPane.setEditable(false);

      // Here's where we force the pane to use our new editor kit
      htmlPane.setEditorKitForContentType("text/html", new ORAEditorKit());

      // And add our smarter listener
      htmlPane.addHyperlinkListener(new ORALinkListener(htmlPane));

      htmlPane.setPage(url);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    JScrollPane jsp = new JScrollPane(htmlPane); 
    getContentPane().add(jsp);

    // set up a menubar
    JMenuBar jmb = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    fileMenu.add(new ExitAction());
    JMenu viewMenu = new JMenu("View");
    viewMenu.add(new InfoAction());
    jmb.add(fileMenu);
    jmb.add(viewMenu);
    setJMenuBar(jmb);
  }

  public static void main(String args[]) {
    ORABrowser bb = null;
    if (args.length == 1) {
      bb = new ORABrowser(args[0]);
    }
    else if (args.length == 0) {
      bb = new ORABrowser();
    }
    else {
      System.err.println("Usage is: java ORABrowser [url]");
      System.exit(1);
    }
    bb.setVisible(true);
  }

  // All the action classes
  public class ExitAction extends AbstractAction {
    public ExitAction() {
      super("Exit");
    }
    public void actionPerformed(ActionEvent ae) {
      System.exit(0);
    }
  }

  public class InfoAction extends AbstractAction {
    public InfoAction() {
      super("View Page Info...");
    }
    public void actionPerformed(ActionEvent ae) {
      if (htmlPane != null) {
	HTMLDocument doc = (HTMLDocument)htmlPane.getDocument();
	if (doc != null) {
	  new HTMLDocInfoFrame(doc);
	}
	else {
	  System.err.println("Null document, cannot display info.");
	}
      }
      else {
	System.err.println("Null pane, cannot display info.");
      }
    }
  }
}

