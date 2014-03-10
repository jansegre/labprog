package com.jansegre.labprog3.validator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class Validator {

    static private String charset = "ISO-8859-1";
    static private Pattern invalidPattern = Pattern.compile(".*\\[Invalid\\].*");

    static private String _encode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, charset);
    }

    static public String validatingQueryUrl(String url) throws UnsupportedEncodingException {
        return "http://validator.w3.org/check?" +
                "uri=" + _encode(url) + "&" +
                "charset=" + _encode("(detect automatically)") + "&" +
                "doctype=" + _encode("Inline") + "&" +
                "group=0";
    }

    static public Document getValidation(String url) throws IOException {
        return Jsoup.connect(validatingQueryUrl(url)).get();
    }

    static public boolean isValid(Document validation) throws IOException {
        return !invalidPattern.matcher(validation.title()).matches();
    }

    static String[] errors(Document validation) {
        Elements errorElements = validation.select(".msg_err .msg");
        String[] errorStrings = new String[errorElements.size()];
        for (int i = 0; i < errorStrings.length; i++)
            errorStrings[i] = Parser.unescapeEntities(errorElements.get(i).html(), false);
        return errorStrings;
    }

    static public void main(String[] args) {
        String url = args.length > 0 ? args[0] : "http://google.com/";
        System.out.println("Validating " + url + " ...");
        try {
            Document doc = getValidation(url);
            boolean valid = isValid(doc);
            System.out.println("The document is" + (valid ? " " : " NOT ") + "VALID.");
            if (!valid) {
                for (String error: errors(doc))
                    System.out.println("- " + error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
