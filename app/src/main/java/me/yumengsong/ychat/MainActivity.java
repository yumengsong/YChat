package me.yumengsong.ychat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int INDEX_INDICATOR_ONE = 0;
    public static final int INDEX_INDICATOR_TWO = 1;
    public static final int INDEX_INDICATOR_THREE = 2;
    public static final int INDEX_INDICATOR_FOUR = 3;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private List<Fragment> mTabFragmentLists = new ArrayList<>();
    private String[] mTabTitles = new String[]{
            "FirstTab", "SecondTab", "ThirdTab", "FourthTab"
    };
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        initView();
        initData();

        mViewPager.setAdapter(mFragmentPagerAdapter);
        initEvent();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }

    private void initData() {
        for (String title : mTabTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(bundle);
            mTabFragmentLists.add(tabFragment);
        }

        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabFragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return mTabFragmentLists.size();
            }
        };
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //根据Log输出，得到如下规律：
                //从第一页到第二页，position = 0，positionOffset 0.0 ~ 1.0
                //从第二页到第一页，position = 0，positionOffset 1.0 ~ 0.0

                if (positionOffset > 0) {
                    ChangeColorIconWithText left = mTabIndicators.get(position);
                    ChangeColorIconWithText right = mTabIndicators.get(position + 1);

                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*Show Overflow option menu item icon*/
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(INDEX_INDICATOR_ONE).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(INDEX_INDICATOR_TWO).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(INDEX_INDICATOR_THREE).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(INDEX_INDICATOR_FOUR).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
        }
    }

    /**
     * Reset other TabIndicators's color
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }
}
