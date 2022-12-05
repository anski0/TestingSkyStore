package sky.an;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;

public class AppTest {
    static HashMap<String, String> headerMap = new HashMap<String, String>();
    static HashMap<String, String> queryParamsMap = new HashMap<String, String>();
    static String url = "https://www.skystore.com/api/web/v2/catalog/assets/top/";

    String getJsonAsString(){
        return given().queryParams(queryParamsMap).headers(headerMap)
                .when().get(url)
                .then().assertThat().extract().asString();
    }

    @BeforeClass
    public static void setup(){
        headerMap.put("x-api-user-agent", "Web/5.69.0");
        headerMap.put("x-api-device-model", "Chrome");
        headerMap.put("x-api-device-manufacturer", "MAC OS");
        headerMap.put("x-api-key", "l_web_sparrow");

        queryParamsMap.put("uuid", "9fe6ace0-0ba3-43d4-8ce2-8b39331244e0");
        queryParamsMap.put("genre", "new-to-buy");
    }

    @Test
    @DisplayName("When accessing Sky Store API, return 200 status code")
    public void whenAccessingSkyStoreApiReturn200StatusCode(){
        given().queryParams(queryParamsMap).headers(headerMap)
                .when().get(url)
                .then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("When accessing sky store api, return not an empty string")
    public void whenAccessingSkyStoreApiReturnNotAnEmptyString(){
        String responseString = getJsonAsString();
        Assertions.assertEquals(false, responseString.isEmpty());
    }

    @Test
    @DisplayName("When getting sky store API, return the amount of assets")
    public void whenGettingSkyStoreApiReturnTheAmountOfAssets(){
        String responseString = getJsonAsString();
        DocumentContext documentContext = JsonPath.parse(responseString);
        List<String> listOfAssets = documentContext.read("$.content.assets");
        System.out.println("The amount of assets are: " + listOfAssets.size());
    }

    @Test
    @DisplayName("When getting sky store API, verify the title of the first asset")
    public void whenGettingSkyStoreApiVerifyTheTitleOfTheFirstAsset(){
        String responseString = getJsonAsString();
        DocumentContext documentContext = JsonPath.parse(responseString);
        String jsonAssetTitle = documentContext.read("$.content.assets[0].title");
        Assertions.assertEquals(".45", jsonAssetTitle);
    }

    @Test
    @DisplayName("When getting response from Sky Store API, verify that the year of the first asset is 2006")
    public void whenGettingResponseFromSkyStoreApiVerifyThatTheYearOfTheFirstAssetIs2006(){
        String responseString = getJsonAsString();
        DocumentContext documentContext = JsonPath.parse(responseString);
        int jsonAssetYear = documentContext.read("$.content.assets[0].year");
        Assertions.assertEquals(2006, jsonAssetYear);
    }

    @Test
    @DisplayName("When getting response from Sky Store API, verify that assetType is Programme")
    public void whenGettingResponseFromSkyStoreApiVerifyThatAssetTypeIsProgramme(){
        String responseString = getJsonAsString();
        DocumentContext documentContext = JsonPath.parse(responseString);
        String jsonAssetType = documentContext.read("$.content.assets[0].assetType");
        Assertions.assertEquals("Programme", jsonAssetType);
    }
}
