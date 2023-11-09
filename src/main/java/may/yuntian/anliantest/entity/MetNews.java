package may.yuntian.anliantest.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;


/**
 * 项目公示
 * 
 * @author zhanghao
 * @data 2022-04-12
 */
@TableName("met_news")
public class MetNews implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;

    private String identifier;//项目编号
	/**
	 *单位名称
	 */
	private String title;
	/**
	 *内容
	 */
	private String content;
	/**
	 *检评：96
	 */
	private Integer class1;
	/**
	 *检评：24
	 */
	private Integer class2;
	/**
	 *检评：93
	 */
	private Integer class3;
	/**
	 *	newanlian
	 */
	private String issue;

	/**
	 *修改时间
	 */
	private Date updatetime;
	/**
	 *入库时间
	 */
	private Date addtime;
	/**
	 *cn
	 */
	private String lang;
	/**
	 *newanlian
	 */
	private String publisher;
	/**
	 *""
	 */
	private String description;
	/**
	 *""
	 */
	private String tag;
	/**
	 *""
	 */
	private String otherInfo;
	/**
	 *""
	 */
	private String customInfo;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getClass1() {
		return class1;
	}

	public void setClass1(Integer class1) {
		this.class1 = class1;
	}

	public Integer getClass2() {
		return class2;
	}

	public void setClass2(Integer class2) {
		this.class2 = class2;
	}

	public Integer getClass3() {
		return class3;
	}

	public void setClass3(Integer class3) {
		this.class3 = class3;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
