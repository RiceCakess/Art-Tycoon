package ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.Game;



public class PopUp {
	public String title;
	private boolean textBox;
	private int width = 400;
	private int height = 420;
	private Runnable r;
	Game parent;
	public boolean destroy = false;
	private String[] lines;
	private int frame = 0;
	Button b = new Button("OK");
	TextBox tb = new TextBox();
	public PopUp(String title, String content, boolean textBox, Game p, Runnable r){
		this.title = title;
		this.textBox = textBox;
		this.r = r;
		parent = p;
		p.time.toggle();
		b.registerListener(p);
		tb.registerListener(p);
		
		b.r = new Runnable(){
			@Override
			public void run() {
				mouseClicked();
			}
		};
		lines = content.split("/");
		this.height = (((lines.length-1))*30) + 80 + 40 + b.bHeight + tb.bHeight;
		int words = 0;
		for (String s : lines){
			words += s.split(" ").length;
		}
		
		Game.sound.playTyping((words/15)+1);
	}
	
	public PopUp(String title, String content, Game p, Runnable r){
		this.title = title;
		this.r = r;
		parent = p;
		p.time.toggle();
		b.registerListener(p);
		tb.registerListener(p);
		
		b.r = new Runnable(){
			@Override
			public void run() {
				mouseClicked();
			}
		};
		lines = content.split("/");
		this.height = (((lines.length-1))*30) + 80 + 40 + b.bHeight;
		
		int words = 0;
		for (String s : lines){
			words += s.split(" ").length;
		}
		
		Game.sound.playTyping(words/5);
	}
	
	public void drawBox(Graphics2D g2){
		if(destroy){
			return;
		}
		g2.setColor(new Color(20,20,20,190));
		g2.fillRect(0, 0, parent.getWidth(), parent.getHeight());
		int words = 0;
		for (String s : lines){
			words += s.split(" ").length;
		}
		
		if(textBox){
			words += 10;
		}
		if(frame <= words*10){
			frame++;
		}
	
		int gameWidth = parent.getWidth()/2;
		int gameHeight = parent.getHeight()/2;
		int startHeight = gameHeight - (height/2);
		
		g2.setColor(new Color(255, 250 ,250));
		g2.fillRect(gameWidth - (width/2), gameHeight - (height/2), width, height);
		g2.setColor(new Color(189, 195, 199));
		g2.setStroke(new BasicStroke(4f));
		g2.drawRect(gameWidth - (width/2), gameHeight - (height/2), width, height);
		
		g2.setColor(Color.black);
		Font f = new Font("Roboto-Medium", Font.BOLD, 40);
		g2.setFont(f);
		FontMetrics fm = g2.getFontMetrics();
		int fwidth = fm.stringWidth(title);
		g2.drawString(title, gameWidth - (fwidth/2), startHeight + 45);
		
		f = new Font("Roboto-Medium", Font.PLAIN, 20);
		g2.setFont(f);
		fm = g2.getFontMetrics();
		// + 80 is for the large title label
		int dWord = 0;
		for(int i = 0; i < lines.length; i++){
			fwidth = fm.stringWidth(lines[i]);
			String s = "";
			for (String ss : lines[i].split(" ")){
				if(frame >= dWord*10){
					s += ss + " ";
					dWord++;
					
				}
				//System.out.println(frame);
			}
			
			g2.drawString(s, gameWidth - (fwidth/2), startHeight + 80 + (i*30));
		}
		if(textBox && frame >= (words*10) -100){
			tb.draw(g2, gameWidth - tb.bWidth/2, startHeight + ((lines.length-1)*30) + 80 + 20);
		}
		b.draw(g2, gameWidth - b.bWidth/2, gameHeight + (height/2) -  b.bHeight - 10);
		
	}
	
	public String getText(){
		return tb.ctext;
	}
	
	public void mouseClicked() {
		if(getText().length() <= 0 && textBox){
			return;
		}
		if(r != null){
			r.run();
		}
		b.unregisterListener(parent);
		tb.unregisterListener(parent);
		destroy = true;
		parent.time.toggle();
	}
	
	public Game getParent(){
		return parent;
	}
	
}
