/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package findmeajob.scrappers;

import findmeajob.supportTools.SupportTools;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ComputrabajoScrapper extends Scrapper{
    
    private final SupportTools supportTools = new SupportTools();
    
    public ComputrabajoScrapper(String _job, String _city) {
        
        super("http://www.computrabajo.com.co",_job,_city,"http://www.computrabajo.com.co");
        formatUrl();
        
        this.offerUrlToken = "a.js-o-link";
        this.offerDataToken = "p.fw_b.fs15.mt10 + p";
        this.offerDataTypeToken = "p.fw_b.fs15.mt10";
    }       
    
    private void formatUrl(){
        String jobParameter = supportTools.replace(this.job,' ',"-");
        String cityParameter = supportTools.replace(this.city,' ',"-");
        setUrl(String.format("http://www.computrabajo.com.co/trabajo-de-%s-en-%s",jobParameter,cityParameter));
    }
    
    public int getNumberOfPages(){
        Elements numberOfPagesElement = getScrap().select("html > body > div#MainContainer > div.cm-12.breadinfo.breadinfo_m.mt10 > div.pg_grid > span");
        if(numberOfPagesElement != null ){
            try {
                int numberOfPages = (int) Math.ceil(Float.parseFloat(numberOfPagesElement.text().split(" ")[3]) / 20);   
                return numberOfPages;
            } catch (Exception e) {
                System.err.println("[ERROR] "+e);
            }
        }else{
         System.err.println("[ERROR] No hay ofertas para su busqueda.");
        }
        return 0;
    }
    
    public ArrayList<List> scrappingAll(){
        int numberOfPages = getNumberOfPages();
        ArrayList<List> offersData = new ArrayList<>();
        
        // For para recorrer todas las páginas de ofertas.
        
        for (int i = 1; i <= numberOfPages; i++) {
            if(i >= 2){
                String jobParameter = supportTools.replace(this.job,' ',"-");
                String cityParameter = supportTools.replace(this.city,' ',"-");
                setUrl(String.format("http://www.computrabajo.com.co/trabajo-de-%s-en-%s&?p=%s", jobParameter,cityParameter,i));
            }
            List<String> offerURLs = scrappingURL();
            List<HashMap> currentPageOffersData = new ArrayList<>();
            
            // For para recorrer todas las URL de ofertas de la página actual y recuperar su información.
            
            for (int url = 0; url < offerURLs.size(); url++) {
                HashMap<String,String> offerData = scrappingOfferData(this.domain + offerURLs.get(url));
                offerData.put("URL",this.domain + offerURLs.get(url));
                currentPageOffersData.add(offerData);
            }
            
            offersData.add(currentPageOffersData);
        }
        return offersData;
    }
    

    private String getUrl(){
        return this.url;
    }
    
    private void setUrl(String newUrl){
        this.url = newUrl;  
    }
}
