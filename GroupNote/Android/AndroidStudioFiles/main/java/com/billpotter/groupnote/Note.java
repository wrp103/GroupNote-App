package com.billpotter.groupnote;

// simple class to store information of each note

public class Note {

    String note_id;
    String note_text;
    String note_title;
    int active = 1;

    public Note(String note_id, String note_text, String note_title) {
        this.note_id = note_id;
        this.note_text = note_text;
        this.note_title = note_title;
    }

    public String getNote_id() {
        return note_id;
    }

    public String getNote_text() {
        return note_text;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
