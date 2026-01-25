package com.snl.test.image.homework01;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class OptionFrame extends JFrame implements MaskPropertyListener, ColorCompomentImplement {

    private ImagePanel imagePanel;
    private JButton executeButton;
    private JButton loadImageButton;
    private JButton resetButton;
    private JTable table;
    private TableModelImplement tableModel;
    private JFileChooser chooser;
    private JFrame f;
    private File oldSelectedFile;
    private File currentFile;

    public OptionFrame(String title) throws HeadlessException {
        super(title);
        createUi();
    }

    public OptionFrame() throws HeadlessException {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "是否想要退出",
                        "退出窗口", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                if (i == JOptionPane.YES_OPTION)
                {
                    //退出程序
                    System.exit(0);
                }
            }
        });
        createImageFrame(); //必须先调用这个
        createUi(); //创建ui
        Utilies.center(this); //居中框架
        setSize(400,300); //设置大小
        setVisible(true); //设计可见性
    }

    /**
     * 创建图像框架
     */
    private void createImageFrame() {
        f = new JFrame("图像");

        imagePanel = new ImagePanel("Ours_en_peluche_-_15.jpg");
        imagePanel.addMaskPropertyListener(this);
        imagePanel.addColorCompomentImplement(this);
        f.setContentPane(imagePanel);
        f.setResizable(false); //设置不能改变大小
        f.setLocationRelativeTo(this); //设置相对位置
        f.pack(); //设置首选大小
        f.setVisible(true); //设置可见性
    }

    /**
     * 创建ui
     */
    private void createUi() {
        JPanel contentPane = (JPanel) getContentPane();
        LayoutManager layout = contentPane.getLayout();
        if (!(layout instanceof GridBagLayout))
        {
            layout = new GridBagLayout();
            contentPane.setLayout(layout);
        }

        executeButton = new CustomButton("执行");
        executeButton.addActionListener(imagePanel);
        executeButton.setEnabled(false);
        executeButton.setToolTipText("执行对图像区域的颜色分量选择");
        tableModel = new TableModelImplement();
        //添加事件
        tableModel.addTableModelListener(imagePanel);
        table = new JTable(tableModel);
        //设置静态渲染器
        table.setDefaultRenderer(Integer.class,new SimpleTableCellRenderer());


        loadImageButton = new CustomButton("加载文件");
        loadImageButton.setToolTipText("从磁盘中加载文件");
        loadImageButton.addActionListener(e -> {
            if (chooser == null)
            {
                chooser = new JFileChooser(".");
                chooser.setFileFilter(new ImageFilter());
                chooser.setFileView(new FileViewDemo());
                chooser.setAccessory(new ImagePreviewer(chooser));
            }
            int i = chooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION)
            {
                //如果选打开
                oldSelectedFile = currentFile;
                currentFile = chooser.getSelectedFile();
                if (currentFile == null ||
                        Objects.equals(oldSelectedFile,currentFile))
                    //如果当前选择为null或者选择相同
                    return;
                if (currentFile.isFile() && currentFile.canRead())
                {
                    //否则
                    BufferedImage image = Utilies.makeBufferImage(Utilies.blockingLoad(currentFile));
                    imagePanel.setmImage(image); //设置图像
                    Utilies.resizeFrame(f,imagePanel);
                    //消除遮罩
                    imagePanel.setMaksShape(null);
                    //清楚模型数据
                    clearModelData();
                    f.revalidate();
                    f.repaint();
                }
            }
        });
        //重置按钮
        resetButton = new CustomButton("重置");
        resetButton.setToolTipText("重置遮罩和颜色数据");
        resetButton.addActionListener(e -> {
            //TODO
            imagePanel.setMaksShape(null); //清空遮罩
            imagePanel.repaint(); //重绘
            clearModelData();
        });
        resetButton.setEnabled(false);
        alignSpace(contentPane);
    }

    /**
     * 分配空间
     * @param contentPane 内容面板
     */
    private void alignSpace(JPanel contentPane) {
        GridBagConstraints c = new GridBagConstraints();
        JScrollPane pane = new JScrollPane(table);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weightx = 0.8f;
        c.weighty = 0.7f;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(20,10,0,10);
        contentPane.add(pane,c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 0.3f;
        c.weighty = 0.1f;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0,10,20,0);
        contentPane.add(executeButton,c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.weighty = 0.1f;
        c.insets = new Insets(0,0,20,0);
        contentPane.add(loadImageButton,c);

        c.gridx = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0,0,20,0);
        contentPane.add(resetButton,c);
    }

    /**
     * 重置底层表格模型数据
     */
    private void clearModelData() {
        tableModel.clear();
        repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(OptionFrame::new);
    }


    @Override
    public void updateMaks(RectangularShape mask) {
        //清除数据
        if (mask == null)
        {
            //如果遮罩为null
            resetButton.setEnabled(false);
            executeButton.setEnabled(false);
        }else {
            resetButton.setEnabled(true);
            executeButton.setEnabled(true);
        }
    }

    @Override
    public void updateColors(int x, int y, int[] comps, int rgb) {
        tableModel.addElements(x,y,comps,rgb);
        repaint();
    }
}
