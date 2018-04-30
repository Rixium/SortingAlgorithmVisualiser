import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Sorter extends JPanel implements Runnable, ActionListener, ChangeListener {

	Thread thread;
	
	boolean bubbleSort = false, 
	insertionSort = false, mergeSort = false, 
	started = false, selectionSort = false, 
	sorted = false, heapSort = false;

	long startTime, endTime;
	float totalTime;
	
	String finishText = "";
	String shuffleType = "Random";
	
	boolean hasSwapped = false, forceStop = false;
	int maxNum = 300;
	int currIndex = 0;
	SortBar[] sortBars;
	
	int speed = 5;
	
	int maxFoundIndex = 0;
	
	int iteration = 2;
	int currX = 0;
	int currY = 1;
	
	boolean leftSorted, rightSorted;
	SortBar[] tempArray;

	int mouseX, mouseY;
	
	public Sorter() {
		maxNum = Constants.SCREEN_HEIGHT;
		initGraph();
		thread = new Thread(this);
		thread.start();
	}
	
	private void initGraph() {
		sorted = false;
		sortBars = new SortBar[(Constants.SCREEN_WIDTH - 10) / 12];
		Random r = new Random();
		int rNum;
		
		if(shuffleType.equals("Random")) {
			for(int i = 0; i < sortBars.length; i++) {
				rNum = r.nextInt((maxNum - 10) + 1) + 10;
				sortBars[i] = new SortBar(rNum);
			}
		}
		
		if(shuffleType.equals("Nearly Sorted")) {
			for(int i = 0; i < sortBars.length; i++) {
				rNum = i * Constants.SCREEN_HEIGHT / sortBars.length;
				if(r.nextInt(100) < 40) {
					if(i > 1) {
						int theNum = sortBars[i - 1].getNum();
						sortBars[i - 1].setNum(rNum);
						rNum = theNum;
					}
				}
				sortBars[i] = new SortBar(rNum);
			}
		}
		
		if(shuffleType.equals("Few Unique")) {
			for(int i = 0; i < sortBars.length; i++) {
				rNum = r.nextInt(5);
				switch(rNum) {
					case 0:
					rNum = (int)((float)Constants.SCREEN_HEIGHT * 1f);
					break;
					case 1:
					rNum = (int)((float)Constants.SCREEN_HEIGHT * .8f);
					break;
					case 2:
					rNum = (int)((float)Constants.SCREEN_HEIGHT * .6f);
					break;
					case 3:
					rNum = (int)((float)Constants.SCREEN_HEIGHT * .4f);
					break;
					case 4:
					rNum = (int)((float)Constants.SCREEN_HEIGHT * .2f);
					break;
					default:
					break;
				}
				
				sortBars[i] = new SortBar(rNum);
			}
		}
		
		if(shuffleType.equals("Reversed")) {
			int currIt = sortBars.length - 1;
			for(int i = 0; i < sortBars.length; i++) {
				rNum = i * Constants.SCREEN_HEIGHT / sortBars.length;
				sortBars[currIt] = new SortBar(rNum);
				currIt--;
			}
		}
	}
	
	@Override
	public void run() {
		while(true) {
			repaint();
			update();
		}
	}
	
	private void update() {
		if(insertionSort) {
			try { 
				Thread.sleep(speed);
			} catch (Exception ex) {}
			if(!started) {
				currX = 0;
				currY = 1;
				iteration = 0;
				startTime = System.currentTimeMillis();
				started = true;
			}
			if(sortBars[currX].getNum() > sortBars[currY].getNum()) {
				int currIndexNum = sortBars[currY].getNum();
				int iNum = sortBars[currX].getNum();
				
				sortBars[currX].setNum(currIndexNum);
				sortBars[currY].setNum(iNum);
				sortBars[currY].playSound();
			}
			
			
			if(currX < sortBars.length - 1) {
				currX++;
			} else {
				currX = 0;
				currY++;
			}
			
			if(currY == sortBars.length){
				for(int i = 0; i < sortBars.length; i++) {
					sortBars[i].playSound();
					currX = i;
					currY = i - 1;
					repaint();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sorted = true;
				end(false);
			}
		}
		
		if(bubbleSort) {
			try { 
				Thread.sleep(speed);
			} catch (Exception ex) {}
			if(!started) {
				iteration = 2;
				startTime = System.currentTimeMillis();
				started = true;
				currY = 1;
			}
			if(sortBars[currX].getNum() > sortBars[currY].getNum()) {
				int x = sortBars[currX].getNum();
				int y = sortBars[currY].getNum();
				
				sortBars[currX].setNum(y);
				sortBars[currY].setNum(x);
				
				hasSwapped = true;
			}
			
			if(currY > sortBars.length - iteration) {
				if(!hasSwapped) {
					for(int i = 0; i < sortBars.length; i++) {
						sortBars[i].playSound();
						currX = i;
						currY = i - 1;
						repaint();
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					sorted = true;
					end(false);
				} else {
					hasSwapped = false;
					sortBars[currY].playSound();
					iteration++;
					currX = 0;
					currY = 1;
				}
			} else {	
				currX++;
				currY++;
			}
		}
		
		if(selectionSort) {
			startTime = System.currentTimeMillis();
			startSelectionSort(sortBars);
			
			for(int i = 0; i < sortBars.length; i++) {
				sortBars[i].playSound();
				currX = i;
				currY = i - 1;
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sorted = true;
			end(false);
		}
		
		if(mergeSort) {
			if(!started) {
				forceStop = false;
				started = true;
			}
			startTime = System.currentTimeMillis();
			tempArray = new SortBar[sortBars.length];
			mergeSort(0, sortBars.length - 1, sortBars);
			
			for(int i = 0; i < sortBars.length; i++) {
				sortBars[i].playSound();
				currX = i;
				currY = i - 1;
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sorted = true;
			end(false);
		}
		
		if(heapSort) {
			heapSortBars(sortBars);
		}
	}
	
	private void heapify(SortBar[] bars, int n, int i) {
		takeBreak();
		int smallest = i;
		int l = 2 * i + 1;
		int r = 2 * i + 2;
		
		if (l < n && bars[l].getNum() > bars[smallest].getNum()) {
			smallest = l;
		}	
		
		if(r < n && bars[r].getNum() > bars[smallest].getNum()) {
			smallest = r;
		}
		
		if(smallest != i) {
			swapBars(i, smallest);
			heapify(bars, n, smallest);
		}
	}
	
	private void swapBars(int s, int l) {
		if(sortBars[s].getNum() < sortBars[l].getNum()) {
			int sNum = sortBars[s].getNum();
			sortBars[s].setNum(sortBars[l].getNum());
			currX = s;
			currY = l;
			sortBars[l].setNum(sNum);
			sortBars[l].playSound();
		}
		repaint();
	}
	
	private void heapSortBars(SortBar[] bars) {
		int n = bars.length;
		
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(bars, n, i);
		}
		
		for (int i=n-1; i>=0; i--) {
			SortBar temp = bars[0];
            bars[0] = bars[i];
            bars[i] = temp;
			heapify(bars, i, 0);
		 }

		sorted = true;
		heapSort = false;
	}
	
	private void takeBreak() {
		try { 
			Thread.sleep(speed);
		} catch (Exception ex) {}
	}
	
	private void startSelectionSort(SortBar[] bars) {
		int maxFound = 0;
		int maxFixed = bars.length - 1;
		forceStop = false;
		while(true) {
			
			currY = maxFixed;
			maxFound = 0;
			maxFoundIndex = 0;
			
		    
			for(int i = 0; i <= maxFixed; i++) {
				currX = i;
				
				if(bars[i].getNum() > maxFound) {
					maxFound = bars[i].getNum();
					maxFoundIndex = i;
				}
				repaint();
				try { 
				Thread.sleep(speed);
				} catch (Exception ex) {}
			}
			
			int currMax = bars[maxFixed].getNum();
			
			bars[maxFoundIndex].setNum(currMax);
			bars[maxFixed].setNum(maxFound);
			
			bars[maxFixed].playSound();
			
			maxFixed--;
			
			if(maxFixed == 0 || forceStop) {
				break;
			}
			
			repaint();
		}
	}
	
	private void merge(int startX, int mid, int endX, SortBar[] bars) {
		for(int i = startX; i <= endX; i++) {
			tempArray[i] = bars[i];
		}
		
		int i = startX;
		int j = mid + 1;
		int k = startX;
		
		while(i <= mid && j <= endX) {
			if(!forceStop) {
				if(tempArray[i].getNum() <= tempArray[j].getNum()) {
					bars[k] = tempArray[i];
					i++;
				} else {
					bars[k] = tempArray[j];
					j++;
				}
				try { 
					Thread.sleep(speed);
				} catch (Exception ex) {}
				k++;
				repaint();
			} else {
				break;
			}
		}
		
		while(i <= mid) {
			if(!forceStop) {
				try { 
					Thread.sleep(speed);
				} catch (Exception ex) {}
				bars[k] = tempArray[i];
				k++;
				i++;
				repaint();
			} else {
				break;
			}
		}
	}
	
	private boolean mergeSort(int startX, int endX, SortBar[] bars) {
		if(startX >= endX) {
			return false;
		}
		repaint();
		int mid = startX + (endX - startX) / 2;
		
		if(endX - startX > 1) {
			mergeSort(startX, mid, bars);
			mergeSort(mid + 1, endX, bars);
			merge(startX, mid, endX, bars);
		} else {
			try { 
				Thread.sleep(speed);
			} catch (Exception ex) {}
			currX = startX;
			currY = endX;
			if(bars[startX].getNum() > bars[endX].getNum()) {
				int xNum = bars[startX].getNum();
				int midNum = bars[endX].getNum();
				bars[startX].setNum(midNum);
				bars[endX].setNum(xNum);
				bars[endX].playSound();
			}
			
			return false;
		}
		if(startX >= bars.length - 1) {
				return false;
		}
		return false;
	}
	
	private void end(boolean reset) {
		currX = 0;
		currY = 0;
		
		bubbleSort = false;
		insertionSort = false;
		mergeSort = false;
		selectionSort = false;
		maxFoundIndex = 0;
		heapSort = false;
		
		started = false;
		hasSwapped = false;
		forceStop = true;
		
		if(!reset) {
			endTime = System.currentTimeMillis();
			totalTime = endTime - startTime;
			finishText = "Finished in: " + (totalTime / 1000L) + " seconds.";
		} else {
			finishText = "";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			// Check if the action is a button.
			JButton button = (JButton) e.getSource();
			if(button.getName().equals("bubbleSort")) {
				end(true);
				bubbleSort = true;
			} else if (button.getName().equals("reset")) {
				end(true);
				initGraph();
			} else if (button.getName().equals("insertionSort")) {
				end(true);
				insertionSort = true;
			} else if (button.getName().equals("mergeSort")) {
				end(true);
				mergeSort = true;
			} else if (button.getName().equals("selectionSort")) {
				end(true);
				selectionSort = true;
			} else if (button.getName().equals("heapSort")) {
				end(true);
				heapSort = true;
			}
		} else if (e.getSource() instanceof JComboBox) {
			JComboBox box = (JComboBox) e.getSource();
			shuffleType = (String)box.getSelectedItem();
		}
		started = false;
	}
	
	public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
		speed = (slider.getValue() - 100) / 5;
		speed = -speed;
    }
	
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		
		for(int i = 0; i < sortBars.length; i++) {
			if(bubbleSort || insertionSort || mergeSort || selectionSort || heapSort) {
				if(currX == i) {
					g.setColor(Color.RED);
				} else if(currY == i){
					g.setColor(Color.BLUE);
				} else {
					g.setColor(Color.WHITE);
				}
				
				if(i == maxFoundIndex && selectionSort) {
					g.setColor(Color.GREEN);
				}
			} else {
				if(!sorted) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.GREEN);
				}
			}
			int xPos = Constants.SCREEN_WIDTH / 2 - ((sortBars.length * 10 + sortBars.length * 2) / 2) + i * 10 + i * 2;
			g.fillRect(xPos, Constants.SCREEN_HEIGHT - 5 - sortBars[i].getNum(), 10, sortBars[i].getNum());
			g.setColor(Color.WHITE);
			g.drawString(finishText, 10,20);
		}
    }
	
}