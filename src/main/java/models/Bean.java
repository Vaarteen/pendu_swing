/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 * Classe mère des beans de base pour DAO.
 *
 * @author Herbert &lt;herbert.caffarel@ldnr.fr&gt;
 */
public abstract class Bean {

    /**
     * L'identifiant de l'objet
     */
    protected Integer id;

    public boolean isNew() {
        return (id == null);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
