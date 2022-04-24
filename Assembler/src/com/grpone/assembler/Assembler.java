package com.grpone.assembler;

import java.io.*;

public class Assembler {
    public static void assemble(File Symbol){
        String result = null;
        // Read the processed file
        try(BufferedReader br = new BufferedReader(new FileReader(Symbol))) {
            StringBuilder sb = new StringBuilder();
            String assemblyInstruction = br.readLine();
            // Read the file completely
            while (assemblyInstruction != null) {
                // If the String contains "@" it is an A instruction
                // Send the String to aInt to process and return the binary instruction
                String intermediate;
                if(assemblyInstruction.contains("@")){
                    // If the input contains @ its considered as an A instruction if not as a C instruction.
                    intermediate = aInt.aInstruction(assemblyInstruction);
                    // Only write to StringBuilder if the returned value is not null
                }
                // If the string doesn't contain "@" it is a C instruction
                // Send the String to cInt to process and return the binary instruction
                else {
                    intermediate = cInt.cInstruction(assemblyInstruction);
                    // Only write to StringBuilder if the returned value is not null
                }
                if(!intermediate.contains("null")){
                    sb.append(intermediate);}
                assemblyInstruction = br.readLine();
            }
            // Put the result to result.
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the result to fileName.hack
        File op;
        try {
            if(AssemblerView.path.contains(".asm")){
                AssemblerView.path = AssemblerView.path.replace(".asm", ".hack");
            }
            op = new File(AssemblerView.path);
            PrintStream fileOut = new PrintStream(op);
            System.setOut(fileOut);
            System.out.println(result);
            fileOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Try to delete Symbol.txt as it's no longer required
        Symbol.deleteOnExit();
    }
}
