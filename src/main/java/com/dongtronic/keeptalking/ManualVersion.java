package com.dongtronic.keeptalking;

public enum ManualVersion {
    CODE241V1("241 version 1", 23, 4),
    MANUAL("Other", 0, 0);

    private String name;
    private int pages;
    private int introPages;

    ManualVersion(String name, int pages, int introPages) {
        this.name = name;
        this.pages = pages;
        this.introPages = introPages;
    }

    public String getName() {
        return name;
    }

    public int getPages() {
        return pages;
    }

    public int getIntroPages() {
        return introPages;
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
