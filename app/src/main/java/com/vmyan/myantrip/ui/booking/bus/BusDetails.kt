package com.vmyan.myantrip.ui.booking.bus

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vmyan.myantrip.R
import kotlinx.android.synthetic.main.fragment_bus_details.view.*


class BusDetails : Fragment() ,View.OnClickListener{

    private var mListener: BusDetails.OnBusDetailsListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_bus_details, container, false)
            view.card_SelectedBusContinue.setOnClickListener(this)
        return view
    }
    interface OnBusDetailsListener {
        //void onFragmentInteraction(Uri uri);
        fun onNextPressed(fragment: Fragment?)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is BusDetails.OnBusDetailsListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): BusDetails {
            return BusDetails()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            R.id.card_SelectedBusContinue -> {
                if (mListener != null) {
                    mListener!!.onNextPressed(this)
                }

            }
        }

    }
}