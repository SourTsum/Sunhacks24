//package controllers;
//
//import events.Pandemonium;
//
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.*;
//import java.awt.*;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Scanner;
//
//public class DebugMode {
//    private Scanner cin;
//    private HashMap<String,Command> commands = new HashMap<String,Command>();
//
//    private final JFrame mainFrame;
//
//    public DebugMode(JFrame mainFrame) throws UnsupportedAudioFileException, LineUnavailableException, IOException, FontFormatException, InterruptedException, AWTException {
//        setup();
//        this.mainFrame = mainFrame;
//        System.out.println("[CLIENT]: Entering debug mode...");
//        System.out.println("          For help type help");
//        cin = new Scanner(System.in);
//
//        while(mainFrame.isVisible()) {
//            String input = cin.nextLine();
//            String[] cmdBreakdown = getKwargs(input);
//
//            if(commands.containsKey(cmdBreakdown[0])) {
//                if(cmdBreakdown.length == 1) {
//                    Command command = commands.get(cmdBreakdown[0]);
//                    System.out.println("\n\033[3mUsage: DebugMode.java [OPTIONS]\033[3m\n");
//                    String helpMSG = command.getHelpMSG();
//                    System.out.printf("  %s\n",helpMSG);
//                    System.out.println("\nOptions:");
//                    for(String option : command.options.keySet()){
//                        System.out.printf("  -%s           %s\n",option,command.options.get(option));
//                    }
//                }
//                if(cmdBreakdown.length >= 2) {
//                    Command command = commands.get(cmdBreakdown[0]);
//                    if (command.getName().equals("start")) {
//                        start(cmdBreakdown);
//                    } else if (command.getName().equals("type")) {
//                        generateLabel(input, cmdBreakdown);
//                    }
//                }
//            } else {
//                System.out.printf("\n\033[3m%s is an invalid command\033[3m\n",cmdBreakdown[0]);
//            }
//        }
//    }
//
//    private void start(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, AWTException, FontFormatException, InterruptedException {
//        String event = args[1];
//        if(event.equals("pandemonium")) {
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            int width = (int) screenSize.getWidth();
//            int height = (int) screenSize.getHeight();
//
//            JPanel layer2 = new JPanel();
//            layer2.setLayout(null);
//            layer2.setBounds(0, 0, width, height);
//            layer2.setBackground(new Color(0, 0, 0, 0)); // Set background color with full transparency
//            layer2.setOpaque(false); // Allow the background to be transparent
//            mainFrame.add(layer2);
//
//            Pandemonium test = new Pandemonium(layer2);
//            test.start();
//            return;
//        }
//        System.out.printf("\n\033[3m%s is an invalid option\033[3m\n",args[1]);
//    }
//
//    private void generateLabel(String fullInput, String[] args) {
//        System.out.println("[CLIENT]: Generating your TypeWriterLabel object");
//
//        // Check for the required arguments
//        if (args.length < 2) {
//            System.out.println("\n\033[3mUsage: label -text <YourText> -italics <true|false>\033[3m\n");
//            return;
//        }
//
//        // Initialize variables
//        String text = "";
//        boolean italics = false;
//
//        // Parse the fullInput for -text and -italics options
//        String[] splitInput = fullInput.split(" -");
//        for (String input : splitInput) {
//            if (input.startsWith("text ")) {
//                text = input.substring(5); // Remove "text " prefix
//            } else if (input.startsWith("italics ")) {
//                String italicsValue = input.substring(8).trim(); // Remove "italics " prefix
//                italics = Boolean.parseBoolean(italicsValue); // Convert string to boolean
//            }
//        }
//
//        // Check if the text was provided
//        if (text.isEmpty()) {
//            System.out.println("\n\033[3mError: Text is required.\033[3m\n");
//            return;
//        }
//
//        // Create the typewriter label
//        TypeWriterLabel typeWriterLabel = new TypeWriterLabel(text, italics);
//
//        // Add the label to the main frame
//        mainFrame.add(typeWriterLabel);
//        mainFrame.repaint();  // Repaint the frame to ensure the label is displayed
//
//        // Start the typing effect
//        typeWriterLabel.type();
//    }
//
//
//
//    private String[] getKwargs(String cmd) {
//        int kwargsCount = 0;
//        for (int i = 0; i < cmd.length(); i++) {
//            if (cmd.charAt(i) == '-') {
//                kwargsCount++;
//            }
//        }
//
//        String[] resKWARGS = new String[kwargsCount + 1];
//
//        int firstSpaceIndex = cmd.indexOf(" ");
//        if (firstSpaceIndex == -1) {
//            resKWARGS[0] = cmd;
//            return resKWARGS;
//        }
//
//        resKWARGS[0] = cmd.substring(0, firstSpaceIndex);
//        cmd = cmd.substring(firstSpaceIndex + 1).trim();
//
//        for (int i = 1; i < resKWARGS.length; i++) {
//            int argStart = cmd.indexOf("-");
//            int argEnd = cmd.indexOf(" ", argStart + 1);
//            if (argEnd == -1) {
//                argEnd = cmd.length();
//            }
//            resKWARGS[i] = cmd.substring(argStart + 1, argEnd);
//            cmd = cmd.substring(argEnd).trim();
//        }
//
//        return resKWARGS;
//    }
//
//    private void setup(){
//        Command start = new Command("start","Starts specified game sequence.");
//        start.addOption("pandemonium","Starts the pandemonium mini game.");
//        commands.put("start",start);
//
//        Command help = new Command("help","Displays a list of all commands.");
//        help.addOption("help","Displays a list of all commands");
//        help.addOption("start","Starts specified game sequence");
//        help.addOption("type","Generates a typewriter label with specified text");
//        commands.put("help",help);
//
//        // Add a new "label" command
//        Command type = new Command("type", "Generates a typewriter label with specified text.");
//        type.addOption("italics","Enables italics for the inputted text.");
//        type.addOption("text", "Text to be displayed on the label.");
//        commands.put("type", type);
//    }
//
//
//}
