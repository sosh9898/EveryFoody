package dct.com.everyfoody.ui.detail.edit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by jyoung on 2017. 8. 1..
 */

public class EditPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> mainPager = new SparseArray<Fragment>();

    public EditPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NormalEditFragment.getInstance();
            case 1:
                return MenuEditFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mainPager.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mainPager.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mainPager.get(position);
    }
}
