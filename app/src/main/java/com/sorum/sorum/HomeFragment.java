package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private String exam = "bos";
    private String username;
    private String name;
    private String question_name;
    private TabLayout tab;
    private ViewPager viewPager;
    private ArrayList<String> question_names;
    private Integer question_name_which;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = getArguments();
        name = bundle.getString("name");
        exam = bundle.getString("exam");
        question_name_which = bundle.getInt("question_name_which");
        username = bundle.getString("username");
        question_name = bundle.getString("question_name");
        question_names = bundle.getStringArrayList("question_names");
        if(exam.isEmpty()){
            Intent ntent =new Intent(getContext(), RegisterTwoActivity.class);///İntent ouşturup 2. activity'e gideceğini belirledik.
            ntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().finish();
            startActivity(ntent);
        }


        if (question_names != null){
            tab = v.findViewById(R.id.tabs);
            Log.d("salman", "burda");
            viewPager = v.findViewById(R.id.frameLayout);
            for (int k = 0; k <question_names.size(); k++) {
                tab.addTab(tab.newTab().setText("" + question_names.get(k)));
            }

            PlansPagerAdapter adapter = new PlansPagerAdapter
                    (getActivity().getSupportFragmentManager(), tab.getTabCount(), question_names,exam);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(1);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
            tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }

            });
            viewPager.setCurrentItem(question_name_which);
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
            if (tab.getTabCount() == 4) {
                tab.setTabMode(TabLayout.MODE_FIXED);
            } else {
                tab.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }
        return v;
    }



}
