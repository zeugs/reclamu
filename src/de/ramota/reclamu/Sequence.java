package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    private final List<Note> notes = new ArrayList<>();
    private double tempo;
    public List<PlayGroup> silencedGroups = new ArrayList<>();
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void addNote(Note note) {
        notes.add(note);
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }
    
    public double getTempo() {
        return this.tempo;
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
        
        sequence.silencedGroups = new ArrayList<>();
        silencedGroups.forEach((group) -> {
            sequence.silencedGroups.add(group.getCopy());
        });
        
        sequence.setTempo(this.tempo);
        
        this.notes.forEach((note) -> {
            sequence.notes.add(note.getCopy());
        });
        return sequence;
    }
}
