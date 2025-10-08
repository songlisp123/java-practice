package wormsubject.gameElement;

import wormsubject.game.jpanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wall {
    public static List<Pair> body;
    private int turn;
    private Warms warms;
    public Wall(int turn,Warms warms) {
        this.turn = turn;
        this.warms = warms;
        this.body = new ArrayList<>();
        paintWall();
    }

    public void paintWall() {
        int r = 0;
        Random random = new Random();
        while (r<turn*3) {
            while (true) {
                int x = random.nextInt(jpanel.columns);
                int y = random.nextInt(jpanel.rows);
                if (!warms.contains(x,y)) {
                    body.add(new Pair(x,y));
                    body.add(new Pair(x+1,y));
                    break;
                }
            }
            r++;
        }
    }

    public List<Pair> showBody() {
        return body;
    }

    public boolean contains(int x,int y) {
        Pair pair = new Pair(x,y);
        return body.contains(pair);
    }
    public static boolean contains(Pair pair) {
        for (Pair pair1 :body) {
            if (pair1.getX() == pair.getX() &&
            pair1.getY() == pair.getY()) return true;
        }
        return false;
    }
    public static void tostring() {
         body.forEach(System.out::println);
    }
}
