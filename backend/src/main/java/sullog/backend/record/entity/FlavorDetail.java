package sullog.backend.record.entity;

import java.util.Arrays;
import java.util.List;

public class FlavorDetail {
    private FlavorTag majorTag;
    private String detailTag;

    public FlavorDetail() {
    }

    private FlavorDetail(FlavorTag majorTag, String detailTag) {
        this.majorTag = majorTag;
        this.detailTag = detailTag;
    }

    public static FlavorDetail of(String majorTag, String detailTag) {
        return new FlavorDetail(FlavorTag.valueOf(majorTag), detailTag);
    }

    public FlavorTag getMajorTag() {
        return majorTag;
    }

    public String getDetailTag() {
        return detailTag;
    }

    enum FlavorTag {

        FLOWER(Arrays.asList("CHRYSANTHEMUM", "PLUM_BLOSSOM", "ACACIA", "LOTUS", "ROSE")),
        FRUIT(Arrays.asList("CITRUS", "STRAWBERRY", "JAPANESE_PLUM", "MELON", "BANANA", "PEAR", "PEACH", "APPLE", "WILD_STRAWBERRY", "APRICOT", "YUZU", "PLUM", "CANTALOUPE", "PINEAPPLE")),
        GRAIN(Arrays.asList("POTATO", "FRESHLY_COOKED_RICE", "SWEET_POTATO", "GRAIN_POWDER", "NURUNGJI", "WHEAT", "RAW_RICE", "CORN")),
        NUT(Arrays.asList("PEANUT", "CHESTNUT", "ALMOND", "PINE_NUT")),
        SWEETENER(Arrays.asList("HONEY", "MALT_SYRUP", "BROWN_RICE_SYRUP", "CARAMEL")),
        DAIRY(Arrays.asList("BUTTER", "YOGURT", "MILK", "CHEESE"));

        private List<String> detailTagList;

        FlavorTag(List<String> detailTagList) {
            this.detailTagList = detailTagList;
        }
    }
}
