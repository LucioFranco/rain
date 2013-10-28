package com.luciofranco.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.luciofranco.game.entity.mob.Player;
import com.luciofranco.game.graphics.Screen;
import com.luciofranco.game.input.Keyboard;
import com.luciofranco.game.level.Level;
import com.luciofranco.game.level.RandomLevel;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	public boolean DevMode;

	private static String title = "Rain - Dev Build - 10/23/13";
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	private Keyboard key;
	private Level level;
	private Player player;

	private BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int fps;

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		level = new RandomLevel(64, 64);
		player = new Player(key);

		addKeyListener(key);
		DevMode = true;

	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1_000_000_000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;

		requestFocus();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println(title + " | " + updates + " updates, " +
				// frames + " FPS");
				frame.setTitle(title + " | " + updates + " updates, " + frames
						+ " FPS");
				fps = frames;
				updates = 0;
				frames = 0;
			}
		}
		stop();

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// Stuff that is being rendered to the screen
		Graphics g = bs.getDrawGraphics();
		{
			screen.clear();
			int xScroll = player.x - screen.width / 2;
			int yScroll = player.y - screen.height / 2;
			level.render(xScroll, yScroll, screen);
			player.render(screen);

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}

			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
			// Draw the FPS
			if (DevMode) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Verdana", 0, 12));
				g.drawString("Development Build", 5, 12);
				g.drawString("FPS = " + this.fps, 5, 25);
				g.drawString("X =  " + player.x, 5, 35);
				g.drawString("Y =  " + player.y, 5, 45);
			}

		}
		// Get ride of the old shit
		g.dispose();
		bs.show();
	}

	public void update() {
		key.update();
		player.update();
	}

	public static void main(String[] args) {
		boolean mouse = false;
		
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		
		if(mouse) {
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		game.frame.getContentPane().setCursor(blankCursor);
		}
		
		game.frame.setVisible(true);

		game.start();
	}
}
