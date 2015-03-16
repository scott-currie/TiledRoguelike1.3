package trl.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
    private Clip[] sounds;
    
    public SoundManager() {
        init();
    }
    
    public void init() {
        sounds = new Clip[1];
    }
    
    public void playSound(String sound) {
//        String file;
//        switch (sound) {
//            case "strike": {
//                file = "strike.aiff";
//                break;
//            }
//            case "miss": {
//                file = "miss.aiff";
//                break;
//            }
//            default: {
//                file = "";
//            }
//        }
        
//        try {
//            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("./res/" + file));
//            Clip clip = AudioSystem.getClip();
////            sounds[0] = clip;
//            clip.open(audio);
//            clip.start();
//        }
        
//        catch(UnsupportedAudioFileException uae) {
//            System.out.println(uae);
//        }
//        catch(IOException ioe) {
//            System.out.println(ioe);
//        }
//        catch(LineUnavailableException lua) {
//            System.out.println(lua);
//        }
    }
}
