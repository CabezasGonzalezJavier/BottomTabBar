package com.thedeveloperworldisyours.tablayoutbottom;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.thedeveloperworldisyours.tablayoutbottom.fragments.FirstFragment;
import com.thedeveloperworldisyours.tablayoutbottom.fragments.SecondFragment;
import com.thedeveloperworldisyours.tablayoutbottom.fragments.ThirdFragment;

public class MainActivity extends AppCompatActivity {

    public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    private TabHost mTabHost;
    private String mCurrentTab;

    public static final String TAB_TAGS = "TAB_TAGS";
    public static final String TAB_MAP = "TAB_MAP";
    public static final String TAB_SETTINGS = "TAB_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup();

        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getString(M_CURRENT_TAB);
            initializeTabs();
            mTabHost.setCurrentTabByTag(mCurrentTab);
            /*
            when resume state it's important to set listener after initializeTabs
            */
            mTabHost.setOnTabChangedListener(listener);
        } else {
            mTabHost.setOnTabChangedListener(listener);
            initializeTabs();
        }
    }

    private View createTabView(final int id, final String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    /*
    create 3 tabs with name and image
    and add it to TabHost
     */
    public void initializeTabs() {

        TabHost.TabSpec spec;

        spec = mTabHost.newTabSpec(TAB_TAGS);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.ic_menu_camera, getString(R.string.fragment_first_title)));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(TAB_MAP);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.ic_menu_gallery, getString(R.string.fragment_second_title)));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(TAB_SETTINGS);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.ic_menu_manage, getString(R.string.fragment_third_title)));
        mTabHost.addTab(spec);

    }

    /*
    first time listener will be trigered immediatelly after first: mTabHost.addTab(spec);
    for set correct Tab in setmTabHost.setCurrentTabByTag ignore first call of listener
    */
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {

            mCurrentTab = tabId;

            if (tabId.equals(TAB_TAGS)) {
                pushFragments(FirstFragment.newInstance(), false,
                        false, null);
            } else if (tabId.equals(TAB_MAP)) {
                pushFragments(SecondFragment.newInstance(), false,
                        false, null);
            } else if (tabId.equals(TAB_SETTINGS)) {
                pushFragments(ThirdFragment.newInstance(), false,
                        false, null);
            }

        }
    };

    /*
    Example of starting nested fragment from another fragment:

    Fragment newFragment = ManagerTagFragment.newInstance(tag.getMac());
                    TagsActivity tAct = (TagsActivity)getActivity();
                    tAct.pushFragments(newFragment, true, true, null);
     */
    public void pushFragments(Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
//        if (shouldAnimate) {
//            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
//                    R.animator.fragment_slide_left_exit,
//                    R.animator.fragment_slide_right_enter,
//                    R.animator.fragment_slide_right_exit);
//        }
        ft.replace(R.id.realtabcontent, fragment, tag);

        if (shouldAdd) {
            /*
            here you can create named backstack for realize another logic.
            ft.addToBackStack("name of your backstack");
             */
            ft.addToBackStack(null);
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            or remove whole:
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
             */
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    /*
    If you want to start this activity from another
     */
    public static void startUrself(Activity context) {
        Intent newActivity = new Intent(context, MainActivity.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newActivity);
        context.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(M_CURRENT_TAB, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
