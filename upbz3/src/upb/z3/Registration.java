//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Do suboru s heslami ulozit aj sal.                           //
// Uloha2: Pouzit vytvorenu funkciu na hashovanie a ulozit heslo        //
//         v zahashovanom tvare.                                        //
//////////////////////////////////////////////////////////////////////////
package upb.z3;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import upb.z3.Database.MyResult;




public class Registration {
    protected static MyResult registracia(String meno, String heslo) throws NoSuchAlgorithmException, Exception{
        if (Database.exist("hesla.txt", meno)){
            System.out.println("Meno je uz zabrate.");
            return new MyResult(false, "Meno je uz zabrate.");
        }
        else {
            /*
            *   Salt sa obvykle uklada ako tretia polozka v tvare [meno]:[heslo]:[salt].
            */
            String salt = Security.getNextSalt();
            String hashPassword=Security.getHashPassword(heslo, salt);
            String check= Security.checkPassword(heslo);
            if(check.equals("valid")){
            Database.add("hesla.txt", meno + ":" + hashPassword + ":" + salt);
            }
            else{
              return new MyResult(false, check);  
            }
        }
        return new MyResult(true, "");
    }
    
    
}
