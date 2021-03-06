package com.tutorial.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1550691097823471818L;
	public static final int WIDTH = 960, HEIGHT = WIDTH / 12 * 9;
	Handler handler;
	public static Random r;
	private Thread thread;
	private Window window;
	
	public enum State {
		Start(),
		Game(),
		Stop();
	}
	
	public static boolean running = false;
	public static State state;
	public Game() {
		handler = new Handler();
		new GameRun(handler);
		this.addKeyListener(new KeyInput(handler));
		window = new Window(WIDTH, HEIGHT, "BLOCKS: Waves of Chaos", this);
		r = new Random();
		state = State.Start;
		
	}
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		state = State.Stop;
		for(int i = 0; i < handler.object.size();i++)
		{
			handler.object.remove(i);
			i--;
		}
		/*BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		handler.render(g);
		//g.setColor(Color.white);
		//g.drawString("Game Over", 10, 80);
		g.setColor(Color.red);
		g.fillRect(WIDTH/2 - 45,HEIGHT/2 - 50,180,40);
		g.setColor(Color.white);
		g.setFont(new Font("arial",1,30));
		g.drawString("Game Over", WIDTH/2-40, HEIGHT/2-20);
		
		
		g.dispose();
		bs.show();
		g.dispose();
		bs.show();*/
		//try {
		//	thread.join();
		//	running = false;
		//} catch(Exception E) {}
		
	}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 /  amountOfTicks;
		double delta = 0;
		//long timer = System.currentTimeMillis();
		//int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
				
			}
			if(running)render();
			//frames++;
			
			/*if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}*/
		}
		stop();
	}
	private void render() {
		// TODO Auto-generated method stub
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		handler.render(g);
		g.dispose();
		bs.show();
		
		
	}
	private void tick() {
		handler.tick();
		if(GameRun.health == 0)stop();
	}
	public static void main (String args[]) {
		new Game();
	}
	
	public static int clamp(int val, int min, int max) {
		if(val<=min)return min;
		if(val>=max)return max;
		return val;
	}
	
	public static boolean isTouching(GameObject o1, GameObject o2)
	{
		if(o1.getX() <= o2.getX() + o2.getSize() && o2.getX() <= o1.getX() + o1.getSize())
		{
			if(o1.getY() <= o2.getY() + o2.getSize() && o2.getY() <= o1.getY() + o1.getSize()) return true;
		}
		return false;
	}
}
