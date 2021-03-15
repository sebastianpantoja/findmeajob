/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package findmeajob.supportTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SupportTools {    
    
    public String replace(String string,char originalChar,String replacementString){
        String modifiedString = "";
        for (int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == originalChar){
               modifiedString += replacementString; 
            }else{
               modifiedString += string.charAt(i);
            }
        }
        return modifiedString;
    }
    
    public void printTable(ArrayList<List> table){
        for (int page = 0; page < table.size(); page++) {
            for (int offer = 0; offer < table.get(page).size(); offer++) {
                System.out.println(table.get(page).get(offer));
            }
        }
    }
}
