package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;

public class AbstractSequence {
    private final List<AbstractNote> notes = new ArrayList<>();
    private double tempo;
    private AbstractSequence parentSequence;

    public AbstractSequence() {
        this.parentSequence = null;
    }
    
    public List<AbstractNote> getNotes() {
        return notes;
    }
    
    public void addNote(AbstractNote note) {
        notes.add(note);
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }
    
    public double getTempo() {
        return this.tempo;
    }
    
    public AbstractSequence getParentSequence() {
        return parentSequence;
    }
    
    public void setParentSequence(AbstractSequence sequence) {
        parentSequence = sequence;
    }

    @Override
    public String toString() {
        String noteInfo = "";

        for (AbstractNote note : notes) {
            noteInfo += note.toString() + " ";
        }

        return noteInfo;
    }

    public AbstractSequence getCopy() {
        AbstractSequence sequence = new AbstractSequence();
        
        sequence.setTempo(this.tempo);
        sequence.setParentSequence(this);
        
        this.notes.forEach((note) -> {
            sequence.notes.add(note.getCopy());
        });
        return sequence;
    }
}
