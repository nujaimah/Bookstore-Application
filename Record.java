package book.store.application;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Record {
    // Name of the associated file
    private String filename;
    private static Record instance = null;
    
    public static Record getInstance(String n){
        instance = new Record(n);
        return instance;
    }

    private Record(String n) {
        filename = n;
        try {
        File f = new File(n + ".txt");
        if (f.createNewFile()) {
        }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Effects: Reads and prints the contents of the associated
    // file to the standard output.
    public void read() {
           Scanner s = null;
          try {
              s = new Scanner(new File(filename + ".txt"));
              while(s.hasNext()){
                  String a = s.next();
                  System.out.printf("%s\n", a);
              }
          } catch (IOException e) {
              System.out.println("An error occurred.");
              e.printStackTrace();
          }

       }
      // Effects: Appends the specified message, msg, to the
      // associated file.
    public void write(String msg) {

        try {
         FileWriter fw = new FileWriter(filename + ".txt", true);
         fw.write(msg);
         fw.close();
       } catch (IOException e) {
         System.out.println("An error occurred.");
         e.printStackTrace();
       }
    }
    
    public void deleteFile() {
        File f = new File(filename + ".txt");
        f.delete();
        instance = null;
    }
    
    public static void main(String[] args) {
       // Fill the blank below that obtains the sole instance
       // of the Record class.
       // (You should not invoke the Record constructor here.)
       Record r = getInstance("books");
       // Do not modify the code below
       r.write("Hello-1\n");
       r.write("Hello-2\n");

       System.out.println("Currently the file record.txt " +
       "contains the following lines:");
       r.read();
    }
}