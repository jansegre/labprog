package com.jansegre.labprog3.validator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

    static public boolean isValid(String url) throws IOException {
        Document doc = Jsoup.connect(validatingQueryUrl(url)).get();
        return !invalidPattern.matcher(doc.title()).matches();
    }

    static public void main(String[] args) {
        String url = args.length > 0 ? args[0] : "http://google.com/";
        System.out.println("validating " + url);
        try {
            boolean valid = isValid(url);
            System.out.println("The document is" + (valid ? " " : " NOT ") + "VALID.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
