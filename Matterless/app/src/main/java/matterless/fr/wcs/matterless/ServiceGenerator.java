package matterless.fr.wcs.matterless;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = "https://chat.wildcodeschool.fr/api/v3/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    public static final Retrofit RETROFIT =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

    /*public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                RETROFIT = builder.build();
            }
        }

        return RETROFIT.create(serviceClass);
    }*/

    
}
