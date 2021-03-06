package com.ymatou.deviceid.test.facade.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.deviceid.facade.model.req.UpdateDeviceIdReq;
import com.ymatou.deviceid.facade.model.resp.DeviceInfoResp;
import com.ymatou.deviceid.facade.model.resp.DeviceInfoSimple;
import com.ymatou.deviceid.facade.model.vo.DeviceInfo;
import com.ymatou.deviceid.facade.rest.BaseNetCompatibleResp;
import com.ymatou.deviceid.facade.rest.DeviceIdResource;
import com.ymatou.deviceid.repository.DeviceIdRepository;
import com.ymatou.deviceid.test.BaseTest;

/**
 * Created by liangzhonghua on 2016/9/26.
 */
public class DeviceIdFacadeTest extends BaseTest {

    @Autowired
    DeviceIdResource deviceIdResource;

    @Autowired
    DeviceIdRepository deviceIdRepository;

    @Test
    public void testSaveDeviceId() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();
        String did = UUID.randomUUID().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("deviceid", deviceId);
        map.put("did", did);
        map.put("userid", 10);
        map.put("activeTime", new Date());
        map.put("signVerified", 2);

        BaseNetCompatibleResp resp = deviceIdResource.saveDeviceId(map);

        System.out.println(resp);
        assertEquals(0, resp.getCode());


        Thread.sleep(100);

        resp = deviceIdResource.get(deviceId, 0);
        println(resp);

        assertEquals(0, resp.getCode());
        assertNotNull(resp.getData());

        BaseNetCompatibleResp resp2 = deviceIdResource.get(deviceId.toUpperCase(), 0);

        assertEquals(0, resp2.getCode());
        assertNotNull(resp2.getData());

