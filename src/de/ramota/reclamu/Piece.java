package de.ramota.reclamu;

import static de.ramota.reclamu.Composer.MAX_SEQUENCE_LENGTH;
import static de.ramota.reclamu.Composer.MIN_SEQUENCE_LENGTH;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

public class Piece {
    public List<Track> Tracks = new ArrayList<>();
    public List<PlayGroup> Groups = new ArrayList<>();
    private final List<Accompaniment> intendedAccomps;
    private Accompaniment currentAccomp;
    
    public Piece() {
        MersenneTwister twister = new MersenneTwister();
        
        intendedAccomps = new ArrayList<>();
        AccompanimentItem accompItem;

        MajorScaleAccompaniment majorChordsAccomp = new MajorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        majorChordsAccomp.Items.add(accompItem);
        intendedAccomps.add(majorChordsAccomp);
        
        MajorScaleAccompaniment minorChordsAccomp = new MajorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        minorChordsAccomp.Items.add(accompItem);
        intendedAccomps.add(minorChordsAccomp);

        /*MajorScaleAccompaniment simpleAccomp = new MajorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        simpleAccomp.Items.add(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(9);
        accompItem.Offsets.add(12);
        simpleAccomp.Items.add(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(11);
        accompItem.Offsets.add(14);
        simpleAccomp.Items.add(accompItem);
        intendedAccomps.add(simpleAccomp);*/

        currentAccomp = intendedAccomps.get(twister.nextInt(intendedAccomps.size()));        
    }
    
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
        
        int i = 0;
        int attackRange = twister.nextInt(90) + 1;
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

            if (twister.nextInt(20) == 0) {
                currentAccomp = intendedAccomps.get(twister.nextInt(intendedAccomps.size()));
                System.out.println("Intended Accomp changed!");
            }
            
            note.IntendedAccomp = currentAccomp;
            
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
            int restDelayRange = twister.nextInt(7) + 2;
            int restStartRange = twister.nextInt(10) + 2;
            int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
            int absoluteValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
            absoluteValue -= absoluteValue % 12;
            int noteDiff = absoluteValue - refSequence.notes.get(0).GetValue();
            
            for (int i = 0; i < refSequence.notes.size(); i++) {
                int delayLength = twister.nextInt(15) + 5;
    
                Note refNote = refSequence.notes.get(i);
                Note note = new Note(refNote.GetValue() + noteDiff + refNote.BaseNote);
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
                
                if (note.Attack > 127) {
                    note.Attack = 115;
                } else if (note.Attack < 0) {
                    note.Attack = 15;
                }

                ArrayList<Integer> offsets = refNote.IntendedAccomp.GetItemOffsets();

                int valueIndex = twister.nextInt(offsets.size());
                int valueToAdd = offsets.get(valueIndex);

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

                if (twister.nextInt(12) == 0) {
                    int skip = twister.nextInt(5);
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
