package ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import main.Game;

public class Button {
	int bWidth = 120;
	public int bHeight = 60;
	private String text = "OK";
	private Color bsColor = new Color(241, 196, 15);
	private Color bColor = new Color(243, 156, 18);
	private Color hoverColor = new Color(251, 206, 25);
	private boolean hover;
	private Rectangle bounds;
	private PopUp parent;
	public Runnable r;

	public Button(String text) {
		this.text = text;
	}

	public Button(int bHeight, int bWidth, String text) {
		this.bHeight = bHeight;
		this.bWidth = bWidth;
		this.text = text;
	}
	
	public void draw(Graphics2D g2, int x, int y) {
		g2.setStroke(new BasicStroke(2f));
		if (hover) {
			g2.setColor(hoverColor);
		} else {
			g2.setColor(bsColor);
		}
		bounds = new Rectangle(x, y, bWidth, bHeight);

		g2.fillRect(x, y, bWidth, bHeight);
		g2.setColor(bColor);
		g2.drawRect(x, y, bWidth, bHeight);

		g2.setColor(Color.black);
		Font f;
		f = new Font("Roboto-Medium", Font.BOLD, 25);
		g2.setFont(f);
		FontMetrics fm = g2.getFontMetrics();
		int fwidth = fm.stringWidth(text);
		g2.drawString(text, x + bWidth/2 - fwidth / 2, y + bHeight/2 + f.getSize()/ 2);
	}

	public void registerListener(JPanel p) {
		p.addMouseListener(m);
		p.addMouseMotionListener(ml);
	}

	public void unregisterListener(JPanel p) {
		p.removeMouseListener(m);
		p.removeMouseMotionListener(ml);
	}

	MouseMotionAdapter ml = new MouseMotionAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			
			if (bounds != null && e.getPoint() != null && bounds.contains(e.getPoint())) {
				hover = true;
			} else {
				hover = false;
			}
		}

	};
	MouseAdapter m = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (bounds!= null && e.getPoint()!=null && bounds.contains(e.getPoint())) {
				Game.sound.playButton();
				if(parent !=null)
					parent.mouseClicked();
				if(r !=null){
					r.run();
				}
			}
		}
	};
}