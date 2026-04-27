package com.snl.swing.game;

import com.snl.swing.game.anime.*;
import com.snl.swing.game.components.*;
import com.snl.swing.game.components.enm.Direction;
import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game.keyBoard.SimpleCleanKeyBoard;
import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.sprite.Player;
import com.snl.swing.game.sprite.Sprite;
import com.snl.swing.game.sprite.YuanSu;
import com.snl.swing.game.utils.ImageCreator;
import com.snl.swing.game.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public class GameStartPanel extends DiKaErPlus implements CollideEventListener, SlideDataChangeListener {

    private SimpleCleanKeyBoard keyBoard;
    private CustomButton backButton;

    private static final int GAME_PAGE = -1;
    private static final int GAME_ON = 0;
    private static final int GAME_START = 1;
    private static final int GAME_RUNNING = 2;
    private static final int GAME_UP = 3;
    private static final int GAME_STOP = 4;
    private static final int GAME_COMPLETED = 5;
    private static final int GAME_FILLED = 6;
    private static final int GAME_END = 7;
    private static final int GAME_EXCEPTION = 100;

    private int gameState;

    Font f1 = Utils.font;
    Font font = new Font("隶书",Font.BOLD | Font.ITALIC,30);

    //游戏页面
    Scene scene01;

    //游戏开始
    int wordRight,wordLeft;
    int pictureW,pictureH;
    int total;
    int startIndex;
    Shape[] shapes;
    //渲染静态图像
    Image[] bufferedImages,cacheImages;
    Sprite[] sprites;
//    Image[][] testImages;
    private int pictureTop,pictureLeft;
    private int clickedIndex;
    private Row leftRow,rightRow;
    CustomButton startButton;
    private double animation;
    private int  aIndex;
    double time,speed,amplitude,offsetY;
    private Slide slide;
    String[] sp = {
            "美杜莎","草泥马","烈火猴"
    };
    String[] induction = {
            "反击: 清除自身1个减益效果。每当自身清除4个减益效果,触发一次攻击，对所有敌人造成1 x 4点水系 / 土系伤害，并对每个敌人施加4层折磨",
            "健康: 战斗开始的时候,给自己施加生命值差量的护盾，如果该护盾被打破，则向队友施加同等量的护盾",
            "燥热: 每当该怪兽受到攻击时，对自身施加一层 力量 ,被向攻击敌人施加燃烧效果。",
    };

    private static final String path = "images/game/game_sprite_";

    //游戏介绍文本面板
    final String s = """
            欢迎来到迷雾大陆，迷雾大陆浩瀚无边，千奇百怪。有一种奇兽在该大陆生活，叫做灵兽。
            灵兽有不同特性。不同特性代表着不同能力。但在某天，不知何时，迷雾降临大陆，灵兽消失无踪，
            作为小镇上的灵兽训练师，面对朝夕相伴的伙伴的离奇失踪，你下定决心，去探寻外围的迷雾世界。
            在调查中，随着深入到迷雾中，怪异离奇的事情接连发生，你发觉到，有某种物质正阻碍您的调查，灵兽失踪的真相也许
            就在迷雾深处，你会……
            """;

    Player player;

    public GameStartPanel() throws HeadlessException {
        WIDTH = 600;
        HEIGHT = 600;
        appSleep = 16;
        drawAxis = false;
        gameState = GAME_PAGE;
        //选择页面
        total = 3;
        wordLeft = wordRight = 20;
        pictureW = pictureH = 250;
        pictureTop = pictureLeft = 50;
        shapes = new Shape[total];
        sprites = new Sprite[total];
        bufferedImages = new Image[total];
        cacheImages = new Image[total];
//        testImages = new Image[total][shapes.length];
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
        leftRow = new Row(lx,ly,w,h, Direction.WEST);
        lx = c.getWidth() - 20 - w;
        rightRow = new Row(lx,ly,w,h, Direction.EAST);

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

        backButton = new CustomButton(w - 100,20,75,30,"返回");
        backButton.addListeners(this);
        backButton.setClickedString("back");

        startButton = new CustomButton(0,h - 40,w,20,"开始");
        startButton.addListeners(this);
        startButton.setClickedString("start");

        List<Part> parts = new ArrayList<>();
        TextEffect tf = new TextEffect("迷雾大陆",Utils.liShu,
                TextEffect.INC | TextEffect.AL | TextEffect.R | TextEffect.SCI
                ,Color.WHITE,0,150);
//        BackGroundEffect bc  = new BackGroundEffect(100,200,Color.WHITE,Color.GRAY);
//        GradientEffect gradientEffect = new GradientEffect(GradientEffect.BUR | GradientEffect.SDH,
//                Color.WHITE,Color.GRAY,160,250);
        DitherDissolveEffect cf = new DitherDissolveEffect(260,340,4,c);
//        TexturePaintEffect texturePaintEffect = new TexturePaintEffect(TexturePaintEffect.OD,Color.BLACK,
//                Color.YELLOW,20,300,350);
        parts.add(tf);
//        parts.add(gradientEffect);
        parts.add(cf);
//        parts.add(texturePaintEffect);
        scene01 = new Scene(parts,"你好","2");

        fillShapes();

        //创建用户
        player = new Player("",ImageCreator.makeBufferedImage(
                ImageCreator.blockingLoad("./queen.gif")
        ),new Vector2D(),new Vector2D(1,1));
    }


    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        keyBoard.processInput(mouseInputEvent,keyBoardEvent);
        switch (gameState)
        {
            case GAME_ON -> startButton.processInput(mouseInputEvent);
            case GAME_START -> {
                startButton.processInput(mouseInputEvent);
            }
            case GAME_RUNNING -> {
                backButton.processInput(mouseInputEvent);
                leftRow.processInput(mouseInputEvent);
                rightRow.processInput(mouseInputEvent);
                startButton.processInput(mouseInputEvent);
                slide.processInput(mouseInputEvent);
            }

            case GAME_UP -> {
                player.processInput(mouseInputEvent,keyBoardEvent);
            }
        }

    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        Point2D po = mouseInputEvent.getCurrentPoint();
        switch (gameState) {
            case GAME_PAGE -> {
                scene01.step(c.getWidth(),c.getHeight());
            }
            case GAME_ON -> {
                startButton.update(delta);
            }
            case GAME_START -> {
                keyBoard.update(delta,po);
                startButton.update(delta);
            }
            case GAME_RUNNING -> {
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

            case GAME_UP -> {
                player.setName(keyBoard.getInputString());
                player.update(delta);
                //变化视角
                viewMat = Matrix3x3f.translate(-player.getPos().getX(),-player.getPos().getY());
                axis.createAxis(getViewportTransform(),c,wordWidth);
            }
        }
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        switch (gameState) {
            case GAME_PAGE -> {
                scene01.render(c.getWidth(),c.getHeight(),g2);
            }
            case GAME_ON -> {
                drawGameStartPanel(g2);
            }
            case GAME_START -> {
                keyBoard.draw(g2,Color.WHITE);
                drawString(g2);
                startButton.draw(g2);
            }
            case GAME_RUNNING -> {
                drawTest(g2);
                backButton.draw(g2);
            }

            case GAME_UP -> {
                player.draw(g2,getViewportTransform());
                drawAxis = true;
            }
        }
        g2.dispose();
    }

    //启动面板
    private void drawGameStartPanel(Graphics2D g2) {
        AttributedString as = new AttributedString(s);
        as.addAttribute(TextAttribute.FONT,Utils.liShu.deriveFont(32F));
        as.addAttribute(TextAttribute.FOREGROUND,Color.WHITE);
        as.addAttribute(TextAttribute.FOREGROUND,Color.RED,20,50);
        FontRenderContext frc = g2.getFontRenderContext();
        AttributedCharacterIterator it = as.getIterator();
        LineBreakMeasurer lbm = new LineBreakMeasurer(it,frc);
        double dy =pictureTop;
        while (lbm.getPosition() < it.getEndIndex()) {
            TextLayout tl = lbm.nextLayout(c.getWidth() - pictureLeft - pictureLeft);
            dy = Utils.drawText(g2,pictureLeft,dy,c.getWidth() - pictureLeft - pictureLeft,tl);
        }
        startButton.draw(g2);
    }

    //*****************************************************************************//
    //*********************************  选择面板  **********************************//
    //*****************************************************************************//

    //精灵选择框
    private void drawTest(Graphics2D g2) {
        g2.setColor(Color.red);
        FontRenderContext frc = g2.getFontRenderContext();
        //TODO
        AttributedString as = new AttributedString("请挑选您的灵兽:");
        as.addAttribute(TextAttribute.FONT,Utils.font02.deriveFont(25F));
        as.addAttribute(TextAttribute.FOREGROUND,Color.WHITE);
        as.addAttribute(TextAttribute.FONT,Utils.liShu.deriveFont(30F));
        as.addAttribute(TextAttribute.FOREGROUND,Color.ORANGE,5,8);
        TextLayout tl = new TextLayout(as.getIterator(),frc);
        drawText(g2,0,0,c.getWidth(),tl);

        //绘制图像
        Sprite sprite = sprites[startIndex + clickedIndex];
        BufferedImage image = sprite.getImage();
        double sin = Math.sin(time * speed);
        offsetY = sin * amplitude;
        double scale = 1.0 + sin * 0.01;
        int newW = (int) (image.getWidth() * scale);
        int newH = (int) (image.getHeight() * scale);
        g2.drawImage(image,
                (int) (pictureLeft - (newW - image.getWidth()) / 2.0),
                (int) (pictureTop + offsetY - (newH - image.getHeight()) / 2.0),
                newW, newH, null);


        double tx = c.getWidth() / 2.0 + wordLeft;
        double ty = pictureTop;
        float wrappingWidth = (float) (tx - 2 *wordLeft - wordRight);
        AffineTransform temp = g2.getTransform();

        g2.translate(tx,ty);
        tl = new TextLayout(sprite.getName(),font,frc);
        //获取左上角
        double dy = Utils.drawText(g2, 0, 0, 0, tl);
        tl = new TextLayout("元素:"+sprite.getYuanShu(),font.deriveFont(20F),frc);
        dy = Utils.drawText(g2,0,dy,0,tl);
        //灵兽特性
        String beiDong = sprite.getBeiDong();
        as = new AttributedString(beiDong);
        int index = beiDong.indexOf(":");
        as.addAttribute(TextAttribute.FONT,font.deriveFont(20F));
        as.addAttribute(TextAttribute.FOREGROUND,new Color(179, 147, 139));
        as.addAttribute(TextAttribute.FOREGROUND,Color.YELLOW,0,index);
        LineBreakMeasurer lbm = new LineBreakMeasurer(as.getIterator(),frc);
        while (lbm.getPosition() < as.getIterator().getEndIndex()) {
            tl = lbm.nextLayout(wrappingWidth);
            dy = Utils.drawText(g2,0,dy,0,tl);
        }
        g2.setTransform(temp);

        //***********    精灵选择框  ********************//
        int count = 0;
        for (int  i =startIndex;i<startIndex + shapes.length;i++) {
            Shape s = shapes[count++%shapes.length];
            BufferedImage im = sprites[i].getImage();
            double w = s.getBounds().getWidth();
            double h = s.getBounds().getHeight();
            tx = s.getBounds().x;
            ty = s.getBounds().y;

            g2.translate(tx,ty);
            if (sprites[i].getCacheImage() == null) {
                BufferedImage bi = new BufferedImage(
                        (int) (w * 0.65), (int) (h * 0.65), BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D biG = bi.createGraphics();
                biG.setComposite(AlphaComposite.Clear);
                biG.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                biG.setComposite(AlphaComposite.Src);
                biG.drawImage(im, 0, 0, bi.getWidth(), bi.getHeight(), null);
                biG.dispose();
                sprites[i].setCacheImage(bi);
            }
            BufferedImage cacheImage = sprites[i].getCacheImage();
            //确定中心
            float lx = (float) ((w - cacheImage.getWidth()) / 2.0F);
            float ly = 10F;

            newW = (int) (cacheImage.getWidth() * scale);
            newH = (int) (cacheImage.getHeight() * scale);
            g2.drawImage(cacheImage,
                    (int) (lx - (newW - cacheImage.getWidth()) / 2.0),
                    (int) (ly + offsetY - (newH - cacheImage.getHeight()) / 2.0),
                    newW, newH, null);

            //绘制说明文本
            AttributedString ast = new AttributedString(sprites[i].getName());
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
            g2.setColor(new Color(0.5F,0.5f,0.5F,0.3f));
            g2.setStroke(new BasicStroke(2));
            g2.fill(s);
        }
        //绘制按钮
    }

    private void drawArraw(Graphics2D g2) {
        leftRow.draw(g2);
        rightRow.draw(g2);
        slide.draw(g2);
    }

    //选择姓名模板
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

    @Override
    protected void animation(double delta) {
        super.animation(delta);
        if (scene01.getIndex() < scene01.getLength())
            scene01.increment();
        if (gameState == GAME_PAGE &&
                scene01.getIndex() == scene01.getLength() && scene01.pause())
            gameState = GAME_ON;
    }

    public static void main(String[] args) {
        launchGame(new GameStartPanel());
    }

    @Override
    public void clicked(ClickedEvent event) {
        Object source = event.getSource();
        if (source == backButton)
            gameState = GAME_ON;

        if (source == startButton) {
            switch (gameState) {
                case GAME_ON -> gameState = GAME_START;
                case GAME_START -> gameState = GAME_RUNNING;
                case GAME_RUNNING -> gameState = GAME_UP;
            }
        }
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
            String relativePath;
            for (int i = 0;i<3;i++) {
                String string = sp[i];
                relativePath = path + (i+1) + ".png";
                Sprite sprite = new Sprite(string,induction[i],1,YuanSu.WATER_EARTH,relativePath);
                sprites[i] = sprite;
            }
        }
    }
}
//                Image[] testImage = testImages[i];
//                for (int k = 0;k<testImage.length;k++) {
//                    if (testImage[k] == null)
//                    {
//                        //TODO
//                        BufferedImage b = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB);
//                        Graphics2D g4 = b.createGraphics();
//                        g4.setComposite(AlphaComposite.Clear);
//                        g4.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//                        g4.setComposite(AlphaComposite.Src);
//                        AffineTransform af = AffineTransform.getRotateInstance(2 * Math.PI / testImage.length * (k+1)
//                                ,bi.getWidth() / 2.0,
//                                bi.getHeight() / 2.0);
//                        af.scale(0.85,0.85);
//                        g4.drawImage(bi,af,null);
//                        g4.dispose();
//                        testImages[i][k] = cacheImages[k];
//                    }
//                }
//获取缩放倍数
//            ImageIcon icon = new ImageIcon(path);
//            BufferedImage bi = new BufferedImage(pictureW,pictureH,BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2 = bi.createGraphics();
//            g2.clearRect(0,0,bi.getWidth(), bi.getHeight());
//            g2.drawImage(icon.getImage(),0,0,bi.getWidth(),bi.getHeight(),null);
//            g2.dispose();
//            bufferedImages[index++] = bi;