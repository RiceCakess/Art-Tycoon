package util;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import main.Game;
import painting.Painting;
import painting.Rect;


public class Save {
	Game game;
	File f;
	String filename = "save.at";
	public Save(Game g){
		this.game = g;
		f = new File(filename);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean read(){
		try {
			Scanner scan = new Scanner(f);
			int num = 0;
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				switch(num){
					case 0: game.name = line; break;
					case 1: game.typeOfPainter = line; break;
					case 2: game.money = Integer.parseInt(line); break;
					case 3: game.fans = Integer.parseInt(line); break;
					case 4: game.time = Time.parseTime(game,line); break;
					default: readPainting(line); break;
				}
				num++;
			}
			
			scan.close();
			if(num == 0){
				return false;
			}
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("no save");
			return false;
		}
	}
	public void readPainting(String line){
		Painting p = parsePainting(line);
		if(p != null){
			game.paintings.add(p);
		}
	}
	public void write(){
		try {
			PrintWriter pr = new PrintWriter(f);
			pr.println(game.name);
			pr.println(game.typeOfPainter);
			pr.println(game.money);
			pr.println(game.fans);
			pr.println(game.time.toString());
			for(Painting p : game.paintings){
				String s = p.name +";"+p.cost+";";
				for(Rect r : p.rects){
					s+=r.x+":"+r.y+":"+r.width+":"+r.height+":"+r.getColor().getRed()+":"+r.getColor().getGreen()+":"+r.getColor().getBlue();
					s+="/";
				}
				pr.println(s);
			}
			pr.flush();
			pr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Painting parsePainting(String line){
		Painting p = new Painting(260,300);
		String[] split = line.split(";");
		if(split.length >= 2){
			p.name = split[0];
			p.cost = Integer.parseInt(split[1]);
			if(split.length >= 3){
				String[] rects = split[2].split("/");
				if(rects.length > 0){
					
					for(String s : rects){
						int[] vals = convertAll(s.split(":"));
						p.rects.add(new Rect(vals[0], vals[1], vals[2], vals[3], new Color(vals[4], vals[5], vals[6])));
						
					}
				}
			}
		}
		return p;
	}
	
	public static int[] convertAll(String[] s){
		int[] ints = new int[s.length];
		for(int i = 0; i < s.length; i++){
			ints[i] = Integer.parseInt(s[i]);
		}
		return ints;
	}
	
}
