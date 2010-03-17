/*
 * GNU General Public License v2
 *
 * @version $Id: SoundManager.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.system;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundManager {

    private HashMap<String,Sound> sounds = new HashMap<String,Sound>();
    private HashMap<String,byte[]> cache = new HashMap<String,byte[]>();

    public void preload() {

        new Thread(new Runnable() {

            public void run() {
                /**
                 * @todo: replace it by dynamic list loading
                 */
                String[] resources = new String[]{"menu", "death", "shoot", "enemy_destroyed", "bullets_impact", "caterpillar"};
                for (String sound : resources) {
                    load(sound);
                }
                Logger.getLogger(SoundManager.class.getName()).info("Sounds " + Arrays.toString(resources) + " are loaded");
            }
        }, "Sounds preloading").start();
    }

    private byte[] load(String sound) {
        byte[] data = null;
        if (cache.get(sound) != null) {
            data = (byte[]) cache.get(sound);
        } else {
            data = convertInputStream(ResourceManager.getStream("sounds/" + sound + ".wav"));
            cache.put(sound, data);
        }
        return data;
    }

    public void play(String name, boolean loop, boolean singleInstance) {
        if (Boolean.parseBoolean(Config.getInstance().get("sound"))) {
            Sound sound = null;
            if (singleInstance && (sound = getSound(name)) != null) {
                if (sound.isRunning()) {
                    return;
                }
            }
            sound = new Sound(load(name), name);
            sound.setLoop();
            sounds.put(name, sound);
            sound.play();
        }
    }

    public void stop(String name) {
        System.out.println("stop");
        Sound sound = getSound(name);
        if (sound != null && sound.isRunning()) {
            sound.stop();
            sounds.remove(name);
        }
    }

    private Sound getSound(String name) {
        if (sounds.get(name) != null) {
            return sounds.get(name);
        }
        return null;
    }

    public void play(String name) {
        if (Boolean.parseBoolean(Config.getInstance().get("sound"))) {
            new Sound(load(name), name).play();
        }
    }

    private byte[] convertInputStream(InputStream stream) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = stream.read()) != -1) {
                output.write(b);
            }
            return output.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

class Sound {

    private InputStream stream;
    private String name;
    private static final int EXTERNAL_BUFFER_SIZE = 32;
    private Thread thread;
    private Task task = new Task();

    private class Task implements Runnable {

        private Logger logger = Logger.getLogger(Sound.class.getName());
        protected boolean play = true;
        protected boolean loop = false;
        protected SourceDataLine line = null;

        /**
         * Get the value of loop
         *
         * @return the value of loop
         */
        public boolean isLoop() {
            return loop;
        }

        /**
         * Set the value of loop
         *
         * @param loop new value of loop
         */
        public void setLoop(boolean loop) {
            this.loop = loop;
        }


        public void run() {
            do {
                AudioInputStream audioInputStream = null;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(stream);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Exception while getting audio stream", e);
                    return;
                }

                AudioFormat audioFormat = audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                try {
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(audioFormat);
                } catch (LineUnavailableException e) {
                    logger.log(Level.SEVERE, "Line unavailable", e);
                    return;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Exception", e);
                    return;
                }

                line.start();

                int nBytesRead = 0;
                byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
                int i = 0;
                while (nBytesRead != -1 && play) {
                    try {
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "IOException while reading sound stream", e);
                    }
                    if (nBytesRead >= 0) {
                        line.write(abData, 0, nBytesRead);
                    }
                }
                try {
                    stream.reset();
                } catch (IOException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (play && loop);

            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Sound(byte[] data, String name) {
        this.name = name;
        this.stream = new ByteArrayInputStream(data);
    }

    public boolean isRunning() {
        return task.line.isOpen();
    }

    public void stop() {
        task.play = false;
    }

    public void setLoop() {
        task.setLoop(true);
    }

    public void play() {
        task.play = true;
        thread = new Thread(task, "Sound-" + name);
        thread.start();
    }
}


