package com.snl.swing.music.ui.filefilter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A file chooser accessory that previews images.
 */
public class ImagePreviewer extends JPanel
{

   private JLabel label;
   private JTextArea area;
   private Task task;
   private File selecedFile;
   private File oldFile;

   private final Pattern pictureReg = Pattern.compile(".+\\.(png|gif|jpg|jepg)$");
   private final Pattern javaReg = Pattern.compile(".+\\.(java|class)");
   private final Pattern musicReg = Pattern.compile(".+\\.(java|class)");
   /**
    * 构建一个图像预览图
    * @param chooser the file chooser whose property changes trigger an image
    *        change in this previewer
    */
   public ImagePreviewer(JFileChooser chooser)
   {

      setPreferredSize(new Dimension(400, 200));
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new BorderLayout(20,20));
      area = new JTextArea();
      area.setLineWrap(true);
      area.setWrapStyleWord(true);
      area.setBackground(Color.black);
      area.setForeground(Color.cyan);
      chooser.addPropertyChangeListener(event ->
         {
            if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
            {
               // the user has selected a new file
               oldFile = selecedFile;
               selecedFile = (File) event.getNewValue();
               if (selecedFile == null)
               {
                  return;
               }
//               if (selecedFile.getPath().matches(pictureReg.pattern()))
//               {
//                  if (label == null) label = new JLabel();
//                  // read the image into an icon
//                  var icon = new ImageIcon(selecedFile.getPath());
//                  // if the icon is too large to fit, scale it
//                  if (icon.getIconWidth() > getWidth())
//                     icon = new ImageIcon(icon.getImage().getScaledInstance(
//                             getWidth(), -1, Image.SCALE_SMOOTH));
//                  else {
//                     icon = new ImageIcon(icon.getImage().getScaledInstance(
//                             getWidth(), -1, Image.SCALE_SMOOTH));
//                  }
//                  label.setIcon(icon);
//                  add(label);
//               }
               if (selecedFile.getPath().matches(javaReg.pattern())) {
                  add(new JScrollPane(area));
                  repaint();
                  if (task != null && !task.isDone()) {
                     task.cancel(true);
                  }
                  if (Objects.equals(oldFile,selecedFile)) {
                     return;
                  }
                  area.setText("");
                  task = new Task();
                  task.execute();
               }
            }
         });
   }

   class Task extends SwingWorker<Void,String> {

      @Override
      protected Void doInBackground() throws Exception {
         try(Stream<String> lines = Files.lines(selecedFile.toPath(), StandardCharsets.UTF_8)) {
            lines.forEach(e->{
               if (task.isCancelled()) return;
               publish(e);
            });
         }
         return null;
      }

      @Override
      protected void process(List<String> chunks) {
         for (String s : chunks)
            area.append(s+"\n");
      }
   }
}
