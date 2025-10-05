package wormsubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static java.awt.Color.*;

public class initPanel extends JPanel {
    private CustomButton customButton;
    private long timeStamp;
    private JLabel label;
    private int fps;
    private int crushCount;
    private boolean turn;
    private static Color[] listColor;
    private static final Random random = new Random();
    private static final int squareSize = 20;
    private static int size;
    private Pair[] body;
    private Pair food;
    private Login login;

    static {
        listColor = new Color[]{BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY,
                MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW};
    }

    public initPanel(JFrame jFrame) {
        setLayout(new BorderLayout());
        customButton = new CustomButton("进入游戏");
        timeStamp = System.currentTimeMillis();
        label = new JLabel("贪吃蛇");
        label.setForeground(WHITE);
        label.setFont(new Font("宋体", Font.PLAIN, 40));
        add(customButton, BorderLayout.SOUTH);
        add(label, BorderLayout.CENTER);
        setBackground(new Color(8, 39, 70));
        addMouseMotionListener(new MouseMotion());
        addMouseListener(new MouseHandler());
        customButton.addActionListener(event -> {
            if (login == null) login = new Login(jFrame);
        });
        timeStamp = System.currentTimeMillis();
        fps = 0;
        crushCount = 0;
        turn = true;
        body = new Pair[]{new Pair(0,0),new Pair(1,0),new Pair(2,0)};
        size = getWidth() / squareSize;

//        food = new Pair(random.nextInt(size),random.nextInt(size));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setLocation();
        Graphics2D g2 = (Graphics2D) g.create();
        Color color = new Color(10, 10, 10);
        g2.setColor(color);

        for (int i = 0; i < getWidth() / 10; i++) {
            g2.drawLine(i, 0, getWidth(), getHeight() - i);
            g2.drawLine(0, i, getWidth() - i, getHeight());
        }

        g2.setColor(new Color(26, 75, 65));
        for (int i = 0; i < getWidth() / 10; i++) {
            g2.drawLine(getWidth() - i, 0, 0, getHeight() - i);
            g2.drawLine(getWidth(), i, i, getHeight());
        }
        Color color1 = new Color(79, 227, 94);
        int R = color1.getRed();
        int G = color1.getGreen();
        int B = color1.getBlue();
        double factor = 0.45;
        R = (int) (R * factor);
        G = (int) (G * factor);
        B = (int) (B * factor);
        color1 = new Color(R, G, B);
        g2.setColor(color1);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < getWidth()/squareSize; i++) {
            g2.drawLine(0, i * squareSize, getWidth(), i * squareSize);
            g2.drawLine(i * squareSize, 0, i * squareSize, getHeight());
        }

        //绘制蛇体
        g2.setColor(Color.PINK);
        for (int i = 0; i < body.length; i++) {
            double x = body[i].getX();
            double y = body[i].getY();
            g2.fill(new Rectangle2D.Double(x*squareSize,y*squareSize,squareSize,squareSize));
        }
        move();

        g2.dispose();
        paintChildren(g);
    }

    public boolean contains(Point point) {
        Rectangle rectangle = customButton.getBounds();
        System.out.println(rectangle);
        if (rectangle.contains(point)) return true;
        return false;
    }

    private class MouseMotion implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent e) {

        }
    }

    private class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() >= 2)
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            else
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void setLocation() {
        int x = label.getX();
        if (x == 0) {
            turn=true;
            label.setForeground(listColor[random.nextInt(listColor.length)]);
        }
        if (x==this.getWidth()) {
            turn=false;
//            label.setForeground(listColor[random.nextInt(listColor.length)]);
        }
        if (turn) {
            x++;
//            label.setForeground(listColor[random.nextInt(listColor.length)]);
        }
        else {
            x--;
            label.setForeground(listColor[random.nextInt(listColor.length)]);
        }
        label.setLocation(x, label.getY());
    }

    public void move() {
        Pair head = body[2];
        int x = head.getX();
        x++;
        body[2] = new Pair(x,head.getY());
    }
}
