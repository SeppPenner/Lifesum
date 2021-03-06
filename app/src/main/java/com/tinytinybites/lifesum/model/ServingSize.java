package com.tinytinybites.lifesum.model;

import com.tinytinybites.lifesum.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SERVING_SIZE".
 */
public class ServingSize implements java.io.Serializable {

    private Long oid;
    private Long lastupdated;
    private Long created;
    private String name;
    private Double proportion;
    private String source;
    private Long servingcategory;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ServingSizeDao myDao;

    private ServingCategory servingCategory;
    private Long servingCategory__resolvedKey;


    public ServingSize() {
    }

    public ServingSize(Long oid) {
        this.oid = oid;
    }

    public ServingSize(Long oid, Long lastupdated, Long created, String name, Double proportion, String source, Long servingcategory) {
        this.oid = oid;
        this.lastupdated = lastupdated;
        this.created = created;
        this.name = name;
        this.proportion = proportion;
        this.source = source;
        this.servingcategory = servingcategory;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServingSizeDao() : null;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(Long lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getServingcategory() {
        return servingcategory;
    }

    public void setServingcategory(Long servingcategory) {
        this.servingcategory = servingcategory;
    }

    /** To-one relationship, resolved on first access. */
    public ServingCategory getServingCategory() {
        Long __key = this.servingcategory;
        if (servingCategory__resolvedKey == null || !servingCategory__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServingCategoryDao targetDao = daoSession.getServingCategoryDao();
            ServingCategory servingCategoryNew = targetDao.load(__key);
            synchronized (this) {
                servingCategory = servingCategoryNew;
            	servingCategory__resolvedKey = __key;
            }
        }
        return servingCategory;
    }

    public void setServingCategory(ServingCategory servingCategory) {
        synchronized (this) {
            this.servingCategory = servingCategory;
            servingcategory = servingCategory == null ? null : servingCategory.getOid();
            servingCategory__resolvedKey = servingcategory;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
