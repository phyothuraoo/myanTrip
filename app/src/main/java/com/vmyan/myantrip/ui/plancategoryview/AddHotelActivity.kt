package com.vmyan.myantrip.ui.plancategoryview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.Timestamp
import com.vmyan.myantrip.R
import com.vmyan.myantrip.ui.TripPlanActivity
import com.vmyan.myantrip.ui.viewmodel.AddPlanViewModel
import com.vmyan.myantrip.utils.DateRange
import com.vmyan.myantrip.utils.Resource
import kotlinx.android.synthetic.main.activity_add_hotel.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AddHotelActivity : AppCompatActivity() {

    private val viewModel: AddPlanViewModel by inject()

    private lateinit var tripStartDate: Timestamp
    private lateinit var tripEndDate: Timestamp
    private lateinit var tripId: String
    private var checkInDate: Timestamp? = null
    private var checkOutDate: Timestamp? = null
    private var checkInTime: String? = null
    private var checkOutTime: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hotel)

        tripId = intent.getStringExtra("tripId")!!
        tripStartDate = intent.getParcelableExtra("tripStartDate")!!
        tripEndDate = intent.getParcelableExtra("tripEndDate")!!
        val planName = intent.getStringExtra("planName")
        val planImg = intent.getStringExtra("planImg")
        titleText.text = "Add $planName"
        Glide.with(this).load(planImg).into(titleImg)

        checkindate.setOnClickListener {
            datePicker("in")
        }

        plan_addhotel_checkout.setOnClickListener {
            datePicker("out")
        }

        checkintime.setOnClickListener {
            pickTime("in")
        }

        plan_checkouttime_btn.setOnClickListener {
            pickTime("out")
        }

        var confirm = true

        if (yes.isChecked){
            confirm = true
        }else if (no.isChecked){
            confirm = false
        }

        addbtn.setOnClickListener {
            addPlan(
                tripId,
                hotelname.text.toString(),
                planImg!!,
                checkInDate!!,
                checkOutDate!!,
                checkInTime!!,
                checkOutTime!!,
                state.text.toString(),
                "",
                city.text.toString(),
                "",
                address.text.toString(),
                "",
                cost.text.toString().toInt(),
                confirm,
                "",
                "",
                "",
                planName!!
            )
        }

    }

    private fun addPlan(
        tripId: String,
        name: String,
        img: String,
        fromDate: Timestamp,
        toDate: Timestamp,
        fromTime: String,
        toTime: String,
        fromState: String,
        toState: String,
        fromCity: String,
        toCity: String,
        fromAddress: String,
        toAddress: String,
        estimationCost: Int,
        confirmation: Boolean,
        type: String,
        description: String,
        details: String,
        viewType : String
    ){
        viewModel.addPlan(
            tripId, name, img, fromDate, toDate, fromTime, toTime, fromState, toState, fromCity, toCity, fromAddress, toAddress, estimationCost, confirmation, type, description, details, viewType
        ).observe(this,{
            when(it){
                is Resource.Loading ->{

                }
                is Resource.Success ->{
                    val intent = Intent(this, TripPlanActivity::class.java)
                    intent.putExtra("tripId",tripId)
                    startActivity(intent)
                }
                is Resource.Failure ->{

                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun datePicker(status: String){
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setCalendarConstraints(
            DateRange.limitRange(
                SimpleDateFormat("dd-MM-yyyy").format(tripStartDate.toDate()),
                SimpleDateFormat("dd-MM-yyyy").format(tripEndDate.toDate()),
                "other"
                )!!.build())
        val picker = builder.build()
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            when(status){
                "in" -> {
                    checkindate.text = picker.headerText
                    checkInDate = Timestamp(Date(it))
                }
                "out" -> {
                    plan_addhotel_checkout.text = picker.headerText
                    checkOutDate = Timestamp(Date(it))
                }
            }
        }
    }

    private fun pickTime(status: String){
        val picker = MaterialTimePicker()
        picker.show(supportFragmentManager,"timepicker")
        picker.setListener {
            val result = "${it.hour}:${it.minute}"
            when(status){
                "in" -> {
                    checkintime.text = DateRange.setAMPM(it.hour,it.minute)
                    checkInTime = result
                }
                "out" -> {
                    plan_checkouttime_btn.text = DateRange.setAMPM(it.hour,it.minute)
                    checkOutTime = result
                }
            }
        }
    }

}