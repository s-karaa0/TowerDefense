package org.example.towerdefense.Model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TowerConfig {
    private List<TowerData> towers;

    public List<TowerData> getTowers() {
        return towers;
    }

    public Map<Integer, TowerData> getTowersAsMap() {
        return towers.stream().collect(Collectors.toMap(TowerData::getId, Function.identity()));
    }
}