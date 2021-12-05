import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;
 	
public class Main implements ActionListener{
	
	Timer timer;
	UserInput in;
	Display display;
	JFrame frame;
	
	final static int initialWidth = 500;
	final static int initialHeight = 500;
	
	int numTicks;
	
	Person person;
	ArrayList<ArrayList<Monster>> allMonsters;
	ArrayList<Monster> monsters;
	Powerup powerup;
	
	int currentLevel;
	ArrayList<Integer[][]> levels;
	
	boolean gameOver;
	int gameOverStartTick;
	double gameOverSpeed;
	
	final Integer[][] level1 = new Integer[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	final Integer[][] level2 = new Integer[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
		{1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
		{1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	final Integer[][] level3 = new Integer[][] {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	public Main() {
		
		gameOver = false;
		gameOverSpeed = 0.001;
		
		person = new Person(this);
		powerup = new Powerup(this);
		
		timer = new Timer(10, this);
		
		in = new UserInput(this);
		
		display = new Display(this);		
		
		createLevels();
		updateLevels();
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(initialWidth,	 initialHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(in);	
		frame.addMouseListener(in);
		frame.add(display);		
				
		numTicks = 0;
		display.repaint();
		timer.start();
		
	}
	
	public static void main(String[] args) {
		System.out.println(args.length);
		new Main();
		
	}
	
	public void createLevels() {
		currentLevel = -1;
		
		levels = new ArrayList<Integer[][]>();
		levels.add(level1);
		levels.add(level2);
		levels.add(level3);
		
		allMonsters = new ArrayList<ArrayList<Monster>>();
		
		//Level 1
		allMonsters.add(new ArrayList<Monster>());
		allMonsters.get(0).add(new Monster(this, 0.5, 0.5));
		allMonsters.get(0).add(new Monster(this, 0.7, 0.7));
		
		//Level 2
		allMonsters.add(new ArrayList<Monster>());
		allMonsters.get(1).add(new Monster(this, 0.1, 0.1));
		allMonsters.get(1).add(new Monster(this, 0.6, 0.6));
		
		//Level 3
		allMonsters.add(new ArrayList<Monster>());
		allMonsters.get(2).add(new Monster(this, (1.0 / 11.0), (2.0 / 11.0)));
		allMonsters.get(2).add(new Monster(this, (4.0 / 11.0), (4.0 / 11.0)));
		allMonsters.get(2).add(new Monster(this, (9.0 / 11.0), (8.0 / 11.0)));
		
	}
	
	public void updateLevels() {
		if(currentLevel < levels.size() - 1) {
			currentLevel++;
		}else {
			endGame();
			return;
		}
		
		monsters = allMonsters.get(currentLevel);
		
		for(int i = 0; i < levels.get(currentLevel).length; i++) {
			for(int b = 0; b < levels.get(currentLevel)[0].length; b++) {
				if(levels.get(currentLevel)[i][b] == 0) {
					Person.position[0] = (double) b / levels.get(currentLevel)[0].length;
					Person.position[1] = (double) i / levels.get(currentLevel).length;
					person.updatePixelAttributes(display.innerWidth, display.innerHeight);
					break;
				}
			}
		}
		
		Person.levelRegen();
		
	}
	
	public void removeMonster(int i) {
		monsters.remove(i);
		
		if(monsters.size() == 0) {
			updateLevels();
		}
	}
	
	public void endGame() {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		numTicks++;
		
		if(numTicks % 10 == 0) {
			for(int i = 0; i < monsters.size(); i++) {
				monsters.get(i).attack(numTicks);
			}
		}
				
		if(numTicks % 20 == 0) {
			Person.cooldown();
			
			for(int i = 0; i < monsters.size(); i++) {
				monsters.get(i).coolDown();
			}
		}
		
		if(numTicks % 10 == 0) {
			Person.updateAvatarTime();
		}
		
		if(numTicks % 5 == 0) {
			Monster.setAttack();
		}
		
		person.updatePosition();
		
		for(int i = 0; i < monsters.size(); i++) {
			monsters.get(i).updatePixelAttributes(display.innerWidth, display.innerHeight);
			monsters.get(i).update(numTicks);
		}
				
		display.repaint();
		
	}
	
}