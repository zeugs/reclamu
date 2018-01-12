package de.ramota.reclamu;

import static de.ramota.reclamu.Composer.MAX_SEQUENCE_LENGTH;
import static de.ramota.reclamu.Composer.MIN_SEQUENCE_LENGTH;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

public class Piece {
    public List<Track> Tracks = new ArrayList<>();
    public List<PlayGroup> Groups = new ArrayList<>();
    
    public void addTrack(Track track) {
        Tracks.add(track);
    }

    public Track getTrack(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        
        Track track = new Track();
        int numberOfSequences = twister.nextInt(20);
        
        Sequence adaptedSequence;
        Sequence sequenceToAdd;
        for (int i = 0; i < numberOfSequences; i++) {
            Sequence sequence = getSequence(instrument);   
            
            int repetitions = twister.nextInt(10);
            System.out.println(String.format("Number of repetitions: %d", repetitions));
            for (int j = 0; j < repetitions; j++) {
                if (twister.nextInt(5) == 0) {
                    adaptedSequence = sequence.getCopy();
                    boolean transposeUp = twister.nextBoolean();
                    adaptedSequence.notes.forEach(note -> {
                        note.addValue(transposeUp ? 12 : -12, instrument, false);
                    });
                    track.Sequences.add(adaptedSequence);
                    sequenceToAdd = adaptedSequence;
                } else {
                    sequenceToAdd = sequence;
                }
                
                if (twister.nextInt(7) == 0) {
                    sequenceToAdd.notes.forEach(note -> {
                        note.IsRest = true;
                    });                    
                }
                track.Sequences.add(sequenceToAdd);
            }
        }
                
        this.Tracks.add(track);
        return track;
    }

    private Sequence getSequence(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Sequence sequence = new Sequence();

        int lengthRange = MAX_SEQUENCE_LENGTH - MIN_SEQUENCE_LENGTH;
        int patternLength = twister.nextInt(lengthRange);
        int targetLength = patternLength + MIN_SEQUENCE_LENGTH;
        int baseLength = instrument.DefaultLength;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int currentValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
        int usedBaseNote = twister.nextInt(12);
        double maxOffset = twister.nextInt(14) + 2;
        int changeBaseNoteRange = twister.nextInt(20) + 5;
        
        IntendedAccompaniment intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];

        int i = 0;
        int attackRange = twister.nextInt(90) + 1;
        int changeAccompRange = twister.nextInt(10) + 1;
        int restDelayRange = twister.nextInt(7) + 1;
        int restStartRange = twister.nextInt(12) + 1;

        while (i < targetLength) {
            boolean adaptLength = twister.nextInt(8) == 0;
            boolean switchLength = twister.nextInt(30) == 0;
            int actualLength = baseLength;
                        
            if (adaptLength) {
                double lengthDelta = twister.nextDouble() * (baseLength * 0.3); // max 30% change
                boolean subtractDelta = twister.nextBoolean();
                
                if (subtractDelta) {
                    lengthDelta *= -1;
                }
                baseLength += lengthDelta;
                
                if (baseLength < Note.MIN_LENGTH) {
                    baseLength = Note.MIN_LENGTH;
                } else if (baseLength > Note.MAX_LENGTH) {
                    baseLength = Note.MAX_LENGTH - twister.nextInt(Note.MAX_LENGTH) / 2;
                }
            }
            
            if (switchLength) {
                baseLength = twister.nextInt(Note.MAX_LENGTH) * Note.MAX_LENGTH / 2 + Note.MIN_LENGTH;
                actualLength = baseLength;
            }
            
            if (twister.nextInt(changeBaseNoteRange) == 0) {
                usedBaseNote = twister.nextInt(12);
            }
            
            double baseOffset = twister.nextInt((int)maxOffset + 1);
            double adjustedOffset = (baseOffset - maxOffset / 2) + 1;
            
            Note note = new Note(currentValue);
            note.Attack = twister.nextInt(attackRange) + 30;
            note.BaseNote = usedBaseNote;
            actualLength = note.SetLength(actualLength, true);

            if (twister.nextInt(changeAccompRange) == 0) {
                intendedAccomp = IntendedAccompaniment.values()[twister.nextInt(1)];
                System.out.println("Intended Accomp changed!");
            }
            
            note.IntendedAccomp = intendedAccomp;
            
            double actualGrip = instrument.VariationGrip;
            
            currentValue = note.addValue(adjustedOffset * actualGrip, instrument, true);

            i += actualLength;

            if (sequence.notes.size() > 0) {
                if (sequence.notes.get(sequence.notes.size() - 1).IsRest) {
                    note.IsRest = true;
                    if (twister.nextInt(restDelayRange) == 0) {
                        note.IsRest = false;
                    }
                } else {
                    note.IsRest = twister.nextInt(restStartRange) == 0;
                }
            }
            
            sequence.addNote(note);
        }    
        
