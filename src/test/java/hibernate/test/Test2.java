package hibernate.test;

import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Test;
import hibernate.model.Item;
import hibernate.model.ItemRelation;


public class Test2 extends JpaLocalTest
{
    @Test
    public void test2()
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Item item = em.createQuery("select x from Item x where x.code = 'first'", Item.class).getSingleResult();

        Set<ItemRelation> lowerItemRelations = item.getLowerItemRelations();
        Hibernate.initialize(lowerItemRelations);

//      initializing higherItemRelations prevents orphanRemoval to work on lowerItemRelations
        Set<ItemRelation> higherItemRelations = item.getHigherItemRelations();
        Hibernate.initialize(higherItemRelations);

        Assert.assertEquals(1, lowerItemRelations.size());

        lowerItemRelations.clear();

        tx.commit();
        em.close();
    }
}
