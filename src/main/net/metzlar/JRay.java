package net.metzlar;

import net.metzlar.network.client.Client;
import net.metzlar.network.server.ClientManager;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class JRay {

    /**
     * @param args client:
     *             -a       full address at which server runs (address:port)
     *             -h       start as server
     *             -o       start server only (no rendering on host)
     *             -p       port on which the server is hosted (default 9090)
     *             -s       full path to scene file
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
        File home = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath(), "/JRay");
        if (!home.exists() && !home.mkdir()) {
            System.err.printf("Failed to create home directory at: %s\n", home.getAbsolutePath());
            return false;
        }

        File models = new File(home, "models");
        if (!models.exists() && !models.mkdir()) {
            System.err.printf("Failed to create models directory at: %s\n", models.getAbsolutePath());
            return false;
        }

        return true;
    }

    private static boolean runServer(File settingsFile, int port, boolean saveOnFinish) {
        Settings settings;

        if (settingsFile == null) {
            System.out.println("No scene file specified.");
            return false;
        } else if (!settingsFile.exists()) {
            System.out.printf("Scene file at %s does not exist.", settingsFile.getAbsolutePath());
            return false;
        } else {
            try {
                settings = new Settings(settingsFile);
            } catch (IOException e) {
                System.err.println("Could not load settings file.");
                e.printStackTrace();
                return false;
            }
        }

        System.out.printf("Using scene: %s\n", settingsFile.getName());

        Image image = new Image(settings.getImageWidth(), settings.getImageHeight());
        new GUI(image);
        new ClientManager(settings, image, saveOnFinish).startServer(port);

        return true;
    }
}