package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    public List<Note> notes = new ArrayList<>();
    
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
}
