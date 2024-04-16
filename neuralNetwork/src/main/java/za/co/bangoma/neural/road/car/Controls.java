package za.co.bangoma.neural.road.car;

import java.awt.event.KeyEvent;

public class Controls {

    private boolean forward, back, left, right;

    // Getters
    public boolean isForward() {
        return forward;
    }

    public boolean isBack() {
        return back;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                setForward(true);
                break;
            case KeyEvent.VK_DOWN:
                setBack(true);
                break;
            case KeyEvent.VK_LEFT:
                setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                setRight(true);
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                setForward(false);
                break;
            case KeyEvent.VK_DOWN:
                setBack(false);
                break;
            case KeyEvent.VK_LEFT:
                setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                setRight(false);
                break;
        }
    }

}
