import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserInput implements KeyListener, MouseListener{
	
	Main m;
	
	//If each of the corresponding keys are pressed.
	boolean up;
	boolean down;
	boolean left;
	boolean right;
	
	public UserInput(Main m) {
		this.m = m;
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		m.person.attack();
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		key = Character.toLowerCase(key);
		
		if(key == 'w') {
			if(!up) {
				up = true;
				Person.direction = "up";
			}
		}else if(key == 's') {
			if(!down) {
				down = true;
				Person.direction = "down";
			}
		}else if(key == 'd') {
			if(!right) {
				right = true;
				Person.direction = "right";
			}	
		}else if(key == 'a') {
			if(!left) {
				left = true;
				Person.direction = "left";
			}
		}else if(key == ' ') {
			m.restartGame();
			m.display.lastScreen = null;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		char key = e.getKeyChar();
		
		if(key == 'w') {
			up = false;
			if(Person.direction.equals("up")) {
				Person.direction = "none";
			}
		}else if(key == 's') {
			down = false;
			if(Person.direction.equals("down")) {
				Person.direction = "none";
			}
		}else if(key == 'd') {
			right = false;
			if(Person.direction.equals("right")) {
				Person.direction = "none";
			}
		}else if(key == 'a') {
			left = false;
			if(Person.direction.equals("left")) {
				Person.direction = "none";
			}		
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
