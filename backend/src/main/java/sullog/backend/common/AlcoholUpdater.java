package sullog.backend.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sullog.backend.alcohol.entity.Alcohol;
import sullog.backend.alcohol.mapper.AlcoholMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlcoholUpdater {

    @Autowired
    private AlcoholMapper alcoholMapper;

//    @PostConstruct
    /** 위도 경도 값 바꾸는 로직 */
    public void switchLatAndLng() {

        /** DB에서 모든 alcohol 값 select */
        List<Alcohol> alcoholList = alcoholMapper.selectAllAlcoholList();
        System.out.println("<기존 알콜>");
        for (Alcohol alcohol : alcoholList) {
            System.out.println(alcohol.getAlcoholId() + " " + alcohol.getProductionLatitude() + ", " + alcohol.getProductionLongitude());
        }
        System.out.println();

        /** 매 alcohol마다 위도 <-> 경도 스위칭 후 update */
        alcoholList.forEach(alcohol -> {
            Alcohol newAlcohol = Alcohol.builder()
                    .alcoholId(alcohol.getAlcoholId())
                    .brandId(alcohol.getBrandId())
                    .name(alcohol.getName())
                    .alcoholType(alcohol.getAlcoholType())
                    .alcoholPercent(alcohol.getAlcoholPercent())
                    .productionLocation(alcohol.getProductionLocation())
                    .productionLatitude(alcohol.getProductionLongitude())
                    .productionLongitude(alcohol.getProductionLatitude())
                    .alcoholTag(alcohol.getAlcoholTag())
                    .build();

            alcoholMapper.updateAlcohol(newAlcohol);
        });

        /** 작업 완료 후 확인차 콘솔 출력*/
        alcoholList = alcoholMapper.selectAllAlcoholList();
        System.out.println("<새 알콜>");
        for (Alcohol alcohol : alcoholList) {
            System.out.println(alcohol.getAlcoholId() + " " + alcohol.getProductionLatitude() + ", " + alcohol.getProductionLongitude());
        }
    }

//    @PostConstruct
    /** json을 읽어 주종 카테고리(type)를 넣어주는 로직*/
    public void alocholTypeUpdater() throws IOException {

        /** json file to String */
        String fileName = "./전통주.json";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10];
        while (reader.read(buffer) != -1) {
            stringBuilder.append(new String(buffer));
            buffer = new char[10];
        }
        reader.close();

//        System.out.println("stringBuilder = " + stringBuilder);

        /** String to Map<String, Object> */
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Map map = gson.fromJson(stringBuilder.toString().trim(), Map.class);

        /** Map안에서 값 하나하나 읽어서 type 넣어 업데이트 */
        ArrayList<LinkedTreeMap<String, Object>> dataList = (ArrayList<LinkedTreeMap<String, Object>>) map.get("data");
        for (LinkedTreeMap<String, Object> dataMap : dataList) {
            String name = (String) dataMap.get("name");
            String type = (String) dataMap.get("type");

            LinkedTreeMap<String, String> addressMap = (LinkedTreeMap<String, String>) dataMap.get("address");
            double lat = Double.parseDouble(addressMap.get("lat"));
            double lng = Double.parseDouble(addressMap.get("lng"));

            /** 이름, 위도, 경도 값으로 DB에 있는 alcohol 오브젝트 찾아옴(이름 중복 가능) */
            List<Alcohol> alcoholList = alcoholMapper.selectFromNameAndLocationInfo(name, lat, lng);
            for (Alcohol alcohol : alcoholList) {
                System.out.println("alcohol = " + alcohol);
                Alcohol newAlcohol = Alcohol.builder()
                        .alcoholId(alcohol.getAlcoholId())
                        .brandId(alcohol.getBrandId())
                        .name(alcohol.getName())
                        .alcoholType(type)
                        .alcoholPercent(alcohol.getAlcoholPercent())
                        .productionLocation(alcohol.getProductionLocation())
                        .productionLatitude(alcohol.getProductionLatitude())
                        .productionLongitude(alcohol.getProductionLongitude())
                        .alcoholTag(alcohol.getAlcoholTag())
                        .build();
                alcoholMapper.updateAlcohol(newAlcohol);
//                System.out.println("newAlcohol = " + newAlcohol);

            }
        }
    }

}
