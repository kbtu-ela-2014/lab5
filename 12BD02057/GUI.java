import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import java.util.StringTokenizer;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
 
public class GUI extends JFrame {
 
  private JTextField txt;
  private JButton send;
  private JTextField txt2;
  private Sender client;
  public GUI(){
    super("Crow calculator");
    //Подготавливаем компоненты объекта
   txt=new JTextField();
   txt2=new JTextField();
   send=new JButton();
   
   send.setText("SEND");
    //Подготавливаем временные компоненты
    JPanel buttonsPanel = new JPanel(new FlowLayout()); 
    //Расставляем компоненты по местам
    add(txt, BorderLayout.NORTH); //О размещении компонент поговорим позже
    add(txt2, BorderLayout.CENTER);
    buttonsPanel.add(send);
    
    add(buttonsPanel, BorderLayout.SOUTH);
    client=new Sender();
    send.addActionListener(new ActionListener(){
    
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String task=txt.getText();
			String num=txt2.getText();
			try {
				client.send(task, num);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			txt2.setText(client.ans);
		}
    	
    });
    
  }
 
public static void main(String[] args) {
    GUI app = new GUI();
    app.setVisible(true);
    app.pack(); //Эта команда подбирает оптимальный размер в зависимости от содержимого окна
  }
}