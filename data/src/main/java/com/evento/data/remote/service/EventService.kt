package com.evento.data.remote.service

import com.evento.data.remote.responsebody.BaseResponseDto
import com.evento.data.remote.responsebody.EventDto
import com.evento.data.remote.responsebody.TimeSlotDto
import com.evento.domain.requestbody.CreateEvent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventService {

    @GET("events")
    suspend fun getBookedEvents(): Response<BaseResponseDto<List<EventDto>>>

    @GET("slots/available")
    suspend fun getAvailableSlots(): Response<BaseResponseDto<List<TimeSlotDto>>>

    @POST("events")
    suspend fun createEvent(@Body createEvent: CreateEvent): Response<BaseResponseDto<EventDto>>
}