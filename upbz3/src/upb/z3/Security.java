//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Vytvorit funkciu na bezpecne generovanie saltu.              //
// Uloha2: Vytvorit funkciu na hashovanie.                              //
// Je vhodne vytvorit aj dalsie pomocne funkcie napr. na porovnavanie   //
// hesla ulozeneho v databaze so zadanym heslom.                        //
//////////////////////////////////////////////////////////////////////////
package upb.z3;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.DictionarySubstringRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordCli;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;



public class Security {
    
   private static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
           throw new RuntimeException(ex);
        }
    }
    
     public static String getHashPassword(String pass, String salt){
    
        String hashPassword=null;
            for(int i=0;i<3;i++){
                if(i==0){
                 hashPassword = sha256(pass + salt);
                }
                else{
                    hashPassword = sha256(hashPassword);
                }
            }
            return hashPassword;
    }
      protected static String getNextSalt() {
        Random RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.encode(salt);
  }
      
  public static String checkPassword(String pass) {
    
       try {
           ArrayWordList awl;
           awl = WordLists.createFromReader(
                   new FileReader[] {new FileReader("dic.txt")},true,
                   new ArraysSort());
           
           WordListDictionary dict = new WordListDictionary(awl);
           
           DictionarySubstringRule dictRule = new DictionarySubstringRule(dict);
           dictRule.setMatchBackwards(true); // match dictionary words backwards
      
           List<Rule> ruleList = new ArrayList<>();
           ruleList.add(dictRule);
           ruleList.add( new LengthRule(8, 16));
           ruleList.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
           ruleList.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
           ruleList.add(new CharacterRule(EnglishCharacterData.Digit, 1));
           ruleList.add(new WhitespaceRule());

            PasswordValidator validator = new PasswordValidator(ruleList);

            PasswordData passwordData = new PasswordData(pass);
            System.out.println(pass);
            RuleResult result = validator.validate(passwordData);

            if (result.isValid()) {

                return "valid";

            } else {

                StringBuilder sb = new StringBuilder();
              for (String msg : validator.getMessages(result)) {

                sb.append(msg);
                sb.append("\n");

              }
              return "Heslo neslpna pravidla"+"\n" + sb.toString();
            }
                   
           
       } catch (IOException ex) {
          return "Nepodarilo sa načítať slovník";
       }

  }
}

