package com.grpone.assembler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
import com.formdev.flatlaf.FlatDarculaLaf;
import static java.lang.System.in;

public class AssemblerView {
    JFrame f;
    JFileChooser fileChooser;
    JPanel gui, gui1, buttonPanel;
    JEditorPane assemblyText;
    JTextArea assembledText;
    JButton open, translateButton, moreOptions;
    static File tempFile;
    static File fileLocation;
    public static String path;

    AssemblerView(){

        SwingUtilities.invokeLater(() -> {
            FlatDarculaLaf.setup();
            JFrame.setDefaultLookAndFeelDecorated(true);

            // Create new JFrame
            // Give "Hack Assembler" as title
            f = new JFrame("Hack Assembler");

            // So the program terminates on clicking the close button
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // JFileChooser
            fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Hack Assembly Code", "asm");
            fileChooser.addChoosableFileFilter(filter);

            // Create 2 panels
            // 1 for displaying input and 1 for displaying output
            gui = new JPanel(new BorderLayout());
            gui1 = new JPanel(new BorderLayout());

            // JEditorPane to display the Hack Assembly Program
            // EditorPane is used as live editing can be added later
            assemblyText = new JEditorPane();
            assemblyText.setCaretColor(Color.white);
            assemblyText.setForeground(Color.WHITE);
            assemblyText.setBackground(Color.decode("#19232d"));


            // Add it to GUI(panel)
            gui.add(new JScrollPane(assemblyText), BorderLayout.CENTER);

            // JTextArea to show the output
            assembledText = new JTextArea();
            assembledText.setBackground(Color.decode("#19232d"));
            assembledText.setForeground(Color.WHITE);
            assembledText.setEditable(false);

            gui1.add(new JScrollPane(assembledText), BorderLayout.CENTER);

            // Button to open file
            // On click will call JFileChooser
            open = new JButton("Open");
            // Set button & text colors
            open.setForeground(Color.black);
            open.setBackground(Color.decode("#455364"));
            open.setFocusable(false);

            translateButton = new JButton("Translate");
            translateButton.setBackground(Color.decode("#455364"));
            translateButton.setForeground(Color.black);
            translateButton.setFocusable(false);

            // Add hover animation when cursor is on button
            translateButton.getModel().addChangeListener(e -> hoverAnimation(translateButton, e));

            open.getModel().addChangeListener(e -> hoverAnimation(open, e));

            // When translate button is clicked, execute this function
            translateButton.addActionListener(e -> translate());

            // When open button is clicked, execute this function
            open.addActionListener(ae -> openFile());

            // Set preferred Size of panels
            gui.setPreferredSize(new Dimension(300,400));
            gui1.setPreferredSize(new Dimension(300,400));

            // Extra functions
            moreOptions = new JButton("More Options");
            moreOptions.setForeground(Color.black);
            moreOptions.setBackground(Color.decode("#455364"));
            moreOptions.setFocusable(false);

            // Add hover animation
            moreOptions.getModel().addChangeListener(e -> hoverAnimation(moreOptions, e));

            // Pop-up menu
            final JPopupMenu moreOptionsMenu = new JPopupMenu();
            // Add all these options to Pop-up menu
            JMenuItem showFormatted = new JMenuItem("Show Formatted");
            JMenuItem showSymbol_less = new JMenuItem("Show Symbol-less");
            JMenuItem saveFileAs = new JMenuItem("Save File As");
            JMenuItem startCpuEmulator = new JMenuItem("Start CPU Emulator");
            JMenuItem compareCodes = new JMenuItem("Compare Hack Codes");
            moreOptionsMenu.add(showFormatted);
            moreOptionsMenu.add(showSymbol_less);
            moreOptionsMenu.add(saveFileAs);
            moreOptionsMenu.add(startCpuEmulator);
            moreOptionsMenu.add(compareCodes);

            // To display pop-up menu when "More Options" button is pressed
            moreOptions.addActionListener(e -> moreOptionsMenu.show(moreOptions, 0, -5*moreOptions.getHeight()+10));

            // Add functions to Pop-up menu items
            // Execute these functions when Pop-up menu items are pressed
            showFormatted.addActionListener(e-> showF());

            showSymbol_less.addActionListener(e-> showSL());

            saveFileAs.addActionListener(e-> sFas());

            // To start custom dark CPU Emulator
            startCpuEmulator.addActionListener(e->{
                try {
                    Runtime.getRuntime().exec("cmd /c start /B CPUEmulator.bat");
                } catch (IOException ignored) {
                }
            });

            // Compare Button
            JButton compareButton = new JButton("Compare");
            compareButton.setVisible(false);
            // Copies the translated code to Left Text View
            // Asks the user to input the code to be converted into Right Text View
            compareCodes.addActionListener(e->{
                assemblyText.setText(assembledText.getText());
                assembledText.setEditable(true);
                assembledText.setText("Enter text/hack binary code to compare");
                compareButton.setVisible(true);
            });

            // When button is pressed execute this function
            compareButton.addActionListener(e-> compare(compareButton));
            compareButton.setForeground(Color.black);
            compareButton.setBackground(Color.decode("#455364"));
            compareButton.getModel().addChangeListener(e -> hoverAnimation(compareButton, e));

            // Panel to hold buttons
            buttonPanel = new JPanel();
            buttonPanel.add(open);
            buttonPanel.add(translateButton);
            buttonPanel.add(moreOptions);
            buttonPanel.add(compareButton);

            // Add the components to frame
            f.getContentPane().setBackground(Color.decode("#19232d"));
            f.add(gui,BorderLayout.CENTER);
            f.add(gui1, BorderLayout.EAST);
            f.add(buttonPanel, BorderLayout.PAGE_END);
            f.pack();
            f.setSize(600,480);
            f.setLocationByPlatform(true);
            f.setVisible(true);
        });
    }
    // Driver code to run the Assembler GUI
    public static void main(String[] args) {
        new AssemblerView();
    }

