package textEditor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A file chooser accessory that previews images.
 */
public class ImagePreviewer extends JPanel
{

   private final LinkedBlockingQueue<String> linkedBlockingQueue
           = new LinkedBlockingQueue<>();

   private JLabel label;
   private JTextArea textArea;

   private final Pattern musicReg = Pattern.compile(".+\\.(mp3|flac)$");
   private final Pattern txtReg = Pattern.compile(".+\\.(txt|java|class)$");
   private final Pattern wordReg = Pattern.compile(".+\\.(docx|doc)$");
   private final Pattern pictureReg = Pattern.compile(".+\\.(png|gif|jpg|jepg)$");
   /**
    * 构建一个图像预览图
    * @param chooser the file chooser whose property changes trigger an image
    *        change in this previewer
    */
   public   ImagePreviewer(JFileChooser chooser)
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
                  if (label != null) remove(label);
                  if (textArea != null) remove(textArea);
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
               if (f.getPath().matches(txtReg.pattern()))
               {
                  if (textArea == null) textArea = new JTextArea();
                  try {
                      String string = Files.readString(f.toPath(),StandardCharsets.UTF_8);
                      textArea.setText(string);
                      textArea.setEditable(false);
                      textArea.setBackground(new Color(30,30,30));
                      textArea.setForeground(Color.WHITE);
                      add(textArea,BorderLayout.CENTER);
                   } catch (IOException e) {
                       e.printStackTrace();
                       textArea.setText(e.getMessage());
                   }
               }

               //失败的读取.docx文件类型，我还是不太明白这个东西，不过，等我以后再学
//               if (f.getPath().matches(wordReg.pattern())) {
//                  long len = f.length();
//                  if (textArea == null) textArea = new JTextArea();
//                  try(InputStreamReader reader = new InputStreamReader(
//                             new BufferedInputStream(new FileInputStream(f))))
//                     {
//                        char[] chars = new char[(int) len];
//                        int read = reader.read(chars);
//                        String s= new String(chars);
//                        textArea.setText(s);
//                        textArea.setEditable(false);
//                        textArea.setBackground(new Color(30,30,30));
//                        textArea.setForeground(Color.WHITE);
//                        add(textArea,BorderLayout.CENTER);
//                     } catch (IOException e) {
//                        e.printStackTrace();
//                     }
//               }

               //读取音乐文件
               if (f.getPath().matches(musicReg.pattern())) {
                  long length = f.length();
                  int count = 0;
                  File file = new File("temp.txt");
//                  try(FileInputStream inputStream = new FileInputStream(f))
//                  {
//                     byte[] bytes = new byte[(int) length];
//                     inputStream.read(bytes);
//                     for (byte b : bytes) {
//                        if (count <30) {
//                           System.out.printf(b+"  ");
//                           count++;
//                        }
//
//                     }
//                  } catch (SecurityException | IOException e) {
//                     e.printStackTrace();
//                  }
                  Instant startTime = Instant.now();
                  Runnable read = () -> {
                      try(FileInputStream fileInputStream = new FileInputStream(f))
                      {
                         byte[] bytes = new byte[(int) length];
                         int read1 = fileInputStream.read(bytes);//这一步会阻塞
                         System.out.println(read1);
                         System.out.println(bytes.length);
                         for (byte b : bytes) linkedBlockingQueue.put(b+"  ");//这一步也会发生阻塞
                         linkedBlockingQueue.put("完成");
                         System.out.println("读取工完毕！");
                      } catch (FileNotFoundException | InterruptedException e) {
                          e.printStackTrace();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  };

                  new Thread(read,"读取者").start();

                  Runnable writr = () ->{
                     boolean done = false;
                      try {
                         while (!done) {
                            var take = linkedBlockingQueue.take();
                            if ("完成".equals(take)) {
                               done = true;
                               linkedBlockingQueue.put(take);
                               continue;
                            }
                            Files.writeString(file.toPath(),take, StandardOpenOption.APPEND);
                         }
                         System.out.println("写入工作完成！");

                      } catch (InterruptedException | IOException e) {
                          e.printStackTrace();
                      }
                  };

                  new Thread(writr,"写入者").start();


               }
//               {
//                  StringBuilder stringBuilder = new StringBuilder();
//                  String string="";
//                  Runnable reader = () -> {
//                      try(var in = new Scanner(f))
//                      {
//                          in.useDelimiter(reg);
//                          while (in.hasNext()) {
//                             String words = in.next();
//                             linkedBlockingQueue.put(words);
//                          }
//                          linkedBlockingQueue.put("完成");
//                      } catch (FileNotFoundException | InterruptedException e) {
//                          e.printStackTrace();
//                      }
//                  };
//
//                  new Thread(reader,"文件读取者").start();
//
//                  Runnable writer = () -> {
//                     boolean done = false;
//                      try {
//                         while (!done) {
//                            String take = linkedBlockingQueue.take();
//                            if ("完成".equals(take)) {
//                               done = true;
//                               linkedBlockingQueue.put(take);
//                               continue;
//                            }
//                            stringBuilder.append(take+"\n");
//                         }
//                         setText(stringBuilder.toString());
//                         setAutoscrolls(true);
//                      } catch (InterruptedException e) {
//                          e.printStackTrace();
//                          setText(e.getMessage());
//                      }
//                  };
//
//                  new Thread(writer,"写入者").start();
//
//               }


            }
         });

   }
}
