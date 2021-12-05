import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Display extends JPanel {

	Main m;
	int displayWidth;
	int displayHeight;
	boolean useWidth;
	double aspectRatio = 1;
	double innerWidth;
	double innerHeight;
	double startX;
	double startY;

	BufferedImage monsterImg;
	BufferedImage attack1Img;
	BufferedImage attack2Img;
	ArrayList<BufferedImage> characterImgs;
	BufferedImage wallImg;
	BufferedImage groundImg;
	BufferedImage gameOver;
	
	boolean monsterAttack;

	public Display(Main m) {
		this.m = m;
		
		loadImages();
	}

	protected void paintComponent(Graphics g) {
		BufferedImage img;
		RescaleOp op;
		
		Graphics2D g2d = (Graphics2D) g;

		setDisplayDimensions();
		resetParameters();
		
		m.person.updatePixelAttributes(innerWidth, innerHeight);
		
		for(int i = 0; i < m.monsters.size(); i++) {
			m.monsters.get(i).updatePixelAttributes(innerWidth, innerHeight);
		}
		
		if(m.gameOver) {
			drawEndScreen(g2d);
		}else {
			drawBackground(g2d);
			
			drawLevel(g2d);
			
			drawCharacter(g2d);
			
			drawMonsterAttack(g2d);
			
			drawMonster(g2d);
			
			drawHealth(g2d);
		}
		
		//drawMouse(g2d);
		
	}
	
	public void drawEndScreen(Graphics2D g2d) {
		BufferedImage img;
		RescaleOp op;
		Font font;
		int fontSize;
		int stringX;
		int stringY;
		double transparency;
		double gameOverDuration = 1 / m.gameOverSpeed;
		int time = m.numTicks - m.gameOverStartTick;
		
		if(time > gameOverDuration) {
			transparency = 1;
		}else {
			transparency = time / gameOverDuration;
		}
		
		img = gameOver;
		op = new RescaleOp(new float[]
		{1.0f, 1.0f, 1.0f, (float) transparency}, new float[] {255f, 255f, 255f, 0f}, null); 
		img = op.filter(img, null);
		
		g2d.drawImage(img, (int) Math.round(startX), (int) Math.round(startY), (int) Math.round(innerWidth), (int) Math.round(innerHeight), null);
		
		fontSize = (int) Math.round(m.display.innerHeight / 10);
		font = new Font("Serif", Font.BOLD, fontSize);
		g2d.setFont(font);	
		FontMetrics fm = g2d.getFontMetrics();
		stringX = (int) (startX + Math.round(innerWidth - fm.stringWidth("GAME OVER")) / 2);
		stringY = (int) (startY + Math.round(innerWidth - fm.getAscent()) / 2);
		g2d.setColor(new Color(255, 0, 0, (int) Math.round(255 * transparency)));
		g2d.drawString("GAME OVER", stringX, stringY);
		
	}
	
	public void drawMouse(Graphics2D g2d) {
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		//Relative mouse position
		Point rMouse = new Point();
		rMouse.x = (int) Math.round(mouse.x - m.frame.getContentPane().getLocationOnScreen().x);
		rMouse.y = (int) Math.round(mouse.y - m.frame.getContentPane().getLocationOnScreen().y);
		g2d.fillOval(rMouse.x, rMouse.y, 5, 5);
	}

	public void loadImages() {
		BufferedImage img;
		RescaleOp op;
		monsterImg = null;
		characterImgs = new ArrayList<BufferedImage>();

		try {
			//Character
			for (int i = 0; i < Person.urls.length; i++) {
				img = ImageIO.read(new File("person/" + Person.urls[i]));
				if(i == 0) {
					Person.aspectRatio = img.getHeight() / img.getWidth();
				}
				characterImgs.add(img);
			}
			
			//Monster
			img = ImageIO.read(new File(Monster.url)); 
			Monster.aspectRatio = (double) img.getHeight() / img.getWidth();
			op = new RescaleOp(new float[]
			{1.0f, 1.0f, 1.0f, 0.8f}, new float[] {0f, 0f, 0f, 0f}, null); 
			img = op.filter(img, null); 
			monsterImg = img;
			
			//Monster attacks
			img = ImageIO.read(new File(Monster.attack1Url)); 
			attack1Img = img;
			img = ImageIO.read(new File(Monster.attack2Url));
			attack2Img = img;
			
			//Wall
			img = ImageIO.read(new File("wall.jpg"));
			wallImg = img;
			 
			//Ground
			img = ImageIO.read(new File("ground.jpg"));
			groundImg = img;
			
			//Game over screen
			img = ImageIO.read(new File("gameover.png"));
			gameOver = img;
			 
			 
		} catch (IOException e) {
			System.out.println("Failed to load images.");
		}

	}
	
	private void drawBackground(Graphics2D g2d) {
		//Background
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, displayWidth, displayHeight);
		
		//Blue background to hide pixel rounding gaps
		g2d.setColor(Color.BLUE);
		g2d.fillRect((int) Math.round(startX), (int) Math.round(startY), 
					(int) Math.round(innerWidth), (int) Math.round(innerHeight));
	}
	
	private void drawLevel(Graphics2D g2d) {
		//Adds 1 to width and height to prevent seams
		for(int i = 0; i < m.levels.get(m.currentLevel).length; i++) {
			for(int b = 0; b < m.levels.get(m.currentLevel)[i].length; b++) {
				if(m.levels.get(m.currentLevel)[i][b] == 1) {
					//Wall drawing
					g2d.drawImage(wallImg,
							(int) (startX + b * innerWidth / m.levels.get(m.currentLevel)[i].length),
							(int) (startY + i * innerHeight / m.levels.get(m.currentLevel).length),
							(int) (innerWidth / m.levels.get(m.currentLevel)[i].length + 1),
							(int) (innerHeight / m.levels.get(m.currentLevel).length + 1),
							null
							);
				}else {
					//Ground drawing
					g2d.drawImage(groundImg,
							(int) (startX + b * innerWidth / m.levels.get(m.currentLevel)[i].length),
							(int) (startY + i * innerHeight / m.levels.get(m.currentLevel).length),
							(int) (innerWidth / m.levels.get(m.currentLevel)[i].length + 1),
							(int) (innerHeight / m.levels.get(m.currentLevel).length + 1),
							null
							);
				}
			}
		}
				
	}
	
	private void drawCharacter(Graphics2D g2d) {
		BufferedImage img;
		RescaleOp op;
		
		img = characterImgs.get(Person.avatarState);
		
		if(Person.cooldown > 0) {
			op = new RescaleOp(new float[]
					{1.0f, 1.0f, 1.0f, 1.0f}, new float[] {255f, 0f, 0f, 0f}, null); 
					img = op.filter(img, null);
		}
		
		g2d.drawImage(img,
				(int) Math.round(Person.pixelPosition[0]),
				(int) Math.round(Person.pixelPosition[1]),
				(int) Math.round(Person.pixelWidth),
				(int) Math.round(Person.pixelWidth * Person.aspectRatio),
				null);
		
	}
	
	public void drawHealth(Graphics2D g2d){
		double aspectRatio = 0.2;
		int width = (int) Math.round(innerWidth / 5);
		int height = (int) Math.round(width * aspectRatio);
		int startX = (int) Math.round(this.startX + innerWidth / 20);
		int startY = (int) Math.round(this.startY + innerWidth / 20);
		int barWidth = (int) Math.round((double) width * Person.health / Person.maxHealth);
		int fontSize = (int) Math.round(innerWidth / 20);
		
		Font font = new Font("Serif", Font.BOLD, fontSize);
		g2d.setFont(font);
		g2d.setColor(Color.WHITE);
		g2d.drawString("HEATLH", startX, startY - fontSize / 4);
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(startX, startY, width, height);
		g2d.setColor(Color.RED);
		g2d.fillRect(startX, startY, barWidth, height);
		g2d.setColor(Color.WHITE);
		g2d.drawRect(startX, startY, width, height);
	}
	
	private void drawMonsterAttack(Graphics2D g2d) {
		BufferedImage img;
		RescaleOp op;
		
		Double[] monsterMiddle = new Double[2];
		
		for(int i = 0; i < m.monsters.size(); i++) {
			if(m.monsters.get(i).attacking) {
				monsterMiddle[0] = m.monsters.get(i).pixelPosition[0] + m.monsters.get(i).pixelWidth / 2;
				monsterMiddle[1] = m.monsters.get(i).pixelPosition[1] + m.monsters.get(i).pixelWidth * Monster.aspectRatio / 2;
				
				if(Monster.attack1) {
					img = attack1Img;
				}else {
					img = attack2Img;
				}
				
				op = new RescaleOp(new float[]
				{1.0f, 1.0f, 1.0f, (float) m.monsters.get(i).attackTransparency}, new float[] {0f, 0f, 0f, 0f}, null); 
				img = op.filter(img, null);
				
				g2d.drawImage(img, 
						(int) Math.round(monsterMiddle[0] - m.monsters.get(i).pixelAttackRadius), 
						(int) Math.round(monsterMiddle[1] - m.monsters.get(i).pixelAttackRadius),
						(int) Math.round(m.monsters.get(i).pixelAttackRadius * 2), 
						(int) Math.round(m.monsters.get(i).pixelAttackRadius * 2), 
								null);
			}
		}	
		
	}
	
	private void drawMonster(Graphics2D g2d) {
		RescaleOp op;
		BufferedImage img;
		
		for(int i = 0; i < m.monsters.size(); i++) {
			op = new RescaleOp(new float[]
			{1.0f, 1.0f, 1.0f, (float) m.monsters.get(i).transparency}, new float[] {0f, 0f, 0f, 0f}, null); 
			img = op.filter(monsterImg, null);
			
			if(m.monsters.get(i).cooldown > 0) {
				op = new RescaleOp(new float[]
						{1.0f, 1.0f, 1.0f, 1.0f}, new float[] {200f, -200f, -200f, 0f}, null); 
				img = op.filter(img, null);

			}
			
			if(m.person.checkMonsterRange(i)) {
				op = new RescaleOp(new float[]
						{1.0f, 1.0f, 1.0f, 1.0f}, new float[] {0f, 0f, 100f, 0f}, null); 
				img = op.filter(img, null);
			}
					
			g2d.drawImage(img,
					(int) Math.round(m.monsters.get(i).pixelPosition[0]),
					(int) Math.round(m.monsters.get(i).pixelPosition[1]),
					(int) Math.round(Monster.pixelWidth),
					(int) Math.round(Monster.pixelWidth * Monster.aspectRatio),
					null);
		}
		
	}
	
	public void resetParameters() {
		/*
		 * If the aspect ratio of the window is smaller, then the width of the window
		 * must be proportionally smaller because the width is the numerator of the
		 * ratio. When the width is smaller, using the height of the display will result
		 * in the sides of the internal window being cut off. Therefore, we should use
		 * the width.
		 */
		try {
			useWidth = displayWidth / displayHeight < aspectRatio;
		} catch (Exception e) {
			useWidth = false;
		}

		if (useWidth) {
			innerWidth = (double) displayWidth;
			innerHeight = innerWidth / aspectRatio;
		} else {
			innerHeight = (double) displayHeight;
			innerWidth = innerHeight * aspectRatio;
		}

		startX = displayWidth / 2 - innerWidth / 2;
		startY = displayHeight / 2 - innerHeight / 2;

	}

	private void setDisplayDimensions() {
		displayWidth = m.frame.getContentPane().getWidth();
		displayHeight = m.frame.getContentPane().getHeight();

	}

}
