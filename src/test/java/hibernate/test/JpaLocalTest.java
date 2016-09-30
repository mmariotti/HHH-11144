package hibernate.test;

import java.sql.DriverManager;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import hibernate.model.Item;
import hibernate.model.ItemRelation;


public abstract class JpaLocalTest
{
    protected static EntityManagerFactory emf;

    @Before
    public void populate()
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Item item1 = new Item();
        item1.setCode("first");
        em.persist(item1);

        Item item2 = new Item();
        item2.setCode("second");
        em.persist(item2);

        ItemRelation rel = new ItemRelation();
        rel.setParent(item1);
        rel.setChild(item2);
        item1.getLowerItemRelations().add(rel);
        item2.getHigherItemRelations().add(rel);
        em.persist(rel);

        tx.commit();
        em.close();
    }

    @After
    public void verify()
    {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Item item = em.createQuery("select x from Item x where x.code = 'first'", Item.class).getSingleResult();

        Set<ItemRelation> lowerItemRelations = item.getLowerItemRelations();
        Hibernate.initialize(lowerItemRelations);

        tx.rollback();
        em.close();

        Assert.assertEquals(0, lowerItemRelations.size());
    }

    @BeforeClass
    public static void beforeClass()
    {
        emf = Persistence.createEntityManagerFactory("test");
    }

    @AfterClass
    public static void afterClass()
    {
        if(emf != null && emf.isOpen())
        {
            emf.close();
        }

        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            DriverManager.getConnection("jdbc:derby:memory:test;drop=true");
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }
    }
}
