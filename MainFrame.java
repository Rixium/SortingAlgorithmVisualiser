import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sort");
		setBackground(new Color(255, 255, 255));
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setVisible(true);
		setResizable(false);

		JPanel container = new JPanel(new BorderLayout());
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		JButton bubbleSort = new JButton("Bubble Sort");
		bubbleSort.setName("bubbleSort");
		
		JButton quickSort = new JButton("Insertion Sort");
		quickSort.setName("insertionSort");
		
		JButton mergeSort = new JButton("Merge Sort");
		mergeSort.setName("mergeSort");
		
		JButton selectionSort = new JButton("Selection Sort");
		selectionSort.setName("selectionSort");
		
		JLabel shuffleTypeLabel = new JLabel("Shuffle Type:");
		shuffleTypeLabel.setForeground(Color.WHITE);
		
		JLabel speedLabel = new JLabel("Speed:");
		speedLabel.setForeground(Color.WHITE);
		
		String[] shuffleSelections = new String[] { "Random", "Nearly Sorted", "Reversed", "Few Unique" };
		JComboBox<String> shuffleType = new JComboBox<>(shuffleSelections);
		JSlider slider = new JSlider();
		slider.setName("speedSlider");
		slider.setBackground(new Color(30, 39, 47));
		slider.setForeground(Color.WHITE);
		
		Sorter sorter = new Sorter();
		
		slider.addChangeListener(sorter);
		
		bubbleSort.addActionListener(sorter);
		quickSort.addActionListener(sorter);
		mergeSort.addActionListener(sorter);
		selectionSort.addActionListener(sorter);
		shuffleType.addActionListener(sorter);
		
		setStyle(bubbleSort);
		setStyle(quickSort);
		setStyle(mergeSort);
		setStyle(selectionSort);
		
		JButton resetButton = new JButton("Reset");
		resetButton.setName("reset");
		resetButton.addActionListener(sorter);
		
		setStyle(resetButton);
		
		buttonPanel.add(shuffleTypeLabel);
		buttonPanel.add(shuffleType);
		buttonPanel.add(speedLabel);
		buttonPanel.add(slider);
		buttonPanel.add(bubbleSort);
		buttonPanel.add(quickSort);
		buttonPanel.add(mergeSort);
		buttonPanel.add(selectionSort);
		buttonPanel.add(resetButton);
		
		buttonPanel.setBackground(Color.BLACK);
		
		container.add(buttonPanel, BorderLayout.NORTH);
		container.add(sorter, BorderLayout.CENTER);
		sorter.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
		add(container);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	private void setStyle(JButton button) {
		button.setBackground(new Color(30, 39, 47));
		button.setForeground(Color.WHITE);
	}
	
}