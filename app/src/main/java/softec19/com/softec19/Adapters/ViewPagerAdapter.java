package softec19.com.softec19.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import softec19.com.softec19.Fragments.BasicUser;
import softec19.com.softec19.Fragments.PremiumUser;
import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;

/**
 * Created by hamza on 09-Jul-18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter implements OnListFragmentInteractionListener {
    Context context;
    String[] tabTitles={"Basic User","Premium User"};
    ArrayList<Fragment> fragments;



    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<>();
        fragments.add(new BasicUser());
        fragments.add(new PremiumUser());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public Fragment getFragment(int position)
    {
        return fragments.get(position);
    }


    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

    }
}
