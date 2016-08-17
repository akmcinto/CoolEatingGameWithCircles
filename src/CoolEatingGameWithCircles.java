import java.awt.EventQueue;

import javax.swing.JFrame;


public class CoolEatingGameWithCircles extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6297097418223955733L;

	public CoolEatingGameWithCircles() {
		
		add(new GameSpace());
		
		setResizable(false);
        pack();
        
        setTitle("Cool Eating Game With Circles");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
	        
		EventQueue.invokeLater(new Runnable() {
			@Override
	        public void run() {                
				JFrame ex = new CoolEatingGameWithCircles();
	            ex.setVisible(true);                
			 }
		 });
	 }

}
