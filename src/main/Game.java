package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import painting.Painting;
import ui.Animate;
import ui.Button;
import ui.PopUp;
import ui.UI;
import util.Save;
import util.Sound;
import util.Time;


@SuppressWarnings("serial")
public class Game extends JPanel{
	double scale = 1;
	Save save = new Save(this);
	
	//Colors
	public static Color bgColor = new Color(236, 240, 241);

	//Essential Variables
    public Time time = new Time(this);
	List<Animate> animations = new ArrayList<Animate>();
	public ArrayList<Painting> paintings = new ArrayList<Painting>();
	public int money = 1000;
	public int fans = 0;
	public String name = "";
	public String typeOfPainter = "_____";
	public static Game instance;
	public ArrayList<PopUp> popups = new ArrayList<PopUp>();
	UI ui = new UI(this);
	boolean gameOver = false;
	public static Sound sound = new Sound();
	PaintScreen ps;
	
	//For moving mainscreen view
	private int view = 0;
	private int moveView = 0;
	private Point startPoint;
	private Button createPaint;
	private Screen currentScreen = Screen.MAIN;
	
	public Game(){
		instance = this;
		init();
		createPaint = new Button("+ (-$500)");
		createPaint.r = new Runnable(){

			@Override
			public void run() {
				change(-500);
				showScreen(Screen.PAINT);
			}
			
		};
		showScreen(Screen.MAIN);
		sound.playMain();
		this.addKeyListener(ky);
	}

	public void init(){
		boolean didRead = save.read();
		this.setBackground(bgColor);
		setPreferredSize(new Dimension(800,600));
		 try {
			 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto-Medium.ttf")));
		} catch (Exception e) {
			e.printStackTrace();
		}
			if(!didRead){
				popups.add(new PopUp("Welcome","Welcome to Art Tycoon! / A game where you paint your way / to fame and fortune! / You are: ", true, this, new Runnable(){

				@Override
				public void run() {
					name = popups.get(0).getText();
					}
				}));
				
				popups.add(new PopUp("Welcome" , "Your style of painting:", true, instance , new Runnable(){
					@Override
					public void run() {
						typeOfPainter = popups.get(0).getText();
						save.write();
					}
				}));
				popups.add(new PopUp("Good Luck", "Remember to watch after your balance / If it falls below -5000 then it's over / "
						+ "Click \"+\" button to create a new painting. / It cost $500 to start a new painting / Good Luck!", false, instance, null));
			}
	}
	
	public enum Screen{
		MAIN, PAINT;
	}
	
	public void showScreen(Screen s){
		currentScreen = s;

		this.removeMouseListener(dragMouse);
		this.removeMouseMotionListener(dragMotion);
		createPaint.unregisterListener(this);
		if(ps !=null){
			ps.exit();
			ps = null;
		}
		if(s.equals(Screen.MAIN)){
			this.addMouseListener(dragMouse);
			this.addMouseMotionListener(dragMotion);
			createPaint.registerListener(this);
		}
		else if(currentScreen.equals(Screen.PAINT)){
			ps = new PaintScreen(this);
		}
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		enableAntiAlias(g2);
		updateLogic();
		ui.draw(g2);
		updateAnimations(g2);
		
		if(currentScreen.equals(Screen.MAIN)){
			drawMainScreen(g2);
		}
		else if(currentScreen.equals(Screen.PAINT)){
			drawPaintScreen(g2);
		}
		
		updatePopUp(g2);
		
		if(money < -5000 && !gameOver){
			gameOver = true;
			popups.add(new PopUp("Game Over!","You are bankrupt! / You do not have what it takes to survive / in the art world / Should have gone to real college!", this, null));
		}
	}
	
	public void updateLogic(){
		time.updateTime();
	}
	
	public void updateAnimations(Graphics2D g2){
		Iterator<Animate> ait = animations.iterator();
		while(ait.hasNext()){
			
			Animate a = ait.next();
			if(a.complete){
				ait.remove();
			}
			else{
				a.update(g2);
			}
		}
	}
	
	public void updatePopUp(Graphics2D g2){
		Iterator<PopUp> po = popups.iterator();
		while(po.hasNext()){
			PopUp pop = po.next();
			if(pop.destroy){
				po.remove();
			}
			else{
				pop.drawBox(g2);;
				break;
			}
		}
	}
	
	
	public void drawMainScreen(Graphics2D g2){
		createPaint.draw(g2, 130 -  view, 300 - createPaint.bHeight/2);
		for(int i = 0; i < paintings.size(); i++){
			Painting p = paintings.get(paintings.size() - i - 1);
			p.paint(g2, 130 + 400 - 130 + ((260 + 160)*i) - (view), 300 - 150);
		}
		if(view >= 0 && moveView < 0){
			view += moveView;
		}
		else if(view <= (paintings.size() * (260 + 160)) && moveView > 0){
			view += moveView;
		}
		
	}
	
	public void drawPaintScreen(Graphics2D g2){
		 ps.draw(g2);
	}
	
	public void change(int amt){
		money +=amt;
		animations.add(new MoneyDeduct(60, amt));
		sound.playCash();
	}
	
	
	MouseMotionAdapter dragMotion = new MouseMotionAdapter(){

		@Override
		public void mouseDragged(MouseEvent e) {
			if(startPoint != null){
				int move = (int)e.getPoint().distance(startPoint)/25;
			
				if(startPoint.getX() - e.getX() < 0){
					move *= -1;
				}
				if(e.getY() > 200){
					if(Math.abs(move) > 1)
						moveView = move;
				}
				else{
					moveView =0;
				}
			}
		}	
	};
	
	MouseAdapter dragMouse = new MouseAdapter(){

		@Override
		public void mousePressed(MouseEvent e) {
			startPoint = e.getPoint();
			moveView = 0;
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			startPoint = null;
			moveView = 0;
		}
	};

	public class MoneyDeduct extends Animate{
		int amt;
		public MoneyDeduct(int maxFrame, int amt) {
			super(maxFrame);
			this.amt = amt;
		}
		
		@Override
		public void update(Graphics2D g2){
			super.update(g2);
			g2.setFont(new Font("ARIAL", Font.BOLD, 25));
			if(amt > 0){
				g2.setColor(new Color(46, 204, 113, 255 - frame*2));
			}
			else{
				g2.setColor(new Color(231, 76, 60, 255 - frame*2));
			}
			g2.drawString(" $" + amt, 125, 40 + 10 + frame/2);
		}
		
	}
	
	public void saveGame(){
		save.write();
	}
	
	public void enableAntiAlias(Graphics2D g2){
		 g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		 g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
				 RenderingHints.VALUE_RENDER_QUALITY);
	}
	
	KeyAdapter ky = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				if(popups.get(0)!=null){
					if(popups.get(0).title != "Paused"){
						popups.add(new PopUp("Paused" , "Click \"OK\" to continue", instance,null));
					}
				}
				else{
					popups.add(new PopUp("Paused" , "Click \"OK\" to continue", instance,null));
				}
			}
		}
	};
	
}
