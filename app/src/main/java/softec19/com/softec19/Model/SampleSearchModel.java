package softec19.com.softec19.Model;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by hamza on 16-Mar-19.
 */

public class SampleSearchModel implements Searchable {

    private String mTitle;

    public SampleSearchModel(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public SampleSearchModel setTitle(String title) {
        mTitle = title;
        return this;
    }
}
