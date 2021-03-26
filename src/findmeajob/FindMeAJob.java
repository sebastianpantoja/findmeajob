/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package findmeajob;

import findmeajob.scrappers.ComputrabajoScrapper;
import findmeajob.scrappers.Scrapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import findmeajob.supportTools.SupportTools;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FindMeAJob {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        
        SupportTools supportTools = new SupportTools();
        ComputrabajoScrapper cpScrapper = new ComputrabajoScrapper("Panadero", "bogota dc");
        
        ArrayList<List> offersData = cpScrapper.scrappingAll();
        System.out.println(offersData.size());
        supportTools.printTable(offersData);
        
        
    }
}
