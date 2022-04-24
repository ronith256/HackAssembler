package com.grpone.assembler;

import java.io.*;
import java.util.Hashtable;

public class Symbols {
    public static File fl = null;
    public static void readSymbols(File formatted){
        String result = null;
        // Hashtable to store symbols and their translated non-symbolic value.
        Hashtable<String, Integer> symbolTable = new Hashtable<>();
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        // For Virtual Registers
        for(int x = 0; x < 16; x++){
            String virtualRegisters = "R"+x;
            symbolTable.put(virtualRegisters, x);
        }

        // To read and give value to Labels/Symbols
        try(BufferedReader br = new BufferedReader(new FileReader(formatted))) {
            String line = br.readLine();
            // Count lines
            int lineCounter = 0;
            // Unless the complete file is read, loop.
            while (line != null) {

                /* If the line contain '(' put the line number as value and the label as Key to the Hash Table
                Example
                If (LOOP) was specified and if it was on line 12,
                The Hash Table will be populated with LOOP as key and 12 as its value */

                if(line.contains("(")){
                    symbolTable.put(line.substring(1, line.indexOf(")")), lineCounter);
                }

                /* Only increment the line counter if the line does not contain label declaration.
                 Label declaration is not counted towards line number. */

                if(!line.contains("(")){
                    lineCounter++;
                }
                // Read the next line and continue with loop
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // To read and give value to variables
        try(BufferedReader br = new BufferedReader(new FileReader(formatted))) {
            String line = br.readLine();
            int variableCounter = 16;

            // Loop until the complete file is read
            while (line != null) {
                /* If the line contains @ and some alphabet.
                 Populate the hashtable with the name of variable as key and its value
                 Variable number starts from 16 */
                if(line.contains("@") && line.matches(".*[a-zA-Z]+.*")){
                    /* If the variable is not already in HashTable populate it with variable name and variableCounter as
                     its value */
                    if(!symbolTable.containsKey(line.substring(1))){
                        symbolTable.put(line.substring(1), variableCounter);
                        variableCounter++;
                    }
                }
                // Read next line and continue loop
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // To read and correctly translate
        try(BufferedReader br = new BufferedReader(new FileReader(formatted))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            int lineCounter = 0;
            while (line != null) {
                if(line.contains("(")){
                    symbolTable.put(line.substring(1, line.indexOf(")")), lineCounter);
                }
                /* If the read line contains an A instruction check if the 'value' part is declared in HashTable
                 If it is then replace the value part with the value in HashTable */
                if(line.contains("@") && symbolTable.containsKey(line.substring(1))){
                    line = "@" + symbolTable.get(line.substring(1));
                }
                /* (something) label declarations are not required in the final result
                 So append only if the processed String is not a label declaration */

                if(!line.contains("(")){
                sb.append(line);
                sb.append(System.lineSeparator());
                lineCounter++;
                }
                // Read next line
                line = br.readLine();
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the result to Symbol.txt
        File tempFile = null;
        try {
            tempFile = File.createTempFile("Symbol", ".txt");
            PrintStream fileOut = new PrintStream(tempFile);
            System.setOut(fileOut);
            System.out.println(result);
            fl = tempFile;
            fileOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Try to delete Formatted.txt as its no longer required.
        formatted.deleteOnExit();
        Assembler.assemble(tempFile);
    }
}
