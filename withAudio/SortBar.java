import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SortBar extends Thread {

	int num;
	byte[] sound;
	boolean shouldGenerate = false;
	Clip clip;
	int SAMPLE_RATE = 2 * 1024;
	final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
	 
	public SortBar(int num) {
		this.num = num;
		sound = generateTone();
	}
	
	public void update() {
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
		shouldGenerate = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		if(!shouldGenerate) {
			SourceDataLine line = null;
			try {
				line = AudioSystem.getSourceDataLine(af);
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		       try {
				line.open(af, SAMPLE_RATE);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  line.start();
			  line.write(sound, 0, sound.length);
			  line.drain();
			  line.close();
		} else {
			sound = generateTone();
			shouldGenerate = false;
		}
	}
	
	public byte[] generateTone() {
		int freq = num + 200; // Frequency of tone
		int ms = 100; // Length of time tone plays.
	    int samples = (int)((ms * SAMPLE_RATE) / 1000);
	    byte[] output = new byte[samples];
	    double period = (double)SAMPLE_RATE / freq;
	    for (int i = output.length / 2; i < output.length; i++) {
	        double angle = 2.0 * Math.PI * i / period;
	        output[i] = (byte)(Math.sin(angle) * 127f); 
	    }
	    return output;
	}
	
	public void playSound() {
		shouldGenerate = false;
		new Thread(this).start();
	}
	
}