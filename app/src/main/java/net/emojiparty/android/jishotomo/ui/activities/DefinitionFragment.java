package net.emojiparty.android.jishotomo.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import net.emojiparty.android.jishotomo.BR;
import net.emojiparty.android.jishotomo.JishoTomoApp;
import net.emojiparty.android.jishotomo.R;
import net.emojiparty.android.jishotomo.analytics.AnalyticsLogger;
import net.emojiparty.android.jishotomo.data.models.EntryWithAllSenses;
import net.emojiparty.android.jishotomo.ui.adapters.DataBindingAdapter;
import net.emojiparty.android.jishotomo.ui.viewmodels.EntryViewModel;
import net.emojiparty.android.jishotomo.ui.viewmodels.EntryViewModelFactory;

public class DefinitionFragment extends Fragment {
    public static final String ENTRY_ID_EXTRA = "ENTRY_ID_EXTRA";
    public static final int ENTRY_EMPTY = -1;
    private View root;
    private AnalyticsLogger analyticsLogger;
    private DefinitionActivity mActivity;

    public static DefinitionFragment instance(int entryId) {
        DefinitionFragment fragment = new DefinitionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ENTRY_ID_EXTRA, entryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_definition, container, false);
        root = binding.getRoot();
        FragmentActivity activity = getActivity();
        binding.setLifecycleOwner(activity);
        setupViewModel(getArguments(), binding);
        analyticsLogger = ((JishoTomoApp) activity.getApplication()).getAnalyticsLogger();
        mActivity = (DefinitionActivity) getActivity();
        return root;
    }

    private void setupViewModel(Bundle bundle, ViewDataBinding binding) {
        RecyclerView sensesRecyclerView = root.findViewById(R.id.senses_rv);
        final DataBindingAdapter adapter = new DataBindingAdapter(R.layout.list_item_sense);
        sensesRecyclerView.setAdapter(adapter);
        int entryId = findEntryId(bundle);
        if (entryId != ENTRY_EMPTY) {
            EntryViewModel viewModel = ViewModelProviders.of(this,
                    new EntryViewModelFactory(getActivity().getApplication(), this, entryId))
                    .get(EntryViewModel.class);
            viewModel.entry.observe(this, (@Nullable EntryWithAllSenses entry) -> {
                if (entry != null) {
                    binding.setVariable(BR.presenter, entry);
                    adapter.setItems(entry.getSenses());
                    analyticsLogger.logViewItem(entry.entry.getId(), entry.getKanjiOrReading());
                    mActivity.currentWord = entry.getKanjiOrReading();
                }
            });
        } else {
            root.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        }
    }

    private int findEntryId(Bundle bundle) {
        if (bundle == null) {
            return ENTRY_EMPTY;
        }
        return bundle.getInt(ENTRY_ID_EXTRA, ENTRY_EMPTY);
    }
}
