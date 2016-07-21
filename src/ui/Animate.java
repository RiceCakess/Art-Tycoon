package ui;
import java.awt.Graphics2D;

	public class Animate{
		public int frame = 0;
		int maxFrame = 0;
		public boolean complete = false;
		public Animate(int maxFrame){
			this.maxFrame = maxFrame;
		}
		
		public void update(Graphics2D g2){
			if(frame < maxFrame){
				frame++;
			}
			else{
				complete = true;
			}
		}
	}