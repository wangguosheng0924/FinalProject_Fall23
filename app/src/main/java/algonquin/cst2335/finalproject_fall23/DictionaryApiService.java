package algonquin.cst2335.finalproject_fall23;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {
    @GET("api/v2/entries/en/{term}")
    Call<List<Definition>> getDefinitions(@Path("term") String term);
}
