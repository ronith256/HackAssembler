package com.grpone.assembler;

public class DecimalToBinary {
    // To convert decimal to binary
    public static String decimalToBinary (int n){
        int [] toBinary = new int[16];
        int b;
        // Divide the number by 2, take the remainder to an array.
        // Continue till the remainder is 0
        // Print the array in reverse order to get the bianry
        for(int i = 14; n > 0; i--) {
            toBinary[i] = n % 2;
            b = n / 2;
            n = b;
        }
        // Save the Binary to a String.
        String binary = null;
        for(int i = 0; i<15; i++){
            if(i==0){
                binary = String.valueOf(toBinary[i]);
            } else{
                binary = binary + String.valueOf(toBinary[i]);}
        }
        return binary;
    }
}