package mohamed.com.marrychat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mohamed on 07/07/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return  requestsFragment;
            case 1:
                ChatsFragment chatsFragment =new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment freindsFragment=new FriendsFragment();
                return  freindsFragment;

            default: return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
            return "Accueil" ;
            case  1:
                return "CHATS";
            case 2:
                return "FREIND";
            default:
                return null;
        }

    }
}
