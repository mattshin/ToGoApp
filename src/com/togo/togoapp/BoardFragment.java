package com.togo.togoapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.togo.togoapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardFragment extends Fragment {
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
            if (container == null) {
        return null;
    }
            LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_board, container, false);
            TextView boardText = (TextView)layout.findViewById(R.id.board_content);
     
            BufferedReader reader;
            try {
            	reader = new BufferedReader(new FileReader(new File(getActivity().getFilesDir(),"boardText.txt")));
                StringBuilder text = new StringBuilder();
                String line = "";

                while ((line=reader.readLine()) != null) {
                    text.append(line + "\n");
                }
                boardText.setText(text.toString());
                reader.close();
            }catch(Exception e){
            	boardText.setText(e.getMessage());
            }

            
            return layout;  }
}