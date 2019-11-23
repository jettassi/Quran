package com.MohamedTaha.Imagine.Quran.presenter;

import com.MohamedTaha.Imagine.Quran.model.ModelAzkar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public interface AzkarFragmentPresenter {
    void getAllData();

    void onDestroy();

    void setOnSearchView(MaterialSearchView materialSearchView);

    void setOnQueryTextForAzkar(MaterialSearchView materialSearchView, List<ModelAzkar> name_azkar);

}
