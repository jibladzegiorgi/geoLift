package ge.idevelopers.Lifti.app.connections;

import ge.idevelopers.Lifti.app.SendClas;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("http://soso.ge/lift/home/android/")
    Call<SendClas> send(@Body SendClas goObject);


}