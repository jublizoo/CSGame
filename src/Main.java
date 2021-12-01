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
	
	int currentLevel;
	ArrayList<Integer[][]> levels;
	
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
	
	public Main() {
		createLevels();
		updateLevels();
		
		person = new Person(this);
		
		timer = new Timer(10, this);
		
		in = new UserInput(this);

		display = new Display(this);		
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(initialWidth,	 initialHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(in);		
		frame.add(display);		
				
		numTicks = 0;
		display.repaint();
		timer.start();
		
	}
	
	public static void main(String[] args) {
		new Main();
		
	}
	
	public void createLevels() {
		currentLevel = -1;
		
		levels = new ArrayList<Integer[][]>();
		levels.add(level1);
		
		allMonsters = new ArrayList<ArrayList<Monster>>();
		
		//Level 1
		allMonsters.add(new ArrayList<Monster>());
		allMonsters.get(0).add(new Monster(this, 0.5, 0.5));
		allMonsters.get(0).add(new Monster(this, 0.7, 0.7));

		
		//Level 2
		allMonsters.add(new ArrayList<Monster>());
		allMonsters.get(1).add(new Monster(this, 0.1, 0.1));
		allMonsters.get(1).add(new Monster(this, 0.6, 0.6));
		
	}
	
	public void updateLevels() {
		currentLevel++;
		
		monsters = allMonsters.get(currentLevel);
		
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