package cn.zeroeden.domain.salarys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName( "sa_user_salary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSalary implements Serializable {
    private static final long serialVersionUID = 7569031833026436746L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 当前基本工资
     */
    private BigDecimal currentBasicSalary;
    /**
     * 当前岗位工资
     */
    private BigDecimal currentPostWage;
    /**
     * 定薪基本工资
     */
    private BigDecimal fixedBasicSalary;
    /**
     * 定薪岗位工资
     */
    private BigDecimal fixedPostWage;
    /**
     * 转正基本工资
     */
    private BigDecimal correctionOfBasicWages;
    /**
     * 转正岗位工资
     */
    private BigDecimal turnToPostWages;

    public BigDecimal getCurrentBasicSalary() {
    	return this.currentBasicSalary==null?BigDecimal.ZERO:this.currentBasicSalary;
    }

	public BigDecimal getCurrentPostWage() {
		return this.currentPostWage==null?BigDecimal.ZERO:this.currentPostWage;
	}



}
