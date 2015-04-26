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

public class MusicPlayer implements ActionListener, StdAudio.AudioEventListener {

    // instance variables
    private Song song;
    private boolean playing = false; // whether a song is currently playing
    private JFrame frame;
    private JPanel panel1,panel2,panel3,panel4;

    private JFileChooser fileChooser;
    private JLabel welcomeText;
    private JSlider currentTimeSlider;
    private JButton button1, button2, button3, button4, button5, button6, button7, button8;
    private JTextField tempoText;


    private boolean loaded =false;

    //these are the two labels that indicate time
    // to the right of the slider
    private JLabel currentTimeLabel;
    private JLabel totalTimeLabel;

    //this the label that says 'welcome to the music player'
    private JLabel statusLabel; 
    private JLabel tempo;

    /*
     * Creates the music player GUI window and graphical components.
     */
    public MusicPlayer() {
        song = null;
        createComponents();
        doLayout();
        StdAudio.addAudioEventListener(this);
        frame.setVisible(true);
    }

    class ClickListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
                if (cmd.equals("Play")) {
                    //fill this 
                    System.out.println("Play");
                    playSong();
                    
                } else if (cmd.equals("Pause")) {
                    StdAudio.setPaused(!StdAudio.isPaused());
                } else if (cmd == "Stop") {
                    StdAudio.setMute(true);
                    StdAudio.setPaused(false);
                } else if (cmd == "Load") {
                    System.out.println("Load");
                    try {
                        loadFile();
                    } catch (IOException ioe) {
                        System.out.println("not able to load from the file");
                    }
                } else if (cmd == "Reverse") {
                    //TODO - fill this 
                    System.out.println("Reverse");
                    System.out.println(playing);
                    song.reverse();
                } else if (cmd == "Up") {
                    //TODO - fill this
                    System.out.println("up");
                    song.octaveUp();

                } else if (cmd == "Down") {
                    //TODO - fill this
                    System.out.println("Down");
                    song.octaveDown();

                } else if (cmd == "Reverse") {
                    //TODO - fill this 
                    System.out.println("Reverse");
                    song.reverse();

                } else if (cmd == "Up") {
                    //TODO - fill this
                    System.out.println("up");
                    song.octaveUp();

                } else if (cmd == "Down") {
                    //TODO - fill this
                    System.out.println("Down");
                    song.octaveDown();

                } else if (cmd == "Change") {
                    //TODO - fill this
                    System.out.println("change tempo");
                    Double ratio =Double.parseDouble(tempoText.getText());
                    System.out.println(ratio);
                    song.changeTempo(ratio);
                }
            }
        

    }



    /*
     * Called when the user interacts with graphical components, such as
     * clicking on a button.
     */
    public void actionPerformed(ActionEvent event) {

    }

    /*
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



    public static void main(String[] args) {
        MusicPlayer mp = new MusicPlayer();
        mp.createComponents();
    }


    /*
     * Sets up the graphical components in the window and event listeners.
     */
    private void createComponents() {
        //TODO - create all your components here.
        // note that you should have already defined your components as instance variables.
        frame = new JFrame("Music Player");

        panel1 = new JPanel();
        welcomeText = new JLabel("Welcome to the MusicPlayer!");

        panel2 = new JPanel();

        currentTimeSlider = new JSlider();
        //String totalTime = Double.toString(song.getTotalDuration()); 
        totalTimeLabel = new JLabel("00000.00 sec");

        panel3 = new JPanel();
        button1 = new JButton("Play");
        button2 = new JButton("Stop");
        button3 = new JButton("Load");
        button4 = new JButton("Reverse");
        button5 = new JButton("Up");
        button6 = new JButton("Down");
        button7= new JButton("Change");
        button8 = new JButton("Pause");

        fileChooser = new JFileChooser();

        //add listener for button1
        ClickListener cl = new ClickListener();
        button1.addActionListener(cl);
        button2.addActionListener(cl);
        button3.addActionListener(cl);
        button4.addActionListener(cl);
        button5.addActionListener(cl);
        button6.addActionListener(cl);
        button7.addActionListener(cl);
        button8.addActionListener(cl);

        // button1.addActionListener(c1);
        statusLabel = new JLabel();
        tempo = new JLabel("Tempo:  ");
        currentTimeLabel = new JLabel("00000.00/");

        tempoText = new JTextField("enter your tempo, default is 1:");

        panel4 = new JPanel();

        doEnabling();
    }

    /*
     * Sets whether every button, slider, spinner, etc. should be currently
     * enabled, based on the current state of whether a song has been loaded and
     * whether or not it is currently playing. This is done to prevent the user
     * from doing actions at inappropriate times such as clicking play while the
     * song is already playing, etc.
     */
    private void doEnabling() {
        if(playing == false && loaded == false){
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(true);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
            tempoText.setEnabled(false);
            currentTimeSlider.setEnabled(false);       
        }
        else if(playing == false && loaded == true){
            button1.setEnabled(true);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(true);
            button5.setEnabled(true);
            button6.setEnabled(true);
            button7.setEnabled(true);
            button8.setEnabled(false);
            tempoText.setEnabled(true);
            currentTimeSlider.setEnabled(true);
        }
        else if(playing == true && loaded == true){
            button1.setEnabled(false);
            button2.setEnabled(true);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(true);
            tempoText.setEnabled(false);
            currentTimeSlider.setEnabled(false);
        }

    }

    /*
     * Performs layout of the components within the graphical window. 
     * Also make the window a certain size and put it in the center of the screen.
     */
    private void doLayout() {
        //TODO - figure out how to layout the components
        frame.pack();

        frame.setSize(640, 400);
        // place in the center
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
                / 2 - frame.getSize().height / 2);

        frame.setLayout(new GridBagLayout());

        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        panel1.add(welcomeText,c);
        c.weighty = 0.5;   //request any extra vertical space
        c.gridx = 0;
        c.gridy = 1;
        panel1.add(statusLabel,c);
        c.weighty = 0.5;   //request any extra vertical space
        c.gridx = 1;
        c.gridy = 1;
        panel1.add(button3,c);
        
        panel2.setLayout(new GridBagLayout());
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;   //2 columns wide
        panel2.add(currentTimeSlider,c);
        
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        panel2.add(currentTimeLabel,c);
        
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel2.add(totalTimeLabel,c);

        panel3.setLayout(new GridLayout(2, 3));
        panel3.add(button1);
        panel3.add(button8);
        panel3.add(button2);
        panel3.add(button4);
        panel3.add(button5);
        panel3.add(button6);
        

        panel4.setLayout(new BoxLayout(panel4,BoxLayout.LINE_AXIS));
        
        panel4.add(Box.createRigidArea(new Dimension(280,0)));
        panel4.add(tempo);
        
        panel4.add( tempoText);
        
        panel4.add(button7);
        

        
        
        
        c.weighty = 1.0;   //request any extra vertical space
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        frame.add(panel1,c);

        c.weighty = 1.0;   //request any extra vertical space
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1; 
        frame.add(panel2,c);

        c.weighty = 1.0;   //request any extra vertical space
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        frame.add(panel3,c);

        c.weighty = 1.0;   //request any extra vertical space
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        frame.add(panel4,c);
    }


    /*
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

    /*
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

        //TODO - create a song from the file
        this.song = new Song(filename);

        tempoText.setText("1.0");
        setCurrentTime(0.0);
        updateTotalTime();
        System.out.println("Loading complete.");
        System.out.println("Song: " + song);
        loaded = true;
        doEnabling();
    }

    /*
     * Initiates the playing of the current song in a separate thread (so
     * that it does not lock up the GUI). 
     * You do not need to change this method.
     * It will not compile until you make your Song class.
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

    /*
     * Sets the current time display slider/label to show the given time in
     * seconds. Bounded to the song's total duration as reported by the song.
     */
    private void setCurrentTime(double time) {
        double total = song.getTotalDuration();
        time = Math.max(0, Math.min(total, time));
        currentTimeLabel.setText(String.format("%08.2f /", time));
        currentTimeSlider.setValue((int) (100 * time / total));
    }

    /*
     * Updates the total time label on the screen to the current total duration.
     */
    private void updateTotalTime() {
        double total = song.getTotalDuration();

        totalTimeLabel.setText(String.format("%08.2f sec", total));
    }
}
