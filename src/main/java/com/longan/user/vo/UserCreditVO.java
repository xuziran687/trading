package com.longan.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.longan.user.entity.UserCredit;
import lombok.Data;

import java.util.Date;
@Data
public class UserCreditVO {
    /**
     * 信用分
     */
    private Integer score;

    /**
     * 信用等级
     */
    private Integer level;

    /**
     * 好评数
     */
    private Integer goodNum;

    /**
     * 差评数
     */
    private Integer badNum;

    public void set(UserCredit userCredit){
        score = userCredit.getScore();
        level = userCredit.getLevel();
        goodNum = userCredit.getGoodNum();
        badNum = userCredit.getBadNum();
    }
}
