package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import painting.Draw;
import painting.Painting;
import painting.Rect;
import ui.Button;
import ui.PopUp;


public class PaintScreen {
	Painting p;
	Draw d;
	Game parent;
	double scale = 1;
	Button b,b2;
	ArrayList<Point> points = new ArrayList<Point>();
	public PaintScreen(Game g){
		this.parent = g;
		p = new Painting(260,300);
		p.setScale(1.2);
		d = new Draw(p.width, p.height);
		d.setScale(1.2);
		b= new Button("Done");
		b2= new Button("Exit");
		b.registerListener(parent);
		b2.registerListener(parent);
		d.register(g);
		b.r = new Runnable(){

			@Override
			public void run() {
				done();
			}
			
		};
		b2.r = new Runnable(){

			@Override
			public void run() {
				cancel();
			}
			
		};
		// 10 pixel margin on all sides
		int minX =  10;
		int maxX = 260 - 10;
		int minY = 10;
		int maxY = 300 - 10;
		Random r = new Random();
		for(int i = 0; i < 10; i++){
			points.add(new Point(r.nextInt(maxX - minX) + minX, r.nextInt(maxY - minY) + minY));
		}
	}
	
	public void draw(Graphics2D g2){
		p.paint(g2, parent.getWidth()/2 - p.sW/2, parent.getHeight()/2 - p.sH/2);
		d.paint(g2, parent.getWidth()/2 - p.sW/2,parent.getHeight()/2 - p.sH/2);
		
		b.draw(g2, parent.getWidth() - parent.getWidth()/5,parent.getHeight() - 120);
		b2.draw(g2, 70, parent.getHeight() - 120);
		
		//for dev purposes
	//	for(Point po : points){
	//		g2.fillRect(parent.getWidth()/2 - p.sW/2 + (int)(po.x*p.scale), parent.getHeight()/2 - p.sH/2 + (int)(po.y*p.scale), 10, 10);
		//}
		
		for(Point po : points){
			Point point = new Point(parent.getWidth()/2 - p.sW/2 + (int)(po.x*p.scale), parent.getHeight()/2 - p.sH/2 + (int)(po.y*p.scale));
			for(Rect r : d.getRect()){
				if(r.contains(point)){
					g2.setColor(new Color(243, 156, 18));
					g2.fillRect(point.x, point.y, 10, 10);
					g2.setColor(Color.GRAY);
					g2.drawRect(point.x, point.y, 10, 10);
				}
			}
		}
		if((parent.getWidth() * parent.getHeight())/(800*600.0) > 1.1){
			//System.out.println((parent.getWidth() * parent.getHeight())/(800*600.0));
			double scale = (parent.getWidth() * parent.getHeight())/(800*600.0);
			
			scale *= .8;
			if(scale < 1.4){
				scale = 1.4;
			}
			p.setScale(scale);
			d.setScale(scale);
		}
		calcVal();
	}
	
	public void exit(){
		d.unregister(parent);
		b.unregisterListener(parent);
		b2.registerListener(parent);
	}
	
	public void cancel(){
		parent.showScreen(Game.Screen.MAIN);
	}
	public void done(){
		if(d.srects.size() <= 0){
			parent.popups.add(new PopUp("Oh no!", "You didn't draw anything! / Keep on drawing!", parent, null));
			return;
		}
		parent.popups.add(new PopUp("Complete!", "You are done! / Now all that's left is to name it! / Your painting's name is: ", true,parent, new Runnable(){

			@Override
			public void run() {
				p.name = parent.popups.get(0).getText();
				p.rects = d.srects;
				p.setScale(1);
				parent.paintings.add(p);
				parent.save.write();
				parent.showScreen(Game.Screen.MAIN);
				
				Timer t = new Timer();
				t.schedule(new TimerTask(){

					@Override
					public void run() {
						Random r = new Random();
						int fans = (p.cost/200 + r.nextInt(5));
						if(p.cost > 0){
							parent.popups.add(new PopUp("SOLD", "Your painting \"" + p.name+"\" was just sold! / Congratulations! / It was purchased for $" + p.cost + " / You gained " + fans + " fans", parent, null));
						}
						else{
							parent.popups.add(new PopUp("FAIL", "Your painting was terrible! / No one wanted to buy it / You lost $" + p.cost + " / You lost " + fans + " fans", parent, null));
						}
						parent.fans += fans;
						if(p.cost < -1000){
							p.cost = -1000;
						}
						parent.change(p.cost);
						parent.save.write();
					}
					
				}, 10*1000);
			}
		}));
		
	}
	
	public void calcVal(){
		int val = 100;
		for(Point po : points){
			Point point = new Point(parent.getWidth()/2 - p.sW/2 + (int)(po.x*p.scale), parent.getHeight()/2 - p.sH/2 + (int)(po.y*p.scale));
			for(Rect r : d.getRect()){
				if(r.contains(point)){
					val+= 500;
					break;
				}
			}
		}
		for(Rect r : d.getRect()){
			int area = (int) ((r.height*r.width)/(p.scale));
			val -= area/15;
		}
		
		val -= d.srects.size()*10;
		val += (parent.fans*5.0);
		val += parent.paintings.size() *5;
		
		p.name = ""+val;
		p.cost = val;
	}
}
