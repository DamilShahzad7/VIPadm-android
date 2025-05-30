package vip.com.vipmeetings.models;

public class LetterModel {

    String letter;
    int pos;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }



    @Override
    public String toString() {
        return letter;
    }


    @Override
    public int hashCode() {
        return letter.hashCode();
    }


    @Override
    public boolean equals(Object obj) {

        if(obj instanceof LetterModel)
        {

            if(((LetterModel) obj).letter.equalsIgnoreCase(letter))
            {

                return true;
            }

        }
        return false;
    }
}
