package de.ramota.reclamu.composers;

import de.ramota.reclamu.Instrument;
import de.ramota.reclamu.AbstractNote;
import de.ramota.reclamu.AbstractSequence;
import de.ramota.reclamu.AbstractTrack;
import de.ramota.reclamu.ScaleItem;

/**
 *
 * @author Mathies Gräske
 */
public class SineWaveTrackComposer extends TrackComposer {

    public SineWaveTrackComposer(String name) {
        super(name);
    }
        
    @Override
    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        AbstractSequence sequence = new AbstractSequence();
        
        sequence.setTempo(tempo);
        
        if (currentVal == 0) {
            currentVal = twister.nextInt((int) ((instrument.MaxNoteIndex - instrument.MinNoteIndex) * 0.75)) + instrument.MinNoteIndex;
        }
        
        int currentLength = twister.nextInt(instrument.DefaultLength / 2) + instrument.DefaultLength / 4;
        int direction = 1;
        double initialOffset = twister.nextDouble() * Math.PI;
        double currentX = initialOffset;
        
        while (currentX < 2 * Math.PI + initialOffset) {
            currentLength += instrument.DefaultLength / 50 * direction;
            
            if (currentLength >= instrument.DefaultLength * 1.5 || currentLength <= instrument.DefaultLength / 15) {
                direction *= -direction; 
            }
            
            AbstractNote note = new AbstractNote(currentVal);

            note.setLength((int)(currentLength / sequence.getTempo()), true);
            note.IntendedScaleType = currentAccomp;
            note.IsRest = false;
            
            sequence.addNote(note);

            currentVal = (int) ((Math.sin(currentX) + 1) * (instrument.MaxNoteIndex - instrument.MinNoteIndex) / 2) + instrument.MinNoteIndex;
            currentX += Math.PI / 100;
        }
        
        return sequence;
    }    
    
    @Override
    public AbstractTrack generateTrack(Instrument instrument, String name, int sequenceNum) {
        AbstractTrack track = new AbstractTrack(name);
        
        double currentTempo = twister.nextDouble() * 2 + 0.1;

        for (int i = 0; i < sequenceNum; i++) {
            if (twister.nextInt(3) == 0) {
                currentTempo += (twister.nextDouble() - 0.5) / 2;

                if (currentTempo < 0) {
                    currentTempo = 0;
                } else if (currentTempo > 2) {
                    currentTempo = 2;
                }
                System.out.println(String.format("Tempo: %d", (int)(currentTempo * 100)));
            }
            
            AbstractSequence sequence;
            sequence = this.getSequence(instrument, currentTempo);   
            track.addSequence(sequence);            
        }
                
        this.findAccompaniment();
        this.findScale();
        
        double currentAttackOffset = 0;
        int sequenceCount = 0;
        for (AbstractSequence sequence: track.Sequences) {
                        
            if (twister.nextInt(6) == 0) {
                this.findScale();
            }

            int noteCount = 0;
            boolean terminate = twister.nextInt(4) == 0 || sequenceCount == track.Sequences.size() - 1;
            int terminateIntro = twister.nextBoolean() ? 1 : 2;
            ScaleItem oldAccomp = this.currentAccomp;
            int oldOffset = currentAccomp.getOffset();
            
            for (AbstractNote note: sequence.getNotes()) {
                
                if (terminate && noteCount > sequence.getNotes().size() * 0.9) {
                    this.currentAccomp = intendedAccomps.get(0);
                    this.currentAccomp.setNewOffset(0);                    
                    System.out.println("!!!-!");
                } else if (terminate && noteCount > sequence.getNotes().size() * 0.8) {
                    oldAccomp = this.currentAccomp;
                    oldOffset = currentAccomp.getOffset();
                    this.currentAccomp = intendedAccomps.get(0);
                    this.currentAccomp.setNewOffset(terminateIntro);
                    System.out.println("!-!");
                } else if (twister.nextInt(14) == 0) {
                    this.findAccompaniment();
                }

                note.IntendedScaleType = currentAccomp;
                note.ScaleOffset = ScaleOffset;
                note.setAttack((int) ((Math.sin(currentAttackOffset)) * 40 + 70));
                note.setValueInRange();
    
                currentAttackOffset += Math.PI / 70;
                noteCount++;
            }
            
            this.currentAccomp = oldAccomp;
            this.currentAccomp.setNewOffset(oldOffset);

            sequenceCount++;
        }
        
        return track;
    }    
}
