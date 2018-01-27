package de.ghc.managementbot.entity;

public class ErrorEntry extends IPEntry {

    private String error;

    public ErrorEntry(String error) {
        super("");
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
