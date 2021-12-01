
public class Monster {
	
	public static final String url = "monster/monster.png";
	public static final String attack1Url = "monster/attack1.png";
	public static final String attack2Url = "monster/attack2.png";

	Main m;
	
	Double[] position;
	public Double[] pixelPosition;

	public static double width;
	public double pixelWidth;
	
	static double aspectRatio;
	
	int health;
	
	boolean attacking;
	double attackRange;
	int attackDamage;
	int attackStartTick;
	int lastAttackTick;
	double attackRadius;
	double pixelAttackRadius;
	double attackSpeed;
	double attackTransparency;
	boolean canDamage;
	static boolean attack1;
	
	int lastMoveTick;
	
	public Monster(Main m, double x, double y) {
		this.m = m;
		
		width = 0.04;
		
		position = new Double[] {x, y};
		pixelPosition = new Double[2];
		
		health = 20;
		attacking = false;
		attackRange = 0.15;
		attackDamage = 3;
		//We want to move first, otherwise the monster will get stuck neither moving nor attacking
		lastAttackTick = -1;
		attackSpeed = 0.07;
		canDamage = true;
		attack1 = false;
		
		lastMoveTick = 0;
	}
	
	public void attack(int tick) {
		/*
		 * Wait after moving to attack, and only attack once after moving. We need to make sure we are not 
		 * currently attacking, because otherwise we may restart the attack (by setting the radius to 0 and 
		 * resetting the time) halfway through an attack. 
		 */
		if(tick - lastMoveTick > 50 && lastMoveTick > lastAttackTick && !attacking) {
			if(Math.random() < 0.1) {
				attacking = true;
				attackStartTick = tick;
				attackRadius = 0;
				attackTransparency = 1;
				canDamage = true;
			}
		}	
		
	}
	
	public void update(int tick) {
		if(attacking) {
			updateAttack(tick);
		} else {
			//Wait after attacking to move
			if(tick - lastAttackTick > 50 && lastAttackTick > lastMoveTick) {
				updateMovement(tick);
			}
		}
		
	}
	
	public void updateAttack(int tick) {
		int time = tick - attackStartTick;
		attackRadius = attackRange * (1 - 1 / (Math.pow(Math.E, time * attackSpeed)));
		pixelAttackRadius = attackRadius * m.display.innerWidth;
		attackTransparency = 1 / Math.pow(Math.E, time * attackSpeed);
		
		if(canDamage) {
			damage();
		}
			
		if(1 - 1 / (Math.pow(Math.E, time * attackSpeed)) > 0.95){
			attacking = false;
			lastAttackTick = tick;
		}
		
	}
	
	public void damage() {
		Double[] middlePosition = new Double[2];
		
		double x;
		double y;
				
		//Top left and bottom right corners of avatar
		double px1 = Person.position[0];
		double px2 = Person.position[0] + Person.width;
		double py1 = Person.position[1];
		double py2 = Person.position[1] + Person.width * Person.aspectRatio;
				
		middlePosition[0] = position[0] + width / 2;
		middlePosition[1] = position[1] + width * aspectRatio / 2;
		
		//Finding the x and y of the closest point on the avatar's bounding box
		if(px2 < middlePosition[0]) {
			x = px2;
		}else if(px1 > middlePosition[0]) {
			x = px1;
		}else {
			x = middlePosition[0];
		}
		
		if(py2 < middlePosition[1]) {
			y = py2;
		}else if(py1 > middlePosition[1]) {
			y = py1;
		}else {
			y = middlePosition[1];
		}
		
		//Making the position relative to the monster
		x -= middlePosition[0];
		y -= middlePosition[1];
		
		double distance = Math.pow(x, 2) + Math.pow(y,  2);
		distance = Math.sqrt(distance);
		
		if(distance < attackRadius) {
			Person.takeDamage(attackDamage);
			System.out.println("bam");
			canDamage = false;
		}
		
	}
	
	public void updateMovement(int tick) {
		double x;
		double y;
		
		double distance;
		double leastDistance = Double.MAX_VALUE;
		Integer[] leastDistanceIndexes = null;					

		for(int i = 0; i < m.levels.get(m.currentLevel).length; i++) {
			for(int b = 0; b < m.levels.get(m.currentLevel)[i].length; b++) {
				if(m.levels.get(m.currentLevel)[i][b] == 0) {
					if(!occupied(i, b)) {
						x = (double) b / m.levels.get(m.currentLevel)[i].length;
						y = (double) i / m.levels.get(m.currentLevel).length;			
						
						//Making the distance relative to the player
						x -= Person.position[0];
						y -= Person.position[1];
						
						distance = Math.pow(x, 2) + Math.pow(y, 2);
						distance = Math.sqrt(distance);
						
						if(distance < leastDistance) {
							leastDistance = distance;
							leastDistanceIndexes = new Integer[] {i, b};
						}
					}
				}
			}
		}
		
		if(leastDistanceIndexes != null) {
			position[0] = (double) leastDistanceIndexes[1] / m.levels.get(m.currentLevel)[0].length;
			position[1] = (double) leastDistanceIndexes[0] / m.levels.get(m.currentLevel).length;
			lastMoveTick = tick;
		}
		
	}
	
	private boolean occupied(int i, int b) {
		int row;
		int column;
		
		for(int c = 0; c < m.monsters.size(); c++) {
			row = (int) (m.monsters.get(c).position[1] * m.levels.get(m.currentLevel).length);
			column = (int) (m.monsters.get(c).position[0] * m.levels.get(m.currentLevel)[0].length);
			 
			if(row == i && column == b) {
				return true;
			}
		}
		
		return false;
	}
	
	public void updatePixelAttributes(double innerWidth, double innerHeight) {
		pixelPosition[0] = m.display.startX + position[0] * innerWidth;
		pixelPosition[1] = m.display.startY + position[1] * innerHeight;
		pixelWidth = width * innerWidth;
	}
	
	public static void setAttack() {
		attack1 = !attack1;
	}
	
}
