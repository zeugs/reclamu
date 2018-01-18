package de.ramota.reclamu;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

public class Piece {
    public List<Track> Tracks = new ArrayList<>();
    public List<PlayGroup> Groups = new ArrayList<>();
    private final List<ScaleItem> intendedScaleItems;

    public Piece() {
        intendedScaleItems = new ArrayList<>();
        AccompanimentItem accompItem;

        /*MajorScaleAccompaniment majorChordsAccomp = new MajorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        majorChordsAccomp.Items.add(accompItem);
        intendedScaleItems.add(majorChordsAccomp);*/
        
        /*MinorScaleAccompaniment minorChordsAccomp = new MinorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(4);
        accompItem.Offsets.add(7);
        minorChordsAccomp.Items.add(accompItem);
        intendedScaleItems.add(minorChordsAccomp);*/

        // contains I, IV, V, VI
        MajorScaleAccompaniment simpleAccomp = new MajorScaleAccompaniment();
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
        /*accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(9);
        accompItem.Offsets.add(13);
        accompItem.Offsets.add(16);
        simpleAccomp.Items.add(accompItem);*/
        intendedScaleItems.add(simpleAccomp);

        // contains i, iv, v, vi, ii
        MinorScaleAccompaniment simpleAccomp2 = new MinorScaleAccompaniment();
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(0);
        accompItem.Offsets.add(3);
        accompItem.Offsets.add(7);
        simpleAccomp2.Items.add(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(8);
        accompItem.Offsets.add(12);
        simpleAccomp2.Items.add(accompItem);
        accompItem = new AccompanimentItem();
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(7);
        accompItem.Offsets.add(10);
        accompItem.Offsets.add(14);
        simpleAccomp2.Items.add(accompItem);
        /*accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(8);
        accompItem.Offsets.add(12);
        accompItem.Offsets.add(16);
        simpleAccomp2.Items.add(accompItem);
        accompItem.Offsets = new ArrayList<>();
        accompItem.Offsets.add(2);
        accompItem.Offsets.add(5);
        accompItem.Offsets.add(9);
        simpleAccomp2.Items.add(accompItem);*/
        intendedScaleItems.add(simpleAccomp2);
    }
    
    public void addTrack(Track track) {
        Tracks.add(track);
    }

    public Track getTrack(Instrument instrument) {
        MersenneTwister twister = new MersenneTwister();
        
        Track track = new Track(instrument, intendedScaleItems);
        int numberOfSequences = twister.nextInt(20) + 10;
        
        Sequence adaptedSequence;
        Sequence sequenceToAdd;
        for (int i = 0; i < numberOfSequences; i++) {
            
            Sequence sequence;
            if (i > 5 && twister.nextInt(3) == 0) {
                int itemToCopy = twister.nextInt(track.Sequences.size());
                sequence = track.Sequences.get(itemToCopy).getCopy();
                System.out.println(String.format("Just copied sequence %d", itemToCopy));
            } else {
                sequence = track.getSequence(instrument);   
            }
            
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
                    continue;
                } else {
                    sequenceToAdd = sequence;
                }
                
                boolean modifyNoteValues = twister.nextBoolean();
                if (modifyNoteValues) {
                    adaptedSequence = sequence.getCopy();
                    adaptedSequence.notes.forEach(note -> {
                        if (twister.nextInt(12) == 0) {
                            note.addValue(twister.nextInt(8) - 3, instrument, true);
                        }
                    });
                    sequenceToAdd = adaptedSequence;
                }

                if (twister.nextInt(7) == 0) {
                    sequenceToAdd.notes.forEach(note -> {
                        note.IsRest = true;
                    });                    
                }
                track.Sequences.add(sequenceToAdd);
            }
        }

        int currentAttack = twister.nextInt(80) + 40;
        
        for (Sequence sequence: track.Sequences) {
            for (Note note: sequence.notes) {
                if (twister.nextInt(150) == 0) {
                    currentAttack = twister.nextInt(80) + 40;
                }
                note.SetAttack(currentAttack);
                currentAttack += twister.nextInt(36) - 18;
                
                if (currentAttack < 40) {
                    currentAttack = 41;
                } else if (currentAttack > 120) {
                    currentAttack = 119;
                }
            }
        }
        this.Tracks.add(track);
        return track;
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
        Track track = new Track(instrument);
        int restRange = twister.nextInt(7) + 1;
        int noteDiff = -1;
        
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
            
            int restDelayRange = twister.nextInt(7) + 2;
            int restStartRange = twister.nextInt(10) + 2;

            if (noteDiff == -1) {
                noteDiff = findNoteDiff(instrument, twister, refSequence);
            }
            
            for (int i = 0; i < refSequence.notes.size(); i++) {
                int delayLength = twister.nextInt(15) + 5;
    
                if (twister.nextInt(30) == 0) {
                    noteDiff = this.findNoteDiff(instrument, twister, refSequence);
                }
                
                Note refNote = refSequence.notes.get(i);
                
                int noteVal = (refNote.GetValue() - refNote.GetValue() % 12) - (noteDiff - noteDiff % 12) + refNote.ScaleOffset;
                Note note = new Note(noteVal);
                note.SetAttack(refNote.GetAttack() + twister.nextInt(40) - 20);
                note.ScaleOffset = refNote.ScaleOffset;
                note.SetLength(refNote.GetLength() - delayLength, false);
                note.IntendedScaleType = refNote.IntendedScaleType;

                if (delayLength > 0) {
                    Note delayPseudoNote = new Note(70);
                    delayPseudoNote.IsRest = true;
                    delayPseudoNote.SetLength(delayLength, false);
                    sequence.addNote(delayPseudoNote);     
                }

                ArrayList<Integer> offsets = refNote.IntendedScaleType.GetItemOffsets();

                int valueIndex = twister.nextInt(offsets.size());
                int valueToAdd = offsets.get(valueIndex);

                note.addValue(valueToAdd, instrument, false);

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

    private int findNoteDiff(Instrument instrument, MersenneTwister twister, Sequence refSequence) throws IllegalArgumentException {
        int noteDiff;
        int instrumentRange = instrument.MaxNoteIndex - instrument.MinNoteIndex;
        int absoluteValue = twister.nextInt(instrumentRange / 2) + instrument.MinNoteIndex + instrumentRange / 4;
        absoluteValue -= absoluteValue % 12;
        noteDiff = absoluteValue - refSequence.notes.get(0).GetValue();
        return noteDiff;
    }
}
