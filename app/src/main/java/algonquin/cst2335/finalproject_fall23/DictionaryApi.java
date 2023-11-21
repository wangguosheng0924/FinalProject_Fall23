package algonquin.cst2335.finalproject_fall23;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryApi {
    private static final String BASE_URL = "https://api.dictionaryapi.dev/";
    private static DictionaryApiService apiService;

    public static DictionaryApiService getService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(DictionaryApiService.class);
        }
        return apiService;
    }
}
