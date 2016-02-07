package Adapter; /**
 * Created by Prathik1 on 1/21/16.
 */
import silentassassins.locatemetemp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prathik1 on 1/21/16.
 */
public class Friends {
    String friendName;
    int photoId;
    String objectId;

    public Friends(String name, int photoId) {
        this.friendName = name;
        this.photoId = photoId;
    }
    public Friends(String name, int photoId,String oid) {
        this.friendName = name;
        this.photoId = photoId;
        this.objectId= oid;
    }
    public String getObjectId(){
        return this.objectId;
    }


}