# HHH-11144
** orphanRemoval not working when other same-table-relations are initialized

execute as 
    
	mvn test

using these entities:


	@Entity
	@Table(name = "ITEM",
		indexes = @Index(columnList = "CODE", unique = true))
	public class Item implements Serializable
	{
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue
		protected Long id;

		@Column
		protected String code;

		@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
		protected Set<ItemRelation> lowerItemRelations = new LinkedHashSet<>();

		@OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
		protected Set<ItemRelation> higherItemRelations = new LinkedHashSet<>();

		...
	}

	@Entity
	@Table(name = "ITEM_RELATION",
		indexes = @Index(columnList = "PARENT_ID, CHILD_ID", unique = true))
	public class ItemRelation implements Serializable
	{
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue
		protected Long id;

		@ManyToOne(optional = false)
		@JoinColumn(name = "PARENT_ID")
		private Item parent;

		@ManyToOne(optional = false)
		@JoinColumn(name = "CHILD_ID")
		private Item child;

		@Column(nullable = false, columnDefinition = "INT DEFAULT 0 NOT NULL")
		private int quantity = 1;

		...
	}

when executing:

	EntityManager em = emf.createEntityManager();
	EntityTransaction tx = em.getTransaction();

	tx.begin();

	Item item = em.createQuery("select x from Item x where x.code = 'first'", Item.class).getSingleResult();

	Set<ItemRelation> lowerItemRelations = item.getLowerItemRelations();
	Hibernate.initialize(lowerItemRelations);

	// initializing higherItemRelations prevents orphanRemoval to work on lowerItemRelations
	Set<ItemRelation> higherItemRelations = item.getHigherItemRelations();
	Hibernate.initialize(higherItemRelations);

	lowerItemRelations.clear();

	tx.commit();
	em.close();

no DELETE statement is issued.

If you don't initialize _higherItemRelations_ then everything is working fine.
