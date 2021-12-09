import java.util.ArrayList;

public class Powerup {
	
	Main m;
	
	int despawnTime;
	
	static ArrayList<Double[]> speed;
	static ArrayList<Integer> speedTime;
	
	static ArrayList<Double[]> bullet;
	static ArrayList<Integer> bulletTime;
	
	static ArrayList<Double[]> damage;
	static ArrayList<Integer> damageTime;
	
	static ArrayList<Double[]> health;
	static ArrayList<Integer> healthTime;
		
	public Powerup(Main m) {
		this.m = m;
		
		despawnTime = 10;
			
		speed = new ArrayList<Double[]>();
		speedTime = new ArrayList<Integer>();
		
		bullet = new ArrayList<Double[]>();
		bulletTime = new ArrayList<Integer>();
		
		damage = new ArrayList<Double[]>();
		damageTime = new ArrayList<Integer>();
		
		health = new ArrayList<Double[]>();
		healthTime = new ArrayList<Integer>();
	}
	
	void createPowerups() {
		Integer[] spot;
		Double[] spotPos = new Double[2];
		
		spot = findEmptySpot();
		if(spot != null) {
			spotPos[0] = (double) spot[0] / m.levels.get(m.currentLevel)[0].length;
			spotPos[1] = (double) spot[1] / m.levels.get(m.currentLevel).length;
					
			if(Math.random() < 0.03) {
				speed.add(spotPos);
				speedTime.add(despawnTime);
			}
		}
		
		spot = findEmptySpot();
		if(spot != null) {
			spotPos[0] = (double) spot[0] / m.levels.get(m.currentLevel)[0].length;
			spotPos[1] = (double) spot[1] / m.levels.get(m.currentLevel).length;
					
			if(Math.random() < 0.03) {
				bullet.add(spotPos);
				bulletTime.add(despawnTime);
			}
		}
		
		spot = findEmptySpot();
		if(spot != null) {
			spotPos[0] = (double) spot[0] / m.levels.get(m.currentLevel)[0].length;
			spotPos[1] = (double) spot[1] / m.levels.get(m.currentLevel).length;
					
			if(Math.random() < 0.03) {
				damage.add(spotPos);
				damageTime.add(despawnTime);
			}
		}
		
		spot = findEmptySpot();
		if(spot != null) {
			spotPos[0] = (double) spot[0] / m.levels.get(m.currentLevel)[0].length;
			spotPos[1] = (double) spot[1] / m.levels.get(m.currentLevel).length;
					
			if(Math.random() < 0.03) {
				health.add(spotPos);
				healthTime.add(despawnTime);
			}
		}
		
	}
	
	public Integer[] findEmptySpot() {
		Integer[] spot = null;
		
		//The lowest random value, which was found when the current spot was updated last
		double lowestRandom = Double.MAX_VALUE;
		double random;
		
		for(int i = 0; i < m.levels.get(m.currentLevel).length; i++) {
			for(int b = 0; b < m.levels.get(m.currentLevel)[i].length; b++) {
				if(m.levels.get(m.currentLevel)[i][b] == 0) {
					if(!occupied(i, b)) {
						random = Math.random();
						if(random < lowestRandom) {
							lowestRandom = random;
							spot = new Integer[] {b, i};
						}
					}
				}
			}
		}
		
		return spot;
		
	}
	
	public boolean occupied(int i, int b) {
		int row;
		int column;

		for(int c = 0; c < speed.size(); c++) {
			row = (int) (speed.get(c)[1] * m.levels.get(m.currentLevel).length);
			column = (int) (speed.get(c)[0] * m.levels.get(m.currentLevel)[0].length);
			 
			if(row == i && column == b) {
				return true;
			}
		}
		
		for(int c = 0; c < bullet.size(); c++) {
			row = (int) (bullet.get(c)[1] * m.levels.get(m.currentLevel).length);
			column = (int) (bullet.get(c)[0] * m.levels.get(m.currentLevel)[0].length);
			 
			if(row == i && column == b) {
				return true;
			}
		}
		
		for(int c = 0; c < damage.size(); c++) {
			row = (int) (damage.get(c)[1] * m.levels.get(m.currentLevel).length);
			column = (int) (damage.get(c)[0] * m.levels.get(m.currentLevel)[0].length);
			 
			if(row == i && column == b) {
				return true;
			}
		}
		for(int c = 0; c < health.size(); c++) {
			row = (int) (health.get(c)[1] * m.levels.get(m.currentLevel).length);
			column = (int) (health.get(c)[0] * m.levels.get(m.currentLevel)[0].length);
			 
			if(row == i && column == b) {
				return true;
			}
		}
		
		return false;
		
	}
	
