/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package upb.z3;

import java.util.Date;

/**
 *
 * @author Michal
 */
public class WaitContants implements java.io.Serializable {
    Date toWait;
    int sec;

    public Date getToWait() {
        return toWait;
    }

    public void setToWait(Date toWait) {
        this.toWait = toWait;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }
    
    
    public WaitContants(Date toWait,int sec){
        this.toWait=toWait;
        this.sec=sec;
    }
    
}
