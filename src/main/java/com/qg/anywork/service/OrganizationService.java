package com.qg.anywork.service;

import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.model.Organization;
import com.qg.anywork.model.User;

import java.util.List;

/**
 * @author logan
 * @date 2017/7/11
 */
public interface OrganizationService {

    /***
     * 根据组织关键字模糊查找
     *
     * @param organizationName 组织名
     * @param userId 用户ID
     * @return Organization List
     */
    RequestResult<List<Organization>> search(String organizationName, int userId);


    Organization getById(int organizationId);

    /***
     * 加入组织
     *
     * @param organizationId 组织ID
     * @param token 口令
     * @param userId 用户ID
     * @return request result
     */
    RequestResult<Organization> join(int organizationId, long token, int userId);

    /***
     * 获取我的组织列表
     * @param userId
     * @return
     */
    RequestResult<List<Organization>> searchByUserId(int userId);

    /***
     * 退出组织
     * @param organizationId
     * @param userId
     * @return
     */
    RequestResult exitOrganization(int organizationId, int userId);

    /***
     * 创建组织
     * @return
     */
    RequestResult addOrganization(Organization organization);

    /***
     * 修改组织
     * @return
     */
    RequestResult alterOrganization(Organization organization);

    /***
     * 删除组织
     * @param organizationId
     * @param userId
     * @return
     */
    RequestResult deleteOrganization(int organizationId, int userId);

    /***
     * 获取我创建过的组织列表
     * @param userId
     * @return
     */
    RequestResult<List<Organization>> getMyOrganization(int userId);

    /**
     * 获取组织下的成员列表
     *
     * @param organizationId
     * @return
     */
    RequestResult<List<User>> getOrganizationPeople(int organizationId);
}
