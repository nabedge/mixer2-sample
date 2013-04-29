package org.mixer2.sample;

public enum Lang {
    EN("English"), JA("日本語");

    private String name;

    private Lang(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
