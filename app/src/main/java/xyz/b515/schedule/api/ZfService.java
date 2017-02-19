package xyz.b515.schedule.api;

        import java.util.Map;

        import io.reactivex.Observable;
        import retrofit2.http.FieldMap;
        import retrofit2.http.FormUrlEncoded;
        import retrofit2.http.GET;
        import retrofit2.http.POST;
        import retrofit2.http.QueryMap;

public interface ZfService {
    @FormUrlEncoded
    @POST("/default_vsso.aspx")
    Observable<String> login(@FieldMap Map<String, String> map);

    @GET("/xskbcx.aspx")
    Observable<String> getSchedule(@QueryMap Map<String, String> map);
}
