package songs;

/*
 * Music Player

 *
 * This instructor-provided file implements the graphical user interface (GUI)
 * for the Music Player program and allows you to test the behavior
 * of your Song class.
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class MusicPlayer implements  StdAudio.AudioEventListener {

	// instance variables
	private Song song;
	private boolean playing = false; // whether a song is currently playing
	private JFrame frame;
	//panel1 is used to lay out welcomeText and status label and loadButton
	//panel2 is used to lay out currentTimeSlider, currentTimeLabel and totalTimeLabel
	//panel3 is used to layout 6 JButtons
	//Panel4 is used to lay out the tempo, tempoText, and changeButton
	private JPanel panel1,panel2,panel3,panel4; 

	private JFileChooser fileChooser; // this is the file choose button
	private JLabel welcomeText; //this the label that says 'welcome to the music player
	private JSlider currentTimeSlider; //this is the slider that shows song playing progress
	private JButton playButton, stopButton, loadButton, reverseButton, upButton, downButton, changeButton, pauseButton;
	private JTextField tempoText; // this is the text field used to get the input from the user
	private boolean loaded =false; //this is the flag used to check if the load button has been pressed or not
	private JLabel currentTimeLabel;//the following are the two labels that indicate time, to the left side of the window
	private JLabel totalTimeLabel; 
	private JLabel statusLabel; //this the label that says "current song is: xxx"
	private JLabel tempo;//this the label that says "tempo:"

	/**
	 * Constructor
	 * Creates the music player GUI window and graphical components.
	 */
	public MusicPlayer() {
		song = null;
		createComponents();
		doLayout();
		StdAudio.addAudioEventListener(this);
		frame.setVisible(true);
	}

	/**
	 * create a ClickListener that implements ActionListener, dealing with commands such as "Play", "Stop", etc
	 */
	class ClickListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			String cmd = event.getActionCommand();
			if (cmd.equals("Play")) {
				playSong(); 
			} else if (cmd.equals("Pause")) {
				StdAudio.setPaused(!StdAudio.isPaused());
			} else if (cmd == "Stop") {
				StdAudio.setMute(true);
				StdAudio.setPaused(false);
			} else if (cmd == "Load") {
				try {
					loadFile();									//load the file
				} catch (IOException ioe) {
					System.out.println("not able to load from the file");
				}
			} else if (cmd == "Reverse") {
				song.reverse();
			} else if (cmd == "Up") {
				song.octaveUp();
			} else if (cmd == "Down") {
				song.octaveDown();
			} else if (cmd == "Change") {
				Double ratio =Double.parseDouble(tempoText.getText());		//get the user input text and throw into changeTempo method
				song.changeTempo(ratio);
				updateTotalTime();
			}
		}
	}


	/**
	 * Called when audio events occur in the StdAudio library. We use this to
	 * set the displayed current time in the slider.
	 */
	public void onAudioEvent(StdAudio.AudioEvent event) {
		// update current time
		if (event.getType() == StdAudio.AudioEvent.Type.PLAY
				|| event.getType() == StdAudio.AudioEvent.Type.STOP) {
			setCurrentTime(getCurrentTime() + event.getDuration());
		}
	}


	/**
	 * main function, used to create a new MusicPlayer class and get the program running
	 * @param args
	 */
	public static void main(String[] args) {
		new MusicPlayer();
	}


	/**
	 * Sets up the graphical components in the window and event listeners.
	 */
	private void createComponents() {
		frame = new JFrame("Music Player");

		//panel1 is used to lay out welcomeText and status label and loadButton
		panel1 = new JPanel();
		welcomeText = new JLabel("Welcome to the Sensational MusicPlayer!");
		statusLabel = new JLabel();
		fileChooser = new JFileChooser();
		loadButton = new JButton("Load");

		//panel2 is used to lay out currentTimeSlider, currentTimeLabel and totalTimeLabel		
		panel2 = new JPanel();
		currentTimeSlider = new JSlider();
		currentTimeLabel = new JLabel("00000.00/");
		totalTimeLabel = new JLabel("00000.00 sec");

		//panel3 is used to layout 6 JButtons
		panel3 = new JPanel();
		playButton = new JButton("Play");
		stopButton = new JButton("Stop");
		reverseButton = new JButton("Reverse");
		upButton = new JButton("Up");
		downButton = new JButton("Down");
		pauseButton = new JButton("Pause");

		//Panel4 is used to lay out the tempo, tempoText, and changeButton
		panel4 = new JPanel();
		changeButton= new JButton("Change");
		tempo = new JLabel("Tempo: ");
		tempoText = new JTextField("enter your tempo, default is 1: ");

		//add listener for playButton, stopButton, etc
		ClickListener cl = new ClickListener();
		playButton.addActionListener(cl);
		stopButton.addActionListener(cl);
		loadButton.addActionListener(cl);
		reverseButton.addActionListener(cl);
		upButton.addActionListener(cl);
		downButton.addActionListener(cl);
		changeButton.addActionListener(cl);
		pauseButton.addActionListener(cl);

		doEnabling();
	}


	/**
	 * Sets whether every button, slider, spinner, etc. should be currently
	 * enabled, based on the current state of whether a song has been loaded and
	 * whether or not it is currently playing. This is done to prevent the user
	 * from doing actions at inappropriate times such as clicking play while the
	 * song is already playing, etc.
	 */
	private void doEnabling() {
		if(playing == false && loaded == false){    //if the load button has not been hit, then the load button is the only one that is lit
			playButton.setEnabled(false);
			stopButton.setEnabled(false);
			loadButton.setEnabled(true);
			reverseButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			changeButton.setEnabled(false);
			pauseButton.setEnabled(false);
			tempoText.setEnabled(false);
		}
		else if(playing == false && loaded == true){  //if the load button has been hit, then the load button is the only one that is dark
			playButton.setEnabled(true);
			stopButton.setEnabled(false);
			loadButton.setEnabled(false);
			reverseButton.setEnabled(true);
			upButton.setEnabled(true);
			downButton.setEnabled(true);
			changeButton.setEnabled(true);
			pauseButton.setEnabled(false);
			tempoText.setEnabled(true);
		}
		else if(playing == true && loaded == true){  //if the play button has been hit, then the pause and stop buttons are lit.
			playButton.setEnabled(false);
			stopButton.setEnabled(true);
			loadButton.setEnabled(false);
			reverseButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			changeButton.setEnabled(false);
			pauseButton.setEnabled(true);
			tempoText.setEnabled(false);
		}
	}


	/**
	 * Performs layout of the components within the graphical window. 
	 * Also make the window a certain size and put it in the center of the screen.
	 */
	private void doLayout() {
		frame.pack();
		frame.setSize(640, 400); //set the size to be a golden ratio rectangular!
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();							// place in the center
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height/ 2 - frame.getSize().height / 2);

		frame.setLayout(new GridBagLayout());  

		panel1.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();			// add welcomeText, statusLabel, and loadButton to panel4
		c.gridx = 0;
		c.gridy = 0;
		panel1.add(welcomeText,c);
		c.weighty = 0.8;   //request any extra vertical space
		c.gridx = 0;
		c.gridy = 1;
		panel1.add(statusLabel,c);
		c.weighty = 0.8;   //request any extra vertical space
		c.gridx = 1;
		c.gridy = 1;
		panel1.add(loadButton,c);

		panel2.setLayout(new GridBagLayout());
		c.weightx = 1;							// add currentTimeSlider, currentTimeLabel, totalTimeLabel to panel2
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;   //3 columns wide
		panel2.add(currentTimeSlider,c);
		c.gridx = 1;
		c.gridy = 1;
		panel2.add(currentTimeLabel,c);
		c.gridx = 1;
		c.gridy = 2;
		panel2.add(totalTimeLabel,c);

		panel3.setLayout(new GridLayout(2, 3));
		panel3.add(playButton);		// add the following 6 buttons to panel3
		panel3.add(pauseButton);
		panel3.add(stopButton);
		panel3.add(reverseButton);
		panel3.add(upButton);
		panel3.add(downButton);


		panel4.setLayout(new BoxLayout(panel4,BoxLayout.LINE_AXIS));
		panel4.add(Box.createRigidArea(new Dimension(280,0)));
		panel4.add(tempo);				// add tempo, tempoText, changeButton to panel4
		panel4.add( tempoText);
		panel4.add(changeButton);


		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		frame.add(panel1,c);    	// add panel 1 to frame
		
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1; 
		frame.add(panel2,c);		// add panel 2 to frame

		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		frame.add(panel3,c);		// add panel 3 to frame

		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		frame.add(panel4,c);		// add panel 4 to frame
	}


	/**
	 * Returns the estimated current time within the overall song, in seconds.
	 */
	private double getCurrentTime() {
		String timeStr = currentTimeLabel.getText();
		timeStr = timeStr.replace(" /", "");
		try {
			return Double.parseDouble(timeStr);
		} catch (NumberFormatException nfe) {
			return 0.0;
		}
	}


	/**
	 * Pops up a file-choosing window for the user to select a song file to be
	 * loaded. If the user chooses a file, a Song object is created and used
	 * to represent that song.
	 */
	private void loadFile() throws IOException {
		if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File selected = fileChooser.getSelectedFile();
		if (selected == null) {
			return;
		}
		statusLabel.setText("Current song: " + selected.getName());
		String filename = selected.getAbsolutePath();
		System.out.println("Loading song from " + selected.getName() + " ...");
		//create a song from the file
		this.song = new Song(filename);
		tempoText.setText("1.0");
		setCurrentTime(0.0);
		updateTotalTime();
		System.out.println("Loading complete.");
		System.out.println("Song: " + song);
		loaded = true;
		doEnabling();
	}


	/**
	 * Initiates the playing of the current song in a separate thread (so
	 * that it does not lock up the GUI). 
	 */
	private void playSong() {
		if (song != null) {
			setCurrentTime(0.0);
			Thread playThread = new Thread(new Runnable() {
				public void run() {
					StdAudio.setMute(false);
					playing = true;
					doEnabling();
					String title = song.getTitle();
					String artist = song.getArtist();
					double duration = song.getTotalDuration();
					System.out.println("Playing \"" + title + "\", by "
							+ artist + " (" + duration + " sec)");
					song.play();
					System.out.println("Playing complete.");
					playing = false;
					doEnabling();
				}
			});
			playThread.start();
		}
	}


	/**
	 * Sets the current time display slider/label to show the given time in
	 * seconds. Bounded to the song's total duration as reported by the song.
	 */
	private void setCurrentTime(double time) {
		double total = song.getTotalDuration();
		time = Math.max(0, Math.min(total, time));
		currentTimeLabel.setText(String.format("%08.2f /", time));
		currentTimeSlider.setValue((int) (100 * time / total));
	}


	/**
	 * Updates the total time label on the screen to the current total duration.
	 */
	private void updateTotalTime() {
		double total = song.getTotalDuration();
		totalTimeLabel.setText(String.format("%08.2f sec", total));
	}
}