package com.ruoyi.project.system.user.service;

import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.domain.UserApp;
import com.ruoyi.project.system.user.domain.UserQrCode;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户 业务层
 * 
 * @author ruoyi
 */
public interface IUserService
{
    /**
     * 根据条件分页查询用户对象
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<User> selectUserList(User user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public User selectUserByLoginName(String userName);

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    public User selectUserByPhoneNumber(String phoneNumber);

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    public User selectUserByEmail(String email);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public User selectUserById(Long userId);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteUserByIds(String ids,HttpServletRequest request) throws Exception;

    /**
     * 保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(User user,HttpServletRequest request);

    /**
     * 保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(User user,HttpServletRequest request);

    /**
     * 修改用户详细信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserInfo(User user,HttpServletRequest request) throws Exception;

    public int updateUserInfo1(User user,String token) throws Exception;

    /**
     * 修改用户密码信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int resetUserPwd(User user,HttpServletRequest request);

    /**
     * 校验用户名称是否唯一
     * 
     * @param loginName 登录名称
     * @return 结果
     */
    public String checkLoginNameUnique(String loginName);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkEmailUnique(User user);

    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public String selectUserRoleGroup(Long userId);

    /**
     * 根据用户ID查询用户所属岗位组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public String selectUserPostGroup(Long userId);

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importUser(List<User> userList, Boolean isUpdateSupport,HttpServletRequest request);

    /**
     * 用户状态修改
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int changeStatus(User user,HttpServletRequest request);

    /**
     * 用户注册业务逻辑接口
     * @param user 用户信息
     * @return 结果
     */
    public int register(User user);

    /**
     * 查询对应的公司的所有的员工信息
     * @return
     */
    List<User> selectComAllUser(Cookie[] cookies);

    /**
     * 获取系统登录用户
     * @return
     */
    User getSysUser(Cookie[] cookies);

    /**
     * 更新用户的登录标记为0
     * @param user
     * @return
     */
    public int changeLoginTag(User user,HttpServletRequest request);

    /**
     * 查询用户二维码
     * @param user 用户信息
     * @return
     */
    List<UserQrCode> selectUserQrCode(User user);

    User selectUserInfo(Long userId);


    /******************************************************************************************************
     *********************************** 用户app交互接口 ***************************************************
     ******************************************************************************************************/
    /**
     * 用户app交互接口
     * @return 结果
     */
    List<UserApp> appSelectUserList(UserApp userApp);

    List<UserApp> appSelectUserInfoList(Integer uid);

    /**
     * 查询用户头像
     * @param uid
     * @return
     */
    String selectUserInfo(Integer uid);
}
