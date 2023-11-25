package com.example.stcProject.util;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TikaFileMetadataExtractor {

    public static Map<String, String> inferContentType(byte[] fileContent) {
        try {
            Parser parser = new AutoDetectParser();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);

            Metadata metadata = new Metadata();

            parser.parse(inputStream, new BodyContentHandler(), metadata, new ParseContext());


            Map<String, String> map = new HashMap<>();
            for (String name : metadata.names()) {
                map.put(name, metadata.get(name));
            }

            return map;
        } catch (IOException | org.apache.tika.exception.TikaException | org.xml.sax.SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

}
