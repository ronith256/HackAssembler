package com.grpone.assembler;

import java.io.*;

public class FormatText {
    public static File fm = null;
    public static void formatText(File fileLocation) throws IOException {

        String result;
        // Read the file specified by the user.
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            // String builder is used to build string of instructions from file
            StringBuilder sb = new StringBuilder();
            // Read the first line from BufferedReader stream.
            String line = br.readLine();

            // Terminate once the file is read completely
            while (line != null) {

                // Enclosed in Try-Catch as if / is not present the program will halt.
                try{
                    // Take substring of the instruction to remove comments
                line = line.substring(0,line.indexOf("/"));
                }catch (Exception ignored){}

                // Enclosed in try catch as whitespaces may not be present always
                try{
                    // Using Regex to remove whitespaces
                    line = line.replaceAll("\\s+","");
                }catch (Exception ignored){}

                // If the line is not empty, add the processed line to string using StringBuilder
                if(!line.isEmpty()){
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
                // Read the next line
                line = br.readLine();
            }
            // Once the while loop is completed. Convert StringBuilder to String and then save it to result
            result = sb.toString();
        }
        // Save the result to Formatted.txt
        File tempFile = null;
        try {
            tempFile = File.createTempFile("Formatted",".tmp");
            tempFile.deleteOnExit();
            PrintStream fileOut = new PrintStream(tempFile);
            System.setOut(fileOut);
            System.out.println(result);
            fm = tempFile;
            fileOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        // Execute the next-part.
        Symbols.readSymbols(tempFile);
    }
}
