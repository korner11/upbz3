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
import java.util.Calendar;
import java.util.Date;
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
                Calendar cal = Calendar.getInstance();
                Date currentTime = cal.getTime();
                // TODO chech map if there is login
                StringTokenizer st = new StringTokenizer(account.getSecond(), ":");
                st.nextToken();      //prvy token je prihlasovacie meno
                /*
                *   Pred porovanim hesiel je nutne k heslu zadanemu od uzivatela pridat prislusny salt z databazy a nasledne tento retazec zahashovat.
                */
                String password=st.nextToken();
                String salt=st.nextToken();
                String hashPassword=Security.getHashPassword(heslo,salt);

                boolean rightPassword = hashPassword.equals(password);
                //if map contains username then check if its good login if not add wait time, else chceck if its good login if not add to map
                if(Database.getAtt().containsKey(meno)){
                        //if user wiated setted time then ceck password if not add wait time
                        MyResult usrWait= userWait(meno,currentTime,rightPassword);
                        //write map to serializable file 
                        Database.writeMap("attends.txt");
                        return usrWait;
                        
                }
                else{
                    if (!rightPassword)  {      
                        Database.getAtt().put(meno,new WaitContants(currentTime,1));
                        Database.writeMap("attends.txt");
                        return new MyResult(false, "Nespravne heslo."); 
                    }
                    else{
                        Database.writeMap("attends.txt");
                        return new MyResult(true, "Uspesne prihlasenie.");
                    }
                }
                
        }
    }
   
     public static Date addTimeBySecondsDemo(Date date,int sec){
    
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(date.getTime());
        calender.add(Calendar.SECOND, sec);
        Date changeDate=calender.getTime();
        return changeDate;            
}
     public static void addWaitTime(String meno, Date currentTime){
        
         // set new wait time in seconds
         Database.getAtt().get(meno).setSec(Database.getAtt().get(meno).getSec()+4);
        //get new time to wait current time + wait sec
        Date toWait=addTimeBySecondsDemo(currentTime,Database.getAtt().get(meno).getSec());
        //set new time to wait
        Database.getAtt().get(meno).setToWait(toWait);
     
     }
     
     public static MyResult userWait(String meno, Date currentTime,boolean rightPassword ){
         //check if  time to wait is waited then check password if its wrong add wait time, else add wait time   
         if(Database.getAtt().get(meno).getToWait().before(currentTime)){
                 if (!rightPassword)  { 
                      addWaitTime(meno,currentTime);
                      return new MyResult(false, "Nesprávne heslo. Možte sa prihlásiť až "+Database.getAtt().get(meno).getToWait().toString());
                 }
                 else{
                       Database.getAtt().remove(meno);
                       return new MyResult(true, "Uspesne prihlasenie.");    
                  }
            }
            else{
                 addWaitTime(meno,currentTime);
                 return new MyResult(false, "Nesprávne heslo. Možte sa prihlásiť až "+Database.getAtt().get(meno).getToWait().toString());
            }
     
     }
     
}
