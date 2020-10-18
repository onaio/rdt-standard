package io.ona.rdt.robolectric.shadow;

import com.google.gson.Gson;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.domain.jsonmapping.util.LocationTree;
import org.smartregister.sync.helper.LocationServiceHelper;

@Implements(LocationServiceHelper.class)
public class LocationServiceHelperShadow extends Shadow {

    public static final String MOCK_LOCATION_TREE_JSON = "{\"locationsHierarchy\":{\"map\":{\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"children\":{\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 1\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 1\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"},\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 2\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 2\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"},\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 3\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 3\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"}},\"id\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Division 1\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Division 1\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"}}},\"parentChildren\":{\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\":[\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\"]}}}";

    @Implementation
    public LocationTree getLocationHierarchy(String parentLocationId) {
        return new Gson().fromJson(MOCK_LOCATION_TREE_JSON, LocationTree.class);
    }
}
