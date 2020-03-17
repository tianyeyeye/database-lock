package com.hello.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author tianye
 * @since 2020-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业编码
     */
    private String jobCode;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 是否启用：0-禁用，1-启用
     */
    private Boolean jobEnable;

    /**
     * 是否锁定：0-空闲，1-锁定
     */
    private Boolean jobLock;

    /**
     * 超时时间（分钟）
     */
    private Integer timeOut;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 版本号
     */
    private Long version;


}
