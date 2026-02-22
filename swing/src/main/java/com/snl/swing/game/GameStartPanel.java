package com.snl.swing.game;

import com.snl.swing.game.components.*;
import com.snl.swing.game.components.enm.DirectionRow;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.keyBoard.SimpleCleanKeyBoard;
import com.snl.swing.game.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

public class GameStartPanel extends DiKaErPlus implements CollideEventListener, SlideDataChangeListener {

    private SimpleCleanKeyBoard keyBoard;
    private CustomButton start;
    private CustomButton backButton;

    private static final int GAME_PAGE = -1;
    private static final int GAME_ON = 0;
    private static final int GAME_START = 1;
    private static final int GAME_RUNNING = 2;
    private static final int GAME_STOP = 3;
    private static final int GAME_COMPLETED = 4;
    private static final int GAME_FILLED = 5;
    private static final int GAME_END = 6;
    private static final int GAME_EXCEPTION = 100;

    private int gameState;

    Font f1 = Utils.font;
    Font font = new Font("隶书",Font.BOLD | Font.ITALIC,30);

    //游戏开始
    int wordRight,wordLeft;
    int pictureW,pictureH;
    int total;
    int startIndex;
    final  String[] strings = {
            "a", "2","3","4","5","6","7","8","9","10","j","q","k"
    };
    final char[] chars = {'c','d','h','s'};
    Shape[] shapes;
    //渲染静态图像
    Image[] bufferedImages,cacheImages;
    Image[][] testImages;
    private int pictureTop,pictureLeft;
    private int clickedIndex;
    private Row leftRow,rightRow;
    CustomButton startButton;
    private double animation;
    private int  aIndex;
    double time,speed,amplitude,offsetY;
    private Slide slide;

    //绘制文本

    public GameStartPanel() throws HeadlessException {
        WIDTH = 600;
        HEIGHT = 600;
        appSleep = 16;
        drawAxis = false;
        gameState = GAME_ON;
        //选择页面
        total = 56;
        wordLeft = wordRight = 20;
        pictureW = pictureH = 250;
        pictureTop = pictureLeft = 50;
        shapes = new Shape[3];
        bufferedImages = new Image[total];
        cacheImages = new Image[total];
        testImages = new Image[total][shapes.length];
        ioTask ioTask = new ioTask();
        ioTask.run();

        speed = Math.PI / 2.0;
        amplitude = 4;
    }

