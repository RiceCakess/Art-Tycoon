package painting;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;


public class Draw{
	public Rectangle bounds;
	private int initialX, initialY;
	private int currentX, currentY;
	private Color color;
	private MouseMotionAdapter mml;
	private MouseAdapter ml;
	private Rectangle rect;
	int x, y, width, height;
	Random r = new Random();
	private boolean dragging = false;
	public List<Rect> srects = new ArrayList<Rect>();
	double scale = 1.4;
	
	public Draw(int sW, int sH) {
		this.width = sW;
		this.height = sH;
		color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		mml = new MouseMotionAdapter(){

			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				if(bounds.contains(p)){
					currentX = e.getX(); 
					currentY = e.getY();
					
				}
			}
		};
		
		ml = new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				if(!dragging && bounds.contains(p)){
					initialX = e.getX();
					initialY = e.getY();
					currentX = e.getX();
					currentY = e.getY();
					dragging = true;
					color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
					
				}	
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(dragging){
					complete(e.getX(), e.getY());
					dragging = false;
				}
			}
		};
		
	}

	public void register(JPanel f){
		f.addMouseListener(ml);
		f.addMouseMotionListener(mml);
	}
	
	public void unregister(JPanel f){
		f.removeMouseListener(ml);
		f.removeMouseMotionListener(mml);
	}
	
	
	public void complete(int lastX, int lastY){
		if(rect.width * rect.height > 40){
			Rect r = new Rect((int) (rect.x/scale - bounds.getX()/scale),(int) (rect.y/scale - bounds.getY()/scale), (int) (rect.width/scale), (int)(rect.height/scale), color);
			//rects.add(new Rect((int)((r.getX()*scale)+ x), (int)((r.getY() *scale)+ y), (int)(r.getWidth()*scale), (int)(r.getHeight()*scale), r.getColor()));
			srects.add(r);
		}
	}
	
	public void setScale(double scale){
		this.scale = scale;
		bounds = new Rectangle(x, y, (int)(width*scale), (int)(height*scale));
	}
	
	public void paint(Graphics2D g2, int x, int y) {
	
		this.x = x;
		this.y = y;
		bounds = new Rectangle(x, y, (int)(width*scale), (int)(height*scale));
		
		g2.setColor(Color.black);
		//g2.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
		//System.out.println(bounds);
		for(Rect r : srects){
			g2.setColor(r.getColor());
			g2.fillRect((int)((r.getX()*scale)+ x), (int)((r.getY() *scale)+ y), (int)(r.getWidth()*scale), (int)(r.getHeight()*scale));
		}
		g2.setColor(color);
		if(dragging){
			if(initialX > currentX && initialY > currentY){
				rect = new Rectangle(currentX, currentY, initialX - currentX, initialY - currentY);
				g2.fillRect(currentX, currentY, initialX - currentX, initialY - currentY );
			}
			else if(initialX > currentX){
				rect = new Rectangle(currentX, initialY, initialX - currentX, currentY - initialY);
				g2.fillRect(currentX, initialY, initialX - currentX, currentY - initialY );
			}
			else if(initialY > currentY){
				rect = new Rectangle(initialX, currentY, currentX- initialX, initialY - currentY );
				g2.fillRect(initialX, currentY, currentX- initialX, initialY - currentY );
			}
			else{
				rect = new Rectangle(initialX, initialY, currentX- initialX, currentY - initialY);
				g2.fillRect(initialX, initialY, currentX- initialX, currentY - initialY);
			}
		}
		
	}
	
	public ArrayList<Rect> getRect(){
		ArrayList<Rect> rects = new ArrayList<Rect>();
		for(Rect r : srects){
			rects.add(new Rect((int)((r.getX()*scale)+ x), (int)((r.getY() *scale)+ y), (int)(r.getWidth()*scale), (int)(r.getHeight()*scale), r.getColor()));
		}
		return rects;
	}
	
}