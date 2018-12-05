package ru.BlackAndWhite.CuteJavaPet.model;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.Objects;

@Data
@Log4j
public class Group {
    private Integer id;
    private String groupName;
    private List<User> users;
    private List<Attach> attaches;

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        Group group = (Group) o;
        if (this == o) return true;
        return Objects.equals(id, group.id) &&
                Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
