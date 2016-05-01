package com.ubi.alonso.givemesomespace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ubi.alonso.givemesomespace.StudySpace;

import java.util.ArrayList;

/**
 * Created by ben on 4/30/16.
 */
class StudySpaceAdapter extends ArrayAdapter<StudySpace> {
    public StudySpaceAdapter(Context context, ArrayList<StudySpace> studyspaces)
    {
        super(context,0,studyspaces);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        StudySpace studySpace = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_studyspace,parent,false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.item_space_name_text);
        TextView tvRating = (TextView) convertView.findViewById(R.id.item_space_name_rating);

        tvName.setText(studySpace.name);
        tvRating.setText(String.valueOf(studySpace.rating));

        return convertView;
    }
}
