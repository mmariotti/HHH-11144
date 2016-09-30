package hibernate.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;


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

    @Override
    public int hashCode()
    {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }

        if(obj == null || !(obj instanceof Item))
        {
            return false;
        }

        Item other = (Item) obj;

        return Objects.equals(code, other.getCode());
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + ": id=[" + id + "] code=[" + code + "]";
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Set<ItemRelation> getLowerItemRelations()
    {
        return lowerItemRelations;
    }

    public void setLowerItemRelations(Set<ItemRelation> lowerItemRelations)
    {
        this.lowerItemRelations = lowerItemRelations;
    }

    public Set<ItemRelation> getHigherItemRelations()
    {
        return higherItemRelations;
    }

    public void setHigherItemRelations(Set<ItemRelation> higherItemRelations)
    {
        this.higherItemRelations = higherItemRelations;
    }
}
