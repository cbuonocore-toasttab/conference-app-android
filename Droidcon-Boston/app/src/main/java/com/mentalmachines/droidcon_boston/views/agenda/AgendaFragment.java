package com.mentalmachines.droidcon_boston.views.agenda;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.DataManager;
import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;
import com.mentalmachines.droidcon_boston.views.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mentalmachines.droidcon_boston.services.MvpServiceFactory.makeMvpStarterService;

/**
 * Created by jinn on 3/11/17.
 */

public class AgendaFragment extends BaseFragment implements AgendaContract.View {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    ScheduleAdapter adapter;
    AgendaPresenter presenter;
    DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public int getLayout() {
        return R.layout.agenda_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataManager = new DataManager(makeMvpStarterService());
        presenter = new AgendaPresenter(dataManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case ScheduleAdapter.TYPE_GENERAL:
                        return 1;
                    case ScheduleAdapter.TYPE_SINGLE_ITEM:
                        return 1;
                    case ScheduleAdapter.TYPE_DOUBLE_ITEM:
                        return 2;
                    case ScheduleAdapter.TYPE_TRIPLE_ITEM:
                        return 3;
                    default:
                        return -1;
                }
            }
        });
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getSchedule();
        });

        presenter.getSchedule();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showSchedule(List<DroidconSchedule> schedule) {
        adapter.setSchedule(schedule);
        adapter.notifyDataSetChanged();

        recycler.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(boolean progress) {

    }

    @Override
    public void showError(Throwable throwable) {

    }
}