package pl.edu.pk.biblioteka.data;

import org.json.JSONObject;

public class Department {
    private int departmentId;
    private String name;

    public Department(int departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }

    public static Department valueOf(JSONObject o) {
        return new Department(
                o.getInt("departmentId"),
                o.getString("name"));
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return departmentId == ((Department) o).departmentId;
    }
}
