package com.becitizen.app.becitizen.presentation;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.becitizen.app.becitizen.R;

import java.util.ArrayList;
import java.util.List;

public class ImageSelection extends ListActivity {

    public static String RESULT_IMAGE = "1";
    public String[] imageCodes;
    private TypedArray imgs;
    private List<ProfileImage> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateImageList();

        ArrayAdapter<ProfileImage> adapter = new ImageListArrayAdapter(this, imageList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileImage c = imageList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_IMAGE, c.getCode());
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }

    private void populateImageList() {
        imageList = new ArrayList<ProfileImage>();
        imageCodes = getResources().getStringArray(R.array.image_codes);
        imgs = getResources().obtainTypedArray(R.array.image_pngs);

        for (int i = 0; i < imageCodes.length; i++){
            imageList.add(new ProfileImage(imageCodes[i], imgs.getDrawable(i)));
        }
    }

    public class ProfileImage {
        private String code;
        private Drawable png;

        public ProfileImage(String code, Drawable png) {
            this.code = code;
            this.png = png;
        }

        public String getCode() {
            return code;
        }

        public Drawable getPng() {
            return png;
        }
    }
}
