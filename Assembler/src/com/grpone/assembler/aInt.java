package com.grpone.assembler;

public class aInt extends DecimalToBinary{
    public static String aInstruction(String aInt){
        String asm1 = aInt.substring(1);
        // Take Substring from index [1], to remove '@'
        int asm2 = Integer.parseInt(asm1);
        // Parse the integer from string
        String value;
            value = 0 + decimalToBinary(asm2) + "\n";
            // Converts the value to binary and adds 0 at the start (index[0] as it's an A instruction.
        return value;
    }
}