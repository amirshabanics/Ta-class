package gameLoop;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sorooshbagheri on 19/03/13.
 */

public class Game extends JFrame implements Runnable {

	private boolean running = false;
	private int repaints = 0;
	private Thread gameThread;

	public static void main(String args[]) {
		Game g = new Game();
		g.start();

	}

	int height = 500;
	int weight = 500;

	public Game() {
		setBackground(Color.BLACK);
		gameThread = new Thread(this);
		setSize(new Dimension(weight, height));
		setVisible(true);
	}

	@Override
	public void run() {
		// The Game Loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0; // per second
		double ns_per_tick = 1000000000 / amountOfTicks; // results in almost: 16 millions
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns_per_tick;
			lastTime = now;
			while (delta >= 1) {
				tick(); // must take less than 16 million nano seconds
				frames++;
				delta--;
			}
			if (running) {
//                render();
				repaint();
//                paintImmediately(0,0,Game.getGameWidth(),Game.getGameHeight());
			}

			try {
				Thread.sleep(frames > 65 ? 30 : 0);
				Thread.sleep((long) Math.max(System.nanoTime() - lastTime - ns_per_tick, 1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (frames > 60) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.format("FPS: %d repaints: %d%n", frames, repaints);
				frames = 0;
				repaints = 0;
			}
		}
		stop();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		repaints++;
		g.setColor(Color.BLACK);
		g.fillOval(x, y, 60, 60);
	}

	int x = 58, y = 95;
	int speedX = 4;
	int speedy = 6;

	private void tick() {
		if (x <= 5 || x >= weight - 5)
			speedX *= -1;
		if (y <= 5 || y >= height - 5)
			speedy *= -1;
		x += speedX;
		y += speedy;
	}

	void start() {
		running = true;
		System.out.println("Start");
		gameThread.start();
	}

	void stop() {
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}