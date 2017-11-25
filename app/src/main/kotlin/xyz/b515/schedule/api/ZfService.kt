package xyz.b515.schedule.api

import io.reactivex.Flowable
import retrofit2.http.*

interface ZfService {
    @FormUrlEncoded
    @POST("/default_vsso.aspx")
    fun login(@FieldMap map: Map<String, String>): Flowable<String>

    @GET("/xskbcx.aspx")
    fun getSchedule(@QueryMap map: Map<String, String>): Flowable<String>
}
