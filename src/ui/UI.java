package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.Game;


public class UI {
	Game parent;
	public static Color UIColor = new Color(52, 152, 219);
	public static Color greenUI = new Color(46, 204, 113);
	public static Color blueUI = new Color(41, 128, 185);
	
	public UI(Game parent){
		this.parent = parent;
	}
	
	public void draw(Graphics2D g2){
		g2.setColor(UIColor);
		g2.fillRect(0, 0, parent.getWidth(), 100);
		g2.setColor(new Color(41, 128, 185));
		g2.fillRect(0, 100, parent.getWidth(), 5);
		
		//Time
		g2.setColor(Color.white);
		g2.setFont(new Font("ARIAL", Font.BOLD, 20));
		g2.drawString(parent.time.toString(), parent.getWidth() - 190, 40);
		g2.setColor(blueUI);
		g2.fillRect(parent.getWidth() - 190, 50, (int)(155 * (1- parent.time.getPer())), 5);
		
		//Money
		g2.setFont(new Font("ARIAL", Font.BOLD, 25));
		g2.setColor(Color.white);
		g2.drawString("Money:", 40, 40);
		if(parent.money < 0){
			g2.setColor(Color.red);
		}
		else{
			g2.setColor(greenUI);
		}
		
		g2.drawString(" $" + parent.money, 125, 40);
		//Fans
		g2.setFont(new Font("ARIAL", Font.BOLD, 25));
		g2.setColor(Color.white);
		g2.drawString("Fans: ", 40, 70);
		g2.drawString("" + parent.fans, 110 , 70);
		
		//Name
		g2.setColor(Color.white);
		Font f = new Font("Roboto-Medium", Font.BOLD, 40);
		g2.setFont(f);
		FontMetrics fm = g2.getFontMetrics(f);
		g2.drawString(parent.name, parent.getWidth()/2 - (fm.stringWidth(parent.name)/2) , 50);
		
		//Painter Title
		f = new Font("Roboto-Medium", Font.PLAIN, 15);
		g2.setFont(f);
		fm =g2.getFontMetrics(f);
		String painterTitle = "The " + parent.typeOfPainter + " Painter";
		g2.drawString(painterTitle, parent.getWidth()/2 - (fm.stringWidth(painterTitle)/2), 70);
	}
}
