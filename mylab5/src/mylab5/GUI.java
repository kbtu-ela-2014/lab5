package mylab5;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
 
public class GUI extends JFrame {
 
  private JTextField txt;
  private JButton btn;
  private Client sender = new Client();
  public GUI(){
    JPanel gui = new JPanel(new FlowLayout()); 
    txt=new JTextField();
    btn=new JButton("Отправить");
    add(txt, BorderLayout.CENTER);
    gui.add(btn);    
    add(gui, BorderLayout.SOUTH);
    
    btn.addActionListener(new ActionListener(){
    	@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String message=txt.getText();
			try {
				txt.setText(sender.run(message));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
    	
    });
    
  }
 
public static void main(String[] args) {
    GUI gui = new GUI();
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
    gui.setVisible(true);
    gui.setSize(200, 200);
  }
}