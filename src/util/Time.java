package util;
import main.Game;
import ui.PopUp;


public class Time {
	int year, month, week;
	long currentTime = System.currentTimeMillis();
	long hanging = 0;
	private boolean paused = false;
	private double per = 0;
	Game parent;
	public Time(Game g){
		year = 2015;
		month = 1;
		week = 1;
		this.parent = g;
	}
	
	public Time(int year, int month, int week){
		this.year = year;
		this.month = month;
		this.week = week;
	}
	
	public void updateTime(){
		if(!paused){
			long timePassed = System.currentTimeMillis() - currentTime;
			per= timePassed/20000.0;
			if(timePassed/20000 >= 1){
				int m = month;
				addWeek(1);
				if(m != month){
					parent.popups.add(new PopUp("Rent!", "A month has passed! / Rent as been auto deducted from /  your balance Have a nice day! / -Landlord", false, parent, new Runnable(){

						@Override
						public void run() {
							parent.change(-1000);
						}
						
					}));
					
				}
				currentTime = System.currentTimeMillis();
			}
		}
	}
	
	@Override
	public String toString(){
		return year + "Y - " + month + "M - " + week + "W ";
	}
	
	public void toggle(){
		if(paused){
			paused = false;
			currentTime = System.currentTimeMillis() - hanging;
		}
		else{
			paused = true;
			hanging = System.currentTimeMillis() - currentTime;
		}
	}
	
	public double getPer(){
		return per;
	}
	public boolean isPaused(){
		return paused;
	}
	public void addWeek(int i){
		week += i;
		if(week/4 >= 1){
			month += week/4;
			week = week % 4;
		}
		if(month/12 >= 1){
			year += month/12;
			month = month % 12;
		}
		parent.saveGame();
	}

	public static Time parseTime(Game g, String line) {
		Time t = new Time(g);
		String cur = line.replace("Y - ", ":");
		cur = cur.replace("M - ", ":");
		cur = cur.replace("W ", "");
		int[] val = Save.convertAll(cur.split(":"));
		t.year = val[0];
		t.month = val[1];
		t.week = val[2];
		return t;
	}
	
}
