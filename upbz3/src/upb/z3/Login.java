//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha2: Upravte funkciu na prihlasovanie tak, aby porovnavala        //
//         heslo ulozene v databaze s heslom od uzivatela po            //
//         potrebnych upravach.                                         //
// Uloha3: Vlozte do prihlasovania nejaku formu oneskorenia.            //
//////////////////////////////////////////////////////////////////////////
package upb.z3;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import upb.z3.Database.MyResult;

public class Login {
    protected static MyResult prihlasovanie(String meno, String heslo) throws IOException, Exception{
        /*
        *   Delay je vhodne vytvorit este pred kontolou prihlasovacieho mena.
        */
        MyResult account = Database.find("hesla.txt", meno);
        if (!account.getFirst()){
            return new MyResult(false, "Nespravne meno.");
        }
        else {
            //Map<String,Integer> att= HashMap<>();
            // TODO chech map if there is login
            StringTokenizer st = new StringTokenizer(account.getSecond(), ":");
            st.nextToken();      //prvy token je prihlasovacie meno
            /*
            *   Pred porovanim hesiel je nutne k heslu zadanemu od uzivatela pridat prislusny salt z databazy a nasledne tento retazec zahashovat.
            */
            String password=st.nextToken();
            String salt=st.nextToken();
            String hashPassword=getHashPassword(heslo,salt);
           
            boolean rightPassword = hashPassword.equals(password);
            if (!rightPassword)  {  
                //TODO : if add name to map, on second bad login increas attempnt and 
                
                return new MyResult(false, "Nespravne heslo.");
                
            }
        }
        return new MyResult(true, "Uspesne prihlasenie.");
    }
    public static String sha256(String base) {
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
}
