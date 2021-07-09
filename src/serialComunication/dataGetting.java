package serialComunication;


import java.awt.TextArea;
import java.nio.ByteBuffer;

import javax.swing.JFrame;
import javax.swing.JTextField;

//import com.google.common.*;
import com.fazecast.jSerialComm.SerialPort;
import org.jfree.data.xy.XYSeries;

public class dataGetting {
	public static SerialPort chosenPort;
	public static long x = 0;
	public static long timeInterval = 60;
	public static void main(String[] args) {
		SerialPort[] portNames = SerialPort.getCommPorts();
		/*for(int i = 0; i < portNames.length; i++) {
			System.out.println(portNames[i].getSystemPortName());
		}*/
			chosenPort = portNames[1];
			System.out.println(chosenPort.getSystemPortName());
			if(chosenPort.openPort()) {
				
				Thread thread = new Thread(){
					@Override public void run() {
						try {Thread.sleep(100); } catch(Exception e) {}
						byte[] bufferOne = new byte [4];
						/*while(chosenPort.bytesAvailable() >= 0) {
							chosenPort.readBytes(bufferOne, 1);
						}*/
						bufferOne = new byte [4];
						while(true) {
						if(chosenPort.bytesAvailable() >= 4) {
							chosenPort.readBytes(bufferOne, 4);
							System.out.println(getUnInt(bufferOne));
						}
						
						try {Thread.sleep(100); } catch(Exception e) {}
					}
				}
				
			};
				thread.start();
		}
	}
	public static boolean StartGetting (SerialPort chosenPort, XYSeries dataToAdd, JFrame window, TextArea output) {
		if(chosenPort.openPort()) {
			byte[] bufferOne = new byte [4];
			boolean connected = false;
			long timeStart = System.currentTimeMillis();
			long timeOut = System.currentTimeMillis() - timeStart;
			
			
//			chosenPort.writeBytes(new byte [] {55}, 1);
			try {Thread.sleep(100); } catch(Exception e) {}
			while(timeOut < 30000) {
				timeOut = System.currentTimeMillis() - timeStart;
				if(chosenPort.bytesAvailable() >= 4) {
					chosenPort.readBytes(bufferOne, 4);
					if(getUnInt(bufferOne) == -1) {
						
						connected = true;
						break;
						
					}
				}
			}
			if(!connected) {
				return false;
			}
			chosenPort.writeBytes(new byte [] {56}, 1);
			System.out.println("here");
			timeStart = System.currentTimeMillis();
			timeOut = System.currentTimeMillis() - timeStart;
			connected = false;
			while(timeOut < 3000) {
				timeOut = System.currentTimeMillis() - timeStart;
				if(chosenPort.bytesAvailable() >= 4) {
					chosenPort.readBytes(bufferOne, 4);
					timeInterval = getUnInt(bufferOne);
					connected = true;
					break;
				}
			}
			if(!connected) {
				return false;
			}
			
			Thread thread = new Thread(){
				@Override public void run() {
					byte[] bufferOne = new byte [4];
					try {Thread.sleep(100); } catch(Exception e) {}
					
					/*while(chosenPort.bytesAvailable() >= 0) {
						chosenPort.readBytes(bufferOne, 1);
					}*/
					bufferOne = new byte [4];
					long counts;
					while(true) {
					if(chosenPort.bytesAvailable() >= 4) {
						chosenPort.readBytes(bufferOne, 4);
						counts = getUnInt(bufferOne);
						dataToAdd.add(x += timeInterval,counts);
						output.append(counts  + System.lineSeparator());
						window.repaint();
					}
					
					try {Thread.sleep(100); } catch(Exception e) {}
				}
			}
			
		};
			thread.start();
			return true;
		}
		return false;
		
	}
	public static long getUnInt(byte[] bytes){
		 long value = 
				((bytes[3] & 0xFF) << 24) | 
				((bytes[2] & 0xFF) << 16) | 
				((bytes[1] & 0xFF) << 8 ) | 
				((bytes[0] & 0xFF) << 0 );
		return value;
	}
	
	

}
