/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upb.z3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Database {
    
    static Map<String,WaitContants> att;

    public static Map<String, WaitContants> getAtt() {
        return att;
    }

    public static void setAtt(Map<String, WaitContants> att) {
        Database.att = att;
    }
    
    
    final static class MyResult {
        private final boolean first;
        private final String second;
        
        public MyResult(boolean first, String second) {
            this.first = first;
            this.second = second;
        }
        public boolean getFirst() {
            return first;
        }
        public String getSecond() {
            return second;
        }
    }
    
    protected static MyResult add(String fileName, String text) throws IOException{ 
        if(exist(fileName, text))
            return new MyResult(false, "Meno uz existuje");
        FileWriter fw = new FileWriter(fileName, true);
        fw.write(text + System.lineSeparator());
        fw.close();    
        return new MyResult(true, "");
    }
    protected static void writeMap(String fileName) throws IOException{ 
        try{
         FileOutputStream fileOut = new FileOutputStream(fileName);   
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(att);
         out.close();
         fileOut.close();    
        }
        catch(IOException i) {
         i.printStackTrace();
      }
    }
    protected static void readMap(String fileName) throws IOException{ 
        att = null;
      try {
         FileInputStream fileIn = new FileInputStream(fileName);
         ObjectInputStream in = new ObjectInputStream(fileIn);
         att = (HashMap<String,WaitContants>) in.readObject();
         in.close();
         fileIn.close();
      }catch(FileNotFoundException e){
          File file = new File(fileName);
          file.createNewFile();
          att= new HashMap<>();
      }
      catch(IOException i) {
         att= new HashMap<>();
          //i.printStackTrace();
         //return;
      }catch(ClassNotFoundException c) {
         System.out.println("Map class not found");
         c.printStackTrace();
         return;
      }
    }
    
    protected static MyResult find(String fileName, String text) throws IOException{
        File frJm = new File(fileName);
        if (frJm.exists() == true){
            FileReader fr = new FileReader(frJm);
            BufferedReader bfr = new BufferedReader(fr);
            String riadok = "";
            String token = "";
            
            while ((riadok=bfr.readLine()) != null){
                StringTokenizer st = new StringTokenizer(riadok, ":");
                token = st.nextToken();
                if (token.equals(text)){
                    fr.close();
                    MyResult mr = new MyResult(true, riadok);
                    return mr;
                }
            }
            fr.close();   
            return new MyResult(false, "Meno sa nenaslo");              
        }
        return new MyResult(false, "Subor neexistuje");  
    }
    
    protected static boolean exist(String fileName, String text) throws IOException{
        StringTokenizer st = new StringTokenizer(text, ":");
        return find(fileName, st.nextToken()).getFirst();
    }
    
}
