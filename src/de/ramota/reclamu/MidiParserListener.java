package de.ramota.reclamu;

import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;

/**
 *
 * @author Mathies Gräske
 */
public class MidiParserListener extends ParserListenerAdapter {
    AbstractSequence sequence;
    int midiTrack;
    boolean ignore = true;
    AbstractNote currentNote;
        
    public MidiParserListener(int midiTrack) {
        sequence = new AbstractSequence();
        this.midiTrack = midiTrack;
    }

    public AbstractTrack getMidiTrack(String name) {
        AbstractTrack track = new AbstractTrack(name);
        track.addSequence(sequence);
        return track;
    }
    
    @Override
    public void onTrackChanged(byte track) {
        super.onTrackChanged(track); 
        System.out.println(String.format("Track : %d", track));
        ignore = track != midiTrack;
    }

    @Override
    public void onInstrumentParsed(byte instrument) {
        super.onInstrumentParsed(instrument);
        if (!ignore) {
            System.out.println(String.format("Parsed instrument: %d", instrument));                    
        }
    }
    
    @Override
    public void onNoteParsed(Note note) {
        super.onNoteParsed(note);
        if (!ignore) {
            AbstractNote abstractNote = new AbstractNote(note.getValue() - 12);
            abstractNote.IsRest = note.isRest();
            abstractNote.setLength((int)(note.getDuration() * 2000), false);
            abstractNote.setAttack(note.getOnVelocity());
            
            sequence.addNote(abstractNote);
            currentNote = abstractNote;
            System.out.println(String.format("New note added: %s", abstractNote.toString()));        
        }
    }
    
}
