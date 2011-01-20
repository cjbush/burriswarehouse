package code.sound;

import java.net.URL;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;

import sun.audio.*;
import java.io.*;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class SoundPlayer {
	
    private AudioSystem audio;
    private String soundDirectory = System.getProperty("user.dir") + File.separatorChar + "src" + File.separatorChar + "data" + File.separatorChar + "sounds" + File.separatorChar;
    private InputStream in;
    private AudioStream as;
    
    private boolean playSounds = true;
    
    //run this in the JME app simpleInitGame() (or equivalent) routine
    public void initGameStuff() {
    	audio = AudioSystem.getSystem();
    	
        //create a track to use as background noise, using some default values
    	AudioTrack background1 = getMusic(SoundPlayer.class.getResource("/data/sounds/background.wav"));
        
    	//Now we have to reset a few of the values to what we're looking for specifically
    	background1.setType(TrackType.ENVIRONMENT);	//see note below on environment type
    	background1.setMaxVolume(.2f);	//volume as percent of system maximum
        
    	/*
        ENVIRONMENT type tracks are all played simultaneously in the background automatically
        assuming they have been added to the EnvironmentalPool
        
        MUSIC type tracks can be added to the MusicQueue which plays through the queue in order
        when told to play
        
        HEADSPACE type tracks
        
        POSITIONAL type tracks
        */
        
        //another background noise track if we want to use it
        AudioTrack background2 = getMusic(SoundPlayer.class.getResource("/data/sounds/background2.wav"));
        background2.setType(TrackType.ENVIRONMENT);
        background2.setMaxVolume(.2f);
        
        //Currently unused code, leaving in to demonstrate some things we can do with the MusicQueue
        audio.getMusicQueue().setRepeatType(RepeatType.ALL);
        audio.getMusicQueue().setCrossfadeinTime(0.5f); 
        audio.getMusicQueue().setCrossfadeoutTime(0.5f);

        //add the background track we created to the EnvironmentalPool
        audio.getEnvironmentalPool().addTrack(background1);

        //bind a key for muting/unmuting the volume
        KeyBindingManager.getKeyBindingManager().set("toggle_sound", KeyInput.KEY_X);

    }
    
    //run this in the JME app simpleUpdate() (or equivalent) routine
    public void updateStuff() {
    	
        audio.update();
    	
        //check for mute/unmute command
    	if(KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_sound", false))
		{
			playSounds = !playSounds;
			
			if(playSounds)
			{
				unmute();
			}
			else
			{
				mute();
			}
		}
    }
    
    private AudioTrack getMusic(URL resource) {
        // Create a non-streaming, non-looping, relative sound clip.
        AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, true);
        sound.setType(TrackType.MUSIC);
        sound.setRelative(true);
        sound.setTargetVolume(0.7f);
        sound.setLooping(false);
        return sound;
    }
    
    /**
     * Plays any sound immediately in the game 
     * 
     * @param filename	just the file name of the sound, path is automatically added in method	
     */
   public void playSound(String filename) {
        try {
        	in = new FileInputStream(soundDirectory+filename);
        	as = new AudioStream(in);
        	AudioPlayer.player.start(as);
        }
        catch (Exception e) {
        	System.out.println(e.toString());
        } 
    }
    
    
    public void cleanup() {
        audio.cleanup();
    }
    
    public void mute() {
    	audio.mute();
    }
    
    public void unmute() {
    	audio.unmute();
    }
    
    //used to start MusicQueue instantly (previous used to play sounds during movement)
    public void playQueue() {
    	audio.getMusicQueue().play();
    }
    
    //used to stop MusicQueue instantly (previous used to stop sounds after movement ceased)
    public void stopQueue() {
    	audio.getMusicQueue().stop();
    }
}