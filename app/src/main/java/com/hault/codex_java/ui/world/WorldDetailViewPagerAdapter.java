package com.hault.codex_java.ui.world;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.hault.codex_java.ui.character.CharacterListFragment;
import com.hault.codex_java.ui.event.EventListFragment;
import com.hault.codex_java.ui.location.LocationListFragment;

public class WorldDetailViewPagerAdapter extends FragmentStateAdapter {

    private final int worldId;

    public WorldDetailViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int worldId) {
        super(fragmentActivity);
        this.worldId = worldId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return CharacterListFragment.newInstance(worldId);
            case 1:
                return LocationListFragment.newInstance(worldId);
            case 2:
                return EventListFragment.newInstance(worldId);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}