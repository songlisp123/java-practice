package com.snl.swing.homework01.ui.fileCHooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.regex.Pattern;

/**
 * 图像预览器
 */
public class ImagePreviewer extends JPanel
{

   private JLabel label;
   private File selecedFile;
   private File oldFile;

   private final Pattern pictureReg = Pattern.compile(".+\\.(png|gif|jpg|jepg)$");

   /**
    * 构建一个图像预览图
    * @param chooser 文件过滤器
    */
   public ImagePreviewer(JFileChooser chooser)
   {

      setPreferredSize(new Dimension(200, 200));
      setBorder(BorderFactory.createEtchedBorder());
      setLayout(new BorderLayout(20,20));

      chooser.addPropertyChangeListener(event ->
         {
            if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
            {
               // 用户选择新文件
               oldFile = selecedFile;
               selecedFile = (File) event.getNewValue();
               if (selecedFile == null)
               {
                  return;
               }
               if (selecedFile.getPath().matches(pictureReg.pattern()))
               {
                  if (label == null) label = new JLabel();
                  //读取文件图标
                  var icon = new ImageIcon(selecedFile.getPath());
                  // 如果图标不适合，采用光滑算法
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
