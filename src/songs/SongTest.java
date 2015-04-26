package songs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


/**
 * The Class SongTest.
 */
public class SongTest {
    private Song song1;
    private Song song2;
    private Song song3;
    private Song song4;
    private Song song5;
    private String filename1 = "birthday.txt";
    private String filename2 = "HeIsAPirate1.txt";
    private String filename3 ="test1.txt";

    
    /**
     * Sets the up.
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        song1 = new Song(filename1);
        song2 = new Song(filename2);
        song3 = new Song(filename1);
        song4 = new Song(filename2);
        song5 = new Song(filename3);
    }
    
    
    /**
     * Test readFile().
     */
    @Test
    public void testReadFile() {
        Note[] notes1 = song1.readFile(filename1);
        assertEquals(25, notes1.length);
        Note[] notes2 = song2.readFile(filename2);
        assertEquals(262, notes2.length);
    }

    
    /**
     * Test play().
     */
    @Test
    public void testPlay() {
        song5.play();
        System.out.println(song5.toString());
        assertEquals("0.2 D 5 NATURAL true\n0.1 D 5 NATURAL false\n0.2 D 5 NATURAL false\n0.1 D 5 NATURAL false\n0.2 D 5 NATURAL true\n"
                + "0.2 D 5 NATURAL true\n0.1 D 5 NATURAL false\n0.2 D 5 NATURAL false\n0.1 D 5 NATURAL false\n0.2 D 5 NATURAL true\n"
                + "0.1 D 5 NATURAL false\n0.1 D 5 NATURAL false\n0.1 D 5 NATURAL false\n0.1 D 5 NATURAL true\n0.2 D 5 NATURAL false\n"
                + "0.1 D 5 NATURAL false\n0.2 D 5 NATURAL false\n0.1 D 10 NATURAL false\n0.2 D 1 NATURAL true\n0.1 D 5 NATURAL true\n"
                + "0.2 D 5 NATURAL false\n0.1 D 5 NATURAL false\n0.2 D 5 NATURAL false\n0.1 D 10 NATURAL false\n0.2 D 1 NATURAL true\n", song5.toString());
        
        song1.play();
        assertEquals("0.25 D 4 NATURAL false\n0.25 D 4 NATURAL false\n0.5 E 4 NATURAL false\n0.5 D 4 NATURAL false\n0.5 G 4 NATURAL false\n"
                + "1.0 F 4 SHARP false\n0.25 D 4 NATURAL false\n0.25 D 4 NATURAL false\n0.5 E 4 NATURAL false\n0.5 D 4 NATURAL false\n"
                + "0.5 A 4 NATURAL false\n1.0 G 4 NATURAL false\n0.25 D 4 NATURAL false\n0.25 D 4 NATURAL false\n0.5 D 5 NATURAL false\n"
                + "0.5 B 4 NATURAL false\n0.5 G 4 NATURAL false\n0.5 F 4 SHARP false\n1.0 E 4 NATURAL false\n0.25 C 5 NATURAL false\n"
                + "0.25 C 5 NATURAL false\n0.5 B 4 NATURAL false\n0.5 G 4 NATURAL false\n0.5 A 4 NATURAL false\n1.5 G 4 NATURAL false\n", song1.toString());
    }
    
    
    /**
     * Test geTitle().
     */
    @Test
    public void testGetTitle() {
        assertEquals("something", song1.getTitle());
        assertEquals("He's a Pirate", song2.getTitle());  
    }

    
    /**
     * Test getArtist().
     */
    @Test
    public void testGetArtist() {
        assertTrue(song1.getArtist().equals("someone"));
        assertTrue(song2.getArtist().equals("Hans Zimmer")); 
    }

    
    /**
     * Test getTotalDuration().
     */
    @Test
    public void testGetTotalDuration() {
        assertEquals(13.0, song1.getTotalDuration(),0.0);
        assertEquals(40.70, song2.getTotalDuration(),0.05);
    }


    /**
     * Test octaveDown().
     */
    @Test
    public void testOctaveDown() {
        assertTrue(song1.octaveDown());
        assertFalse(song5.octaveDown());
    }

    
    /**
     * Test octaveUp().
     */
    @Test
    public void testOctaveUp() {
        assertTrue(song1.octaveUp());
        assertFalse(song5.octaveUp());
    }

    
    /**
     * Test changeTempo().
     */
    @Test
    public void testChangeTempo() {
        double duration1 = song1.getTotalDuration();
        song1.changeTempo(3);
        assertEquals(duration1*3, song1.getTotalDuration(),0.0);
        
        double duration2 = song2.getTotalDuration();
        song2.changeTempo(0.25);
        assertEquals(duration2*0.25, song2.getTotalDuration(),0.0);  
    }

    
    /**
     * Test reverse().
     */
    @Test
    public void testReverse() {
        song1.reverse();
        int length = this.song1.getNoteArray().length;
        for (int i=0; i < length/2; i++){
            assertEquals(song1.getNoteArray()[i], song3.getNoteArray()[length -i-1]); 
        }
        
        song2.reverse();
        int length2 = this.song2.getNoteArray().length;
        for (int i=0; i < length2/2; i++){
            assertEquals(song2.getNoteArray()[i], song4.getNoteArray()[length2 -i-1]);
        }
    }

    
    /**
     * Test toString().
     */
    @Test
    public void testToString() {
        song1.play();
        assertEquals("0.25 D 4 NATURAL false\n0.25 D 4 NATURAL false\n0.5 E 4 NATURAL false\n0.5 D 4 NATURAL false\n0.5 G 4 NATURAL false\n"
                + "1.0 F 4 SHARP false\n0.25 D 4 NATURAL false\n0.25 D 4 NATURAL false\n0.5 E 4 NATURAL false\n0.5 D 4 NATURAL false\n"
                + "0.5 A 4 NATURAL false\n1.0 G 4 NATURAL false\n0.25 D 4 NATURAL false\n0.25 D 4 NATURAL false\n0.5 D 5 NATURAL false\n"
                + "0.5 B 4 NATURAL false\n0.5 G 4 NATURAL false\n0.5 F 4 SHARP false\n1.0 E 4 NATURAL false\n0.25 C 5 NATURAL false\n"
                + "0.25 C 5 NATURAL false\n0.5 B 4 NATURAL false\n0.5 G 4 NATURAL false\n0.5 A 4 NATURAL false\n1.5 G 4 NATURAL false\n", song1.toString());
    }

}