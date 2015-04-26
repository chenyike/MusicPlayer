package songs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Class Song.
 */
public class Song {
    private Note[] noteArray = null;   // noteArray is used to store the song information from txt file
    private String title = "";
    private String artist = "";
    private double totalDuration = 0;
    private int totalNotes = 0;
    private ArrayList<Note> notePlayed = new ArrayList<Note>(); // notePlayered is used for storing total played notes


    /**
     * Instantiates a new song.
     * @param filename
     */
    public Song(String filename){
        this.noteArray = this.readFile(filename);
    }


    /**
     * Read file.
     * read lines from a txt file and store them in song ArrayList, 
     * from which we judge the length of each string, if the length is 5, and it ends with a true or false, then we 
     * know it is a piece of note information. we further exact the note information and put it into a new Note class
     * Also, we default first line as the song's title, the second line as the song's author, the third line as the total note number
     * @return the note[]
     */
    Note[] readFile(String filename){
        ArrayList<String> song = new ArrayList<String>();
        int i = 0;
        double duration = 0;
        File file = new File(filename);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.equals("")) continue; // ignore possible blank lines
                song.add(line);  //put the information into an ArrayList called song
            }
            //The first three lines of song contain title, artists, and total Notes
            this.title = song.get(0);
            this.setArtist(song.get(1));
            this.totalNotes = Integer.parseInt(song.get(2));
            noteArray = new Note[totalNotes];
            for (String string: song){
                String[] noteInfo = string.split(" ");
              //if the length is 3, and it ends with a true or false, then we 
                //know it is a piece of note information. we further exact the note information and put it into a new Note class
                if (noteInfo.length == 3){
                    if(noteInfo[2].equals("true")||noteInfo[2].equals("false") ){
                        noteArray[i] = new Note(Double.parseDouble(noteInfo[0].trim()),Boolean.valueOf(noteInfo[2].trim()));
                        duration += Double.parseDouble(noteInfo[0].trim()); 
                        i++;
                    }
                }
                //if the length is 5, and it ends with a true or false, then we 
                //know it is a piece of note information. we further exact the note information and put it into a new Note class
                if (noteInfo.length == 5){
                    if(noteInfo[4].equals("true")||noteInfo[4].equals("false") ){
                        noteArray[i] = new Note(Double.parseDouble(noteInfo[0].trim()),Pitch.getValueOf(noteInfo[1].trim()),Integer.parseInt(noteInfo[2].trim()),Accidental.getValueOf(noteInfo[3].trim()),Boolean.valueOf(noteInfo[4].trim()));
                        duration += Double.parseDouble(noteInfo[0].trim()); 
                        i++;
                    }
                }
            }
            this.setTotalDuration(duration);
            reader.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return noteArray;
    }


    /**
     * Play each note by calling the note's play method. The note is played from the beginning to the end, 
     * unless there are marked as being part of a repeated section.If a series of notes represents a repeated section
     * that sequence is played twice.
     */
 public void play(){
        int start =0;      // start note to play
        int count=0;       // how many "true" has been played, set to be 0 whenever a repeat part has been repeated 
        int restart=0;     // restart point for a repeat part
        int end;           // end part for a repeat part
        int replay = 0;    // has replayed or not
        ArrayList<Integer> restartPoints = new ArrayList<Integer>();
        // play a song
        while(start < noteArray.length){
            noteArray[start].play();
            notePlayed.add(noteArray[start]);
            // check for start point for a repeat part
            if(noteArray[start].isRepeat() && count==0 &&!(restartPoints.contains(start))){
                restart = start;
                replay = 0;
                restartPoints.add(start);
                count++;
            }
            // check for end point for a repeat part
            else if(noteArray[start].isRepeat() && count == 1 && replay == 0){
                end=start;
                count=0;
                replay++;
                start= restart;
                restartPoints.add(end);
                continue;
            }
            start++;
        }   
    }


    /**
     * Octave down.modify the state of the notes in your internal array so that they are 
     * all exactly 1 octave lower in pitch than their current state. For example, a C note 
     * in octave 4 would become a C note in octave 3. Any note that is a rest is not 
     * affected by this method, and the notes’ state is otherwise unchanged other than the octaves.
     * Octave 1 is the lowest possible octave allowed. If any note(s) in your song are already down 
     * at octave 1, then the entire octaveDown call should do nothing. In such a case, no notes are changed
     * @return true, if successful
     */
    public boolean octaveDown(){
        int i = 0;
        for(Note note: this.noteArray){
            if (note.getOctave() > 1){
                note.setOctave(note.getOctave() - 1);
            }
            else{
                i ++;
            }
        }
        if (i == 0){
            return true;
        }
        return false;
    }


    /**
     * Octave up. Similar to octaveDown, this raises the octave by 1. This method also has a special 
     * case. We do not allow octaves above 10. 10 is the max value that is allowed.
     * @return true, if successful
     */
    public boolean octaveUp(){
        int i = 0;
        for(Note note: this.noteArray){
            if (note.getOctave() < 10){
                note.setOctave(note.getOctave()+1);
            }
            else{
                i ++;
            }
        }
        if (i == 0){
            return true;
        }
        return false;
    }


    /**
     * Change tempo. multiply the duration of each note in your song by the given ratio. 
     * Passing a ratio of 1 will do nothing. A ratio of 3 will make each note’s duration 
     * three times as long (slow down the song), or a ratio of 0.25 will make the song speed past at 4 times the speed.
     * @param ratio the ratio
     */
    public void changeTempo(double ratio){
        if (ratio > 0){
            for (Note note: this.noteArray){
                note.setDuration(note.getDuration()*ratio);
            }
            this.totalDuration = this.totalDuration*ratio;
        }
    }


    /**
     * Reverse. reverse the order of the notes in your melody. Future calls to play 
     * would play the notes in the opposite of the order they were in before the call. 
     * For example, a song containing notes A, F, G, B would become B, G, F, A. 
     * This amounts to reversing the order of the elements of your internal array of notes.
     */
    public void reverse(){
        for (int i=0; i < this.noteArray.length/2; i++){
            Note replacedNote = null;
            replacedNote = this.noteArray[i];
            this.noteArray[i] = this.noteArray[this.noteArray.length-i-1];
            this.noteArray[this.noteArray.length-i-1] =  replacedNote;
        }
    }


    /**
     * toString method
     * simply for debugging and for the purposes of also being able to write some kind of unit test
     * In this case, we let the song class output the total played notes
     */
    public String toString(){
        String noteString = "";
        for (Note note: this.notePlayed){
            noteString += note.toString()+'\n';
        }
        return noteString;
    }


    /**
     * Gets the title.
     * @return the title
     */
    public String getTitle(){
        return this.title;
    }


    /**
     * Gets the artist.
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }


    /**
     * Sets the artist.
     * @param artist the new artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }


    /**
     * Gets the total duration.
     * @return the total duration
     */
    public double getTotalDuration() {
        return totalDuration;
    }


    /**
     * Sets the total duration.
     * @param totalDuration the new total duration
     */
    public void setTotalDuration(double totalDuration) {
        this.totalDuration = totalDuration;
    }
    
    
    /**
     * get noteArray
     * @return Note[]
     */
    public Note[] getNoteArray(){
        return this.noteArray;
    }


}
