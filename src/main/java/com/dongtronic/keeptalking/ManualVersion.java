package com.dongtronic.keeptalking;

public enum ManualVersion {
    CODE241V1("241 version 1", 23),
    MANUAL("Other", 0);

    private String name;
    private int pages;

    ManualVersion(String name, int pages) {
        this.name = name;
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public int getPages() {
        return pages;
    }

    public static ManualVersion fromName(String name) {
        for (ManualVersion version : ManualVersion.values()) {
            if (version.getName().equalsIgnoreCase(name)) {
                return version;
            }
        }
        return null;
    }
}
