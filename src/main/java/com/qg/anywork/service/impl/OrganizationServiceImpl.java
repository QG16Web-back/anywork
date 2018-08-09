package com.qg.anywork.service.impl;

import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.OrganizationException;
import com.qg.anywork.model.Organization;
import com.qg.anywork.model.User;
import com.qg.anywork.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Create by ming on 18-8-5 下午1:47
 * 组织逻辑实现类
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public RequestResult<List<Organization>> search(String organizationName, int userId) {
        if (organizationName == null) {
            throw new OrganizationException("搜索的组织名为null");
        }
        List<Organization> organizations = organizationDao.getByKeyWords("%" + organizationName + "%");
        List<Organization> myOrganizations = organizationDao.getByUserId(userId);
        List<Integer> myOrganizationsId = new ArrayList<>();
        for (Organization myO : myOrganizations) {
            myOrganizationsId.add(myO.getOrganizationId());
        }
        for (Organization o : organizations) {
            o.setCount(organizationDao.getOrganizationCount(o.getOrganizationId()));
            if (myOrganizationsId.contains(o.getOrganizationId())) {
                o.setIsJoin(1);
            }
        }
        return new RequestResult<>(StatEnum.ORGAN_SEARCH_SUCCESS, organizations);
    }


    @Override
    public Organization getById(int organizationId) {
        return organizationDao.getById(organizationId);
    }

    @Override
    public synchronized RequestResult<Organization> join(int organizationId, long token, int userId) {
        if (organizationDao.isJoin(organizationId, userId) > 0) {
            throw new OrganizationException("用户已加入该组织");
        }
        Organization organization = organizationDao.getById(organizationId);
        if (organization == null) {
            throw new OrganizationException("组织不存在");
        }
        if (organization.getToken() != token) {
            throw new OrganizationException("口令错误");
        }
        organizationDao.joinOrganization(organizationId, userId);
        return new RequestResult<>(StatEnum.ORGAN_JOIN_SUCCESS, organization);
    }

    @Override
    public RequestResult<List<Organization>> searchByUserId(int userId) {
        List<Organization> organizations = organizationDao.getByUserId(userId);
        for (Organization o : organizations) {
            o.setIsJoin(1);
            o.setCount(organizationDao.getOrganizationCount(o.getOrganizationId()));
        }
        return new RequestResult<>(StatEnum.ORGAN_SEARCH_SUCCESS, organizations);
    }

    @Override
    public RequestResult exitOrganization(int organizationId, int userId) {
        if (organizationDao.isJoin(organizationId, userId) == 0) {
            throw new OrganizationException("用户未加入该组织");
        }
        int flag = organizationDao.exitOrganization(organizationId, userId);
        if (flag == 0) {
            return new RequestResult(0, "退出失败");
        }
        return new RequestResult(1, "退出成功");
    }

    @Override
    public RequestResult addOrganization(Organization organization) {
        int randomInt = new Random().nextInt(99999);
        organization.setToken(randomInt);
        int flag = organizationDao.addOrganization(organization);
        organizationDao.joinOrganization(organization.getOrganizationId(), organization.getTeacherId());
        if (flag == 1) {
            return new RequestResult(1, "创建组织成功");
        } else {
            return new RequestResult(0, "创建组织失败");
        }
    }


    @Override
    public RequestResult alterOrganization(Organization organization) {
        organizationDao.updateOrganization(organization);
        return new RequestResult(1, "修改组织成功");
    }

    /***
     * 删除组织
     * @param organizationId
     * @param userId
     * @return
     */
    @Override
    public RequestResult deleteOrganization(int organizationId, int userId) {
        Organization o = organizationDao.getById(organizationId);
        if (o == null) throw new OrganizationException("组织不存在");
        if (o.getTeacherId() != userId) throw new OrganizationException("没有删除权限");
        int flag = organizationDao.deleteOrganization(organizationId);

        if (flag == 1) return new RequestResult(1, "删除组织成功");
        else return new RequestResult(0, "删除组织失败");
    }

    /***
     * 获取我创建过的组织列表
     * @param userId
     * @return
     */
    @Override
    public RequestResult<List<Organization>> getMyOrganization(int userId) {
        List<Organization> organizations = organizationDao.getMyOrganization(userId);
        for (Organization o : organizations) {
            o.setCount(organizationDao.getOrganizationCount(o.getOrganizationId()));
        }
        return new RequestResult<>(1, "获取成功", organizations);
    }

    /**
     * 获取组织下的成员列表
     *
     * @param organizationId
     * @return
     */
    @Override
    public RequestResult<List<User>> getOrganizationPeople(int organizationId) {
        if (organizationDao.getById(organizationId) == null) throw new OrganizationException("该组织不存在");
        return new RequestResult<>(1, "获取成功", organizationDao.getOrganizationPeople(organizationId));
    }

}
