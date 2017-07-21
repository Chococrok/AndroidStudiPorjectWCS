package matterless.fr.wcs.matterless;

import java.lang.reflect.Array;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by apprenti on 18/03/17.
 */

public interface MattermostService {
    @POST("users/login")
    Call<Void> basicLogin(@Body UsersLogin usersLogin);

    @GET("users/me")
    Call<UserProfile> getUser(@Header("Authorization") String token);

    @GET
    Call<ResponseBody> getProfilePicture(@Header("Authorization") String token, @Url String url);

    @POST("teams/3zch5boyc3ds5kgiyqidmucjbc/channels/bfsnn43zfpne3m877s66a454ih/posts/create")
    Call<Post> sendPost(@Header("Authorization") String token, @Body Post post);

    @GET("teams/3zch5boyc3ds5kgiyqidmucjbc/channels/")
    Call<ChannelRequest> getChannels(@Header("Authorization") String token);

}
