package org.example.towerdefense.Wave;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WaveLoader {


    public static List<Wave> loadWaves() {

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

                // the group the wave belongs to
                int group = Integer.parseInt(
                        waveElement.getElementsByTagName("group").item(0).getTextContent());

                // the id of the wave
                int waveID = Integer.parseInt(
                        waveElement.getElementsByTagName("waveID").item(0).getTextContent());

                // the amount of enemies in the wave
                int count = Integer.parseInt(
                        waveElement.getElementsByTagName("enemyCount").item(0).getTextContent());

                // the time it takes to spawn enemies after another
                double interval = Double.parseDouble(
                        waveElement.getElementsByTagName("spawnInterval").item(0).getTextContent());

                String type = waveElement.getElementsByTagName("type").item(0).getTextContent();
                String bosstype = null;
                if (waveElement.getElementsByTagName("bossType").item(0) != null) {
                    bosstype = waveElement.getElementsByTagName("bossType").item(0).getTextContent();
                }

                waves.add(new Wave(group, waveID, count, interval, type, bosstype));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " in WaveLoader.java");
        }

        return waves;
    }
}
