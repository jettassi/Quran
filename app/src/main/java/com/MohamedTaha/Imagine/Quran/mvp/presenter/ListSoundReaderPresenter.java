package com.MohamedTaha.Imagine.Quran.mvp.presenter;

import com.MohamedTaha.Imagine.Quran.mvp.model.ImageModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public interface ListSoundReaderPresenter {
    void onDestroy();

    void getAllData();

    void setOnSearchViewListener(MaterialSearchView searchView);

    void setOnQueryTextListener(MaterialSearchView searchView, List<ImageModel> imageModel);

}
