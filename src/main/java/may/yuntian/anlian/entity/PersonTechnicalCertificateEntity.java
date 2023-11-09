package may.yuntian.anlian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 *人员技能证书类
 *
 * @author ZhangHao
 * @data 2021-06-04
 */
@TableName("t_person_technical_certificate")
public class PersonTechnicalCertificateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 * 人员Id
	 */
	private Long userId;
	/**
     * 人员名称
	 */
	private String userName;
	/**
	 *技能证书名称
	 */
	private String name;
	/**
	 *技能证书路径
	 */
	private String path;
	/**
	 * 份数
	 */
	private Integer numberCopies;
	/**
	 * 页数
	 */
	private Integer numberPages;
	/**
	 * 收录日期
	 */
	private Date collectionDate;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 *文件收录时间
	 */
	private Date createtime;
	/**
	 *修改时间
	 */
	private Date updatetime;
    /**
     * minio路径
     */
    private String minioPath;

    public String getMinioPath() {
        return minioPath;
    }

    public void setMinioPath(String minioPath) {
        this.minioPath = minioPath;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getNumberCopies() {
		return numberCopies;
	}

	public void setNumberCopies(Integer numberCopies) {
		this.numberCopies = numberCopies;
	}

	public Integer getNumberPages() {
		return numberPages;
	}

	public void setNumberPages(Integer numberPages) {
		this.numberPages = numberPages;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	@Override
	public String toString() {
		return "PersonBasicFilesEntity{" +
				"id=" + id +
				", userId=" + userId +
				", name='" + name + '\'' +
				", path='" + path + '\'' +
				", numberCopies=" + numberCopies +
				", numberPages=" + numberPages +
				", remarks='" + remarks + '\'' +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				'}';
	}
}
