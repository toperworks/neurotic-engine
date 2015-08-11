package net.toper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.KeyStroke;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

	private static boolean[] keyboard = new boolean[999];
	private static int mouseX, mouseY, mouseButton;

	public static int UP = KeyEvent.VK_UP;
	public static int DOWN = KeyEvent.VK_DOWN;
	public static int LEFT = KeyEvent.VK_LEFT;
	public static int RIGHT = KeyEvent.VK_RIGHT;
	public static int ENTER = KeyEvent.VK_ENTER;
	public static int NUM1 = KeyEvent.VK_1;
	public static int NUM2 = KeyEvent.VK_2;
	public static int NUM3 = KeyEvent.VK_3;
	public static int NUM4 = KeyEvent.VK_4;
	public static int NUM5 = KeyEvent.VK_5;
	public static int NUM6 = KeyEvent.VK_6;
	public static int NUM7 = KeyEvent.VK_7;
	public static int NUM8 = KeyEvent.VK_8;
	public static int NUM9 = KeyEvent.VK_9;
	public static int NUM0 = KeyEvent.VK_0;
	public static int NUMPAD1 = KeyEvent.VK_NUMPAD1;
	public static int NUMPAD2 = KeyEvent.VK_NUMPAD2;
	public static int NUMPAD3 = KeyEvent.VK_NUMPAD3;
	public static int NUMPAD4 = KeyEvent.VK_NUMPAD4;
	public static int NUMPAD5 = KeyEvent.VK_NUMPAD5;
	public static int NUMPAD6 = KeyEvent.VK_NUMPAD6;
	public static int NUMPAD7 = KeyEvent.VK_NUMPAD7;
	public static int NUMPAD8 = KeyEvent.VK_NUMPAD8;
	public static int NUMPAD9 = KeyEvent.VK_NUMPAD9;
	public static int NUMPAD0 = KeyEvent.VK_NUMPAD0;
	public static int F1 = KeyEvent.VK_F1;
	public static int F2 = KeyEvent.VK_F2;
	public static int F3 = KeyEvent.VK_F3;
	public static int F4 = KeyEvent.VK_F4;
	public static int F5 = KeyEvent.VK_F5;
	public static int F6 = KeyEvent.VK_F6;
	public static int F7 = KeyEvent.VK_F7;
	public static int F8 = KeyEvent.VK_F8;
	public static int F9 = KeyEvent.VK_F9;
	public static int F10 = KeyEvent.VK_F10;
	public static int F11 = KeyEvent.VK_F11;
	public static int F12 = KeyEvent.VK_F12;

	private static Frame f;

	public Input(Frame f) {
		Input.f = f;
		for (int i = 0; i < keyboard.length; i++) {
			keyboard[i] = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		keyboard[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keyboard[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		mouseButton = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mouseButton = -1;
	}

	public static int getActualMouseX() {
		return mouseX;
	}

	public static int getActualMouseY() {
		return mouseY;
	}

	public static int getScaledMouseX() {
		return mouseX / f.scale;
	}

	public static int getScaledMouseY() {
		return mouseY / f.scale;
	}

	public static int getMouseButton() {
		return mouseButton;
	}

	public static boolean[] keys() {
		return keyboard;
	}

	public static boolean isKeyDown(int keyCode) {
		if (keyCode < keyboard.length)
			return keyboard[keyCode];
		else
			return false;
	}

	public static boolean isCharDown(char c) {
		c = Character.toUpperCase(c);
		KeyStroke ks = KeyStroke.getKeyStroke(c, 0);
		int keyCode = ks.getKeyCode();
		if (keyCode < keyboard.length)
			return keyboard[keyCode];
		else
			return false;
	}

}
