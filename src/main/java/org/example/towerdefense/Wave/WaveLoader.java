package org.example.towerdefense.Wave;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WaveLoader {


    public static List<Wave> loadWaves(String difficulty) {

        List<Wave> waves = new ArrayList<>();

        try {

            InputStream is = WaveLoader.class.getResourceAsStream("/waves.xml");

            if (is == null) {
                System.out.println("waves.xml not found!");
            }
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList waveNodes = doc.getElementsByTagName("wave");

            for (int i = 0; i < waveNodes.getLength(); i++) {

                Element waveElement = (Element) waveNodes.item(i);

                String diff = waveElement
                        .getElementsByTagName("difficulty")
                        .item(0)
                        .getTextContent();

                if (!diff.equals(difficulty)) continue;

                int group = Integer.parseInt(
                        waveElement.getElementsByTagName("group").item(0).getTextContent());

                int waveID = Integer.parseInt(
                        waveElement.getElementsByTagName("waveID").item(0).getTextContent());

                int count = Integer.parseInt(
                        waveElement.getElementsByTagName("enemyCount").item(0).getTextContent());

                double interval = Double.parseDouble(
                        waveElement.getElementsByTagName("spawnInterval").item(0).getTextContent());

                float health = Float.parseFloat(
                        waveElement.getElementsByTagName("health").item(0).getTextContent());

                float speed = Float.parseFloat(
                        waveElement.getElementsByTagName("speed").item(0).getTextContent());

                int money = Integer.parseInt(
                        waveElement.getElementsByTagName("money").item(0).getTextContent());

                float attack = Float.parseFloat(
                        waveElement.getElementsByTagName("attack").item(0).getTextContent());

                String type = waveElement.getElementsByTagName("type").item(0).getTextContent();
                waves.add(new Wave(group, waveID, count, interval, health, speed, money, attack, type));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return waves;
    }
}
