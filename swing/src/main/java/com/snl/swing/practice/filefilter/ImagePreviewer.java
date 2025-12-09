package com.snl.swing.practice.filefilter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

/**
 * A file chooser accessory that previews images.
 */
public class ImagePreviewer extends JPanel
{

   private final LinkedBlockingQueue<String> linkedBlockingQueue
           = new LinkedBlockingQueue<>();

   private JLabel label;

   private final Pattern pictureReg = Pattern.compile(".+\\.(png|gif|jpg|jepg)$");
   /**
    * 构建一个图像预览图
    * @param chooser the file chooser whose property changes trigger an image
    *        change in this previewer
    */
   public ImagePreviewer(JFileChooser chooser)
   {

      setPreferredSize(new Dimension(300, 200));
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new BorderLayout(20,20));
      chooser.addPropertyChangeListener(event ->
         {
            if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
            {
               // the user has selected a new file
               File f = (File) event.getNewValue();
               if (f == null)
               {
                  return;
               }
               if (f.getPath().matches(pictureReg.pattern()))
               {
                  if (label == null) label = new JLabel();
                  // read the image into an icon
                  var icon = new ImageIcon(f.getPath());
                  // if the icon is too large to fit, scale it
                  if (icon.getIconWidth() > getWidth())
                     icon = new ImageIcon(icon.getImage().getScaledInstance(
                             getWidth(), -1, Image.SCALE_SMOOTH));
                  else {
                     icon = new ImageIcon(icon.getImage().getScaledInstance(
                             getWidth(), -1, Image.SCALE_SMOOTH));
                  }
                  label.setIcon(icon);
                  add(label);
               }
            }
         });
   }
}
