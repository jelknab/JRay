package net.metzlar;

import net.metzlar.network.client.Client;
import net.metzlar.network.server.Server;
import net.metzlar.renderEngine.Statistics;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;

public class JRay {
    public static final Statistics statistics = new Statistics();

    public static final File HOME_DIRECTORY = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath(), "/JRay");
    public static final File MODEL_DIRECTORY = new File(HOME_DIRECTORY, "models");

    /**
     * @param args client:
     *             -a       full address at which server runs (address:port)
     *             -h       start as server
     *             -o       start server only (no rendering on host)
     *             -p       port on which the server is hosted (default 9090)
     *             -s       full path to sceneSettings file
     *             --save   Save image
     */
    public static void main(String[] args) {
        System.out.printf("JRay thread: %s\n\n", ManagementFactory.getRuntimeMXBean().getName());

        // Defaults
        boolean     serverMode      = false;
        boolean     clientMode      = true;
        boolean     saveOnFinish    = false;
        int         hostPort        = 9090;
        String      serverAddress   = "";
        File        settingsFile    = null;

        // Commandline argument parsing
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-h":
                    serverMode = true;
                    break;

                case "-p":
                    hostPort = Integer.parseInt(args[++i]);
                    break;

                case "-a":
                    serverAddress = args[++i];
                    break;

                case "-s":
                    settingsFile = new File(args[++i]);
                    break;

                case "-o":
                    clientMode = false;
                    break;

                case "--save":
                    saveOnFinish = true;
                    break;

                default:
                    System.out.printf("Unknown parameter: %s\n", arg);
            }
        }

        if (!createDirectories()) {
            System.err.println("Could not create application directories, shutting down.");
            return;
        }

        if (serverMode && !runServer(settingsFile, hostPort, saveOnFinish)) {
            System.out.println("Server could not be started. Shutting down.");
            return;
        }

        if (clientMode) {
            try {
                new Client(serverAddress).start();
            } catch (IOException e) {
                System.out.println("Failed to start client");
                e.printStackTrace();
            }
        }
    }

    private static boolean createDirectories() {
        if (!HOME_DIRECTORY.exists() && !HOME_DIRECTORY.mkdir()) {
            System.err.printf("Failed to create home directory at: %s\n", HOME_DIRECTORY.getAbsolutePath());
            return false;
        }

        if (!MODEL_DIRECTORY.exists() && !MODEL_DIRECTORY.mkdir()) {
            System.err.printf("Failed to create models directory at: %s\n", MODEL_DIRECTORY.getAbsolutePath());
            return false;
        }

        return true;
    }

    private static boolean runServer(File settingsFile, int port, boolean saveOnFinish) {
        Settings settings;
        String settingsXML;

        if (settingsFile == null) {
            System.out.println("No sceneSettings file specified.");
            return false;
        } else if (!settingsFile.exists()) {
            System.out.printf("SceneSettings file at %s does not exist.", settingsFile.getAbsolutePath());
            return false;
        } else {
            try {
                settingsXML = new String(Files.readAllBytes(settingsFile.toPath()));
            } catch (FileNotFoundException e) {
                System.err.println("Could not find settingsXML file.");
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                System.err.println("Could not loadSettingsDocument settingsXML file.");
                e.printStackTrace();
                return false;
            }
        }

        settings = new Settings(Jsoup.parse(settingsXML));

        System.out.printf("Using sceneSettings: %s\n", settingsFile.getName());

        Image image = new Image(settings.parseImageSettings());
        new GUI(image);
        new Server(settingsXML, image, saveOnFinish).start(port);

        return true;
    }
}
