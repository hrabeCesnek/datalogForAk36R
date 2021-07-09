package serialComunication;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.Color;
import java.awt.TextField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.TextArea;
import java.awt.Frame;

public class GeneralGui {

	private JFrame frame;
	static int x = 0;
	public static SerialPort chosenPort;
	public static TextArea CountsInput;
	public static XYSeries series;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GeneralGui window = new GeneralGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GeneralGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1099, 524);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JComboBox<String> portList = new JComboBox<String>();
		portList.setBounds(22, 6, 109, 20);
		JButton connectButton = new JButton("Connect");
		connectButton.setBounds(141, 5, 157, 23);
		JPanel topPanel = new JPanel();
		topPanel.setBounds(0, 0, 1083, 33);
		topPanel.setBackground(Color.GREEN);
		topPanel.setLayout(null);
		topPanel.add(portList);
		topPanel.add(connectButton);
		frame.getContentPane().add(topPanel);
		
		JButton SaveButton = new JButton("Save Data");
		SaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				FileDialog dialog = new FileDialog((Frame)null, "Select Folder to save");                                           //uložení do souboru
				dialog.setVisible(true);
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(dialog.getDirectory()+dialog.getFile()+ ".txt"))) //zde následuje zápis výsledkù do souboru, verze programu se liší podle vypisování
				
				{
				
				        bw.write(CountsInput.getText());
				        bw.flush();
				   
				      
				        
				        
				        
				        
					}
					
					
				catch (Exception e1)
				{
				        System.err.println("Do souboru se nepovedlo zapsat.");
				}
				
			}
		});
		SaveButton.setBounds(807, 5, 151, 23);
		topPanel.add(SaveButton);
		
		JButton btnNewButton = new JButton("Clear All data");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*if(!series.isEmpty()) {
					
				}*/
				series.clear();
				CountsInput.setText("");
			}
		});
		btnNewButton.setBounds(618, 5, 151, 23);
		topPanel.add(btnNewButton);
		
		// populate the drop-down box
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(int i = 0; i < portNames.length; i++)
			portList.addItem(portNames[i].getSystemPortName());
		
		
		
		series = new XYSeries("Ak-3.6 Readings");
		XYSeriesCollection dataset = new XYSeriesCollection(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Ak-3.6 Readings", "Time (seconds)", "Counts per interval", dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(0, 44, 621, 427);
		chartPanel.setDisplayToolTips(true);
		chartPanel.setBackground(Color.RED);
		frame.getContentPane().add(chartPanel);
		
		JLabel lblNewLabel = new JLabel("Counts per Interval:");
		lblNewLabel.setBounds(631, 54, 125, 25);
		frame.getContentPane().add(lblNewLabel);
		
		
		BufferedImage myPicture;
		
		
		
		CountsInput = new TextArea();
		CountsInput.setBounds(627, 85, 131, 386);
		frame.getContentPane().add(CountsInput);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(/*"D:\\Dropboxy\\Dan\\Dropbox\\eclipseWorkspace2\\Ak3point6RLogger\\src\\serialComunication\\images\\*/"kgbSecurityApproved.png"));
		lblNewLabel_2.setBounds(818, 66, 259, 405);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Warning! You are using " + "Soviet technology!");
		lblNewLabel_1.setBounds(766, 31, 311, 44);
		frame.getContentPane().add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBackground(Color.YELLOW);
		lblNewLabel_1.setForeground(Color.RED);
		
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
				if(connectButton.getText().equals("Connect")) {
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(chosenPort.openPort()) {
						connectButton.setText("Disconnect");
						portList.setEnabled(false);
					}
					if(dataGetting.StartGetting(chosenPort, series, frame,CountsInput)) {
						System.out.println("succes");
						
					}
					else {
						System.out.println("failed");
					}
					
				}
				else {
				// disconnect from the serial port
				chosenPort.closePort();
				portList.setEnabled(true);
				connectButton.setText("Connect");
				series.clear(); 
				x = 0;
			}
		
	}
		});
}
}