    /* Hover animation code
     Takes JButton and Change Event as input */
    public void hoverAnimation(JButton button, ChangeEvent te){
        ButtonModel model1 = (ButtonModel) te.getSource();
       // If cursor is on button, then change the text color to white
        if (model1.isRollover()) {
            button.setForeground(Color.white);
            // Else set the color to black (default)
        } else {
            button.setForeground(Color.black);
        }
    }

    // To read files
    private void readFiles(File path) {
        BufferedReader buff = null;
        try {
            assembledText.setText("");
            buff = new BufferedReader(new FileReader(path));
            String str;
            while ((str = buff.readLine()) != null) {
                assembledText.append(str + "\n");
            }

        } catch (IOException ignored) {
        } finally {
            try {
                // Close the streams
                in.close();
                if (buff != null) {
                    buff.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    /* To show formatted hack assembly code.
    Code with no whitespaces or comments */
    public void showF() {
        File path = FormatText.fm;
        readFiles(path);
    }

    /* To show Symbol-less hack assembly code.
    Assembly codes with no symbols, whitespaces and comments */
    public void showSL() {
        File path = Symbols.fl;
        readFiles(path);
    }

    // To save the translated code to custom location
    public void sFas() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
       // Add file filter to JFileChooser
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Hack Binary Code", "hack");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            try{
                // Write the output in Right Text View to custom file
                String store = assembledText.getText();
                PrintStream fileOut = new PrintStream(chooser.getSelectedFile().getAbsolutePath());
                System.setOut(fileOut);
                System.out.println(store);
                fileOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to open file (Get location of the asm file)
    public void openFile(){
        int result = fileChooser.showOpenDialog(f);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                assembledText.setText(null);
                // Display the file user chose in JEditorPane
                assemblyText.setPage(file.toURI().toURL());
                fileLocation = file;
                // Read the output and display it in JTextArea
                // Enclosed in try catch as it can throw exception when the file is not found
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void translate(){
        // Buffered read to read
        assembledText.setText("");
        BufferedReader buff = null;
        // Create tempFile to enable editing
        if(fileLocation != null){
            path = String.valueOf(fileLocation);
        } else { path = System.getProperty("user.dir") + "\\Assembled.hack";
        }

        try {
            tempFile = File.createTempFile(".tempAsm", ".asm");
            tempFile.deleteOnExit();
            PrintStream fileOut = new PrintStream(tempFile);
            System.setOut(fileOut);
            System.out.println(assemblyText.getText());
            fileOut.close();
        } catch (IOException ignored) {
        }

        // Send the tempFile to formatText method
        try {
            FormatText.formatText(tempFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Read the result
        try {
            buff = new BufferedReader(new FileReader(path));
            String str;
            while ((str = buff.readLine()) != null) {
                assembledText.append(str+"\n");
            }

        } catch (IOException ignored) {
        } finally {
            try { in.close();
                assert buff != null;
                buff.close(); } catch (Exception ignored) { }
        }
    }

    // To compare the texts in both TextView
    public void compare(JButton compareButton){
        Scanner scanner = new Scanner(assemblyText.getText());
        Scanner scanner1 = new Scanner(assembledText.getText());
        int lineNumber = 1;
        while (scanner.hasNextLine() && scanner1.hasNextLine()) {
            if(scanner.nextLine().equals(scanner1.nextLine())){
                lineNumber++;
            } else{
                JOptionPane.showMessageDialog(f,
                        "Comparison error at line number: " + lineNumber,
                        "Comparison Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        scanner.close();
        scanner1.close();
        compareButton.setVisible(false);

        JOptionPane.showMessageDialog(f,
                "Comparison completed successfully");
    }
}
