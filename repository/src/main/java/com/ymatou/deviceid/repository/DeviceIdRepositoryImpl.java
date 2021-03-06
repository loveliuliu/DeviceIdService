package com.ymatou.deviceid.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ymatou.deviceid.facade.model.vo.DeviceInfo;


/**
 * Created by liangzhonghua on 2016/9/22.
 */

@Repository("deviceIdRepository")
public class DeviceIdRepositoryImpl implements DeviceIdRepository {

    private final String CollectionName = "deviceId";

    // 取出最新的DeviceId
    private final int BY_ACTIVETIME_DESC = 1;

    // 设备验证通过标识
    private final int MD5VerifiedOK = 2;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(HashMap<String, Object> deviceIdInfo) {
        mongoTemplate.insert(deviceIdInfo, CollectionName);
    }



    @Override
    public void save(DeviceInfo deviceInfo) {
        mongoTemplate.save(deviceInfo, CollectionName);
    }

    @Override
    public DeviceInfo getDeviceInfo(String deviceId, int type) {
        Criteria criteria;

        // 如果deviceId转成小写后与原值不同，则采用IN的方式进行兼容性处理
        String deviceIdLower = deviceId.toLowerCase();
        if (!deviceIdLower.equals(deviceId)) {
            List<String> deviceIdList = new ArrayList<>();
            deviceIdList.add(deviceId);
            deviceIdList.add(deviceIdLower);

            criteria = Criteria.where("deviceid").in(deviceIdList);
        } else {
            criteria = Criteria.where("deviceid").is(deviceId);
        }

        Query query;
        if (type == BY_ACTIVETIME_DESC) {
            query = new Query(criteria).with(new Sort(Sort.Direction.DESC, "activeTime"));
        } else {
            query = new Query(criteria).with(new Sort(Sort.Direction.ASC, "activeTime"));
        }
        return mongoTemplate.findOne(query, DeviceInfo.class, CollectionName);
    }

    @Override
    public DeviceInfo getDeviceInfoByDid(String did, int type) {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("did").is(did));

        Query query;
        if (type == BY_ACTIVETIME_DESC) {
            query = new Query(criteria).with(new Sort(Sort.Direction.DESC, "activeTime"));
        } else {
            query = new Query(criteria).with(new Sort(Sort.Direction.ASC, "activeTime"));
        }
        return mongoTemplate.findOne(query, DeviceInfo.class, CollectionName);
    }

    /**
     * 获取到已经验证的设备号信息
     * 
     * @param deviceId
     * @param did
     * @return
     */
    @Override
    public DeviceInfo getValidateDeviceInfo(String deviceId, String did) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("deviceid").is(deviceId),
                Criteria.where("did").is(did),
                Criteria.where("signVerified").is(MD5VerifiedOK));

        Query query = new Query(criteria);

        return mongoTemplate.findOne(query, DeviceInfo.class, CollectionName);
    }

    @Override
    public DeviceInfo getFirstDeviceInfo(String deviceId, String did) {

        Criteria criteria = new Criteria();

        if ("00000000-0000-0000-0000-000000000000".equals(did) || "000000000000000".equals(did)) {
            criteria = Criteria.where("deviceid").is(deviceId);
        } else {
            criteria.orOperator(Criteria.where("deviceid").is(deviceId), Criteria.where("did").is(did));
        }

        Query query = new Query(criteria).with(new Sort(Sort.Direction.ASC, "activeTime"));
        return mongoTemplate.findOne(query, DeviceInfo.class, CollectionName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.deviceid.repository.DeviceIdRepository#getDeviceInfoList(int, int, int)
     */
    @Override
    public List<DeviceInfo> getDeviceInfoList(int userId, int type, int limit) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("userid").is(userId));

        Query query;
        if (type == BY_ACTIVETIME_DESC) {
            query = new Query(criteria).with(new Sort(Sort.Direction.DESC, "activeTime"));
        } else {
            query = new Query(criteria).with(new Sort(Sort.Direction.ASC, "activeTime"));
        }

        query.limit(limit);

        return mongoTemplate.find(query, DeviceInfo.class, CollectionName);
    }

    @Override
    public List<DeviceInfo> getDeviceInfoList(String deviceId, int type, int limit) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("deviceid").is(deviceId),
                Criteria.where("signVerified").is(MD5VerifiedOK));

        Query query;
        if (type == BY_ACTIVETIME_DESC) {
            query = new Query(criteria).with(new Sort(Sort.Direction.DESC, "activeTime"));
        } else {
            query = new Query(criteria).with(new Sort(Sort.Direction.ASC, "activeTime"));
        }
        query.limit(limit);

        return mongoTemplate.find(query, DeviceInfo.class, CollectionName);
    }
}
