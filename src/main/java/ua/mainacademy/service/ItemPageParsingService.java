package ua.mainacademy.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.mainacademy.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemPageParsingService {

    public static Item getItemFromPage(String url) {

        Document document = DocumentExtractorService.getDocument(url);
        Element productBlock = document.getElementById("dp-container");


        String name = extractName(productBlock);
        String code = extractCode(url);
        int price = extractPrice(productBlock);
        int initPrice = extractInitPrice(productBlock) == 0 ? price : extractInitPrice(productBlock);
        String imageUrl = extractImageUrl(productBlock);
        String group = extractGroup(productBlock);
        String seller = extractSeller(productBlock);

        return Item.builder()
                .code(code)
                .name(name)
                .price(price)
                .initPrice(initPrice)
                .imageUrl(imageUrl)
                .url(url)
                .group(group)
                .seller(seller)
                .build();

    }

    private static String extractSeller(Element element) {
        Element sellerElement = element.getElementById("tabular-buybox-truncate-1").getElementsByClass("tabular-buybox-text").first();
        return sellerElement.text();

    }

    private static String extractGroup(Element document) {
        String result ="";
        Element groupDiv=document.getElementById("wayfinding-breadcrumbs_feature_div");
        Elements groupElements=groupDiv.getElementsByTag("a");
        List<String>groups=new ArrayList<>();
        for (Element element :groupElements ){
        groups.add(element.text());
        }
        return StringUtils.join(groups, ">");
    }

    private static String extractImageUrl(Element element) {
        Element imageElement = element.getElementById("landingImage");
        String imageLinks = imageElement.attr("data-a-dynamic-image");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map result = objectMapper.readValue(imageLinks, Map.class);
            return (String) result.keySet().iterator().next();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return StringUtils.substringBefore(StringUtils.substringAfter(imageLinks, "{\""), "\":");

    }

    private static int extractInitPrice(Element element) {
        List<Element> elementList = element.getElementsByAttributeValueStarting("class", "a-size-base a-color-secondary");
        if (elementList.isEmpty()) {
            return 0;
        }
        return Integer.valueOf(elementList.get(0).text().replaceAll("\\D", ""));
    }

//     using jsoup from mvn repository for all code

    private static int extractPrice(Element element) {
        String priseRow = element.getElementById("priceblock_ourprice").text();
        return Integer.valueOf(priseRow.replaceAll("\\D", ""));
    }
//using string utils (apache commons lens) from mvn repository

    private static String extractCode(String url) {
        return StringUtils.substringAfterLast(url, "/");

    }

    private static String extractName(Element element) {
        return element.getElementById("title").text();
    }
}
