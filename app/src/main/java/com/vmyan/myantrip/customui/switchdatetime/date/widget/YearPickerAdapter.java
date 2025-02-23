package com.vmyan.myantrip.customui.switchdatetime.date.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vmyan.myantrip.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for manage elements of ListPickerYearView
 * @author JJamet
 */
class YearPickerAdapter extends RecyclerView.Adapter<YearPickerAdapter.TextIndicatorViewHolder> {

    private static final int LIST_ITEM_TYPE_STANDARD = 0;
    private static final int LIST_ITEM_TYPE_INDICATOR = 1;

    public static final int UNDEFINED = -1;

    private SimpleDateFormat yearFormat;
    private Calendar calendar;

    private List<Integer> listYears;
    private Integer selectedYear;
    private int positionSelectedYear;

    private OnClickYearListener onClickYearListener;

    /**
     * Initialize adapter with list of years and element selected
     */
    YearPickerAdapter() {
        this.listYears = new ArrayList<>();
        this.selectedYear = UNDEFINED;
        this.yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        this.calendar = Calendar.getInstance();
    }

    @Override
    public long getItemId(int i) {
        return listYears.get(i);
    }

    @Override
    public int getItemCount() {
        return listYears.size();
    }

    @Override
    public TextIndicatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case LIST_ITEM_TYPE_STANDARD:
                return new TextIndicatorViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.year_text, parent, false));
            case LIST_ITEM_TYPE_INDICATOR:
                return new TextIndicatorViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.year_text_indicator, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(TextIndicatorViewHolder holder, int position) {
        Integer currentYear = listYears.get(position);

        calendar.set(Calendar.YEAR, currentYear);
        holder.textView.setText(yearFormat.format(calendar.getTime()));

        if(onClickYearListener != null)
            holder.container.setOnClickListener(new BufferYearClickListener(currentYear, position));
    }

    @Override
    public int getItemViewType(int position) {
        if(listYears.get(position).equals(selectedYear))
            return LIST_ITEM_TYPE_INDICATOR;
        else
            return LIST_ITEM_TYPE_STANDARD;
    }

    /**
     * Get the list of years
     * @return years
     */
    public List<Integer> getListYears() {
        return listYears;
    }

    /**
     * Assign the list of years, replace current list
     * @param listYears list of years
     */
    public void setListYears(List<Integer> listYears) {
        this.listYears = listYears;
    }

    /**
     * Get the current selected year or value of UNDEFINED if undefined
     */
    public int getSelectedYear() {
        return selectedYear;
    }

    /**
     * Assign the current selected year
     * @param selectedYear year selected
     */
    public void setSelectedYear(int selectedYear) throws SelectYearException {
        if(!listYears.contains(selectedYear))
            throw new SelectYearException(selectedYear, listYears);
        this.selectedYear = selectedYear;
        this.positionSelectedYear = listYears.indexOf(selectedYear);
    }

    /**
     * Get position of selected year or value of UNDEFINED if undefined
     */
    public int getPositionSelectedYear() {
        return positionSelectedYear;
    }

    /**
     * Get the listener called when the year is clicked
     */
    public OnClickYearListener getOnClickYearListener() {
        return onClickYearListener;
    }

    /**
     * Set the listener called when the year is clicked
     * @param onClickYearListener year listener
     */
    public void setOnClickYearListener(OnClickYearListener onClickYearListener) {
        this.onClickYearListener = onClickYearListener;
    }

    /**
     * Listener when a click on Year item is performed
     */
    public interface OnClickYearListener {
        /**
         * Called on click
         * @param view Current view clicked
         * @param year Year of current item clicked
         * @param position Position of item clicked
         */
        void onItemYearClick(View view, Integer year, int position);
    }

    private class BufferYearClickListener implements View.OnClickListener {

        private Integer year;
        private int position;

        BufferYearClickListener(Integer yearClicked, int position) {
            this.year = yearClicked;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            onClickYearListener.onItemYearClick(view, year, position);
        }
    }

    /**
     * Holder for TextIndicatorView
     */
    class TextIndicatorViewHolder extends RecyclerView.ViewHolder {
        /**
         * Views of year
         */
        private ViewGroup container;
        private TextView textView;

        TextIndicatorViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.year_element_container);
            textView = itemView.findViewById(R.id.year_textView);
        }
    }

    /**
     * Exception class for year selected
     */
    class SelectYearException extends Exception {
        SelectYearException(Integer yearSelected, List<Integer> listYears) {
            super("Year selected " + yearSelected + " must be in list of years : " + listYears);
        }
    }
}
