package hibernate.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


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

    @Override
    public int hashCode()
    {
        return Objects.hash(parent, child);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }

        if(obj == null || !(obj instanceof ItemRelation))
        {
            return false;
        }

        ItemRelation other = (ItemRelation) obj;

        return Objects.equals(parent, other.getParent()) && Objects.equals(child, other.getChild());
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + ": id=[" + id + "] parent=[" + parent + "] child=[" + child + "]";
    }

    public Item getParent()
    {
        return parent;
    }

    public void setParent(Item parent)
    {
        this.parent = parent;
    }

    public Item getChild()
    {
        return child;
    }

    public void setChild(Item child)
    {
        this.child = child;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}