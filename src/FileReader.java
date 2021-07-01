import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FileReader {
	static JFrame frame = new JFrame();//create a frame to house the interface
	static JPanel display = new JPanel();//create jpanel to hold the components
	static JButton selectFile = new JButton("Select A Text File");//create button to open file explorer
	static JLabel wordCountLabel = new JLabel();//create label to hold the output word count
	static JLabel lineCountLabel = new JLabel();//create label to hold the output line count
	static JLabel meanWordLabel = new JLabel();//create label to hold the output mean word length
	final static JFileChooser fileChooser = new JFileChooser();//create a file chooser object
	static ArrayList<Integer> wordLengthList = new ArrayList<Integer>();//create list to hold the list of word lengths for mean calculations
	
	
	public static void main (String args[]) {//main method
		initComponents();//initialise interface components
	}
	
	//run method is called when the selectFile button is pressed by the user
	//it houses the main operations of the program
	private static void run() {
		int wordCount = 0;//var to hold the count of words in the file
		int lineCount = 0;//var to hold the count of lines in the file
		float meanWordLength = 0;//var to hold the mean word length
		
		File file = getFile();//call method to obtain a reference to the selected text file
		try {
			Scanner lineScanner = new Scanner(file);//create a scanner to read lines from the file
			while(lineScanner.hasNextLine()) {//while the line scanner has a line to read
				String line = lineScanner.nextLine();//var holds the scanners read line
				lineCount++;//increment the line counter variable
				
				Scanner wordScanner = new Scanner(line);//create a scanner to read words form the indicated line
				while(wordScanner.hasNext()) {//while there is a word to read in the scanner
					String word = wordScanner.next();//var holds the scanners read word
					word.replaceAll("[^A-Za-z0-9]","");//remove any symbols from the word
					wordCount++;//increment the word counter
					wordLengthList.add(word.length());//add the length of the word to the list
				}
				wordScanner.close();//close the word scanner object
			}
			lineScanner.close();//close the line scanner object
			
			//loop calculates the mean by getting the overall lengths and dividing by the number of words
			int count = 0;//var holds the combined count of words lengths
			for(int i = 0; i < wordLengthList.size(); i++) {//loop through the length of words list
				count = count + wordLengthList.get(i);//add the indexed word length to the count variable
			}
			meanWordLength = count / wordLengthList.size();//calculate the mean by dividing the count by the length of the list
			
		} catch (Exception exc) {exc.printStackTrace();}//catch any exceptions
		
		wordCountLabel.setText("Word Count: " + wordCount);//update the label on the display to show the word count
		lineCountLabel.setText("Line Count: " + lineCount);//update the label on the display to show the line count
		meanWordLabel.setText("Mean Word Length: " + meanWordLength);//update the label on the display to show the mean word length
	}
	
	//method is called to obtain a usable text file
	private static File getFile() {
		Boolean found = false;//var holds whether or not the text file found is valid
		File file = null;//create a file object
		
		while(!found) {//while a suitable file hasn't been found
			int returnVal = fileChooser.showOpenDialog(frame);//var holds the return of the file chooser object
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {//if the returned var is equal to the choosers 'approve option'
	            file = fileChooser.getSelectedFile();//set file object to the file selected by the user
	    		
	    		String fileName = file.getName();//var holds the name of the selected file
	    		String fileType = fileName.substring(fileName.length() - 3);//var holds the file type by taking the last 3 letters of the file name
	
	    		if(fileType.equals("txt")) {//if the file type is equal to txt (the desired file type)
	    			found = true;//set the validity boolean to true
	    		}	
	        }
			
			if(!found) {//if the found var is still false
				//open popup window to ask if the user still wishes to proceed
				int choice = JOptionPane.showConfirmDialog(null, "Do you wish to try again?", "Wrong File Type", JOptionPane.YES_NO_OPTION);
				if(choice == 1) {//if the user selected no
					System.exit(0);//exit the program
				}
			}
		}
		
		return file;//return the selected file object
	}
	
	//method is called to initialise the components of the jframe
	private static void initComponents(){
		frame.setSize(300,200);//set the dimensions of the jframe
		display.setSize(frame.getWidth(), frame.getHeight());//set the dimensions of the jpanel to the same as the jframe (to fill it)
		display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//apply an empty border to the panel (acting as a margin)
		display.setLayout(new BoxLayout(display, BoxLayout.PAGE_AXIS));//assign a vertical layout list layout to the jpanel
		selectFile.setPreferredSize(new Dimension(200,75));//set the size of the select file button
		
		frame.add(display);//add the display jpanel to the jframe
		display.add(selectFile);//add the select file button to the jpanel
		display.add(wordCountLabel);//add the word count label to the jpanel
		display.add(lineCountLabel);//add the line count label to the jpanel
		display.add(meanWordLabel);//add the mean word label to the jpanel
		
		selectFile.addActionListener(new ActionListener(){//add an action listener to the select file button (when the user presses it) 
			public void actionPerformed(ActionEvent e) {
				run();//call the run method above
			}
		});  
		
		frame.setLocationRelativeTo(null);//centre the frame to the centre of the screen
		frame.setVisible(true);//set the jframe to visible
		
	}
}
