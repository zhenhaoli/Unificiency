package lmu.de.unificiencyandroid.rest;

import lmu.de.unificiencyandroid.domain.Building;
import lmu.de.unificiencyandroid.domain.LMUBuildings;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

public interface UnificiencyAPI {
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Call<LMUBuildings> loadQuestions(@Query("tagged") String tags);
}
