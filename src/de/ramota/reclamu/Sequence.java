package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    private final List<Note> notes = new ArrayList<>();
    public List<PlayGroup> SilencedGroups = new ArrayList<>();
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public String toString() {
        String noteInfo = "";

        for (Note note : notes) {
            noteInfo += note.toString() + " ";
        }

        return noteInfo;
    }

    public Sequence getCopy() {
        Sequence sequence = new Sequence();
        this.notes.forEach((note) -> {
            sequence.notes.add(note.getCopy());
        });
        return sequence;
    }
}