	static void updatePowerups() {
		
		for(int i = 0; i < speedTime.size(); i++) {
			speedTime.set(i, speedTime.get(i) - 1);
		}
		
		for(int i = 0; i < bulletTime.size(); i++) {
			bulletTime.set(i, bulletTime.get(i) - 1);
		}
		
		for(int i = 0; i < damageTime.size(); i++) {
			damageTime.set(i, damageTime.get(i) - 1);
		}
		
		for(int i = 0; i < healthTime.size(); i++) {
			healthTime.set(i, healthTime.get(i) - 1);
		}
		
		deletePowerups();
		
	}
	
	//TODO Account for powerups deleted at the same time
	static void deletePowerups() {
		for(int i = 0; i < speedTime.size(); i++) {
			if(speedTime.get(i) == 0) {
				speed.remove(i);
				speedTime.remove(i);
			}
		}
		
		for(int i = 0; i < bulletTime.size(); i++) {
			if(bulletTime.get(i) == 0) {
				bullet.remove(i);
				bulletTime.remove(i);
			}
		}
		
		for(int i = 0; i < damageTime.size(); i++) {
			if(damageTime.get(i) == 0) {
				damage.remove(i);
				damageTime.remove(i);
			}
		}
		
		for(int i = 0; i < healthTime.size(); i++) {
			if(healthTime.get(i) == 0) {
				health.remove(i);
				healthTime.remove(i);
			}
		}
	}
	
	public void collision(){
		int x1;
		int y1;
		int x2;
		int y2;
		int squareSize = (int) Math.round(m.display.innerWidth / m.levels.get(m.currentLevel)[0].length);
		double pixelHeight;
		
		for(int i = 0; i < speed.size(); i++) {
			x1 = (int) Math.round(m.display.startX + Powerup.speed.get(i)[0] * m.display.innerWidth);
			y1 = (int) Math.round(m.display.startY + Powerup.speed.get(i)[1] * m.display.innerHeight);
			x2 = x1 + squareSize;
			y2 = y1 + squareSize;
			
			pixelHeight = Person.pixelWidth * Person.aspectRatio;
			
			if(Person.pixelPosition[0] < x2 && Person.pixelPosition[0] + Person.pixelWidth > x1
			&& Person.pixelPosition[1] < y2 && Person.pixelPosition[1] + pixelHeight > y1) {
				speed.remove(i);
				speedTime.remove(i);
				Person.speedPwr();
			}
		}
		
		for(int i = 0; i < bullet.size(); i++) {
			x1 = (int) Math.round(m.display.startX + Powerup.bullet.get(i)[0] * m.display.innerWidth);
			y1 = (int) Math.round(m.display.startY + Powerup.bullet.get(i)[1] * m.display.innerHeight);
			x2 = x1 + squareSize;
			y2 = y1 + squareSize;
			
			pixelHeight = Person.pixelWidth * Person.aspectRatio;
			
			if(Person.pixelPosition[0] < x2 && Person.pixelPosition[0] + Person.pixelWidth > x1
			&& Person.pixelPosition[1] < y2 && Person.pixelPosition[1] + pixelHeight > y1) {
				bullet.remove(i);
				bulletTime.remove(i);
				Person.bulletPwr();
			}
		}
		
		for(int i = 0; i < damage.size(); i++) {
			x1 = (int) Math.round(m.display.startX + Powerup.damage.get(i)[0] * m.display.innerWidth);
			y1 = (int) Math.round(m.display.startY + Powerup.damage.get(i)[1] * m.display.innerHeight);
			x2 = x1 + squareSize;
			y2 = y1 + squareSize;
			
			pixelHeight = Person.pixelWidth * Person.aspectRatio;
			
			if(Person.pixelPosition[0] < x2 && Person.pixelPosition[0] + Person.pixelWidth > x1
			&& Person.pixelPosition[1] < y2 && Person.pixelPosition[1] + pixelHeight > y1) {
				damage.remove(i);
				damageTime.remove(i);
				Person.damagePwr();
			}
		}
		
		for(int i = 0; i < health.size(); i++) {
			x1 = (int) Math.round(m.display.startX + Powerup.health.get(i)[0] * m.display.innerWidth);
			y1 = (int) Math.round(m.display.startY + Powerup.health.get(i)[1] * m.display.innerHeight);
			x2 = x1 + squareSize;
			y2 = y1 + squareSize;
			
			pixelHeight = Person.pixelWidth * Person.aspectRatio;
			
			if(Person.pixelPosition[0] < x2 && Person.pixelPosition[0] + Person.pixelWidth > x1
			&& Person.pixelPosition[1] < y2 && Person.pixelPosition[1] + pixelHeight > y1) {
				health.remove(i);
				healthTime.remove(i);
				Person.healthPwr();
			}
		}
	}
	
}
