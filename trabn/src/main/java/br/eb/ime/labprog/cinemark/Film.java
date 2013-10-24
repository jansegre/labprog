package br.eb.ime.labprog.cinemark;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

public class Film {
    // <film
    // id="4448"
    // genre="Drama"
    // parent-guide-rating="14 Anos"
    // n="1" media-3d="false"
    // media-35mm="true"
    // trailer="4448"
    // top="60"
    // first-print="false"
    // runtime="125"
    // screens="5"
    // showtimes="6"
    // distributor="Pandora">
    //   A Espuma dos Dias
    // </film>
    private int id;
    private String title;
    private String genre;

    public Film(int id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }


    private static final String baseUrl = "http://cinemark.com.br/";
    private static final String filmsUrl = baseUrl + "mobile/xml/films";
    private static final SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    // exemplo: http://cinemark.com.br/filmes/4655/photo2.jpg
    private static final String imageBaseUrl = baseUrl + "/filmes/";

    // 1 <= i <= 4
    public String getImageURL(int i) {
        return imageBaseUrl + id + "/photo" + i + ".jpg";
    }

    public String getImageUrl() {
        return getImageURL(1);
    }

    public static Collection<Film> getFilms() {
        final Vector<Film> films = new Vector<>();

        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(filmsUrl, new DefaultHandler() {
                private Film currentFilm;

                @Override
                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("film")) {
                        int id = Integer.parseInt(attributes.getValue("id"));
                        String genre = attributes.getValue("genre");
                        currentFilm = new Film(id, "", genre);
                        films.add(currentFilm);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    if (currentFilm != null)
                        currentFilm.setTitle(currentFilm.getTitle() + new String(ch, start, length));
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("film"))
                        currentFilm = null;
                }
            });
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return films;
    }
}
