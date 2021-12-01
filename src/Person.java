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
	public static int health;
	public static int cooldown;
	
	public Person(Main m) {
		this.m = m;
		
		avatarTime = 0;
		avatarState = 4;
		width = 0.04;
		
		direction = "";
		position = new Double[] {0.5, 0.5};
		pixelPosition = new Double[2];
		movementSpeed = 0.01;
		
		health = 20;
		cooldown = 0;
		
	}
	
	public static void cooldown() {
		if(cooldown > 0) {
			cooldown--;
		}
		
	}
	
	public static void takeDamage(int damage) {
		if(cooldown == 0) {
			if(health - damage > 0) {
				health -= damage;
			}else {
				System.out.println("Game over");
			}
		
		}
		
		cooldown = 3;
		
	}
	
	public void updatePosition() {
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
