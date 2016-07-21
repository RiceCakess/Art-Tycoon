package painting;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;



@SuppressWarnings("serial")
public class Rect extends Rectangle{
		private Color color;
		
		public Rect(int x, int y, int width, int height, Color c) {
			super(x,y,width,height);
			color = c;
		}
		
		public void paint(Graphics2D g2){
			g2.setColor(color);
			g2.fillRect(x, y, width, height);
		}
		public Color getColor(){
			return color;
		}
		
}