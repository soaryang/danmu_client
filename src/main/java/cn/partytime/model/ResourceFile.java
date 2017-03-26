package cn.partytime.model;

/**
 * Created by liuwei on 2016/8/19.
 */

import org.springframework.util.StringUtils;

/**
 * 资源文件
 */
public class ResourceFile{

    private String id;

    /**
     * 所属活动的id
     */
    private String partyId;

    /**
     *资源名称
     */
    private String resourceName;

    /**
     * 文件类型  0:表情   1:特效图片   2:特效视频   3: h5背景图
     */
    private Integer fileType;

    /**
     * 文件下载地址
     */
    private String fileUrl;

    /**
     * 文件保存的服务器路径
     */
    private String filePath;

    /**
     * 本地的资源文件路径
     */
    private String localFilePath;

    /**
     * 压缩图片的路径
     */
    private String smallFileUrl;

    /**
     * 文件尺寸
     */
    private Long fileSize;

    /**
     * 缩略图文件尺寸
     */
    private Long smallFileSize;

    public String getName(){
        if (!StringUtils.isEmpty(this.fileUrl)) {
            String[] ss = this.fileUrl.split("/");
            return ss[ss.length-1];
        }
        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getSmallFileUrl() {
        return smallFileUrl;
    }

    public void setSmallFileUrl(String smallFileUrl) {
        this.smallFileUrl = smallFileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getSmallFileSize() {
        return smallFileSize;
    }

    public void setSmallFileSize(Long smallFileSize) {
        this.smallFileSize = smallFileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceFile that = (ResourceFile) o;

        if (!id.equals(that.id)) return false;
        if (!partyId.equals(that.partyId)) return false;
        if (!resourceName.equals(that.resourceName)) return false;
        if (!fileType.equals(that.fileType)) return false;
        if (!fileUrl.equals(that.fileUrl)) return false;
        if (!filePath.equals(that.filePath)) return false;
        return localFilePath != null ? localFilePath.equals(that.localFilePath) : that.localFilePath == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + partyId.hashCode();
        result = 31 * result + resourceName.hashCode();
        result = 31 * result + fileType.hashCode();
        result = 31 * result + fileUrl.hashCode();
        result = 31 * result + filePath.hashCode();
        result = 31 * result + (localFilePath != null ? localFilePath.hashCode() : 0);
        return result;
    }
}


