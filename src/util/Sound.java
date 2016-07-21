package util;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	AudioInputStream fmain,fbutton,ftyping,fcash;
	Clip main,button,typing,cash;
	public Sound(){
		try {
			fmain = AudioSystem.getAudioInputStream(new File("main.wav"));
			fbutton = AudioSystem.getAudioInputStream(new File("button2.wav"));
			fcash = AudioSystem.getAudioInputStream(new File("cash.wav"));
			ftyping = AudioSystem.getAudioInputStream(new File("typing.wav"));
			
			main = AudioSystem.getClip();
			button = AudioSystem.getClip();
			typing = AudioSystem.getClip();
			cash = AudioSystem.getClip();
			
			openSound(main,fmain,-18.0f);
			openSound(button,fbutton,-10.0f);
			openSound(typing,ftyping,-14.0f);
			openSound(cash,fcash,-14.0f);
			
			//cash.setLoopPoints(0, (int) (cash.getFrameLength()/1.25));
			//typing.setLoopPoints(0, (int) (typing.getFrameLength()/10));
			
		}
		catch(Exception e){
			System.out.println("Error loading sounds");
		}
	}
	
	public void openSound(Clip c, AudioInputStream as, float vol){
		try {
			c.open(as);
			FloatControl gaincontrol = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			gaincontrol.setValue(vol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	boolean playingSound = false;
	
	public void playMain(){
		main.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void playButton(){
		button.loop(1);
	}
	
	public void playCash(){
		//cash.start();
		//cash.stop();
		cash.loop(1);
		//System.out.println("test");
	}
	
	public void playTyping(int amt){
		typing.loop(amt);
	}
	
}