    private void fillShapes() {
        int left = 100;
        int right = 100;
        int top = 50;
        int hGap = 10;
        int y = pictureTop + pictureH + top;
        int x  = left;
        int rw = (c.getWidth() - (left + right) - hGap) / 3;
        int rh = rw + 20;
        for (int  i = 0;i<shapes.length;i++) {
            x = (i == 0) ? x : x + rw + hGap ;
            Shape r = new Rectangle2D.Double(x,y,rw,rh);
            shapes[i] = r;
        }
        int lx = 20;
        int w = 50,h = 50;
        int ly = y + (rh - h) / 2;
        leftRow = new Row(lx,ly,w,h, DirectionRow.WEST);
        lx = c.getWidth() - 20 - w;
        rightRow = new Row(lx,ly,w,h, DirectionRow.EAST);

        leftRow.addListeners(this);
        rightRow.addListeners(this);

        //这是什么东西
        ly = y + rh + 20;
        slide = new Slide(left,ly,400,10,total-shapes.length);
        slide.addListener(this);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        int w = c.getWidth();
        int h = c.getHeight();
        keyBoard = new SimpleCleanKeyBoard(0,h / 2.0,w,h / 2.0);
        keyBoard.setShowingInputFrame(false);

        start = new CustomButton(0,30,w,30,"进入游戏");
        start.addListeners(this);
        start.setClickedString("enter");

        backButton = new CustomButton(w - 100,20,75,30,"返回");
        backButton.addListeners(this);
        backButton.setClickedString("back");

        startButton = new CustomButton(0,h - 40,w,20,"开始");
        startButton.addListeners(this);
        startButton.setClickedString("start");
        fillShapes();
    }


    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        keyBoard.processInput(mouseInputEvent,keyBoardEvent);
        switch (gameState)
        {
            case GAME_ON -> start.processInput(mouseInputEvent);
            case GAME_START -> {
                backButton.processInput(mouseInputEvent);
                leftRow.processInput(mouseInputEvent);
                rightRow.processInput(mouseInputEvent);
                startButton.processInput(mouseInputEvent);
                slide.processInput(mouseInputEvent);
            }
        }

    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D po = mouseInputEvent.getCurrentPoint();
        switch (gameState) {
            case GAME_PAGE -> {}
            case GAME_ON -> {
                keyBoard.update(delta,po);
                start.update(delta);
            }
            case GAME_START -> {
                int temp = checkPos(po, shapes);
                clickedIndex = temp != -1 ? temp : clickedIndex;
                backButton.update(delta);
                startButton.update(delta);
                leftRow.update(delta);
                rightRow.update(delta);
                slide.update(delta,po);
                double frameCount = 1.5 / 8.0;
                animation += delta;
                while (animation >= frameCount)
                {
                    animation -= frameCount;
                    aIndex = ++aIndex % 8;
                }
                time += delta;
                slide.setValue(startIndex);
            }
        }
    }

    @Override
    protected void draw(Graphics g) {
//        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        switch (gameState) {
            case GAME_PAGE -> {}
            case GAME_ON -> {
                keyBoard.draw(g2,Color.WHITE);
                drawString(g2);
                start.draw(g2);
            }
            case GAME_START -> {
                drawTest(g2);
                backButton.draw(g2);
            }
        }
        g2.dispose();
    }

    private void drawTest(Graphics2D g2) {
        g2.setColor(Color.red);
        FontRenderContext frc = g2.getFontRenderContext();
        //TODO
        String string = keyBoard.getInputString();
        AttributedString as = new AttributedString(string);
        as.addAttribute(TextAttribute.FONT,Utils.font02.deriveFont(30F));
        as.addAttribute(TextAttribute.FOREGROUND,Color.WHITE);
        as.addAttribute(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
        TextLayout tl = new TextLayout(as.getIterator(),frc);
        drawText(g2,0,20,c.getWidth(),tl);

        //绘制图像
        Image image = bufferedImages[startIndex + clickedIndex];
        double sin = Math.sin(time * speed);
        offsetY = sin * amplitude;
        double scale = 1.0 + sin * 0.01;
        if (image != null) {
            int newW = (int) (image.getWidth(null) * scale);
            int newH = (int) (image.getHeight(null) * scale);
            g2.drawImage(image,
                    (int) (pictureLeft - (newW - image.getWidth(null)) / 2.0),
                    (int) (pictureTop + offsetY - (newH - image.getHeight(null)) / 2.0),
                    newW, newH, null);
        }


        double tx = c.getWidth() / 2.0 + wordLeft;
        double ty = pictureTop;
        float wrappingWidth = (float) (tx - 2 *wordLeft - wordRight);
        AffineTransform temp = g2.getTransform();
        g2.translate(tx,ty);
        tl = new TextLayout("名称：美杜莎",font,frc);
        //获取左上角
        double dy = Utils.drawText(g2, 0, 0, 0, tl);
        tl = new TextLayout("属性：毒/水/土" ,font.deriveFont(20F),frc);
        dy = Utils.drawText(g2,0,dy,0,tl);
        as = new AttributedString("反击: 清除自身1个减益效果。每当自身清除4个减益效果，" +
                "触发一次攻击，对所有敌人造成1 x 4点水系 / 土系伤害，并对每个敌人施加4层折磨。");
        as.addAttribute(TextAttribute.FONT,font.deriveFont(20F));
        as.addAttribute(TextAttribute.FOREGROUND,new Color(179, 147, 139));
        as.addAttribute(TextAttribute.FOREGROUND,Color.YELLOW,0,2);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,8,9);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,21,22);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,32,34);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,43,44);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,45,46);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,48,55);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,49,52);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,66,67);
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,68,70);
        LineBreakMeasurer lbm = new LineBreakMeasurer(as.getIterator(),frc);
        while (lbm.getPosition() < as.getIterator().getEndIndex()) {
            tl = lbm.nextLayout(wrappingWidth);
            dy = Utils.drawText(g2,0,dy,0,tl);
        }
        g2.setTransform(temp);

        int count = 0;
        for (int  i =startIndex;i<startIndex + shapes.length;i++) {
            Shape s = shapes[count++%shapes.length];
            Image im = bufferedImages[i];
//            g2.draw(s);
            double w = s.getBounds().getWidth();
            double h = s.getBounds().getHeight();
            tx = s.getBounds().x;
            ty = s.getBounds().y;

            g2.translate(tx,ty);
            if (cacheImages[i] == null) {
                BufferedImage bi = new BufferedImage(
                        (int) (w * 0.65), (int) (h * 0.65), BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D biG = bi.createGraphics();
                biG.setComposite(AlphaComposite.Clear);
                biG.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                biG.setComposite(AlphaComposite.Src);
                biG.drawImage(im, 0, 0, bi.getWidth(), bi.getHeight(), null);
                cacheImages[i] = bi;
                Image[] testImage = testImages[i];
                for (int k = 0;k<testImage.length;k++) {
                    if (testImage[k] == null)
                    {
                        //TODO
                        BufferedImage b = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g4 = b.createGraphics();
                        g4.setComposite(AlphaComposite.Clear);
                        g4.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                        g4.setComposite(AlphaComposite.Src);
                        AffineTransform af = AffineTransform.getRotateInstance(2 * Math.PI / testImage.length * (k+1)
                                ,bi.getWidth() / 2.0,
                                bi.getHeight() / 2.0);
                        af.scale(0.85,0.85);
                        g4.drawImage(bi,af,null);
                        g4.dispose();
                        testImages[i][k] = cacheImages[k];
                    }
                }
                biG.dispose();
            }

            Image cacheImage = cacheImages[i];
            //确定中心
            float lx = (float) ((w - cacheImage.getWidth(null)) / 2.0F);
            float ly = 10F;

            int newW = (int) (cacheImage.getWidth(null) * scale);
            int newH = (int) (cacheImage.getHeight(null) * scale);
            g2.drawImage(cacheImage,
                    (int) (lx - (newW - cacheImage.getWidth(null)) / 2.0),
                    (int) (ly + offsetY - (newH - cacheImage.getHeight(null)) / 2.0),
                    newW, newH, null);

            //绘制说明文本
            AttributedString ast = new AttributedString("美杜莎");
            ast.addAttribute(TextAttribute.FOREGROUND,Color.WHITE);
            ast.addAttribute(TextAttribute.FONT,font.deriveFont(15f));
            tl = new TextLayout(ast.getIterator(),frc);
            ly = (float) Utils.drawText(g2,0,ly + cacheImage.getHeight(null),w,tl);

            as = new AttributedString("属性:毒 x");
            as.addAttribute(TextAttribute.FOREGROUND,Color.YELLOW,0,2);
            as.addAttribute(TextAttribute.FONT,font.deriveFont(12F));
            Shape r = new Ellipse2D.Double(0,-2,5,5);
            ShapeGraphicAttribute st = new ShapeGraphicAttribute(r,ShapeGraphicAttribute.CENTER_BASELINE,false);
            as.addAttribute(TextAttribute.CHAR_REPLACEMENT,st,5,6);
            as.addAttribute(TextAttribute.FOREGROUND,Color.magenta,4,6);
            tl = new TextLayout(as.getIterator(),frc);
            ly = (float) Utils.drawText(g2,0,ly,w,tl);

            as = new AttributedString("元素:土 x / 水 x");
            st = new ShapeGraphicAttribute(r,ShapeGraphicAttribute.CENTER_BASELINE,false);
            as.addAttribute(TextAttribute.CHAR_REPLACEMENT,st,5,6);
            as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,0,2);
            as.addAttribute(TextAttribute.FOREGROUND,Color.orange,5,6);
            st = new ShapeGraphicAttribute(r,ShapeGraphicAttribute.CENTER_BASELINE,false);
            as.addAttribute(TextAttribute.CHAR_REPLACEMENT,st,11,12);
            as.addAttribute(TextAttribute.FOREGROUND,Color.cyan,11,12);
            tl = new TextLayout(as.getIterator(),frc);
            Utils.drawText(g2,0,ly,w,tl);
            g2.translate(-tx,-ty);
        }
        //绘制滑动块
        drawArraw(g2);
        //绘制
        startButton.draw(g2);
        if (clickedIndex != -1)
        {
            Shape s = shapes[clickedIndex];
            g2.setColor(Color.PINK);
            g2.setStroke(new BasicStroke(2));
            g2.draw(s);
        }
        //绘制按钮
    }

    private void drawArraw(Graphics2D g2) {
        leftRow.draw(g2);
        rightRow.draw(g2);
        slide.draw(g2);
    }

    private void drawString(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        int w = c.getWidth();
        int h = c.getHeight();
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout("您的名字:",appFont.deriveFont(30.0F),frc);
        float lx = (w - tl.getAdvance()) / 2.0F;
        float ly = h / 4.0F;
        tl.draw(g2,lx,ly - tl.getAscent());

        if (keyBoard.getInputString().isEmpty())
            return;
        tl = new TextLayout(keyBoard.getInputString(), Utils.font.deriveFont(30.F),frc);
        float ascent = tl.getAscent();
        float height = ascent + tl.getDescent() + tl.getLeading();
        float advance = tl.getAdvance();
        lx = (w - advance) / 2.0F;
        tl.draw(g2,lx,ly + ascent);
        //绘制基线
        Line2D l = new Line2D.Double(lx,ly+height,lx + advance,ly + height);
        g2.draw(l);
    }

    private <T extends Shape> int checkPos(Point2D mousePoint,T[] data) {
        if (data == null ||data.length == 0)
            return -1;
        for (int i = 0;i<data.length ;i++)
        {
            T s = data[i];
            if (s == null) {
                return -1;
            }
            if (s.contains(mousePoint) &&
                    mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        launchGame(new GameStartPanel());
    }

    @Override
    public void clicked(ClickedEvent event) {
        Object source = event.getSource();
        if (source == start)
        {
            gameState = GAME_START;
        }
        if (source == backButton)
            gameState = GAME_ON;

        if (source == startButton)
            gameState = GAME_ON;

        String s = event.getActionString();

        if (source == leftRow)
            startIndex--;
        if (source == rightRow)
            startIndex++;
        startIndex = Math.max(0,Math.min(startIndex,total - shapes.length));
    }

    @Override
    public void change(Object source, double oldValue, double newValue) {
        startIndex = (int)newValue;
    }

    class ioTask extends SwingWorker<Void,Void> {

        @Override
        protected Void doInBackground() throws Exception {
            fillImages();
            return null;
        }

        @Override
        protected void done() {
            Utils.beep();
        }

        private void fillImages() {
            int index = 0;
            String path = "images/cards/deck/FINAL/";
            for (String s :strings) {
                for (char c :chars)
                {
                    String relativePath = path + s + c + ".png";
                    index = fills(relativePath,index);
                }
            }
//            for (int i = 0;i<8;i++) {
//                String relativePath = path  + "medusa_idle_frame_" + (i+1) + ".png";
//                index = fills(relativePath,index);
//            }
        }

        private int fills(String path,int index) {
            //获取缩放倍数
            ImageIcon icon = new ImageIcon(path);
            BufferedImage bi = new BufferedImage(pictureW,pictureH,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.clearRect(0,0,bi.getWidth(), bi.getHeight());
            g2.drawImage(icon.getImage(),0,0,bi.getWidth(),bi.getHeight(),null);
            g2.dispose();
            bufferedImages[index++] = bi;
            return index;
        }

    }
}
