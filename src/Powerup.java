import java.util.ArrayList;

public class Powerup {
	
	Main m;
	
	static ArrayList<Double[]> speed;
	static ArrayList<Integer> speedTime;
	
	static ArrayList<Double[]> bullet;
	static ArrayList<Integer> bulletTime;
	
	static ArrayList<Double[]> damage;
	static ArrayList<Integer> damageTime;
	
	public Powerup(Main m) {
		this.m = m;
		
		speed = new ArrayList<Double[]>();
		speedTime = new ArrayList<Integer>();
		
		bullet = new ArrayList<Double[]>();
		bulletTime = new ArrayList<Integer>();
		
		damage = new ArrayList<Double[]>();
		damageTime = new ArrayList<Integer>();
	}
	
	static void createPowerups() {
		if(Math.random() < 0.05) {
			
		}
		
		if(Math.random() < 0.05) {
			
		}
		
		if(Math.random() < 0.05) {
			
		}
	}
	
	public Double[] findEmptySpot() {
		for(int i = 0; i < m.levels.get(m.currentLevel).length; i++) {
			
		}
		
		return null;
	}
	
	static void updatePowerups() {
		
		for(int i = 0; i < speedTime.size(); i++) {
			speedTime.set(i, speedTime.get(i) - 1);
		}
		
		for(int i = 0; i < bulletTime.size(); i++) {
			bulletTime.set(i, bulletTime.get(i) - 1);
		}
		
		for(int i = 0; i < speedTime.size(); i++) {
			speedTime.set(i, speedTime.get(i) - 1);
		}
		
		
		deletePowerups();
		
	}
	
	static void deletePowerups() {
		
	}
	
}