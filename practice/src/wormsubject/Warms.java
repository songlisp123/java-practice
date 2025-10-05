package wormsubject;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Warms {
    private Pair[] body;
    private static final int DEFAULT_Size = 5;
    private static final Logger logger = Logger.getLogger("worms");
    private int direction;
    public static final int DOWN = -1;
    public static final int UP = 1;
    public static final int LEFT = -2;
    public static final int RIGHT = 2;

    private int turn;
    public Warms(int turn) {

        body = new Pair[DEFAULT_Size];
        for (int i = 0; i < body.length; i++) {
            body[i] = new Pair(i+1,1);
        }
        direction = DOWN;
        this.turn = turn;
//        this.wall = wall;
    }

    public int Size() {
        return body.length;
    }

    public Pair[] getBody() {
        return body;
    }

    public boolean hit() {
         return hit(direction);
    }

    /**
     * 这是一个判断蛇是否自相交的代码
     * @return {@code true} 发生自相交
     * {@code false} 未发生自相交
     */
    public boolean contains() {
        Pair head = createHead(this.direction);
        boolean hit = Wall.contains(head);
        if (hit) {
            return true;
        } else {
            for (int i = 1; i < body.length; i++) {
                if (head.equals(body[i])) {
                    logger.warning("发生碰撞！重新开始！");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 这个代码与上面的{@code contains}一致，主要逻辑验证蛇是否自相交
     * @param x 一个整数类型的x坐标
     * @param y 一个整数类型的y坐标
     * @return {@code true} 如果蛇自相交<br />
     * {@code false} 如果蛇不自相交
     */
    public boolean contains(int x,int y) {
        for (Pair p: body) {
            if (p.equals(x,y)) {
                logger.warning("发生碰撞！重新开始！");
                return true;
            }
        }
        return false;
    }

    /**
     * 判断蛇是否吃到食物.主要逻辑是验证当前方向与蛇的运动方向
     * 如果蛇与方向参数相反，则不处理任何操作；否则，设置蛇的运动方向
     * 为当前方向，通过{@code createHead(direction)}函数获取当前
     * 蛇的头部，{@code boolean eat = head.equals(food)}判断蛇是否
     * 获得食物，如果蛇吃到了食物，就会通过调用{@code body = Arrays.copyOf(body,body.length+1)}
     * 增加蛇身体数组，然后依次将其他节点向后移一位，然后调用{@code body[0] =head}设置新的头部
     * @param direction 方向参数
     * @param food 食物位置
     * @return {@code true} 代表蛇获取食物<br />
     * {@code false} 代表着未获取食物
     */
    public boolean creep(int direction,Pair food) {
        if (this.direction+direction==0) {
            return false;
        }
        this.direction = direction;
        Pair head = createHead(direction);
        boolean eat = head.equals(food);
        if (eat) {
            body = Arrays.copyOf(body,body.length+1);
        }
        for (int i = body.length - 1; i >= 1; i--) {
            body[i] = body[i - 1];
        }
        body[0] =head;
        return eat;
    }

    /**
     * 重载的{@code creep}方法，返回蛇的身体
     * @param food 食物的位置
     */
    public boolean creep(Pair food) {
        return creep(this.direction,food);
    }

    /**
     * 判断蛇碰撞的逻辑，与蛇碰撞的主要原因有<br />
     * 自相交，碰撞墙体
     * @param direction 蛇头运动的方向
     * @return {@code true}如果发生碰撞<br />
     * {@code false}如果未发生碰撞
     */
    public boolean hit(int direction) {

        //如果输入方向与蛇的方向相反，则修正此效果，不处理碰撞
        if (this.direction+direction ==0 ) {
            return false;
        }

        //如果头部超出边界则显示碰撞或者蛇与自身或者墙体相撞
        Pair head = createHead(direction);
        if (head.getX()<0 || head.getX() >= jpanel.columns ||
        head.getY()<0 || head.getY()>=jpanel.rows) {
            logger.warning("发生碰撞！重新开始！");
            return true;
        }
        return contains();
    }

    /**
     * 此函数是为了生产蛇的头部设置的函数
     * @param direction 用户点击按钮后生成的方向事件
     * @return 一个表示新的蛇头的位置Pair类型的函数
     */
    public Pair createHead(int direction) {
        int x = body[0].getX();
        int y = body[0].getY();
        switch (direction) {
            case DOWN :
                y++;
                break;
            case UP:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        return new Pair(x,y);
    }


}
