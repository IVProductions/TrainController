package traincontroller.traincontroller.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bitreactive.library.mqtt.MQTTConfigParam;
import com.bitreactive.library.mqtt.MQTTMessage;
import com.bitreactive.library.mqtt.robustmqtt.RobustMQTT;
import com.bitreactive.library.mqtt.robustmqtt.RobustMQTT.Parameters;

import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {

	public void openGUI() {
		final String train_id = "train_2";
		JFrame frame = new JFrame("Controller");
		frame.setLayout(new GridLayout(5, 1));

		Font font = new Font("SansSerif", Font.BOLD, 18);

		JPanel speedPanel = new JPanel();
		speedPanel.setLayout(new BorderLayout());
		JPanel destinationPanel = new JPanel();
		destinationPanel.setLayout(new BorderLayout());
		   
		JLabel panelTitle = new JLabel("Train1");
		panelTitle.setFont(font);
		speedPanel.add(panelTitle, BorderLayout.NORTH);

		final JButton setSpeedAngleButton = new JButton("Set SpeedAngle");
		setSpeedAngleButton.setOpaque(true);
		setSpeedAngleButton.setBorderPainted(false);
		setSpeedAngleButton.setBackground(Color.gray);
		setSpeedAngleButton.setForeground(Color.white);
		speedPanel.add(setSpeedAngleButton, BorderLayout.WEST);
		
		final JTextField angleValue = new JTextField();
		Font font1 = new Font("SansSerif", Font.BOLD, 30);
		angleValue.setFont(font1);
		angleValue.setHorizontalAlignment(JTextField.CENTER);
		speedPanel.add(angleValue);
		
		final JTextField destinationValue = new JTextField();
		Font font0 = new Font("SansSerif", Font.BOLD, 30);
		destinationValue.setFont(font0);
		destinationValue.setHorizontalAlignment(JTextField.CENTER);
		destinationPanel.add(destinationValue);
		
		final JButton setDestinationButton = new JButton("Set Dest");
		setDestinationButton.setOpaque(true);
		setDestinationButton.setBorderPainted(false);
		setDestinationButton.setBackground(Color.gray);
		setDestinationButton.setForeground(Color.white);
		destinationPanel.add(setDestinationButton, BorderLayout.EAST);
				
		final JButton terminate = new JButton("Terminate Train");
		terminate.setOpaque(true);
		terminate.setBorderPainted(false);
		terminate.setBackground(Color.red);
		terminate.setForeground(Color.white);
		destinationPanel.add(terminate, BorderLayout.SOUTH);
		
		//SET SPEED
		setSpeedAngleButton.addActionListener(new ActionListener(){ //send message to train
		      public void actionPerformed(ActionEvent e){
		    	  sendToBlock("SENDCOMMAND", "controller;"+train_id+";setangle;"+angleValue.getText());
		      }
		  });
		setDestinationButton.addActionListener(new ActionListener(){ //send destination to train
		      public void actionPerformed(ActionEvent e){
		    	  sendToBlock("SENDCOMMAND", "controller;"+train_id+";destination;"+destinationValue.getText());
		      }
		  });
		  terminate.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent e){
		    	  sendToBlock("SENDCOMMAND", "controller;"+train_id+";terminate");
		      }
		  });

		frame.add(speedPanel);
		frame.add(destinationPanel);
		frame.setSize(400, 400);
		frame.setVisible(true);

	}

	public Parameters initMQTTParam() {		
		MQTTConfigParam m = new MQTTConfigParam("dev.bitreactive.com");
		m.addSubscribeTopic("IVProductionsTrainController");
		Parameters p = new Parameters(m);
		return p;
	}

	//request is the actual string that is sent to the given topic
	public MQTTMessage createMQTTMessage(String request) {
		byte[] bytes = request.getBytes();
	    String topic = "IVProductionsTrainController";
		MQTTMessage message = new MQTTMessage(bytes, topic);
		message.setQoS(2);
		return message;
	}

	public void handleMessage(MQTTMessage mqttMessage) {
		//boolean wasSentFromPhone = false;
		String initialRequestString = new String(mqttMessage.getPayload());
		System.out.println(initialRequestString);
	}
	
}
