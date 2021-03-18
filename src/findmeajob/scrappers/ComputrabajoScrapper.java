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
    private final String offerBusinessToken;
    
    public ComputrabajoScrapper(String _job, String _city) {
        
        super("http://www.computrabajo.com.co",_job,_city,"http://www.computrabajo.com.co");
        formatUrl();
        
        this.offerUrlToken = "a.js-o-link";
        this.offerDataToken = "p.fw_b.fs15.mt10 + p";
        this.offerDataTypeToken = "p.fw_b.fs15.mt10";
        this.offerBusinessToken = "a#urlverofertas";
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
                HashMap<String,String> normalizedOfferData = normalizeHashmap(offerData);
                currentPageOffersData.add(normalizedOfferData);
            }
            
            offersData.add(currentPageOffersData);
        }
        return offersData;
    }
    
    @Override
    public HashMap<String,String> scrappingOfferData(String offerPageURL){
        HashMap<String,String> offerData = new HashMap<String,String>();
        try {
            Elements dataTypeElements = getScrap(offerPageURL).select(this.offerDataTypeToken);
            Elements dataElements = getScrap(offerPageURL).select(this.offerDataToken);
            
            if(dataTypeElements.get(0).text().equals("Empresa")){
                for (int i = 0; i < dataElements.size(); i++) {
                    offerData.put(dataTypeElements.get(i+1).text(), dataElements.get(i).text());
                }
            }else{
                for (int i = 0; i < dataElements.size(); i++) {
                    offerData.put(dataTypeElements.get(i).text(), dataElements.get(i).text());
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] "+e);
        }
        return offerData;
    }
    
    public HashMap<String,String> normalizeHashmap(HashMap<String,String> originalHashmap){
        
        HashMap<String,String> normalizedHashmap = new HashMap<>();
        
        if(originalHashmap.get("Tipo de contrato") != null) {
            switch (originalHashmap.get("Tipo de contrato")){
                case "Contrato a término indefinido":
                    normalizedHashmap.put("Tipo de contrato","fijo");
                    break;
                case "Contrato de Obra o labor":
                    normalizedHashmap.put("Tipo de contrato","servicios");
                    break;
                default:
                    normalizedHashmap.put("Tipo de contrato","0");
                    break;
            }
        }else{
            normalizedHashmap.put("Tipo de contrato","0");
        }
        
        if(originalHashmap.get("Jornada") != null) {
            normalizedHashmap.put("Jornada",originalHashmap.get("Jornada"));
        }else{
            normalizedHashmap.put("Jornada","0");
        }
        
        if(originalHashmap.get("Salario") != null) {
            normalizedHashmap.put("Salario",originalHashmap.get("Salario"));
        }else{
            normalizedHashmap.put("Salario","0");
        }
        
        if(originalHashmap.get("URL") != null) {
            normalizedHashmap.put("URL",originalHashmap.get("URL"));
        }else{
            normalizedHashmap.put("URL","0");
        }
        
        return normalizedHashmap;
    }

    private String getUrl(){
        return this.url;
    }
    
    private void setUrl(String newUrl){
        this.url = newUrl;  
    }
}