        System.out.println(String.format("Sequence note num: %s", sequence.notes.size()));
        
        return sequence;
    }
    
    public void AddAccompTrack(Track track, Instrument instrument, int trackNum, int mirroredTrackNum) {
        List<Track> newTracks = new ArrayList<>();
        MersenneTwister twister = new MersenneTwister();
        
        for (int i = 0; i < trackNum - mirroredTrackNum; i++) {
            Track accompTrack = this.getAccompanimentTrack(track, instrument);
            this.addTrack(accompTrack);
            newTracks.add(accompTrack);
        }
        for (int i = 0; i < mirroredTrackNum; i++) {
            Track accompTrack = newTracks.get(twister.nextInt(newTracks.size()));
            Track mirrorTrack = accompTrack.getCopy();
            this.addTrack(mirrorTrack);
        }
    }
    
    private Track getAccompanimentTrack(Track masterTrack, Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        Track track = new Track();
        int restRange = twister.nextInt(7) + 1;

        for (Sequence refSequence: masterTrack.Sequences) {
            Sequence sequence = new Sequence();
    
            boolean addRest = (twister.nextInt(restRange) == 0);
            boolean silence = refSequence.SilencedGroups.contains(instrument.Group);
            
            if (addRest || silence) {
                for (int j = 0; j < refSequence.notes.size(); j++) {
                    Note noteCopy = refSequence.notes.get(j).getCopy();
                    noteCopy.IsRest = true;
                    sequence.notes.add(noteCopy);
                }    
                track.Sequences.add(sequence);
                
                continue;
            }                
            
            int attackRange = twister.nextInt(90) + 1;
            int restDelayRange = twister.nextInt(7) + 1;
            int restStartRange = twister.nextInt(12) + 1;
            int noteLengthRange = twister.nextInt(12) + 1;
            int noteSkipRange = twister.nextInt(8) + 1;
            int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
            int sequenceOffset = twister.nextInt(instrumentRange) - instrumentRange / 2;
            sequenceOffset = sequenceOffset - sequenceOffset % 12;
                    
            for (int i = 0; i < refSequence.notes.size(); i++) {
                int delayLength = twister.nextInt(15) + 5;
                Note refNote = refSequence.notes.get(i);
                Note note = new Note(refNote.GetValue() + sequenceOffset + refNote.BaseNote);
                note.Attack = twister.nextInt(attackRange) + 30;
                note.BaseNote = refNote.BaseNote;
                note.SetLength(refNote.GetLength() - delayLength, false);
                note.IntendedAccomp = refNote.IntendedAccomp;

                if (delayLength > 0) {
                    Note delayPseudoNote = new Note(70);
                    delayPseudoNote.IsRest = true;
                    delayPseudoNote.SetLength(delayLength, false);
                    sequence.addNote(delayPseudoNote);     
                }
                
                int rand = 0;
                int valueToAdd = 0;

                if (note.Attack > 127) {
                    note.Attack = 115;
                } else if (note.Attack < 0) {
                    note.Attack = 15;
                }

                if (refNote.IntendedAccomp == IntendedAccompaniment.MAJOR) {
                    twister.nextInt(3);
                    switch (rand) {
                        case 0: valueToAdd = 0; break;
                        case 1: valueToAdd = 4; break;
                        case 2: valueToAdd = 7; break;
                        default: break;
                    }                
                }

                if (refNote.IntendedAccomp == IntendedAccompaniment.MINOR) {
                    twister.nextInt(3);
                    switch (rand) {
                        case 0: valueToAdd = 0; break;
                        case 1: valueToAdd = 3; break;
                        case 2: valueToAdd = 7; break;
                        default: break;
                    }                
                }

                note.addValue(valueToAdd, instrument, true);

                if (sequence.notes.size() > 0) {
                    if (sequence.notes.get(sequence.notes.size() - 1).IsRest) {
                        note.IsRest = true;
                        if (twister.nextInt(restDelayRange) == 0) {
                            note.IsRest = false;
                        }
                    } else {
                        note.IsRest = twister.nextInt(restStartRange) == 0;
                    }                    
                }

                if (twister.nextInt(noteLengthRange) == 0) {
                    int skip = twister.nextInt(noteSkipRange) + 1;
                    int startPos = i + 1;

                    for (int j = startPos; j < startPos + skip; j++) {
                        if (j < refSequence.notes.size()) {
                            note.SetLength(note.GetLength() + refSequence.notes.get(j).GetLength(), false);
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                
                sequence.addNote(note);                
            }
            
            track.Sequences.add(sequence);
        }
        
        return track;
    }    
}
