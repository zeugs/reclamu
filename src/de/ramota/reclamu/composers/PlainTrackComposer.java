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
public class PlainTrackComposer extends TrackComposer {

    public PlainTrackComposer(String name) {
        super(name);
    }
        
    @Override
    public AbstractSequence getSequence(Instrument instrument, double tempo) {
        AbstractSequence sequence = new AbstractSequence();
        
        sequence.setTempo(tempo);

        if (currentVal == 0 || twister.nextInt(3) == 0) {
            currentVal = twister.nextInt((int) ((instrument.MaxNoteIndex - instrument.MinNoteIndex) * 0.75)) + instrument.MinNoteIndex;
        }

        int divisionStartRange = twister.nextInt(3) + 5;
        int lengthenStartRange = twister.nextInt(3) + 1;

        int sequenceLength = (twister.nextInt(2) + 1) * 8 * AbstractNote.MIN_LENGTH;

        int currentLength = twister.nextInt(instrument.DefaultLength / 4) + instrument.DefaultLength / 8;
        int summedLength = 0;

        while (summedLength < sequenceLength) {
            if (twister.nextInt(3) == 0) {
                currentLength += twister.nextInt(instrument.DefaultLength / 8) - instrument.DefaultLength / 16;        
            } else if (twister.nextInt(30) == 0) {
                currentLength = twister.nextInt(instrument.DefaultLength / 6) + instrument.DefaultLength / 12;        
            }
            
            AbstractNote note = new AbstractNote(currentVal);

            if (twister.nextInt(6) == 0) {
                note.AttackSet = true;
            }

            if (twister.nextInt(lengthenStartRange) == 0) {
                note.LengtheningPossible = true;
            }

            if (twister.nextInt(divisionStartRange) == 0) {
                note.DividingPossible = true;
            }

            note.setAttack(60);
            note.setLength((int)(currentLength / sequence.getTempo()), true);
            note.IntendedScaleType = currentAccomp;

            if (twister.nextInt(3) == 0) {
                note.RelativeOffset += twister.nextInt(3) - 1;
            }

            if (sequence.getNotes().size() > 0) {
                if (sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                    note.IsRest = twister.nextBoolean();
                } else if (twister.nextInt(10) == 0) {
                    note.IsRest = true;
                }
            }

            sequence.addNote(note);
            summedLength += currentLength;

            currentVal += (twister.nextInt(6) - 3) * instrument.VariationGrip;

            if (currentVal < instrument.MinNoteIndex) {
                currentVal = instrument.MinNoteIndex;
            } else if (currentVal > instrument.MaxNoteIndex) {
                currentVal = instrument.MaxNoteIndex;
            }
        }
        
        return sequence;
    }

    protected AbstractNote generateNote(int currentNoteLength, AbstractSequence sequence) {
        AbstractNote note = new AbstractNote(currentVal);
        note.setAttack(twister.nextInt(80) + 30);
        note.setLength((int)(currentNoteLength / sequence.getTempo()), true);
        note.IntendedScaleType = currentAccomp;
        if (twister.nextInt(3) == 0) {
            note.RelativeOffset += twister.nextInt(3) - 1;
        }
        if (sequence.getNotes().size() > 0) {
            if (sequence.getNotes().get(sequence.getNotes().size() - 1).IsRest) {
                note.IsRest = twister.nextBoolean();
            } else if (twister.nextInt(400) == 0) {
                note.IsRest = true;
            }
        }
        return note;
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

            if (twister.nextInt(10) != 0 && i > 0) {
                AbstractSequence sequenceToCopy = track.Sequences.get(twister.nextInt(track.Sequences.size()));
                if (twister.nextInt(5) != 0) {
                    sequenceToCopy = track.Sequences.get(track.Sequences.size() - 1);
                }
                AbstractSequence adaptedSequence = sequenceToCopy.getCopy();

                boolean alterSequence = twister.nextInt(3) == 0;
                if (alterSequence) {
                    boolean transpose = twister.nextInt(4) == 0;
                    boolean transposeUp = twister.nextInt(4) == 0;
                    for (AbstractNote note: adaptedSequence.getNotes()) {
                        if (twister.nextInt(2) == 0) {
                            note = this.generateNote(note.getValue(), sequenceToCopy);
                        }
                        if (transpose) {
                            note.addValue(transposeUp ? 12 : -12, instrument);
                        }
                    }
                }

                track.addSequence(adaptedSequence);
            } else {
                AbstractSequence sequence;
                sequence = this.getSequence(instrument, currentTempo);
                track.addSequence(sequence);
            }
        }

        int currentAttack = twister.nextInt(80) + 40;

        this.findAccompaniment();
        this.findScale();

        for (AbstractSequence sequence: track.Sequences) {
                        
            if (twister.nextInt(6) == 0) {
                this.findScale();
            }

            if (twister.nextInt(3) == 0) {
                currentAttack = twister.nextInt(80) + 40;
            }

            for (AbstractNote note: sequence.getNotes()) {

                if (twister.nextInt(6) == 0) {
                    this.findAccompaniment();
                }

                int actualAttack = currentAttack;
                if (note.AttackSet) {
                    actualAttack = currentAttack + twister.nextInt(80) - 40;
                    if (actualAttack > 119) {
                        actualAttack = 119;
                    } else if (actualAttack < 0) {
                        actualAttack = 0;
                    }
                }

                note.setAttack(actualAttack);

                note.IntendedScaleType = currentAccomp;
                note.ScaleOffset = ScaleOffset;
                
                if (twister.nextInt(7) != 0) {
                    note.setValueInRange();
                }
                
                currentAttack += twister.nextInt(30) - 15;
                
                if (currentAttack < 25) {
                    currentAttack = 26;
                } else if (currentAttack > 120) {
                    currentAttack = 119;
                }
            }
        }
        
        return track;
    }    
}
