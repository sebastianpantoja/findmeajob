/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package findmeajob.scrappers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {
    
    String url;   // Página de ofertas en la que se encuentra actualmente el Scrapper
    String job;   // El trabajo del cual se buscarán ofertas
    String city;        // Ciudad en donde se buscarán ofertas
    String domain;      // Dominio del sitio web sobre el cual se está realizando WebScrapping
    String offerUrlToken;           // Expresión clave para identificar las URL dentro del HTML de las páginas de ofertas
    String offerDataToken;          // Expresión clave para identificar los datos de las ofertas de trabajo dentro del HTML de la página de la oferta
    String offerDataTypeToken;      // Expresión clave para identificar el tipo de los datos de las ofertas de trabajo dentro del HTML de la página de la oferta
    
    public Scrapper(String _url, String _job, String _city, String _domain){
        this.url = _url;
        this.job = _job;
        this.city = _city;
        this.domain = _domain;
    }
    
    // Método para obtener el HTML de las páginas de ofertas
    
    public Document getScrap(){
        Document scrap = null;
        try {
           scrap = Jsoup.connect(this.url).get();
        } catch (Exception e) {
            System.out.println("[ERROR] "+e);
        }
        return scrap;
    }
    
    // Método para obtener el HTML de las ofertas de trabajo.
    
    public Document getScrap(String pageUrl){
        Document scrap = null;
        try {
           scrap = Jsoup.connect(pageUrl).get();
        } catch (Exception e) {
            System.out.println("[ERROR] "+e);
        }
        return scrap;
    }
    
     // Método para obtener las URL de las paginas de cada oferta de trabajo.
    
    public List<String> scrappingURL(){    
        List<String> offerURLs = new ArrayList<>();
        
        Elements urlElements = getScrap().select(this.offerUrlToken);
        for (Element urlElement : urlElements) {
            offerURLs.add(urlElement.attr("href"));
        }
        
        return offerURLs;
    }
    
    // Método para obtener la información relevante de cada oferta de trabajo.
    
    public HashMap<String,String> scrappingOfferData(String offerPageURL){
        HashMap<String,String> offerData = new HashMap<String,String>();
        try {
            Elements dataTypeElements = getScrap(offerPageURL).select(this.offerDataTypeToken);
            Elements dataElements = getScrap(offerPageURL).select(this.offerDataToken);
            
            for (int i = 0; i < dataElements.size(); i++) {
                offerData.put(dataTypeElements.get(i).text(), dataElements.get(i).text());
            }
            
        } catch (Exception e) {
            System.out.println("[ERROR] "+e);
        }
        return offerData;
    }
}