        // 同样的设备号再做一次保存，不插入数据库，返回成功
        resp = deviceIdResource.saveDeviceId(map);
        assertEquals(0, resp.getCode());
    }

    @Test
    public void testUpdateDeviceId() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deviceid", deviceId);
        map.put("userid", 10);
        map.put("clienttype", 1);
        map.put("acceptversion", "1.0.0");
        map.put("network", "wifi");
        map.put("appname", "buyer");
        map.put("osversion", "ios10.1");
        map.put("signVerified", 2);

        BaseNetCompatibleResp resp = deviceIdResource.saveDeviceId(map);
        assertEquals(0, resp.getCode());

        Thread.sleep(100);

        resp = deviceIdResource.get(deviceId, 1);
        assertEquals(0, resp.getCode());
        assertNotNull(resp.getData());

        DeviceInfoResp deviceInfoBefore = (DeviceInfoResp) resp.getData();
        assertNotNull(deviceInfoBefore);
        assertEquals(10, deviceInfoBefore.getUserId());


        UpdateDeviceIdReq updateDeviceIdReq = new UpdateDeviceIdReq();
        updateDeviceIdReq.setDeviceid(deviceId);
        updateDeviceIdReq.setDid(UUID.randomUUID().toString());
        updateDeviceIdReq.setUserid(20);
        BaseNetCompatibleResp updateResp = deviceIdResource.updateDeviceId(updateDeviceIdReq);

        assertNotNull(updateResp);
        assertEquals(0, updateResp.getCode());

        Thread.sleep(300);

        DeviceInfo deviceInfoAfter = deviceIdRepository.getDeviceInfoByDid(updateDeviceIdReq.getDid(), 1);
        assertNotNull(deviceInfoAfter);
        assertEquals(2, deviceInfoAfter.getSignVerified());
        assertEquals(20, deviceInfoAfter.getUserid());
        assertEquals("ios10.1", deviceInfoAfter.getOsversion());
    }

    @Test
    public void testUpdateDeviceId1() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deviceid", deviceId);
        map.put("userid", 0);
        map.put("clienttype", 1);
        map.put("acceptversion", "1.0.0");
        map.put("network", "wifi");
        map.put("appname", "buyer");
        map.put("osversion", "ios10.0");

        BaseNetCompatibleResp resp = deviceIdResource.saveDeviceId(map);
        assertEquals(0, resp.getCode());

        Thread.sleep(100);

        resp = deviceIdResource.get(deviceId, 1);
        assertEquals(0, resp.getCode());
        assertNotNull(resp.getData());

        DeviceInfoResp deviceInfoBefore = (DeviceInfoResp) resp.getData();
        assertNotNull(deviceInfoBefore);
        assertEquals(0, deviceInfoBefore.getUserId());

        UpdateDeviceIdReq updateDeviceIdReq = new UpdateDeviceIdReq();
        updateDeviceIdReq.setDeviceid(deviceId);
        updateDeviceIdReq.setDid(UUID.randomUUID().toString());
        updateDeviceIdReq.setUserid(20);
        BaseNetCompatibleResp updateResp = deviceIdResource.updateDeviceId(updateDeviceIdReq);

        assertNotNull(updateResp);
        assertEquals(0, updateResp.getCode());

        Thread.sleep(100);

        BaseNetCompatibleResp respAfter = deviceIdResource.get(deviceId, 1);
        assertEquals(0, respAfter.getCode());
        assertNotNull(respAfter.getData());

        DeviceInfoResp deviceInfoAfter = (DeviceInfoResp) respAfter.getData();
        assertNotNull(deviceInfoAfter);
        assertEquals(1, deviceInfoAfter.getSignVerified());
        assertEquals(20, deviceInfoAfter.getUserId());
        assertEquals(deviceInfoBefore.getActiveTime(), deviceInfoAfter.getActiveTime());
    }

    @Test
    public void testUpdateDeviceId2() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deviceid", deviceId);
        map.put("userid", 20);
        map.put("clienttype", 1);
        map.put("acceptversion", "1.0.0");
        map.put("network", "wifi");
        map.put("appname", "buyer");
        map.put("osversion", "ios10.0");

        BaseNetCompatibleResp resp = deviceIdResource.saveDeviceId(map);
        assertEquals(0, resp.getCode());

        Thread.sleep(100);

        resp = deviceIdResource.get(deviceId, 1);
        assertEquals(0, resp.getCode());
        assertNotNull(resp.getData());

        DeviceInfoResp deviceInfoBefore = (DeviceInfoResp) resp.getData();
        assertNotNull(deviceInfoBefore);
        assertEquals(20, deviceInfoBefore.getUserId());


        UpdateDeviceIdReq updateDeviceIdReq = new UpdateDeviceIdReq();
        updateDeviceIdReq.setDeviceid(deviceId);
        updateDeviceIdReq.setDid(UUID.randomUUID().toString());
        updateDeviceIdReq.setUserid(20);
        BaseNetCompatibleResp updateResp = deviceIdResource.updateDeviceId(updateDeviceIdReq);

        assertNotNull(updateResp);
        assertEquals(0, updateResp.getCode());

        Thread.sleep(100);

        BaseNetCompatibleResp respAfter = deviceIdResource.get(deviceId, 1);
        assertEquals(0, respAfter.getCode());
        assertNotNull(respAfter.getData());

        DeviceInfoResp deviceInfoAfter = (DeviceInfoResp) respAfter.getData();
        assertNotNull(deviceInfoAfter);
        assertEquals(1, deviceInfoAfter.getSignVerified());
        assertEquals(20, deviceInfoAfter.getUserId());
    }

    @Test
    public void testUpdateDeviceId3() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();

        UpdateDeviceIdReq updateDeviceIdReq = new UpdateDeviceIdReq();
        updateDeviceIdReq.setDeviceid(deviceId);
        updateDeviceIdReq.setDid(UUID.randomUUID().toString());
        updateDeviceIdReq.setUserid(20);
        BaseNetCompatibleResp updateResp = deviceIdResource.updateDeviceId(updateDeviceIdReq);

        assertNotNull(updateResp);
        assertEquals(0, updateResp.getCode());

        Thread.sleep(100);

        BaseNetCompatibleResp respAfter = deviceIdResource.get(deviceId, 1);
        assertEquals(0, respAfter.getCode());
        assertNotNull(respAfter.getData());

        DeviceInfoResp deviceInfoAfter = (DeviceInfoResp) respAfter.getData();
        assertNotNull(deviceInfoAfter);
        assertNotNull(deviceInfoAfter.getActiveTime());
        assertEquals(1, deviceInfoAfter.getSignVerified());
        assertEquals(20, deviceInfoAfter.getUserId());
    }


    @Test
    public void testUpdateDeviceId4() throws InterruptedException {
        UpdateDeviceIdReq updateDeviceIdReq = new UpdateDeviceIdReq();
        updateDeviceIdReq.setDid(UUID.randomUUID().toString());
        updateDeviceIdReq.setUserid(20);
        BaseNetCompatibleResp updateResp = deviceIdResource.updateDeviceId(updateDeviceIdReq);

        assertNotNull(updateResp);
        assertEquals(100, updateResp.getCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetList() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();
        int userId = 10;

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deviceid", deviceId);
        map.put("userid", userId);
        map.put("activeTime", new Date());
        map.put("signVerified", 2);
        map.put("clienttype", 2);
        deviceIdRepository.insert(map);

        Thread.sleep(200);

        BaseNetCompatibleResp resp = deviceIdResource.getList(userId, 1, 1);
        assertEquals(0, resp.getCode());
        assertEquals(0, resp.getBcode());

        List<DeviceInfoSimple> deviceInfoList = (List<DeviceInfoSimple>) resp.getData();
        assertNotNull(deviceInfoList);
        assertEquals(deviceId, deviceInfoList.get(0).getDeviceid());
        assertEquals(2, deviceInfoList.get(0).getClienttype());

        resp = deviceIdResource.getList(userId, 1, 2);
        deviceInfoList = (List<DeviceInfoSimple>) resp.getData();
        assertNotNull(deviceInfoList);
        assertEquals(2, deviceInfoList.size());

        resp = deviceIdResource.getList(-1, 1, 2);
        assertEquals(100, resp.getCode());

        resp = deviceIdResource.getList(userId, 10, 2);
        assertEquals(100, resp.getCode());

        resp = deviceIdResource.getList(userId, 1, 21);
        assertEquals(100, resp.getCode());

        resp = deviceIdResource.getList(userId, 1, -1);
        assertEquals(100, resp.getCode());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUserIdList() throws InterruptedException {
        String deviceId = UUID.randomUUID().toString();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deviceid", deviceId);
        map.put("userid", 10);
        map.put("activeTime", new Date());
        map.put("signVerified", 2);
        map.put("clienttype", 2);
        deviceIdRepository.insert(map);

        map.put("userid", 20);
        map.put("activeTime", new Date());
        deviceIdRepository.insert(map);

        // 模拟重复数据
        map.put("userid", 20);
        map.put("activeTime", new Date());
        deviceIdRepository.insert(map);

        map.put("userid", 30);
        map.put("activeTime", new Date());
        map.put("signVerified", 1); // 这条设备号信息没有验证通过，不会被查出来
        deviceIdRepository.insert(map);

        Thread.sleep(100);

        BaseNetCompatibleResp resp = deviceIdResource.getUserIdList(deviceId, 1, 10);
        assertEquals(0, resp.getCode());
        assertEquals(0, resp.getBcode());

        List<Integer> userList = (List<Integer>) resp.getData();
        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals(20, userList.get(0).intValue());
        assertEquals(10, userList.get(1).intValue());

        resp = deviceIdResource.getUserIdList(deviceId, 0, 10);
        assertEquals(0, resp.getCode());
        assertEquals(0, resp.getBcode());

        userList = (List<Integer>) resp.getData();
        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals(10, userList.get(0).intValue());
        assertEquals(20, userList.get(1).intValue());

        resp = deviceIdResource.getUserIdList(deviceId + "not exist", 0, 10);
        assertEquals(102, resp.getCode());
        assertEquals(0, resp.getBcode());

        resp = deviceIdResource.getUserIdList(deviceId + "not exist", 3, 10);
        assertEquals(100, resp.getCode());
        assertEquals("type必须是0或者1", resp.getMsg());

        resp = deviceIdResource.getUserIdList(deviceId + "not exist", 1, 30);
        assertEquals(100, resp.getCode());
        assertEquals("limit必须是1到20之间的数", resp.getMsg());
    }
}
