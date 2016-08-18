import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameSpace extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4002887667861501327L;
	
	private Integer spaceWidth = 1000;
	private Integer spaceHeight = 750;
	
	private int velocity = 5;
	
	private PlayerCircle me;
	private int startSize = 15;
	private int edibleMultiplier = 5;
	
	private int delays = 100;

	private ArrayList<Circle> edibleCircles = new ArrayList<Circle>();
	private int numEdibles = 10;

	public GameSpace() {
		
		addKeyListener(new ArrowAdapter());
		setBackground(Color.white);
        setFocusable(true);

        setPreferredSize(new Dimension(spaceWidth, spaceHeight));
		
        initialize();
        
	}
	
	private void initialize() {
		
        me = new PlayerCircle();
        for (int j = 0; j < numEdibles; j++) {
            edibleCircles.add(new Circle());
        }
        
        Timer timer = new Timer(delays, this);
        timer.start();
		
	}
	
	public void actionPerformed(ActionEvent actionEvent) {
		
		me.moveOn();
		
    	Iterator<Circle> iter = edibleCircles.iterator();
    	while (iter.hasNext()) {
    		Circle ec = iter.next();
    		ec.moveOn();
    	}

        repaint();
		
	}
	
	@Override
    public void paintComponent(Graphics circles) {
		
        super.paintComponent(circles);
        moveCircles(circles);
        
    }	
	
    private void moveCircles(Graphics circles) {
    	
    	circles.fillOval((int) me.h_position, (int) me.v_position, me.size, me.size);

    	Iterator<Circle> iter = edibleCircles.iterator();
    	while (iter.hasNext()) {
    		Circle ec = iter.next();
    		circles.setColor(ec.circleColour);
    		circles.fillOval((int) ec.h_position, (int) ec.v_position, ec.size, ec.size);
    	}
    	
    	Toolkit.getDefaultToolkit().sync();

    }  
	
	/***** Private Classes *****/
	
	private class Circle {
		int h_velocity;
		
		double h_position;
		double v_position;
		
		int size;
		
		Color circleColour;
		
		double h_center;
		double v_center;
		
		private Circle() {
			
			this.h_velocity = (int) (Math.random() * 10);  
			// Make sure it does not have 0 velocity
			while (this.h_velocity == 0) {
				this.h_velocity = (int) (Math.random() * 10);
			}
	
			if (Math.random() < 0.5) {
				this.h_position = 0;
			} else {
				this.h_position = spaceHeight;
				this.h_velocity *= -1;
			}
			this.v_position = (int) (Math.random() * spaceHeight);
						
			this.size = ((int) (Math.random() * 10));
			// Make sure it does not have 0 size
			while (this.size == 0) {
				this.size = ((int) (Math.random() * 10));
			}
			this.size *= edibleMultiplier;
			
			this.h_center = this.h_position + (this.size / 2);
			this.v_center = this.v_position + (this.size / 2);

			this.circleColour = new Color((this.size*25) % 255, ((int) this.v_position*10) % 255, (this.size*10) % 255);

		}
		
		private void moveOn() {
			
			this.h_position += this.h_velocity;
			this.h_center = this.h_position + (this.size / 2);
			
			checkEdge();
			
		}	
		
		private void checkEdge() {
			
			if (this.h_position <= 0 || this.h_position >= spaceWidth) {
				circleRegenerate();
			}
			
		}
		
		private void circleRegenerate() {
			
			int index = edibleCircles.indexOf(this);
			edibleCircles.set(index, new Circle());
			
		}
	}
	    
	private class PlayerCircle extends Circle {
		
		int v_velocity;
		
		private PlayerCircle() {
			this.h_position = spaceWidth / 2;
			this.v_position = spaceHeight / 2;
			this.h_velocity = 0;
			this.v_velocity = 0;
			this.size = startSize;
			
			this.h_center = this.h_position + (this.size / 2);
			this.v_center = this.v_position + (this.size / 2);
		}
	
		private void moveOn() {
			
			this.h_position += this.h_velocity;
			this.v_position += this.v_velocity;
			
			this.h_center = this.h_position + (this.size / 2);
			this.v_center = this.v_position + (this.size / 2);
			
			checkTouch();
			checkEdge();
			
		}
		
		private void checkTouch() {
			double v_diff;
			double h_diff;
			double sep;
			// http://stackoverflow.com/questions/8566336/java-circle-circle-collision-detection
			for (Circle edible : edibleCircles) {
				v_diff = this.v_center - edible.v_center;
				h_diff = this.h_center - edible.h_center; 
				sep = (this.size / 2) + (edible.size / 2);
				if ((v_diff * v_diff)+(h_diff * h_diff) < (sep * sep)) {
					if (this.size > edible.size) {
						eat(edible);
					} else {
						die();
					}
				}
			}
		}
		
		private void eat(Circle eaten) {
			grow(eaten.size / edibleMultiplier);
			eaten.circleRegenerate();
		}
		
		private void die() {
			
		}
		
		private void checkEdge() {
			
			if (this.h_position <= 0) {
				this.h_position = spaceWidth - 1;
			} 
			else if (this.h_position >= spaceWidth) {
				this.h_position = 1;
			}
			
			if (this.v_position <= 0) {
				this.v_position = spaceHeight - 1;
			} 
			else if (this.v_position >= spaceHeight) {
				this.v_position = 1;
			}
			
		}
		
		private void grow(int eatenSize) {
			this.size += eatenSize;
		}
	}
	
	private class ArrowAdapter extends KeyAdapter {
		
		public void keyPressed(KeyEvent keyPress) {
			int key = keyPress.getKeyCode();
			
			if (key == KeyEvent.VK_UP) {
				me.v_velocity = -1 * velocity;
			}
			
			if (key == KeyEvent.VK_DOWN) {
				me.v_velocity = velocity;
			}
			
			if (key == KeyEvent.VK_LEFT) {
				me.h_velocity = -1 * velocity;
			}
			
			if (key == KeyEvent.VK_RIGHT) {
				me.h_velocity = velocity;
			}
		}
		
		public void keyReleased(KeyEvent keyRelease) {
			int key = keyRelease.getKeyCode();
			
			if (key == KeyEvent.VK_UP) {
				me.v_velocity = 0;
			}
			
			if (key == KeyEvent.VK_DOWN) {
				me.v_velocity = 0;
			}
			
			if (key == KeyEvent.VK_LEFT) {
				me.h_velocity = 0;
			}
			
			if (key == KeyEvent.VK_RIGHT) {
				me.h_velocity = 0;
			}
		}
		
	}
	
}
