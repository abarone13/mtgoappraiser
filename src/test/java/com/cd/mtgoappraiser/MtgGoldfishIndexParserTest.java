package com.cd.mtgoappraiser;

import com.cd.mtgoappraiser.model.MarketCard;
import com.cd.mtgoappraiser.http.mtggoldfish.MtgGoldfishIndexParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Cory on 8/10/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class MtgGoldfishIndexParserTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        @Scope("prototype")
        public MtgGoldfishIndexParser mtgGoldfishIndexParser() {
            MtgGoldfishIndexParser mtgGoldfishIndexParser = new MtgGoldfishIndexParser();

            return mtgGoldfishIndexParser;
        }
    }
    
    @Autowired
    private MtgGoldfishIndexParser mtgoGoldfishIndexParser;

    @Test
    public void testParseIndexes() throws Exception {
        InputStream theIndexes = loadResource("indexes.html");

        Document theHtml = Jsoup.parse(theIndexes, "UTF-8", "");

        List<String> indexUrls = mtgoGoldfishIndexParser.getIndexUrls(theHtml);

        assertTrue(indexUrls != null);
        assertTrue(indexUrls.size() > 100);
    }

    @Test
    public void testParsePrices() throws Exception {
        InputStream theCards = loadResource("standardsoi.html");

        Document theHtml = Jsoup.parse(theCards, "UTF-8", "");

        List<MarketCard> cards = mtgoGoldfishIndexParser.getCardsFromPage(theHtml);

        assertTrue(cards != null);
        assertTrue(cards.size() == 404);

        cards.stream().forEach(card -> {assertTrue(card.getName() != null);
                                        assertTrue(card.getRetailPrice() != null);});
    }

    private InputStream loadResource(String resourceName) throws IOException {
        return this.getClass().getResourceAsStream(resourceName);
    }
}
