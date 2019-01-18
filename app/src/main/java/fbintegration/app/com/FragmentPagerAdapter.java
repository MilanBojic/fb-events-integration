package fbintegration.app.com;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public FragmentPagerAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return new fbintegration.app.com.SessionDataFragment();
            case 1:
                return new fbintegration.app.com.SecondFragment();
            default:
                return new FragmentEventDetails();
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(final Object object) {
        return super.getItemPosition(object);
    }
}
