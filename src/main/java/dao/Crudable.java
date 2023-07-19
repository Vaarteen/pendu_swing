
package dao;

import models.Bean;

/**
 * Une interface imposant la création des méthodes fondamentales d'accès à une
 * table de DB.
 *
 * @author Herbert Caffarel
 * @param <T> Un objet de type Bean, donc possédant un id dont la correspondance
 * dans la table visée est id_&lt;table&gt;
 */
public interface Crudable<T extends Bean> {
    /**
     * Hydrate un objet depuis la DB grâce à son identifiant.
     *
     * @param id L'identifiant de l'objet dans la DB
     * @return L'objet hydraté.
     */
    T getById(Integer id);

    /**
     * Supprime un objet dans la DB par son identifiant.
     *
     * @param object
     */
    void delete(T object);

    /**
     * Persiste l'objet en DB. Le crée ou le modifie selon le cas.
     *
     * @param object
     */
    void persist(T object);

    /**
     * Crée un nouvel objet dans la DB.
     *
     * @param object L'objet à persister
     */
    void create(T object);

    /**
     * Met à jour un objet dans la DB.
     *
     * @param object L'objet à persister
     */
    void update(T object);
}
