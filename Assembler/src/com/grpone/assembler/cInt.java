package com.grpone.assembler;

public class cInt {

    public static String cInstruction(String cInt){


        int equals = cInt.indexOf('=');
        int semiColon = cInt.indexOf(';');

        //...Initialize the Strings...//
        String dest = "";
        String comp = "";
        String jump;

        //...Used to find if there is a destination or Jump..//
        boolean noDest = false;
        boolean noJump = false;

        try{
            // Wrapped in try block as, if there is no '=' symbol in instruction it will throw an exception.
            dest = cInt.substring(0, equals);} catch (Exception e) {
            // If there is no '=' in instruction, there is no destination mentioned.
            noDest = true;
        }

        try{
            // comp takes substring of cInt from '=' to ';'
            // Similar to previous try block. It's required if there is no Jump or Destination specified substring
            // method will throw an exception.
            comp = cInt.substring(equals+1,semiColon);} catch (Exception ignored) {
        }

        try{
            // If there is no destination Comp instruction is from starting to ;
            // Wrapped in try block as there can be an absence of ; (JUMP)
            if(noDest){
                comp = cInt.substring(0,semiColon);
            }} catch (Exception e) {
            // Set noJump to be true if an exception is thrown.
            noJump = true;
        }

        // If there is no Jump specified nor there is a semicolon take comp as the string from '=' to end of cInt.
        if(!cInt.contains(";")){
            comp = cInt.substring(equals+1);
        }

        // This is a fail-safe
        if(noJump){
            comp = cInt;
        }

        // Take Jump as substring of cInt from ';'
        jump = cInt.substring(semiColon+1);

        // Return complete binary string
        return "111" +  compCheck(comp) + destCheck(dest) + jumpCheck(jump) + "\n";
    }

    private static String jumpCheck(String a){
        // Method to calculate binary code of JUMP instruction
        // Switch statement which returns a value if a match is found.
        switch(a){
            case "JGT":
                return "001";

            case "JEQ":
                return "010";

            case "JGE":
                return "011";

            case "JLT":
                return "100";

            case "JNE":
                return "101";

            case "JLE":
                return "110";

            case "JMP":
                return "111";

            default:
                return "000";
        }
    }

    private static String destCheck(String a){
        // Similar to jumpCheck but for destination instruction.
        switch (a){
            case"M":
                return "001";

            case "D":
                return "010";

            case "MD":
                return "011";

            case "A":
                return "100";

            case "AM":
                return "101";

            case "AD":
                return "110";

            case "AMD":
                return "111";

            default:
                return "000";
        }
    }

    public static String compCheck(String a){
        // Similar to the methods above, but for Comp instruction.
        switch (a){
            case "0":
                return "0101010";

            case "1":
                return "0111111";

            case "-1":
                return "0111010";

            case "D":
                return "0001100";

            case "A":
                return "0110000";

            case "!D":
                return "0001101";

            case "!A":
                return "0110001";

            case "-D":
                return "0001111";

            case "-A":
                return "0110011";

            case "D+1":
                return "0011111";

            case "A+1":
                return "0110111";

            case "D-1":
                return "0001110";

            case "A-1":
                return "0110010";

            case "D+A":
                return "0000010";

            case "D-A":
                return "0010011";

            case "A-D":
                return "0000111";

            case "D&A":
                return "0000000";

            case "D|A":
                return "0010101";

            case "M":
                return "1110000";

            case "!M":
                return "1110001";

            case "-M":
                return "1110011";

            case "M+1":
                return "1110111";

            case "M-1":
                return "1110010";

            case "D+M":
                return "1000010";

            case "D-M":
                return "1010011";

            case "M-D":
                return "1000111";

            case "D&M":
                return "1000000";

            case "D|M":
                return "1010101";

            default:
                return null;

        }
    }

}
