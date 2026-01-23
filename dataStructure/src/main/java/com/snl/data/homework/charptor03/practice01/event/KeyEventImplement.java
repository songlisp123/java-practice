package com.snl.data.homework.charptor03.practice01.event;

import com.snl.data.homework.charptor03.practice01.state.Direction;
import com.snl.data.homework.charptor03.practice01.state.GameState;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventImplement implements KeyListener {

    private final InputState input;

    public KeyEventImplement(InputState input) {
        this.input = input;
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        空实现
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();
        switch (keyChar) {
            case 'w','W' :
                input.up = true;
                input.change = true;
                break;
            case 's','S' :
                input.down = true;
                input.change = true;
                break;
            case  'a','A' :
                input.left = true;
                input.change = true;
                break;
            case 'd' , 'D' :
                input.right = true;
                input.change = true;
                break;
            case '1' ,'2','3' , '4','5','6' , '7':
                InputState.c = keyChar;
                break;
            case 't' , 'T' :
                InputState.changeIngColor = !InputState.changeIngColor;
                break;
            default:
                break;
        }
        //实验版本
        if (keyCode == KeyEvent.VK_SPACE) {
            if (!input.attackHeld)
                input.attackPressed = true;
            input.attackHeld = true;
            if (input.up) {
                input.direction = Direction.NORTH;
            } else if (input.down) {
                input.direction = Direction.SOUTH;
            } else if (input.left) {
                input.direction = Direction.WEST;
            } else if (input.right) {
                input.direction = Direction.EAST;
            }else
                //默认子弹方向向东
                input.direction = Direction.EAST;
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            //如果按下esc键，暂停游戏
            GameState.stopping = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();
        switch (keyChar) {
            case 'w','W' :
                input.up = false;
                input.change = false;
                break;
            case 's','S' :
                input.down = false;
                input.change = false;
                break;
            case  'a','A' :
                input.left = false;
                input.change = false;
                break;
            case 'd' , 'D' :
                input.right = false;
                input.change = false;
                break;
            default:
                break;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            input.direction = Direction.EAST;
            input.attackPressed = false;
            input.attackHeld = false;
        }
    }
}
