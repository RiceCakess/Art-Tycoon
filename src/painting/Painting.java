package painting;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Painting {
	public List<Rect> rects = new ArrayList<Rect>();
	Color frameColor2 = new Color(130, 90, 44);
	Color frameColor1 = new Color(97, 67, 33);
	Color plaque = new Color(189, 195, 199);
	public int width,height;
	public int x,y;
	public String name = "----";
	public int cost = 500;
	public double scale = 1;
	
	public int sW = (int) (width*scale);
	public int sH = (int) (height*scale);
	public Painting(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public void paint(Graphics2D g2, int x, int y){
		this.x = x;
		this.y = y;
		sW = (int) (width*scale);
		sH = (int) (height*scale);
		paintFrame(g2);
		paintName(g2);
		for(Rect r : rects){
			Rect nr = new Rect(r.x+x, r.y+ y, (int)(r.width*scale), (int)(r.height*scale), r.getColor());
			nr.paint(g2);
		}
	}
	
	public void paintFrame(Graphics2D g2){
		g2.setColor(Color.white);
		
		int strokeWidth = (int) (8 * scale);
		int hs = strokeWidth/2;
		g2.fillRect(x, y, sW, sH);
		
		g2.setColor(frameColor1);
		g2.setStroke(new BasicStroke(strokeWidth));
		g2.drawRect(x-hs, y-hs, sW+strokeWidth, sH+strokeWidth);
		g2.setColor(frameColor2);
		
		g2.drawLine(x +hs, y+sH + +hs , x+sW+ +hs, y+sH + +hs);
		g2.drawLine(x+sW + +hs, y + +hs, x+sW+ +hs, y+sH + +hs);
	
		/*g2.setStroke(new BasicStroke(2));
		g2.setColor(frameColor2);
		g2.drawLine(x, y + height, x+width, y);*/
		
	}
	public void setScale(double scale){
		this.scale = scale;	
	}
	public void paintName(Graphics2D g2){
		g2.setColor(Color.white);
		Font f = new Font("Roboto-Medium", Font.PLAIN, 20);
		g2.setFont(f);
		FontMetrics fm = g2.getFontMetrics(f);
		g2.setStroke(new BasicStroke(4f));
		g2.setColor(new Color(149, 165, 166));
		g2.drawRect(x + sW/2 - ((fm.stringWidth(name)+20)/2), sH+y+20, fm.stringWidth(name) + 20 ,  30);
		g2.setColor(plaque);
		g2.fillRect(x + sW/2 - ((fm.stringWidth(name)+20)/2), sH+y+20, fm.stringWidth(name) + 20 ,  30);
		g2.setColor(Color.white);
		g2.drawString(name, x + sW/2  - ((fm.stringWidth(name))/2), sH+y+40);
	}

}
