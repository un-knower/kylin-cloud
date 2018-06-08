package com.kylin.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author liugh
 * @since 2018-06-08
 */
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 自我介绍
     */
    private String aboutme;
    /**
     * 加密密码
     */
    private String passwd;
    /**
     * 头像图片
     */
    private String avatar;


  @JSONField(deserialize=false,serialize=false)
  @TableField(exist = false)
  private MultipartFile avatarFile;

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }

    @TableField(exist = false)
    private String enableUrl;

    public String getEnableUrl() {
        return enableUrl;
    }

    public void setEnableUrl(String enableUrl) {
        this.enableUrl = enableUrl;
    }

    @TableField(exist = false)
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 1:普通用户，2:房产经纪人
     */
    private Integer type;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 是否启用,1启用，0停用
     */
    private Integer enable;
    /**
     * 所属经纪机构
     */
    @TableField("agency_id")
    private Integer agencyId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", name=" + name +
        ", phone=" + phone +
        ", email=" + email +
        ", aboutme=" + aboutme +
        ", passwd=" + passwd +
        ", avatar=" + avatar +
        ", type=" + type +
        ", createTime=" + createTime +
        ", enable=" + enable +
        ", agencyId=" + agencyId +
        "}";
    }
}
