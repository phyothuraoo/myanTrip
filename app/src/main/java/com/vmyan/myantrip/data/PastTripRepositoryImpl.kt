package com.vmyan.myantrip.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.orhanobut.hawk.Hawk
import com.vmyan.myantrip.model.Trip
import com.vmyan.myantrip.utils.Resource
import kotlinx.coroutines.tasks.await

class PastTripRepositoryImpl : PastTripRepository {
    override suspend fun getPastTripList() : Resource<MutableList<Trip>> {
        val tripList = mutableListOf<Trip>()

        val resultList = FirebaseFirestore.getInstance()
            .collection("TripList")
            .whereArrayContains("userId", Hawk.get("user_id"))
            .get()
            .await()

        for (doc in resultList){
            val tripId = doc.id
            val tripImg = doc.getString("tripImg")
            val tripStartDate = doc.getTimestamp("tripStartDate")
            val tripEndDate = doc.getTimestamp("tripEndDate")
            val tripType = doc.getString("tripType")
            val tripName = doc.getString("tripName")
            val tripDestination = doc.getString("tripDestination")
            val tripDesc = doc.getString("tripDesc")
            val tripCost = doc.getDouble("tripCost")
            val userId = doc.get("userId") as ArrayList<String>

            if (tripEndDate!! < Timestamp.now()){
                tripList.add(Trip(tripId,tripImg!!,tripStartDate!!,tripEndDate,tripType!!,tripName!!,tripDestination!!,tripDesc!!,tripCost!!.toInt(),
                    userId))
            }
        }

        return Resource.Success(decList(tripList))
    }

    private fun decList(list: MutableList<Trip>): MutableList<Trip>{
        list.sortWith { p0, p1 ->
            var res = -1
            if (p0!!.tripStartDate < p1!!.tripStartDate) {
                res = 1
            }
            res
        }

        return list
    }

}