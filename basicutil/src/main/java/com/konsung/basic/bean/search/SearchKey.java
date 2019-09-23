package com.konsung.basic.bean.search;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

@Entity
public class SearchKey {

    // 本地保存id
    @Id(autoincrement = true)
    private Long localId;

    private int id;
    private String link;
    @Unique
    private String name;
    private int order;
    private int visible;
    private Date saveTime;

    @Generated(hash = 1970013391)
    public SearchKey(Long localId, int id, String link, String name, int order,
                     int visible, Date saveTime) {
        this.localId = localId;
        this.id = id;
        this.link = link;
        this.name = name;
        this.order = order;
        this.visible = visible;
        this.saveTime = saveTime;
    }

    @Generated(hash = 11165861)
    public SearchKey() {
    }

    public Long getLocalId() {
        return this.localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return this.visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public Date getSaveTime() {
        return this.saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }
}
