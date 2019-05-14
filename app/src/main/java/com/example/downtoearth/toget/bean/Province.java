package com.example.downtoearth.toget.bean;

import java.io.Serializable;
import java.util.List;

public class Province implements Serializable {
    private String name;
    private List<City> areaList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<City> areaList) {
        this.areaList = areaList;
    }

    public static class City{
        private String name;
        private List<String> areaList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<String> areaList) {
            this.areaList = areaList;
        }
    }
}
