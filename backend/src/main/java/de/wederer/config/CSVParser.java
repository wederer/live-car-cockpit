package de.wederer.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVParser {

    private Scanner scanner;

    public CSVParser(String filePath, String delimiter) {
        try {
            this.scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.scanner.useDelimiter(delimiter);
    }

    public String read(){
        String returnString = "";
        this.scanner.nextLine();
        while(this.scanner.hasNext()){
            returnString += this.scanner.next()+"|";
        }
        this.scanner.close();
        return returnString;
    }
}
