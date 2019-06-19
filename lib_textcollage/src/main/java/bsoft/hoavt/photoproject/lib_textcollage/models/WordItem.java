package bsoft.hoavt.photoproject.lib_textcollage.models;

import java.util.ArrayList;

import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;

/**
 * Created by vutha on 7/13/2017.
 */

public class WordItem extends ArrayList<CharacterItem> {

    private static final String TAG = WordItem.class.getSimpleName();
    private int id = -1;

    public String getWord() {
        String word = "";
        int len = this.size();
        Flog.d(TAG, "len=" + len);
        for (int i = 0; i < len; i++) {
            word += this.get(i).getText();
        }
        Flog.d(TAG, "word=" + word);
        return word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
