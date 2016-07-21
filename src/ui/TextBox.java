package ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class TextBox {
	int bWidth = 280;;
	int bHeight = 40;
	String ctext = "";
	private int frame;

	public void draw(Graphics2D g2, int x, int y) {
		g2.setStroke(new BasicStroke(2f));
		g2.setColor(Color.black);
		g2.drawRect(x, y, bWidth, bHeight);
		Font f = new Font("Roboto-Medium", Font.BOLD, 30);
		g2.setFont(f);
		FontMetrics fm = g2.getFontMetrics();
		int fwidth = fm.stringWidth(ctext);
		if(frame > 40 && frame < 80){
			g2.drawLine(x +fwidth/2 + bWidth/2 , y  +5 , x + fwidth/2 + bWidth/2, y  + 35);
			frame++;
		}
		else if (frame > 80){
			frame = 0;
		}
		else{
			frame++;
		}
		g2.drawString(ctext, x + bWidth/2 - fwidth/2 , y + 30);
	}

	public void registerListener(JPanel p) {
		p.addKeyListener(ky);
	}

	public void unregisterListener(JPanel p) {
		p.removeKeyListener(ky);
	}
	
	public boolean validKeyCode(int keycode){
		return  (keycode > 47 && keycode < 58)   || // number keys
		        keycode == 32 || keycode == 13   || // spacebar & return key(s) (if you want to allow carriage returns)
		        (keycode > 64 && keycode < 91)   || // letter keys
		        (keycode > 95 && keycode < 112)  || // numpad keys
		        (keycode > 185 && keycode < 193) || // ;=,-./` (in order)
		        (keycode > 218 && keycode < 223);  
	}

	KeyAdapter ky = new KeyAdapter(){
		
		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				if(ctext.length()>0)
					ctext = ctext.substring(0, ctext.length() - 1);
			}
			else if(ctext.length() <= 16 && validKeyCode(code)){
				ctext +=e.getKeyChar();
			}
		}

	};
}