package main;
import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		  JFrame frame = new JFrame("Art Tycoon");
		  Game game = new Game();
		  frame.add(game);
		  frame.setVisible(true);
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setSize(800, 600);
		  frame.pack();
		  game.requestFocusInWindow();
		  
		  while(true){
			  game.repaint();

			  try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}

}
