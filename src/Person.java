import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

public class Person {
	
	Main m;
	
	public static final String[] urls = new String[] {
		"up1.png",
		"up2.png",
		"right1.png",
		"right2.png",
		"down1.png",
		"down2.png",
		"left1.png",
		"left2.png"
	};
	
	public static int avatarTime;
	public static int avatarState;
	
	//Direction the player is moving
	public static String direction;
	
	public static Double[] position;
	public static Double[] pixelPosition;
	
	public static double width;
	public static double pixelWidth;
	
	public static double aspectRatio;
	
	public static double movementSpeed;
	
	public static final int maxHealth = 5;
	public static int health;
	public static int cooldown;
	
	public static int attackDamage;
	public static double attackRange;
	
	public Person(Main m) {
		this.m = m;
		
		avatarTime = 0;
		avatarState = 4;
		width = 0.04;
		
		direction = "";
		position = new Double[] {0.5, 0.5};
		pixelPosition = new Double[2];
		movementSpeed = 0.01;
		
		health = maxHealth;
		cooldown = 0;
		
		attackDamage = 2;
		attackRange = 0.08;
		
	}
	
	public static void cooldown() {
		if(cooldown > 0) {
			cooldown--;
		}
		
	}
	
	public void takeDamage(int damage, int tick) {
		if(cooldown == 0) {
			if(health - damage > 0) {
				health -= damage;
			}else {
				health = 0;
			}
		
		}
		
		cooldown = 3;
		
		if(health <= 0 && !m.gameOver) {
			m.gameOver = true;
			m.gameOverStartTick = tick;
		}
		
	}
	
	public void attack() {
		double x1;
		double x2;
		double y1; 
		double y2;
		
		//Positions of each corner of the monster
		Double[] middlePosition = new Double[2];
		
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		//Relative mouse position
		Point rMouse = new Point();
		rMouse.x = (int) Math.round(mouse.x - m.frame.getContentPane().getLocationOnScreen().x);
		rMouse.y = (int) Math.round(mouse.y - m.frame.getContentPane().getLocationOnScreen().y);
		
		for(int i = 0; i < m.monsters.size(); i++) {
			m.monsters.get(i).updatePixelAttributes(m.display.innerWidth, m.display.innerHeight);
			x1 = m.monsters.get(i).pixelPosition[0];
			y1 = m.monsters.get(i).pixelPosition[1];
			x2 = x1 + Monster.pixelWidth;
			y2 = y1 + Monster.pixelWidth * Monster.aspectRatio;
			
			if(rMouse.x < x2 && rMouse.x > x1) {
				if(rMouse.y < y2 && rMouse.y > y1) {
					if(checkMonsterRange(i)) {
						m.monsters.get(i).takeDamage(2);
						
						if(m.monsters.get(i).health <= 0) {
							m.removeMonster(i);
						}
					}
				}
			}
		}
		
	}
	
	public boolean checkMonsterRange(int i) {
		double x1;
		double x2;
		double y1; 
		double y2;
		
		double distance;
		
		//Positions of each corner of the monster
		Double[][] monsterPositions = new Double[4][2];
		Double[] middlePosition = new Double[2];
		
		x1 = m.monsters.get(i).position[0];
		y1 = m.monsters.get(i).position[1];
		x2 = x1 + Monster.width;
		y2 = y1 + Monster.width * Monster.aspectRatio;
		
		monsterPositions[0] = new Double[] {x1, y1};
		monsterPositions[1] = new Double[] {x1, y2};
		monsterPositions[2] = new Double[] {x2, y1};
		monsterPositions[3] = new Double[] {x2, y2};
		
		middlePosition[0] = position[0] + width / 2;
		middlePosition[1] = position[1] + width * aspectRatio / 2;
		
		for(int b = 0; b < monsterPositions.length; b++){
			double xDistance = monsterPositions[b][0] - middlePosition[0];
			double yDistance = monsterPositions[b][1] - middlePosition[1];
			distance = Math.pow(xDistance, 2) + Math.pow(yDistance, 2);
			distance = Math.sqrt(distance);

			if(distance < attackRange) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public static void levelRegen() {
		health = health + (maxHealth - health) / 2;
		
	}
	
	public void updatePosition() {
		//TODO step back until not collding
		if(direction.equals("up")) {
			if(avatarTime == 0) {
				avatarState = 0;
			}else {
				avatarState = 1;
			}
			
			position[1] -= movementSpeed;
			if(wallCollision()) {
				position[1] += movementSpeed;
			}
		}else if(direction.equals("right")) {
			if(avatarTime == 0) {
				avatarState = 2;
			}else {
				avatarState = 3;
			}
			
			position[0] += movementSpeed;
			if(wallCollision()) {
				position[0] -= movementSpeed;
			}
		}else if(direction.equals("down")) {
			if(avatarTime == 0) {
				avatarState = 4;
			}else {
				avatarState = 5;
			}
			
			position[1] += movementSpeed;
			if(wallCollision()) {
				position[1] -= movementSpeed;
			}
		}else if(direction.equals("left")) {
			if(avatarTime == 0) {
				avatarState = 6;
			}else {
				avatarState = 7;
			}
			
			position[0] -= movementSpeed;
			if(wallCollision()) {
				position[0] += movementSpeed;
			}
		} 
		
	}
	
	private boolean wallCollision() {
		double x1;
		double x2;
		double y1;
		double y2;
		double pixelHeight;
		
		/*
		 * We need to most up to date information. Specifically, the pixel position. Otherwise,
		 * after we move the character, the pixel position will not be updated. Therefore, if 
		 * we move and collide with the wall, this function will return false, because we are 
		 * using the pixelPosition BEFORE we moved to collide with the wall. If this function
		 * returns false, we will not move backwards (out of the wall), meaning we are now stuck
		 * in the wall.
		 */
		updatePixelAttributes(m.display.innerWidth, m.display.innerHeight);
		
		for(int i = 0; i < m.levels.get(m.currentLevel).length; i++) {
			for(int b = 0; b < m.levels.get(m.currentLevel)[i].length; b++) {
				if(m.levels.get(m.currentLevel)[i][b] == 1) {
					x1 = m.display.startX + b * m.display.innerWidth / m.levels.get(m.currentLevel)[i].length;
					y1 = m.display.startY + i * m.display.innerHeight / m.levels.get(m.currentLevel).length;
					x2 = x1 + (m.display.innerWidth / m.levels.get(m.currentLevel)[i].length);
					y2 = y1 + (m.display.innerHeight / m.levels.get(m.currentLevel).length);
					
					pixelHeight = pixelWidth * aspectRatio;
					
					if(pixelPosition[0] < x2 && pixelPosition[0] + pixelWidth > x1
					&& pixelPosition[1] < y2 && pixelPosition[1] + pixelHeight > y1) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}
	
	public void updatePixelAttributes(double innerWidth, double innerHeight) {
		pixelPosition[0] = m.display.startX + position[0] * innerWidth;
		pixelPosition[1] = m.display.startY + position[1] * innerHeight;
		pixelWidth = width * innerWidth;
	}
	
	public static void updateAvatarTime() {
		if(avatarTime == 0) {
			avatarTime = 1;
		}else {
			avatarTime = 0;
		}
	}
	
}
